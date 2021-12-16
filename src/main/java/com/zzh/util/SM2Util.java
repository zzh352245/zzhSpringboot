package com.zzh.util;

import com.alibaba.fastjson.JSONObject;
import com.zzh.model.LoginUser;
import com.zzh.security.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.ShortenedDigest;
import org.bouncycastle.crypto.generators.KDF1BytesGenerator;
import org.bouncycastle.crypto.params.ISO18033KDFParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;
import org.springframework.stereotype.Component;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author ：zz
 * @date ：Created in 2021/12/9 15:06
 * @description：SM2加解密工具类
 */
@Slf4j
@Component
public class SM2Util {

    /** 素数p */
    private static final BigInteger p = new BigInteger("FFFFFFFE" + "FFFFFFFF"
            + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF"
            + "FFFFFFFF", 16);

    /** 系数a */
    private static final BigInteger a = new BigInteger("FFFFFFFE" + "FFFFFFFF"
            + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF"
            + "FFFFFFFC", 16);

    /** 系数b */
    private static final BigInteger b = new BigInteger("28E9FA9E" + "9D9F5E34"
            + "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41"
            + "4D940E93", 16);

    /** 坐标x */
    private static final BigInteger xg = new BigInteger("32C4AE2C" + "1F198119"
            + "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589"
            + "334C74C7", 16);

    /** 坐标y */
    private static final BigInteger yg = new BigInteger("BC3736A2" + "F4F6779C"
            + "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5"
            + "2139F0A0", 16);

    /** 基点G, G=(xg,yg),其介记为n */
    private static final BigInteger n = new BigInteger("FFFFFFFE" + "FFFFFFFF"
            + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409"
            + "39D54123", 16);

    private static SecureRandom random = new SecureRandom();
    public ECCurve.Fp curve;
    private ECPoint G;

    public static String printHexString(byte[] b) {
        StringBuffer builder = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
            builder.append(hex);
        }
        return builder.toString();
    }

    public BigInteger random(BigInteger max) {
        BigInteger r = new BigInteger(256, random);
        // int count = 1;
        while (r.compareTo(max) >= 0) {
            r = new BigInteger(128, random);
            // count++;
        }
        // log.info("count: " + count);
        return r;
    }

    private boolean allZero(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != 0)
                return false;
        }
        return true;
    }

    /**
     * 加密
     * @param input 待加密消息M
     * @param publicKey 公钥
     * @return byte[] 加密后的字节数组
     */
    public byte[] encrypt(String input, ECPoint publicKey) {

        log.info("publicKey is: "+publicKey);

        byte[] inputBuffer = input.getBytes();
        /*printHexString(inputBuffer);*/

        /* 1 产生随机数k，k属于[1, n-1] */
        BigInteger k = random(n);
        /*System.out.print("k: ");
        printHexString(k.toByteArray());*/

        /* 2 计算椭圆曲线点C1 = [k]G = (x1, y1) */
        ECPoint C1 = G.multiply(k);
        byte[] C1Buffer = C1.getEncoded(false);
        /*System.out.print("C1: ");
        printHexString(C1Buffer);*/

        // 3 计算椭圆曲线点 S = [h]Pb * curve没有指定余因子，h为空

        //           BigInteger h = curve.getCofactor(); System.out.print("h: ");
        //           printHexString(h.toByteArray()); if (publicKey != null) { ECPoint
        //           result = publicKey.multiply(h); if (!result.isInfinity()) {
        //           log.info("pass"); } else {
        //          log.info("计算椭圆曲线点 S = [h]Pb失败"); return null; } }

        /* 4 计算 [k]PB = (x2, y2) */
        ECPoint kpb = publicKey.multiply(k).normalize();

        /* 5 计算 t = KDF(x2||y2, klen) */
        byte[] kpbBytes = kpb.getEncoded(false);
        DerivationFunction kdf = new KDF1BytesGenerator(new ShortenedDigest(
                new SHA256Digest(), 20));
        byte[] t = new byte[inputBuffer.length];
        kdf.init(new ISO18033KDFParameters(kpbBytes));
        kdf.generateBytes(t, 0, t.length);

        if (allZero(t)) {
            log.info("all zero");
        }

        /* 6 计算C2=M^t */
        byte[] C2 = new byte[inputBuffer.length];
        for (int i = 0; i < inputBuffer.length; i++) {
            C2[i] = (byte) (inputBuffer[i] ^ t[i]);
        }

        /* 7 计算C3 = Hash(x2 || M || y2) */
        byte[] C3 = calculateHash(kpb.getXCoord().toBigInteger(), inputBuffer,
                kpb.getYCoord().toBigInteger());

        /* 8 输出密文 C=C1 || C2 || C3 */
        byte[] encryptResult = new byte[C1Buffer.length + C2.length + C3.length];
        System.arraycopy(C1Buffer, 0, encryptResult, 0, C1Buffer.length);
        System.arraycopy(C2, 0, encryptResult, C1Buffer.length, C2.length);
        System.arraycopy(C3, 0, encryptResult, C1Buffer.length + C2.length,
                C3.length);

        System.out.print("密文: ");
//        printHexString(encryptResult);

        return encryptResult;
    }

    public String decrypt(byte[] encryptData, BigInteger privateKey) {
        log.info("privateKey is: "+privateKey);
        log.info("encryptData length: " + encryptData.length);

        byte[] C1Byte = new byte[65];
        System.arraycopy(encryptData, 0, C1Byte, 0, C1Byte.length);

        ECPoint C1 = curve.decodePoint(C1Byte).normalize();

        /* 计算[dB]C1 = (x2, y2) */
        ECPoint dBC1 = C1.multiply(privateKey).normalize();

        /* 计算t = KDF(x2 || y2, klen) */
        byte[] dBC1Bytes = dBC1.getEncoded(false);
        DerivationFunction kdf = new KDF1BytesGenerator(new ShortenedDigest(
                new SHA256Digest(), 20));

        int klen = encryptData.length - 65 - 20;
        log.info("klen = " + klen);

        byte[] t = new byte[klen];
        kdf.init(new ISO18033KDFParameters(dBC1Bytes));
        kdf.generateBytes(t, 0, t.length);

        if (allZero(t)) {
            log.info("all zero");
        }

        /* 5 计算M'=C2^t */
        byte[] M = new byte[klen];
        for (int i = 0; i < M.length; i++) {
            M[i] = (byte) (encryptData[C1Byte.length + i] ^ t[i]);
        }

        /* 6 计算 u = Hash(x2 || M' || y2) 判断 u == C3是否成立 */
        byte[] C3 = new byte[20];
        System.arraycopy(encryptData, encryptData.length - 20, C3, 0, 20);
        byte[] u = calculateHash(dBC1.getXCoord().toBigInteger(), M, dBC1
                .getYCoord().toBigInteger());
        if (Arrays.equals(u, C3)) {
            log.info("解密成功");
        } else {
            System.out.print("u = ");
            printHexString(u);
            System.out.print("C3 = ");
            printHexString(C3);
            log.info("解密验证失败");
        }
        return new String(M);
    }

    private byte[] calculateHash(BigInteger x2, byte[] M, BigInteger y2) {
        ShortenedDigest digest = new ShortenedDigest(new SHA256Digest(), 20);
        byte[] buf = x2.toByteArray();
        digest.update(buf, 0, buf.length);
        digest.update(M, 0, M.length);
        buf = y2.toByteArray();
        digest.update(buf, 0, buf.length);

        buf = new byte[20];
        digest.doFinal(buf, 0);
        return buf;
    }

    private boolean between(BigInteger param, BigInteger min, BigInteger max) {
        if (param.compareTo(min) >= 0 && param.compareTo(max) < 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 公钥校验
     * @param publicKey 公钥
     * @return boolean true或false
     */
    private boolean checkPublicKey(ECPoint publicKey) {
        if (!publicKey.isInfinity()) {
            BigInteger x = publicKey.getXCoord().toBigInteger();
            BigInteger y = publicKey.getYCoord().toBigInteger();
            if (between(x, new BigInteger("0"), p) && between(y, new BigInteger("0"), p)) {
                BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);
                log.info("xResult: " + xResult.toString());
                BigInteger yResult = y.pow(2).mod(p);
                log.info("yResult: " + yResult.toString());
                if (yResult.equals(xResult) && publicKey.multiply(n).isInfinity()) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 获得公私钥对
     * @return
     */
    public SM2KeyPair generateKeyPair() {
        BigInteger d = random(n.subtract(new BigInteger("1")));
        SM2KeyPair keyPair = new SM2KeyPair(G.multiply(d).normalize(), d);
        if (checkPublicKey(keyPair.getPublicKey())) {
            exportPrivateKey(keyPair.getPrivateKey());
            exportPublicKey(keyPair.getPublicKey());
            log.info("generate key successfully");
            return keyPair;
        } else {
            log.info("generate key failed");
        }
        return null;
    }

    public SM2Util() {
        curve = new ECCurve.Fp(p, // q
                a, // a
                b); // b
        G = curve.createPoint(xg, yg);
    }

    /**
     * 导出私钥到本地
     */
    public void exportPrivateKey(BigInteger privateKey) {
        File file = new File("prk");
        try {
            if (!file.exists())
                file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(privateKey);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出公钥到本地
     */
    public void exportPublicKey(ECPoint publicKey) {
        File file = new File("puk");
        try {
            if (!file.exists())
                file.createNewFile();
            byte buffer[] = publicKey.getEncoded(false);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 从本地导入私钥
     */
    public BigInteger importPrivateKey() {
        File file = new File("prk");
        try {
            if (!file.exists())
                return null;
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            BigInteger res = (BigInteger) (ois.readObject());
            ois.close();
            fis.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 从本地导入公钥
     */
    public ECPoint importPublicKey() {
        File file = new File("puk");
        try {
            if (!file.exists())
                return null;
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte buffer[] = new byte[16];
            int size;
            while ((size = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, size);
            }
            fis.close();
            return curve.decodePoint(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * create by: zz
     * description: 字符串转为16进制byte数组
     * create time: 2021/12/10 10:51
     * @param: hex
     * @return byte[]
     */
    public static byte[] hexToByte(String hex) throws IllegalArgumentException {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        if (hex.length() < 1) {
            return null;
        } else {
            byte[] result = new byte[hex.length() / 2];
            int j = 0;
            for(int i = 0; i < hex.length(); i+=2) {
                result[j++] = (byte)Integer.parseInt(hex.substring(i,i+2), 16);
            }
            return result;
        }
    }

    /**
     * create by: zz
     * description: 加密
     * create time: 2021/12/10 10:41
     * @param: param
     * @return java.lang.String
     */
    public String encrypt(String param){
        byte[] data = encrypt(param, importPublicKey());
        return printHexString(data);
    }

    /**
     * create by: zz
     * description: 解密
     * create time: 2021/12/10 10:54
     * @param: param
     * @return java.lang.String
     */
    public String decrypt(String param){
        byte[] data = hexToByte(param);
        if(data == null){
            return null;
        }
        return decrypt(data, importPrivateKey());
    }

    /**
     * create by: zz
     * description: 解密后返回用户信息
     * create time: 2021/12/10 10:54
     * @param: param
     * @return java.lang.String
     */
    public UserDetail decryptToUser(String param){
        byte[] data = hexToByte(param);
        if(data == null){
            return null;
        }
        String result = decrypt(data, importPrivateKey());
        try {
            LoginUser loginUser = JSONObject.parseObject(result, LoginUser.class);
            UserDetail userDetail = new UserDetail(loginUser.getUser(), loginUser.getPermissions());
            return userDetail;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*public static void main(String[] args){
        String aaa = "123456";
        SM2Util sm2 = new SM2Util();
//        sm2.generateKeyPair(); 生成密钥对，并写入文件中，非第一次不要放开此行注释
        String data = sm2.encrypt(aaa);
        System.out.println("秘闻\n"+data);
//        System.out.println("data is:"+ Arrays.toString(data));
        String mw="04bc2cb303c23984771505dbdf6d2455e982b7869c1b051d32c41433da44a0e3b3d6f2ae891b83bddc9d1e0911c21037f4d87fbdc323b38f1bd761b9da1b2af4b465da7326a67d307cf43d834e9fd9e4e990536ec05651b50f6e3683172eb4264ebd8e107cec3596e93e4a0bb881b4f14ff4d9c7858e54ee08d37d280a335c19de7f7531312593c60b12cea39b36ef53c78bf50f31579ef75462f0bfe68fa35e908873b4ee653c16bf4d99d8d912bbfa599e6e5c1b312bdb3b43498651ce636046e554c9f9243294d88cf71fafc2a00d065b17109a865c5fcbf3f1b793a948dae9cb07d29df6ee2ea5bf73c070781e85af63a98eeaac8c222c22658c58fbaac685d4f19cdc4c2ac2409f51932ea4b83d1538bb3eb85dd705647861da93b43c9ff6ca4572bea3d0d241a3dbddb6629e3097ee4487c74866bee7fe13a77fdfe256168443ea602fbc059dfe9f15571c83f0d69fd4dc5fac0384a26ab969b9df5aa22ea1773ac37f9a38a32a514ae157614be88898afafbfda672c51c22ec7d5075d704a5cc9cb58980015126ffe3c205f823b4d04bf311af549d13e9a50978c0a76a27661d913e7bd645c20a05b75df55cec0cc6e23616e0b7be4cff30e785927964c21d4314c0980b7b137d9efed8082232515b9bec6143573e9ad7db678a9b79d0d8f3c265e10b578fbea0df25f5d661f712efae019003ed5376367c1d73c81c7e11cea1e3b430c6e0842e911c8cd213b2bbd2c6ff6eef89758a8f6bf6521f523764dc9abd5c7d6036b756a43811146f46676979c0fa08d7404742763d7bdf18a590cffd571919a17fb40fa7465d9f95c0673edfda0f1ef572219437a36ecbbb501db467f19c8096d6d07f624ed4255d80878fb4f633201f97bd8a731254cb03ebe319c2d3f693fe18373458d1d54cbd94bcfb19197bebe76e3ad94f2bba8449d4bdd884b18791de9627c0ae5218acc0d93501d9e45f4422aba30db2d2e950cc9224f";
        System.out.println(sm2.decrypt(data));
    }*/

}
