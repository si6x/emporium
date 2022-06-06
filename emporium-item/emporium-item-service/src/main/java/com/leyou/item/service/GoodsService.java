package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;

import java.util.List;

/**
 * @author Si6x
 */
public interface GoodsService {

    /**
     * 根据条件分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows);

    /**
     * 新增商品
     * @param spuBo
     */
    void saveGoods(SpuBo spuBo);

    /**
     * 根据spuId查询spuDetail
     * @param spuId
     * @return
     */
    SpuDetail querySpuDetailBySpuId(Long spuId);

    /**
     * 根据spuId查询sku集合
     * @param spuId
     * @return
     */
    List<Sku> querySkusBySpuId(Long spuId);

    /**
     * 修改商品信息
     * @param spuBo
     */
    void updateGoods(SpuBo spuBo);

    /**
     * 删除商品信息,实则是修改spu表中的valid变成false
     * @param spuId
     */
    void deleteGoods(Long spuId);

    /**
     * 下上架商品,实则修改spu表中的saleable变成true或者false
     * @param spuId
     * @param saleable
     */
    void offAndOnGoods(Long spuId,Boolean saleable);

    /**
     * 根据spuid查询spu
     * @param id
     * @return
     */
    Spu querySpuById(Long id);

    /**
     *根据skuId查询Sku
     * @param skuId
     * @return
     */
    Sku querySkuBySkuId(Long skuId);
}
