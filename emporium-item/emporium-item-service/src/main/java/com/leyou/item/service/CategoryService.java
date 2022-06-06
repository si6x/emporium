package com.leyou.item.service;

import com.leyou.item.pojo.Category;

import java.util.List;

/**
 * @author Si6x
 */
public interface CategoryService {

    /**
     * 根据父节点的id查询子节点
     * @param pid
     * @return
     */
    List<Category> queryCategoryByPid(Long pid);

    /**
     * 根据品牌id查询商品分类
     * @param bid
     * @return
     */
    List<Category> queryByBrandId(Long bid);

    /**
     * 根据多个id查询商品分类
     * @return
     */
    List<String> queryNamesByIds(List<Long> ids);

    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    List<Category> queryAllByCid3(Long id);
}
