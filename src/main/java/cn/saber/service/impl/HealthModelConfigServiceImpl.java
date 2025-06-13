package cn.saber.service.impl;

import cn.saber.context.LocalThreadHolder;
import cn.saber.mapper.HealthModelConfigMapper;
import cn.saber.mapper.UserHealthMapper;
import cn.saber.pojo.api.ApiResult;
import cn.saber.pojo.api.PageResult;
import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.HealthModelConfigQueryDto;
import cn.saber.pojo.entity.HealthModelConfig;
import cn.saber.pojo.vo.HealthModelConfigVO;
import cn.saber.service.HealthModelConfigService;
import cn.saber.service.UserHealthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 健康模型业务逻辑实现
 */
@Service
public class HealthModelConfigServiceImpl implements HealthModelConfigService {

    @Resource
    private HealthModelConfigMapper healthModelConfigMapper;
    private UserHealthMapper userHealthMapper;

    /**
     * 健康模型新增
     *
     * @param healthModelConfig 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(HealthModelConfig healthModelConfig) {
        // 将用户的ID设置上
        healthModelConfig.setUserId(LocalThreadHolder.getUserId());
        healthModelConfigMapper.save(healthModelConfig);
        return ApiResult.success();
    }

    /**
     * 健康模型删除
     *
     * @param ids 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Long> ids) {
        healthModelConfigMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 健康模型修改
     *
     * @param healthModelConfig 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(HealthModelConfig healthModelConfig) {
        healthModelConfigMapper.update(healthModelConfig);
        return ApiResult.success();
    }

    /**
     * 查询用户自己配置的模型及全局模型
     *
     * @return Result<List < HealthModelConfigVO>>
     */
    @Override
    public Result<List<HealthModelConfigVO>> modelList() {
        HealthModelConfigQueryDto healthModelConfigQueryDto = new HealthModelConfigQueryDto();
        healthModelConfigQueryDto.setUserId(LocalThreadHolder.getUserId());
        List<HealthModelConfigVO> modelConfigs = healthModelConfigMapper.query(healthModelConfigQueryDto);
        healthModelConfigQueryDto.setUserId(null);
        healthModelConfigQueryDto.setIsGlobal(true);
        List<HealthModelConfigVO> modelConfigsGlobal = healthModelConfigMapper.query(healthModelConfigQueryDto);
        List<HealthModelConfigVO> modelAll = new ArrayList<>();
        modelAll.addAll(modelConfigs);
        modelAll.addAll(modelConfigsGlobal);
        return ApiResult.success(modelAll);
    }

    /**
     * 健康模型查询
     *
     * @param healthModelConfigQueryDto 查询参数
     * @return Result<List < HealthModelConfigVO>>
     */
    @Override
    public Result<List<HealthModelConfigVO>> query(HealthModelConfigQueryDto healthModelConfigQueryDto) {
        List<HealthModelConfigVO> modelConfigs = healthModelConfigMapper.query(healthModelConfigQueryDto);
        Integer totalCount = healthModelConfigMapper.queryCount(healthModelConfigQueryDto);
        return PageResult.success(modelConfigs, totalCount);
    }

}
