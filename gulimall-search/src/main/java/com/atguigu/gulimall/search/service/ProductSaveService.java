package com.atguigu.gulimall.search.service;

import com.atguigu.common.to.to.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @Auther :朱树广
 * @Date :2021/3/14
 * @Description :com.atguigu.gulimall.search.service
 * @Version :1.0
 */
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;
}
