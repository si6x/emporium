package com.leyou.item.mapper;

import com.leyou.item.pojo.Category;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.additional.idlist.SelectByIdListMapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Si6x
 */

public interface CategoryMapper extends Mapper<Category>, SelectByIdListMapper<Category,Long> {
    /**
     * 根据品牌id查询商品分类
     * @param bid
     * @return
     */
    @Select("select * from tb_category where id in (select tb_category_brand.category_id from tb_category_brand where tb_category_brand.brand_id = #{bid})")
    List<Category> queryByBrandId(Long bid);
}
