package com.leyou.goods.service;

import java.util.Map;

public interface GoodsService {

    /**
     * 组装商品详情页需要的所有数据
     * @param spuId
     * @return
     */
    public Map<String,Object> loadData(Long spuId);
}
