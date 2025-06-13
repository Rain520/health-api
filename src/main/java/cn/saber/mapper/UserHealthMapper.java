package cn.saber.mapper;

import cn.saber.pojo.dto.query.extend.UserHealthQueryDto;
import cn.saber.pojo.entity.UserHealth;
import cn.saber.pojo.vo.UserHealthVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户健康记录持久化接口
 */
@Mapper
public interface UserHealthMapper {

    void batchSave(List<UserHealth> userHealths);

    void update(UserHealth userHealth);

    void batchDelete(@Param(value = "ids") List<Long> ids);

    List<UserHealthVO> query(UserHealthQueryDto userHealthQueryDto);

    Integer queryCount(UserHealthQueryDto userHealthQueryDto);

}
