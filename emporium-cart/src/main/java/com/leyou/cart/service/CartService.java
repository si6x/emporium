package com.leyou.cart.service;

import com.leyou.cart.pojo.Cart;

import java.util.List;

/**
 * @author Si6x
 */
public interface CartService {

    /**
     * 添加购物车
     * @param cart
     */
    void addCart(Cart cart);

    /**
     * 查询购物车列表
     * @return
     */
    List<Cart> queryCarts();

    /**
     * 修改商品的数量
     * @param cart
     */
    void updateCartGoodsNum(Cart cart);

    /**
     * 删除购物车的商品
     * @param skuId
     */
    void deleteCartGoods(String skuId);

    /**
     * 用户已登录后浏览器的LocalStorage有数据的话添加到服务器中
     * @param carts
     */
    void addLocalStorageGoods(List<Cart> carts);
}
