package com.zzh.api;

/**
 * @author ：zz
 * @date ：Created in 2021/12/1 11:02
 * @description：结果编码
 */
public enum ResultCode {

    SUCCESS(200, "操作成功"),
    FAILURE(400, "业务异常"),
    UN_AUTHORIZED(401, "请求未授权"),
    NOT_FOUND(404, "404 没找到请求"),
    PASSWORD_ERROR(405, "密码错误"),
    UN_LOGIN(415, "未登录"),
    REQ_REJECT(403, "请求被拒绝"),
    INTERNAL_SERVER_ERROR(500, "服务器异常"),
    PARAM_MISS(400, "缺少必要的请求参数"),
    PARAM_TYPE_ERROR(400, "请求参数类型错误"),
    PARAM_BIND_ERROR(400, "请求参数绑定错误"),
    PARAM_VALID_ERROR(400, "参数校验失败");

    final int code;
    final String message;

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
