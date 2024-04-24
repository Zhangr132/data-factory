package com.data.utils;

public final class R<T> {
    private int code;
    private String msg;
    private T data;

    public R() {

    }

    public R(int code) {
        this.code = code;
        this.msg = "";
        this.data = null;
    }

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = null;
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 返回成功
     */
    public static R Success(Object data) {
        return new R(ResultCondeEnum.SUCCESS.getCode(), ResultCondeEnum.SUCCESS.getMessage(), data);
    }

    public static R Success(String message, Object data) {
        return new R(ResultCondeEnum.SUCCESS.getCode(), message, data);
    }

    public static R Success() {
        return Success("");
    }

    /**
     * 系统异常
     */
    public static R Failed(String msg) {
        return new R(ResultCondeEnum.SYSTEM_EXCEPTION.getCode(), msg);
    }

    public static R Failed() {
        return Failed("Failed");
    }

    public static R Failed(int code, String msg) {
        return new R(code, msg);
    }

    /**
     * 参数列表错误（缺少，格式不匹配）
     */
    public static R BAD_REQUEST(String msg) {
        return new R(ResultCondeEnum.BAD_REQUEST.getCode(), msg);
    }
    public static R BAD_REQUEST() {
        return BAD_REQUEST("参数列表错误（缺少，格式不匹配）");
    }
    public static R BAD_REQUEST(int code, String msg) {
        return new R(code, msg);
    }

    /**
     * 对象创建成功
     */
    public static R CREATED() {
        return CREATED("对象创建成功");
    }
    public static R CREATED(String msg) {
        return new R(ResultCondeEnum.CREATED.getCode(), msg);
    }
    public static R CREATED(int code, String msg) {
        return new R(code, msg);
    }

    /**
     * 访问受限，授权过期
     */
    public static R FORBIDDEN() {
        return FORBIDDEN("访问受限，授权过期");
    }
    public static R FORBIDDEN(String msg) {
        return new R(ResultCondeEnum.FORBIDDEN.getCode(), msg);
    }
    public static R FORBIDDEN(int code, String msg) {
        return new R(code, msg);
    }

    /**
     * 请求参数错误
     */
    public static R REQUEST_PARAM_ERROR() {
        return REQUEST_PARAM_ERROR("请求参数错误");
    }
    public static R REQUEST_PARAM_ERROR(String msg) {
        return new R(ResultCondeEnum.REQUEST_PARAM_ERROR.getCode(), msg);
    }
    public static R REQUEST_PARAM_ERROR(int code, String msg) {
        return new R(code, msg);
    }

    /**
     * 接口未实现
     */
    public static R NOT_IMPLEMENTED() {
        return NOT_IMPLEMENTED("接口未实现");
    }
    public static R NOT_IMPLEMENTED(String msg) {
        return new R(ResultCondeEnum.NOT_IMPLEMENTED.getCode(), msg);
    }
    public static R NOT_IMPLEMENTED(int code, String msg) {
        return new R(code, msg);
    }





    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean succeeded() {
        return getCode() == ResultCondeEnum.SUCCESS.getCode();
    }


    public boolean failed() {
        return getCode() != ResultCondeEnum.SUCCESS.getCode();
    }

}

