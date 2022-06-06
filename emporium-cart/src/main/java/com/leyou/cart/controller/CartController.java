package com.leyou.cart.controller;

import com.leyou.cart.pojo.Cart;
import com.leyou.cart.service.CartService;
import org.apache.coyote.OutputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author Si6x
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 添加购物车
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        this.cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 查询购物车列表
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> carts = this.cartService.queryCarts();
        if (CollectionUtils.isEmpty(carts)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物车商品的数量
     * @param cart
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCartGoodsNum(@RequestBody Cart cart){
        this.cartService.updateCartGoodsNum(cart);
        return ResponseEntity.noContent().build();
    }


    /**
     * 删除购物车的商品
     * @param skuId
     * @return
     */
    @DeleteMapping("{skuId}")
    public ResponseEntity<Void> deleteCartGoods(@PathVariable("skuId") String skuId){
        this.cartService.deleteCartGoods(skuId);
        return ResponseEntity.ok().build();
    }

    /**
     * 用户已登录后浏览器的LocalStorage有数据的话添加到服务器中
     * @param cartsMap
     * @return
     */
    @PostMapping("/addLocalStorage")
    public ResponseEntity<Void> addLocalStorageGoods(@RequestBody Map<String,List<Cart>> cartsMap){
        //获取前端传过来的map的值
        List<Cart> carts = cartsMap.get("carts");
        if (CollectionUtils.isEmpty(carts)){
            return ResponseEntity.badRequest().build();
        }
        this.cartService.addLocalStorageGoods(carts);
        return ResponseEntity.ok().build();
    }


}
