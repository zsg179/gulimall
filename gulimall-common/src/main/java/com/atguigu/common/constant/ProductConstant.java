package com.atguigu.common.constant;

/**
 * @Auther :朱树广
 * @Date :2021/2/28
 * @Description :com.atguigu.common.constant
 * @Version :1.0
 */
public class ProductConstant {
    public enum AttrEnum {
        ATTR_TYPE_BASE(1,"基本属性"), ATTR_TYPE_SALE(0,"销售属性");

        private int code;
        private String msg;

        AttrEnum(int code, String msg) {
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
}
