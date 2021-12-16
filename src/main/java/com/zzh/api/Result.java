package com.zzh.api;

import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author ：zz
 * @date ：Created in 2021/12/1 11:03
 * @description：统一返回结果封装
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private boolean success;
    private T data;
    private String msg;

    private Result(ResultCode resultCode) {
        this(resultCode, null, resultCode.getMessage());
    }

    private Result(ResultCode resultCode, String msg) {
        this(resultCode, null, msg);
    }

    private Result(ResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMessage());
    }

    private Result(ResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ResultCode.SUCCESS.code == code;
    }

    public static boolean isSuccess(@Nullable Result<?> result) {
        return Optional.ofNullable(result).map((x) -> ObjectUtils.nullSafeEquals(ResultCode.SUCCESS.code, x.code)).orElse(Boolean.FALSE);
    }

    public static boolean isNotSuccess(@Nullable Result<?> result) {
        return !isSuccess(result);
    }

    public static <T> Result<T> data(T data) {
        return data(data, "操作成功");
    }

    public static <T> Result<T> data(T data, String msg) {
        return data(200, data, msg);
    }

    public static <T> Result<T> data(int code, T data, String msg) {
        return new Result(code, data, data == null ? "暂无承载数据" : msg);
    }

    public static <T> Result<T> success() {
        return new Result(ResultCode.SUCCESS);
    }

    public static <T> Result<T> success(String msg) {
        return new Result(ResultCode.SUCCESS, msg);
    }

    public static <T> Result<T> success(ResultCode resultCode) {
        return new Result(resultCode);
    }

    public static <T> Result<T> success(ResultCode resultCode, String msg) {
        return new Result(resultCode, msg);
    }

    public static <T> Result<T> fail(String msg) {
        return new Result(ResultCode.FAILURE, msg);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result(code, (Object)null, msg);
    }

    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result(resultCode);
    }

    public static <T> Result<T> fail(ResultCode resultCode, String msg) {
        return new Result(resultCode, msg);
    }

    public static <T> Result<T> status(boolean flag) {
        return flag ? success("操作成功") : fail("操作失败");
    }

    public int getCode() {
        return this.code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public T getData() {
        return this.data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String toString() {
        return "Result(code=" + this.getCode() + ", success=" + this.isSuccess() + ", data=" + this.getData() + ", msg=" + this.getMsg() + ")";
    }

    public Result() {
    }

}
