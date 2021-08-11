package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther :朱树广
 * @Date :2021/3/2
 * @Description :com.atguigu.common.to
 * @Version :1.0
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}

