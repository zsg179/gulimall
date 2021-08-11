package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther :朱树广
 * @Date :2021/3/5
 * @Description :com.atguigu.gulimall.ware.vo
 * @Version :1.0
 */
@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id;//采购单id

    private List<PurchaseItemDoneVo> items;
}
