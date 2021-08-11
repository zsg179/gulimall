package com.atguigu.common.to.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Auther :朱树广
 * @Date :2021/3/14
 * @Description :com.atguigu.common.to.to
 * @Version :1.0
 */
@Data
public class SkuEsModel {
    private Long skuId;
    private Long spuId;
    private String skuTitle;
    private BigDecimal skuPrice;
    private String skuImg;
    private Long saleCount;
    private boolean hasStock;
    private Long hotScore;
    private Long brandId;
    private Long catalogId;
    private String brandName;
    private String brandImg;
    private String catalogName;
    private List<Attr> attrs;

    @Data
    public static class Attr{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
