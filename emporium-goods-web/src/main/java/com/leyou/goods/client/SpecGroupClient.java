package com.leyou.goods.client;

import com.leyou.item.api.SpecGroupApi;
import com.leyou.item.api.SpecParamApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Si6x
 */
@FeignClient("item-service")
public interface SpecGroupClient extends SpecGroupApi {
}
