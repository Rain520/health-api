package cn.saber.mapper;

import cn.saber.pojo.dto.query.extend.MessageQueryDto;
import cn.saber.pojo.entity.Message;
import cn.saber.pojo.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息持久化接口
 */
@Mapper
public interface MessageMapper {

    void batchSave(List<Message> messages);

    void update(@Param(value = "userId") Integer userId,
                @Param(value = "isRead") Boolean isRead);

    void batchDelete(@Param(value = "ids") List<Long> ids);

    List<MessageVO> query(MessageQueryDto messageQueryDto);

    Integer queryCount(MessageQueryDto messageQueryDto);

    void updateOne(@Param(value = "id") Integer id,
                @Param(value = "isRead") Boolean isRead);
}
