package com.leyou.cart.service.impl;

import com.leyou.cart.client.GoodsClient;
import com.leyou.cart.interceptor.LoginInterceptor;
import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import com.leyou.common.pojo.UserInfo;
import com.leyou.common.utils.JsonUtils;
import com.leyou.item.pojo.Sku;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import rx.internal.operators.BackpressureUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Si6x
 */
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String KEY_PREFIX = "user:cart:";

    @Autowired
    private GoodsClient goodsClient;

    /**
     * 添加购物车
     * @param cart
     */
    @Override
    public void addCart(Cart cart) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        String key = cart.getSkuId().toString();
        Integer num = cart.getNum();

        //判断当前的商品是否在购物车中
        if (hashOperations.hasKey(key)){
            //在，更新数量
            String cartJson = hashOperations.get(key).toString();
            cart = JsonUtils.parse(cartJson,Cart.class);
            cart.setNum(cart.getNum() + num);
        }else {
            //不在，新增购物车
            Sku sku = goodsClient.querySkuBySkuId(cart.getSkuId());
            cart.setUserId(userInfo.getId());
            cart.setTitle(sku.getTitle());
            cart.setPrice(sku.getPrice());
            cart.setOwnSpec(sku.getOwnSpec());
            cart.setImage(StringUtils.isBlank(sku.getImages()) ? "" : StringUtils.split(sku.getImages(),",")[0]);
        }
        hashOperations.put(key,JsonUtils.serialize(cart));


    }

    /**
     * 查询购物车列表
     * @return
     */
    @Override
    public List<Cart> queryCarts() {
        // 获取登录用户
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        // 判断用户是否有购物车记录
        String key = KEY_PREFIX + userInfo.getId();
        if (!this.redisTemplate.hasKey(key)){
            // 不存在，直接返回
            return null;
        }

        //获取用户的购物车信息
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);
        List<Object> cartsJson = hashOperations.values();

        // 判断购物车集合为空,直接返回null
        if (CollectionUtils.isEmpty(cartsJson)){
            return null;
        }
        // 把List<Object>集合转化为List<Cart>集合
        return cartsJson.stream().map(cartJson -> JsonUtils.parse(cartJson.toString(),Cart.class)).collect(Collectors.toList());


    }

    /**
     * 修改商品的数量
     * @param cart
     */
    @Override
    public void updateCartGoodsNum(Cart cart) {
        //获取登陆信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        //判断用户是否有购物车记录
        String key = KEY_PREFIX + userInfo.getId();
        if (!this.redisTemplate.hasKey(key)){
            //不存在，直接返回
            return;
        }

        Integer num = cart.getNum();
        //获取hash操作对象
        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(key);

        //获取购物车信息
        String cartJson = hashOperations.get(cart.getSkuId().toString()).toString();
        cart = JsonUtils.parse(cartJson, Cart.class);

        //更新数量
        cart.setNum(num);
        //写入购物车
        hashOperations.put(cart.getSkuId().toString(),JsonUtils.serialize(cart));

    }

    /**
     * 删除购物车的商品
     * @param skuId
     */
    @Override
    public void deleteCartGoods(String skuId) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();

        BoundHashOperations<String, Object, Object> hashOperations = this.redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());

        hashOperations.delete(skuId);
    }

    /**
     *
     * @param carts
     */
    @Override
    public void addLocalStorageGoods(List<Cart> carts) {
        //获取用户信息
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        //查询购物车记录
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        //遍历
        for (Cart cart : carts) {
            String key = cart.getSkuId().toString();
            //判断当前商品是否存在,如果存在则添加商品的数量
            if (hashOperations.hasKey(key)){
                String cartJson = hashOperations.get(key).toString();
                Cart redisCart = JsonUtils.parse(cartJson, Cart.class);
                cart.setNum(cart.getNum() + redisCart.getNum());
            }

            //如果商品不存在，正常添加
            cart.setUserId(userInfo.getId());
            hashOperations.put(key,JsonUtils.serialize(cart));
        }

    }
}
