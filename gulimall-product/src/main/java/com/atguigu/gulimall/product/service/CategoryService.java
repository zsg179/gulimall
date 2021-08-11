package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.Catalog2Vo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author zhusg02
 * @email zsg179@163.com
 * @date 2021-01-16 19:10:48
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();

    void removeMenusByIds(List<Long> asList);

    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    List<CategoryEntity> getLevel1Catagories();

    Map<String, List<Catalog2Vo>> getCategoryMap();

//    Map<String, List<Catalog2Vo>> getCatalogJsonDbWithRedisLock();

    Map<String, List<Catalog2Vo>> getCatalogJsonDbWithRedisson();

    Map<String, List<Catalog2Vo>> getCatalogJsonDbWithSpringCache();
}


