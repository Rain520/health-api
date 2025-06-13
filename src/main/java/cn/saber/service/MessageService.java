package cn.saber.service;

import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.MessageQueryDto;
import cn.saber.pojo.entity.Message;
import cn.saber.pojo.vo.MessageVO;

import java.util.List;

/**
 * 消息业务逻辑接口
 */
public interface MessageService {

    Result<Void> save(List<Message> messages);

    Result<Void> evaluationsReplySave(Message message);

    Result<Void> evaluationsUpvoteSave(Message message);

    Result<Void> systemInfoSave(List<Message> messages);

    Result<Void> dataWordSave(List<Message> messages);

    Result<Void> batchDelete(List<Long> ids);

    Result<List<MessageVO>> query(MessageQueryDto messageQueryDto);

    Result<Void> systemInfoUsersSave(Message message);

    Result<Void> clearMessage();


    Result<Void> clearOneMessage(Message message);
}
