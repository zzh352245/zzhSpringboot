package com.zzh.util;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * @author ：zz
 * @date ：Created in 2021/12/9 15:24
 * @description：SM2密钥
 */
public class SM2KeyPair {

    /** 公钥 */
    private ECPoint publicKey;

    /** 私钥 */
    private BigInteger privateKey;

    SM2KeyPair(ECPoint publicKey, BigInteger privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public ECPoint getPublicKey() {
        return publicKey;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

}
