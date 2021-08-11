package com.atguigu.common.exception;

/**
 * @Auther :朱树广
 * @Date :2021/2/21
 * @Description :com.atguigu.common.exception
 * @Version :1.0
 */
public enum BizCodeEnume {

    UNKNOWN_EXCEPTION(10000, "系统未知异常"),
    VAILD_EXCEPTION(30000, "校验异常"),
    PRODUCT_UP_EXCEPTION(11000,"商品上架异常");

    private int code;
    private String msg;

    BizCodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
