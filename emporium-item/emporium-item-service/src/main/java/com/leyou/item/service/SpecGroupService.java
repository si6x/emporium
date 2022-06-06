package com.leyou.item.service;

import com.leyou.item.pojo.SpecGroup;

import java.util.List;

/**
 * @author Si6x
 */
public interface SpecGroupService {
    /**
     * 根据分类id查询参数组
     * @param cid
     * @return
     */
    List<SpecGroup> queryGroupsByCid(Long cid);

    /**
     * 添加参数组
     * @param specGroup
     */
    void addGroup(SpecGroup specGroup);

    /**
     * 修改参数组
     * @param specGroup
     */
    void updateGroup(SpecGroup specGroup);

    /**
     * 删除参数组
     * @param gid
     */
    void deleteGroup(Long gid);

    /**
     * 根据分类id查询参数组和规格参数
     * @param cid
     * @return
     */
    List<SpecGroup> queryGroupsWithParam(Long cid);
}
