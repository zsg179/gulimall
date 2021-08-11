package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther :朱树广
 * @Date :2021/3/3
 * @Description :com.atguigu.common.to
 * @Version :1.0
 */
@Data
public class MemberPrice {

    private Long id;
    private String name;
    private BigDecimal price;

}
