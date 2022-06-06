package com.leyou.item.service;

import com.leyou.item.pojo.SpecParam;

import java.util.List;

/**
 * @author Si6x
 */
public interface SpecParamService {

    /**
     * 根据条件查询规格参数
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching);

    /**
     * 添加规格参数
     * @param specParam
     */
    void addParam(SpecParam specParam);

    /**
     * 修改规格参数
     * @param specParam
     */
    void updateParam(SpecParam specParam);

    /**
     * 删除规格参数
     * @param pid
     */
    void deleteParam(Long pid);


}
