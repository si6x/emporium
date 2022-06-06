package com.leyou.goods.listener;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Si6x
 */
@Component
public class GoodsListener {

    @Autowired
    private GoodsHtmlService goodsHtmlService;

    /**
     * 处理insert和update的消息添加页面
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.GOODS.WEB.CREATE.QUEUE",durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU.ITEM.EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.insert","item.update"}))
    public void listenCreate(Long id){
        if (id == null){
            return;
        }
        // 创建页面
        this.goodsHtmlService.createHtml(id);
    }

    /**
     * 处理delete的消息删除页面
     * @param id
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "LEYOU.GOODS.WEB.DELETE.QUEUE",durable = "true"),
            exchange = @Exchange(
                    value = "LEYOU.ITEM.EXCHANGE",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC),
            key = {"item.delete"}))
    public void listenDelete(Long id){
        if (id == null){
            return;
        }
        // 删除
        this.goodsHtmlService.deleteHtml(id);
    }

}
