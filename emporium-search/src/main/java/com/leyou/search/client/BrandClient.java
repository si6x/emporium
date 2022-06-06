package com.leyou.search.client;

import com.leyou.item.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Si6x
 */
@FeignClient("item-service")
public interface BrandClient extends BrandApi {
}
