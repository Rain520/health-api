package cn.saber.service;

import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.HealthModelConfigQueryDto;
import cn.saber.pojo.entity.HealthModelConfig;
import cn.saber.pojo.vo.HealthModelConfigVO;

import java.util.List;

/**
 * 健康模型业务逻辑接口
 */
public interface HealthModelConfigService {

    Result<Void> save(HealthModelConfig healthModelConfig);

    Result<Void> batchDelete(List<Long> ids);

    Result<Void> update(HealthModelConfig healthModelConfig);

    Result<List<HealthModelConfigVO>> query(HealthModelConfigQueryDto healthModelConfigQueryDto);

    Result<List<HealthModelConfigVO>> modelList();


}
