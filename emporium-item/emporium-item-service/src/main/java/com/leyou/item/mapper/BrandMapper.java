package com.leyou.item.mapper;

import com.leyou.item.pojo.Brand;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Si6x
 */
public interface BrandMapper extends Mapper<Brand> {

    /**
     * 新增品牌和商品类目中间表
     * @param cid
     * @param bid
     */
    @Insert("insert into leyou.tb_category_brand(category_id,brand_id) values(#{cid},#{bid})")
    void insertCategoryAndBrand(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据品牌id删除品牌和商品类目的中间表
     * @param bid
     */
    @Delete("delete from leyou.tb_category_brand where tb_category_brand.brand_id = #{bid}")
    void deleteCategoryAndBrand(Long bid);

    /**
     * 根据分类cid查询此分类下的所有品牌
     * @param cid
     * @return
     */
    @Select("SELECT * FROM tb_brand a INNER JOIN tb_category_brand b ON a.id = b.brand_id WHERE b.category_id = #{cid}")
    List<Brand> selectBransByCid(Long cid);
}
