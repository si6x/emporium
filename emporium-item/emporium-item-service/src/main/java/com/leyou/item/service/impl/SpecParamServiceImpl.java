package com.leyou.item.service.impl;

import com.leyou.item.mapper.SpecParamMapper;
import com.leyou.item.pojo.SpecParam;
import com.leyou.item.service.SpecParamService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Si6x
 */
@Service
public class SpecParamServiceImpl implements SpecParamService {

    @Resource
    private SpecParamMapper specParamMapper;

    /**
     * 根据条件查询规格参数
     * @param gid
     * @param cid
     * @param generic
     * @param searching
     * @return
     */
    @Override
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        SpecParam record = new SpecParam();
        record.setGroupId(gid);
        record.setCid(cid);
        record.setGeneric(generic);
        record.setSearching(searching);
        return this.specParamMapper.select(record);
    }

    /**
     * 添加规格参数
     * @param specParam
     */
    @Override
    public void addParam(SpecParam specParam) {
        this.specParamMapper.insertSelective(specParam);
    }

    /**
     * 修改规格参数
     * @param specParam
     */
    @Override
    public void updateParam(SpecParam specParam) {
        this.specParamMapper.updateByPrimaryKey(specParam);
    }

    /**
     * 删除规格参数
     * @param pid
     */
    @Override
    public void deleteParam(Long pid) {
        this.specParamMapper.deleteByPrimaryKey(pid);
    }


}
