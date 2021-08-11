package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @Auther :朱树广
 * @Date :2021/3/4
 * @Description :com.atguigu.gulimall.ware.vo
 * @Version :1.0
 */
@Data
public class MergeVo {

    private Long purchaseId; //整单id
    private List<Long> items;//[1,2,3,4] //合并项集合
}
