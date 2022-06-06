package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecGroupMapper;
import com.leyou.item.pojo.SpecGroup;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecGroupService;
import com.leyou.item.service.SpecParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Si6x
 */
@Service
public class SpecGroupServiceImpl implements SpecGroupService {

    @Resource
    private SpecGroupMapper specGroupMapper;

    @Autowired
    private SpecParamService specParamService;

    /**
     * 根据分类id查询参数组
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroup record = new SpecGroup();
        record.setCid(cid);
        return this.specGroupMapper.select(record);
    }

    /**
     * 添加参数组
     * @param specGroup
     */
    @Override
    public void addGroup(SpecGroup specGroup) {
        this.specGroupMapper.insertSelective(specGroup);
    }

    /**
     * 修改参数组
     * @param specGroup
     */
    @Override
    public void updateGroup(SpecGroup specGroup) {
        this.specGroupMapper.updateByPrimaryKey(specGroup);
    }

    /**
     * 删除参数组
     * @param gid
     */
    @Override
    public void deleteGroup(Long gid) {
        this.specGroupMapper.deleteByPrimaryKey(gid);
    }

    /**
     * 根据分类id查询参数组和规格参数
     * @param cid
     * @return
     */
    @Override
    public List<SpecGroup> queryGroupsWithParam(Long cid) {
        List<SpecGroup> groups = this.queryGroupsByCid(cid);
        for (SpecGroup group : groups) {
            List<SpecParam> params = this.specParamService.queryParams(group.getId(), null, null, null);
            group.setParams(params);
        }
        return groups;
    }
}
