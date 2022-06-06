package com.leyou.item.api;

import com.leyou.item.pojo.SpecGroup;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author Si6x
 */
@RequestMapping("/spec")
public interface SpecGroupApi {

    /**
     * 根据分类id查询参数组和规格参数
     * @param cid
     * @return
     */
    @GetMapping("/group/param/{cid}")
    public List<SpecGroup> queryGroupsWithParam(@PathVariable("cid")Long cid);
}
