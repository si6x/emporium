package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import com.leyou.item.api.CategoryApi;
import com.leyou.item.api.GoodsApi;
import com.leyou.item.api.SpecParamApi;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.search.pojo.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Si6x
 */
@FeignClient("item-service")
public interface GoodsClient extends GoodsApi {

}
