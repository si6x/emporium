package com.leyou.item.service;

import com.leyou.common.pojo.PageResult;
import com.leyou.item.pojo.Brand;

import java.util.List;

/**
 * @author Si6x
 */
public interface BrandService {

    /**
     * 根据查询条件分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    void saveBrand(Brand brand, List<Long> cids);

    /**
     * 修改品牌
     * @param brand
     * @param cids
     */
    void updateBrand(Brand brand, List<Long> cids);

    /**
     * 删除品牌
     * @param bid
     */
    void deleteBrand(Long bid);

    /**
     * 根据分类cid查询此分类下的所有品牌
     * @param cid
     * @return
     */
    List<Brand> queryBrandsByCid(Long cid);

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    Brand queryBrandById(Long id);
}
