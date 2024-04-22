package com.data.utils;

/**
 *信息枚举类
 */
public enum ResultCondeEnum {

    SUCCESS(200, "返回成功"),
    CREATED(201,"对象创建成功"),
    ACCEPTED(202,"请求已被接受"),
    NO_CONTENT(204,"操作已经执行成功，但是没有返回数据"),
    MOVED_PERM(301,"资源已被移除"),
    SEE_OTHER(303,"重定向"),
    NOT_MODIFIED(304,"资源没有被修改"),
    BAD_REQUEST(400,"参数列表错误（缺少，格式不匹配）"),
    REQUEST_PARAM_ERROR(401, "请求参数错误"),
    FORBIDDEN(403,"访问受限，授权过期"),
    REQUEST_NOT_FOUND(404, "请求的资源或服务未找到"),
    BAD_METHOD(405,"不允许的http方法"),
    REQUEST_OUT_OVERTIME(408, "请求超时"),
    CONFLICT(409,"资源冲突，或者资源被锁"),
    REQUEST_LENGTH_LIMIT(414, "请求URI太长"),
    REQUEST_Format_NOT_SUPPORTED(415, "请求的格式不支持"),
    SYSTEM_EXCEPTION(500, "系统异常"),
    NOT_IMPLEMENTED(501,"接口未实现"),
    WAR(601,"系统警告消息"),
    ;
    /**
     * 枚举值
     */
    private final Integer code;

    /**
     * 枚举描述
     */
    private final String message;

    /**
     * 构造一个<code>LocalCacheEnum</code>枚举对象
     *
     * @param code    枚举值
     * @param message 枚举描述
     */
    ResultCondeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static boolean isSuccessCode(int code) {
        return code == SUCCESS.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}