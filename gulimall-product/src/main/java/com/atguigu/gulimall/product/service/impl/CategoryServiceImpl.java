package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.Catalog2Vo;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<CategoryEntity> getLevel1Catagories() {
//        long start = System.currentTimeMillis();
        List<CategoryEntity> parent_cid = this.list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
//        System.out.println("查询一级菜单时间:"+(System.currentTimeMillis()-start));
        return parent_cid;
    }

    @Cacheable(value = {"category"},key = "#root.methodName",sync = true)
    public Map<String, List<Catalog2Vo>> getCatalogJsonDbWithSpringCache() {
        return getCategoriesDb();
    }

    public Map<String, List<Catalog2Vo>> getCatalogJsonDbWithRedisson() {
        Map<String, List<Catalog2Vo>> categoryMap=null;
        RLock lock = redissonClient.getLock("CatalogJson-Lock");
        lock.lock(10, TimeUnit.SECONDS);
        try {
            Thread.sleep(15000);
            categoryMap = getCategoryMap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            return categoryMap;
        }
    }

    @Override
    public Map<String, List<Catalog2Vo>> getCategoryMap() {
//        List<CategoryEntity> categoryEntities = this.list(new QueryWrapper<CategoryEntity>().eq("cat_level", 2));
//
//        List<Catalog2Vo> catalog2Vos = categoryEntities.stream().map(categoryEntity -> {
//            List<CategoryEntity> level3 = this.list(new QueryWrapper<CategoryEntity>().eq("parent_cid", categoryEntity.getCatId()));
//            List<Catalog2Vo.Catalog3Vo> catalog3Vos = level3.stream().map(cat -> {
//                return new Catalog2Vo.Catalog3Vo(cat.getParentCid().toString(), cat.getCatId().toString(), cat.getName());
//            }).collect(Collectors.toList());
//            Catalog2Vo catalog2Vo = new Catalog2Vo(categoryEntity.getParentCid().toString(), categoryEntity.getCatId().toString(), categoryEntity.getName(), catalog3Vos);
//            return catalog2Vo;
//        }).collect(Collectors.toList());
//        Map<String, List<Catalog2Vo>> catalogMap = new HashMap<>();
//        for (Catalog2Vo catalog2Vo : catalog2Vos) {
//            List<Catalog2Vo> list = catalogMap.getOrDefault(catalog2Vo.getCatalog1Id(), new LinkedList<>());
//            list.add(catalog2Vo);
//            catalogMap.put(catalog2Vo.getCatalog1Id(),list);
//        }
//        return catalogMap;

//        //缓存改写1：使用map作为本地缓存
//        Map<String, List<Catalog2Vo>> catalogMap = (Map<String, List<Catalog2Vo>>) cache.get("catalogMap");
//        if (catalogMap == null) {
//            catalogMap = getCategoriesDb();
//            cache.put("catalogMap",catalogMap);
//        }
//        return catalogMap;

//        //缓存改写2：使用redis作为本地缓存
//        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
//        String catalogJson = ops.get("catalogJson");
//        if (StringUtils.isEmpty(catalogJson)) {
//            Map<String, List<Catalog2Vo>> categoriesDb = getCategoriesDb();
//            String toJSONString = JSON.toJSONString(categoriesDb);
//            ops.set("catalogJson",toJSONString);
//            return categoriesDb;
//        }
//        Map<String, List<Catalog2Vo>> listMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {});
//        return listMap;

        //缓存改写3：加锁解决缓存穿透问题
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        String catalogJson = ops.get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            System.out.println("缓存不命中，准备查询数据库。。。");
//            synchronized (this) {
//                String synCatalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
//                if (StringUtils.isEmpty(synCatalogJson)) {
            Map<String, List<Catalog2Vo>> categoriesDb= getCategoriesDb();
            String toJSONString = JSON.toJSONString(categoriesDb);
            ops.set("catalogJson", toJSONString);
            return categoriesDb;
//                }else {
//                    Map<String, List<Catalog2Vo>> listMap = JSON.parseObject(synCatalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {});
//                    return listMap;
//                }
//            }

        }
        System.out.println("缓存命中。。。。");
        Map<String, List<Catalog2Vo>> listMap = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catalog2Vo>>>() {});
        return listMap;
    }

    //从数据库中查出三级分类
    private  Map<String, List<Catalog2Vo>> getCategoriesDb() {
        System.out.println("查询了数据库");
        //优化业务逻辑，仅查询一次数据库
        List<CategoryEntity> categoryEntities = this.list();
        //查出所有一级分类
        List<CategoryEntity> level1Categories = getCategoryByParentCid(categoryEntities, 0L);
        Map<String, List<Catalog2Vo>> listMap = level1Categories.stream().collect(Collectors.toMap(k->k.getCatId().toString(), v -> {
            //遍历查找出二级分类
            List<CategoryEntity> level2Categories = getCategoryByParentCid(categoryEntities, v.getCatId());
            List<Catalog2Vo> catalog2Vos=null;
            if (level2Categories!=null){
                //封装二级分类到vo并且查出其中的三级分类
                catalog2Vos = level2Categories.stream().map(cat -> {
                    //遍历查出三级分类并封装
                    List<CategoryEntity> level3Catagories = getCategoryByParentCid(categoryEntities, cat.getCatId());
                    List<Catalog2Vo.Catalog3Vo> catalog3Vos = null;
                    if (level3Catagories != null) {
                        catalog3Vos = level3Catagories.stream()
                                .map(level3 -> new Catalog2Vo.Catalog3Vo(level3.getParentCid().toString(), level3.getCatId().toString(), level3.getName()))
                                .collect(Collectors.toList());
                    }
                    Catalog2Vo catalog2Vo = new Catalog2Vo(v.getCatId().toString(), cat.getCatId().toString(), cat.getName(), catalog3Vos);
                    return catalog2Vo;
                }).collect(Collectors.toList());
            }
            return catalog2Vos;
        }));
        return listMap;
    }

    private List<CategoryEntity> getCategoryByParentCid(List<CategoryEntity> categoryEntities, long l) {
        List<CategoryEntity> collect = categoryEntities.stream().filter(cat -> cat.getParentCid() == l).collect(Collectors.toList());
        return collect;
    }
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2、组装成父子的树形结构
        //2.1）、找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
            categoryEntity.getParentCid() == 0
        ).map((menu)->{
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).sorted((menu1,menu2)->{
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());
        return level1Menus;
    }

    @Override
    public void removeMenusByIds(List<Long> asList) {
        //TODO 1.检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        ArrayList<Long> paths = new ArrayList<>();
        findParentPath(catelogId, paths);
        Collections.reverse(paths);
        return (Long[]) paths.toArray(new Long[0]);
    }

    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    private void findParentPath(Long catelogId, List<Long> paths) {
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity,all));
            return categoryEntity;
        }).sorted((menu1,menu2)->{
            //2、菜单的排序
            return (menu1.getSort()==null?0:menu1.getSort()) - (menu2.getSort()==null?0:menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }

}