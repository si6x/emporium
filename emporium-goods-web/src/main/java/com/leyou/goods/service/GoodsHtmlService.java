package com.leyou.goods.service;

/**
 * @author Si6x
 */
public interface GoodsHtmlService {

    /**
     * 创建一个静态化页面
     * @param spuId
     */
    public void createHtml(Long spuId);

    /**
     * 删除一个静态化页面
     * @param spuId
     */
    void deleteHtml(Long spuId);
}
