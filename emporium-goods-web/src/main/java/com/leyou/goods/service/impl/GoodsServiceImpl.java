package com.leyou.goods.service.impl;

import com.leyou.goods.client.BrandClient;
import com.leyou.goods.client.CategoryClient;
import com.leyou.goods.client.GoodsClient;
import com.leyou.goods.client.SpecGroupClient;
import com.leyou.goods.client.SpecParamClient;
import com.leyou.goods.service.GoodsService;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Si6x
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private BrandClient brandClient;
    @Autowired
    private CategoryClient categoryClient;
    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private SpecGroupClient specGroupClient;
    @Autowired
    private SpecParamClient specParamClient;

    /**
     * 组装商品详情页需要的所有数据
     * @param spuId
     * @return
     */
    @Override
    public Map<String,Object> loadData(Long spuId){
        Map<String,Object> model = new HashMap<>();

        //根据spu的id查询spu
        Spu spu = this.goodsClient.querySpuById(spuId);
        //查询spudetail
        SpuDetail spuDetail = this.goodsClient.querySpuDetailBySpuId(spuId);
        //查询分类:Map<String,Object>
        List<Long> cids = Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3());
        List<String> names = this.categoryClient.queryNamesByIds(cids);
        //初始化一个分类的map
        List<Map<String,Object>> categories = new ArrayList<>();
        for (int i = 0; i < cids.size(); i++) {
            Map<String,Object> map = new HashMap<>();
            map.put("id",cids.get(i));
            map.put("name",names.get(i));
            categories.add(map);
        }
        //查询品牌
        Brand brand = brandClient.queryBrandById(spu.getBrandId());
        //查询skus
        List<Sku> skus = this.goodsClient.querySkusBySpuId(spuId);
        //查询参数组合规格参数
        List<SpecGroup> groups = this.specGroupClient.queryGroupsWithParam(spu.getCid3());
        //查询特殊的规格参数
        List<SpecParam> params = this.specParamClient.queryParams(null, spu.getCid3(), false, null);
        //初始化特殊规格参数的map
        Map<Long,String> paramMap = new HashMap<>();
        for (SpecParam param : params) {
            paramMap.put(param.getId(),param.getName());
        }


        model.put("spu",spu);
        model.put("spuDetail",spuDetail);
        model.put("categories",categories);
        model.put("brand",brand);
        model.put("skus",skus);
        model.put("groups",groups);
        model.put("paramMap",paramMap);

        return model;
    }

}
