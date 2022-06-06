package com.leyou.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leyou.common.pojo.PageResult;
import com.leyou.item.bo.SpuBo;
import com.leyou.item.mapper.BrandMapper;
import com.leyou.item.mapper.SkuMapper;
import com.leyou.item.mapper.SpuDetailMapper;
import com.leyou.item.mapper.SpuMapper;
import com.leyou.item.mapper.StockMapper;
import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Sku;
import com.leyou.item.pojo.Spu;
import com.leyou.item.pojo.SpuDetail;
import com.leyou.item.pojo.Stock;
import com.leyou.item.service.CategoryService;
import com.leyou.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Si6x
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private SpuMapper spuMapper;
    @Resource
    private SpuDetailMapper spuDetailMapper;
    @Resource
    private BrandMapper brandMapper;
    @Resource
    private SkuMapper skuMapper;
    @Resource
    private StockMapper stockMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 根据条件分页查询spu
     * @param key
     * @param saleable
     * @param page
     * @param rows
     * @return
     */
    @Override
    public PageResult<SpuBo> querySpuByPage(String key, Boolean saleable, Integer page, Integer rows) {
        Example example = new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //添加查询条件
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%" + key + "%");
        }

        //添加上下架的过滤条件
        if (saleable != null){
            criteria.andEqualTo("saleable",saleable);
        }

        //添加分页
        PageHelper.startPage(page,rows);

        //执行查询，获取spu集合
        List<Spu> spus = this.spuMapper.selectByExample(example);
        PageInfo<Spu> spuPageInfo = new PageInfo<>(spus);

        //spu集合转换成spubo集合
        List<SpuBo> spuBos = spus.stream().map(spu -> {
            SpuBo spuBo = new SpuBo();
            BeanUtils.copyProperties(spu, spuBo);

            //查询品牌名称
            Brand brand = this.brandMapper.selectByPrimaryKey(spuBo.getBrandId());
            spuBo.setBname(brand.getName());
            //查询分类名称
            List<String> names = categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            spuBo.setCname(StringUtils.join(names, "-"));
            return spuBo;
        }).collect(Collectors.toList());

        //返回pageResult<spuBo>
        return new PageResult<>(spuPageInfo.getTotal(),spuBos);
    }

    /**
     * 新增商品
     * @param spuBo
     */
    @Override
    @Transactional
    public void saveGoods(SpuBo spuBo) {

        //先新增spu
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        //再去新增spudetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        saveSkuAndStock(spuBo);

        sendMsg("insert",spuBo.getId());
    }

    /**
     * 发送消息到mq的方法
     * @param type
     * @param id
     */
    private void sendMsg(String type,Long id) {
        //发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (AmqpException e) {
            e.printStackTrace();
        }
    }

    /**
     * 抽取新增sku和stock的方法
     * @param spuBo
     */
    private void saveSkuAndStock(SpuBo spuBo) {
        for (Sku sku : spuBo.getSkus()) {
            //新增sku
            sku.setId(null);
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            //新增stock
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        }
    }

    /**
     * 根据spuId查询spuDetail
     * @param spuId
     * @return
     */
    @Override
    public SpuDetail querySpuDetailBySpuId(Long spuId) {
        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据spuId查询sku集合
     * @param spuId
     * @return
     */
    @Override
    public List<Sku> querySkusBySpuId(Long spuId) {
        Sku record = new Sku();
        record.setSpuId(spuId);
        List<Sku> skus = this.skuMapper.select(record);
        for (Sku sku : skus) {
            Stock stock = this.stockMapper.selectByPrimaryKey(sku.getId());
            sku.setStock(stock.getStock());
        }
        return skus;
    }

    /**
     * 修改商品信息
     * @param spuBo
     */
    @Override
    @Transactional
    public void updateGoods(SpuBo spuBo) {
        //根据spuId查询要删除的sku
        Sku record = new Sku();
        record.setSpuId(spuBo.getId());
        List<Sku> skus = this.skuMapper.select(record);
        for (Sku sku : skus) {
            //删除stock
            this.stockMapper.deleteByPrimaryKey(sku.getId());
        }

        //删除sku
        Sku sku = new Sku();
        sku.setSpuId(spuBo.getId());
        this.skuMapper.delete(sku);

        //修改spu和spuDetail
        spuBo.setSaleable(null);
        spuBo.setValid(null);
        spuBo.setCreateTime(null);
        spuBo.setLastUpdateTime(new Date());
        this.spuMapper.updateByPrimaryKeySelective(spuBo);
        this.spuDetailMapper.updateByPrimaryKeySelective(spuBo.getSpuDetail());

        //新增sku和stock
        saveSkuAndStock(spuBo);

        sendMsg("update",spuBo.getId());

    }

    /**
     * 删除商品信息,实则是修改spu表中的valid变成false
     * @param spuId
     */
    @Override
    public void deleteGoods(Long spuId) {
        Spu record = new Spu();
        record.setId(spuId);
        record.setValid(false);
        this.spuMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 下上架商品,实则修改spu表中的saleable变成true或者false
     * @param spuId
     * @param saleable
     */
    @Override
    public void offAndOnGoods(Long spuId, Boolean saleable){
        Spu record = new Spu();
        record.setId(spuId);
        record.setSaleable(saleable);
        this.spuMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 根据spuId查询spu
     * @param id
     * @return
     */
    @Override
    public Spu querySpuById(Long id) {
        return this.spuMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据skuId查询Sku
     * @param skuId
     * @return
     */
    @Override
    public Sku querySkuBySkuId(Long skuId) {
        return this.skuMapper.selectByPrimaryKey(skuId);
    }
}
