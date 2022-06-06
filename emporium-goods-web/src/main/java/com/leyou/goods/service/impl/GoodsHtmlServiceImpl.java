package com.leyou.goods.service.impl;

import com.leyou.goods.service.GoodsHtmlService;
import com.leyou.goods.service.GoodsService;
import org.checkerframework.checker.nullness.qual.AssertNonNullIfNonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

/**
 * @author Si6x
 */
@Service
public class GoodsHtmlServiceImpl implements GoodsHtmlService {

    @Autowired
    private TemplateEngine engine;

    @Autowired
    private GoodsService goodsService;

    /**
     * 创建一个静态化页面
     * @param spuId
     */
    @Override
    public void createHtml(Long spuId) {
        //初始化运行上下文
        Context context = new Context();
        //设置数据模型
        context.setVariables(this.goodsService.loadData(spuId));

        PrintWriter printWriter = null;
        try {
            //把静态文件生成到服务器本地
            File file = new File("D:\\IDEA\\nginx-1.20.2\\html\\item\\" + spuId + ".html");
            printWriter = new PrintWriter(file);

            this.engine.process("item",context,printWriter);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null){
                printWriter.close();
            }
        }

    }

    /**
     * 删除一个静态化页面后
     * @param spuId
     */
    @Override
    public void deleteHtml(Long spuId) {
        File file = new File("D:\\IDEA\\nginx-1.20.2\\html\\item\\" + spuId + ".html");
        file.deleteOnExit();
    }
}
