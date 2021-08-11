package com.atguigu.gulimall.ware.vo;

import lombok.Data;

/**
 * @Auther :朱树广
 * @Date :2021/3/5
 * @Description :com.atguigu.gulimall.ware.vo
 * @Version :1.0
 */
@Data
public class PurchaseItemDoneVo {
    //{itemId:1,status:4,reason:""}
    private Long itemId;
    private Integer status;
    private String reason;
}
