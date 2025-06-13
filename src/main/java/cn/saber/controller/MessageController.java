package cn.saber.controller;

import cn.saber.aop.Pager;
import cn.saber.pojo.api.ApiResult;
import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.MessageQueryDto;
import cn.saber.pojo.em.IsReadEnum;
import cn.saber.pojo.em.MessageType;
import cn.saber.pojo.entity.Message;
import cn.saber.pojo.vo.MessageTypeVO;
import cn.saber.pojo.vo.MessageVO;
import cn.saber.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息的 Controller
 */
@RestController
@RequestMapping(value = "/message")
public class MessageController {

    @Resource
    private MessageService messageService;

    /**
     * 查询全部的消息类型
     *
     * @return Result<List < MessageTypeVO>> 通用响应体
     * // 系统通知 ： 一种是全站用户都需要接收的；一种是向指定用户发送指定消息的。
     */
    @GetMapping(value = "/types")
    public Result<List<MessageTypeVO>> all() {
        MessageType[] messageTypes = MessageType.values();
        List<MessageTypeVO> messageTypeVOS = new ArrayList<>();
        for (MessageType messageType : messageTypes) {
            MessageTypeVO messageTypeVO = new MessageTypeVO(messageType.getType(), messageType.getDetail());
            messageTypeVOS.add(messageTypeVO);
        }
        return ApiResult.success(messageTypeVOS);
    }

    /**
     * 全站的系统通知
     *
     * @param message 消息通知集合
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/systemInfoUsersSave")
    public Result<Void> systemInfoUsersSave(@RequestBody Message message) {
        return messageService.systemInfoUsersSave(message);
    }

    /**
     * 消息通知
     *
     * @param messages 消息通知集合
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/systemInfoSave")
    public Result<Void> systemInfoSave(@RequestBody List<Message> messages) {
        messages.forEach(message -> {
            message.setMessageType(MessageType.SYSTEM_INFO.getType());
            message.setIsRead(IsReadEnum.READ_NO.getStatus());
            message.setCreateTime(LocalDateTime.now());
        });
        return messageService.systemInfoSave(messages);
    }


    /**
     * 消息删除
     *
     * @param ids 要删除的消息ID列表
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        return messageService.batchDelete(ids);
    }


    /**
     * 将全部消息设置为已读
     *
     * @return Result<Void> 响应
     */
    @PutMapping(value = "/clearMessage")
    public Result<Void> clearMessage() {
        return messageService.clearMessage();
    }


    /**
     * 将消息设置为已读
     *
     * @return Result<Void> 响应
     */
    @PutMapping(value = "/clearOneMessage")
    public Result<Void> clearOneMessage(@RequestBody Message message) {
        return messageService.clearOneMessage(message);
    }


    /**
     * 消息查询
     *
     * @param messageQueryDto 查询参数
     * @return Result<List < MessageVO>> 通用响应
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<MessageVO>> query(@RequestBody MessageQueryDto messageQueryDto) {
        return messageService.query(messageQueryDto);
    }

}
