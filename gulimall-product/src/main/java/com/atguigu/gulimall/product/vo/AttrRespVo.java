package com.atguigu.gulimall.product.vo;

import lombok.Data;

/**
 * @Auther :朱树广
 * @Date :2021/2/28
 * @Description :com.atguigu.gulimall.product.vo
 * @Version :1.0
 */
@Data
public class AttrRespVo extends AttrVo{
    private String catelogName;
    private String groupName;
    private Long[] catelogPath;
}
