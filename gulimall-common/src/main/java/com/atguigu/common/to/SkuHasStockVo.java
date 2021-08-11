package com.atguigu.common.to;

import lombok.Data;

/**
 * @Auther :朱树广
 * @Date :2021/3/14
 * @Description :com.atguigu.gulimall.ware.vo
 * @Version :1.0
 */
@Data
public class SkuHasStockVo {
    private Long skuId;
    private Boolean hasStock;
}
