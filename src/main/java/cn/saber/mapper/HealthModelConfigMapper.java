package cn.saber.mapper;

import cn.saber.pojo.dto.query.extend.HealthModelConfigQueryDto;
import cn.saber.pojo.entity.HealthModelConfig;
import cn.saber.pojo.vo.HealthModelConfigVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HealthModelConfigMapper {

    void save(HealthModelConfig healthModelConfig);

    void update(HealthModelConfig healthModelConfig);

    void batchDelete(@Param(value = "ids") List<Long> ids);

    List<HealthModelConfigVO> query(HealthModelConfigQueryDto healthModelConfigQueryDto);

    Integer queryCount(HealthModelConfigQueryDto healthModelConfigQueryDto);

}
