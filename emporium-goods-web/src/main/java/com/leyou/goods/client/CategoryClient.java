package com.leyou.goods.client;

import com.leyou.item.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Si6x
 */
@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
