package com.leyou.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Spu;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;

/**
 * @author Si6x
 */
public interface SearchService {

    /**
     * 构建一个Goods对象
     * @param spu
     * @return
     * @throws Exception
     */
    public Goods buildGoods(Spu spu) throws Exception;

    /**
     * 查询条件并分页
     * @param request
     * @return
     */
    SearchResult search(SearchRequest request);


    /**
     * 添加或更新索引
     * @param id
     * @throws JsonProcessingException
     */
    void createIndex(Long id) throws JsonProcessingException;

    /**
     * 删除索引
     * @param id
     */
    void deleteIndex(Long id);
}
