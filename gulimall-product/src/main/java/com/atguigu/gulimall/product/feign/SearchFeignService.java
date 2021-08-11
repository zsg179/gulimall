package com.atguigu.gulimall.product.feign;

import com.atguigu.common.to.to.SkuEsModel;
import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @Auther :朱树广
 * @Date :2021/3/14
 * @Description :com.atguigu.gulimall.product.feign
 * @Version :1.0
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {

    @PostMapping("/search/save/product")
    R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
