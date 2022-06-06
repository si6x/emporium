package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Si6x
 */
@Service
public class BrandServiceImpl implements BrandService {

    @Resource
    private BrandMapper brandMapper;

    /**
     * 根据查询条件分页查询品牌信息
     * @param key
     * @param page
     * @param rows
     * @param sortBy
     * @param desc
     * @return
     */
    @Override
    public PageResult<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        //初始化example对象
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();

        //根据name模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("name","%"+key+"%").orEqualTo("letter",key);
        }

        //添加分页条件
        PageHelper.startPage(page,rows);

        //添加排序条件
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }

        List<Brand> brands = this.brandMapper.selectByExample(example);

        //包装成pageinfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);

        //包装成分页结果集返回
        return new PageResult<>(pageInfo.getTotal(),pageInfo.getList());

    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @Override
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {
        //先新增brand
        this.brandMapper.insertSelective(brand);

        //再新增中间表
        for (Long cid : cids) {
            this.brandMapper.insertCategoryAndBrand(cid,brand.getId());
        }

    }

    /**
     * 修改品牌
     * @param brand
     * @param cids
     */
    @Override
    @Transactional
    public void updateBrand(Brand brand, List<Long> cids) {
        //先修改brand
        this.brandMapper.updateByPrimaryKey(brand);
        //修改中间表删除后再添加
        this.brandMapper.deleteCategoryAndBrand(brand.getId());
        for (Long cid : cids) {
            this.brandMapper.insertCategoryAndBrand(cid,brand.getId());
        }
    }

    /**
     * 删除品牌
     * @param bid
     */
    @Override
    @Transactional
    public void deleteBrand(Long bid) {
        this.brandMapper.deleteByPrimaryKey(bid);
        this.brandMapper.deleteCategoryAndBrand(bid);
    }

    /**
     * 根据分类cid查询此分类下的所有品牌
     * @param cid
     * @return
     */
    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.selectBransByCid(cid);
    }

    /**
     * 根据id查询品牌
     * @param id
     * @return
     */
    @Override
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }
}
