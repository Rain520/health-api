package cn.saber.service;

import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.UserHealthQueryDto;
import cn.saber.pojo.entity.UserHealth;
import cn.saber.pojo.vo.ChartVO;
import cn.saber.pojo.vo.UserHealthVO;

import java.util.List;

/**
 * 用户健康记录业务逻辑接口
 */
public interface UserHealthService {

    Result<Void> save(List<UserHealth> userHealths);

    Result<Void> batchDelete(List<Long> ids);

    Result<Void> update(UserHealth userHealth);

    Result<List<UserHealthVO>> query(UserHealthQueryDto userHealthQueryDto);

    Result<List<ChartVO>> daysQuery(Integer day);

}
