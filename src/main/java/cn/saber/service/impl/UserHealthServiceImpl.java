package cn.saber.service.impl;

import cn.saber.context.LocalThreadHolder;
import cn.saber.mapper.HealthModelConfigMapper;
import cn.saber.mapper.UserHealthMapper;
import cn.saber.pojo.api.ApiResult;
import cn.saber.pojo.api.PageResult;
import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.base.QueryDto;
import cn.saber.pojo.dto.query.extend.HealthModelConfigQueryDto;
import cn.saber.pojo.dto.query.extend.UserHealthQueryDto;
import cn.saber.pojo.em.IsReadEnum;
import cn.saber.pojo.em.MessageType;
import cn.saber.pojo.entity.HealthModelConfig;
import cn.saber.pojo.entity.Message;
import cn.saber.pojo.entity.UserHealth;
import cn.saber.pojo.vo.ChartVO;
import cn.saber.pojo.vo.HealthModelConfigVO;
import cn.saber.pojo.vo.UserHealthVO;
import cn.saber.service.MessageService;
import cn.saber.service.UserHealthService;
import cn.saber.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户健康记录业务逻辑实现
 */
@Service
public class UserHealthServiceImpl implements UserHealthService {

    @Resource
    private UserHealthMapper userHealthMapper;
    @Resource
    private HealthModelConfigMapper healthModelConfigMapper;
    @Resource
    private MessageService messageService;

    /**
     * 用户健康记录新增
     *
     * @param userHealths 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(List<UserHealth> userHealths) {
        dealMessage(userHealths);
        dealRole(userHealths);
        userHealthMapper.batchSave(userHealths);
        return ApiResult.success();
    }

    public void dealRole(List<UserHealth> userHealths) {
        LocalDateTime nowTime = LocalDateTime.now();
        // 获取当前用户的ID
        Integer userId = LocalThreadHolder.getUserId();
        userHealths.forEach(userHealth -> {
            userHealth.setUserId(userId);
            userHealth.setCreateTime(nowTime);
        });
    }

    /**
     * 如果有异常指标情况，此方法做通知转发
     *
     * @param userHealths 用户健康记录集合
     */
    private void dealMessage(List<UserHealth> userHealths) {
        List<Message> messageList = new ArrayList<>();
        userHealths.forEach(userHealth -> {
            userHealth.setCreateTime(LocalDateTime.now());
            Integer healthModelConfigId = userHealth.getHealthModelConfigId();
            HealthModelConfigQueryDto queryDto = new HealthModelConfigQueryDto();
            queryDto.setId(healthModelConfigId);
            List<HealthModelConfigVO> healthModelConfigs = healthModelConfigMapper.query(queryDto);
            if (!CollectionUtils.isEmpty(healthModelConfigs)) {
                HealthModelConfig healthModelConfig = healthModelConfigs.get(0);
                // 值范围
                String valueRange = healthModelConfig.getValueRange();
                String[] values = valueRange.split(",");
                // 最小值
                int mixValue = Integer.parseInt(values[0]);
                // 最大值
                int maxValue = Integer.parseInt(values[1]);
                // 如果用户输入的指标是超出正常范围的，需要通知用户处理
                double value = Double.parseDouble(String.valueOf(userHealth.getValue()));
                // 异常情况
                if (value < mixValue || value > maxValue) {
                    // 封装消息体
                    Message message = sendMessage(healthModelConfig, userHealth);
                    messageList.add(message);
                }
            }
        });
        if (!CollectionUtils.isEmpty(messageList)) {
            // 丢给消息业务逻辑处理
            messageService.dataWordSave(messageList);
        }
    }

    /**
     * 处理符合消息通知的用户健康记录
     *
     * @param userHealth 用户健康记录
     * @return Message
     */
    private Message sendMessage(HealthModelConfig healthModelConfig, UserHealth userHealth) {
        Message message = new Message();
        // 指标提醒类通知
        message.setMessageType(MessageType.DATA_MESSAGE.getType());
        // 消息提醒时间
        message.setCreateTime(LocalDateTime.now());
        // 是否已读
        message.setIsRead(IsReadEnum.READ_NO.getStatus());
        // 接收者
        message.setReceiverId(LocalThreadHolder.getUserId());
        // 消息体
        message.setContent("你记录的【" + healthModelConfig.getName() + "】不正常，正常值范围:[" + healthModelConfig.getValueRange() + "]，请注意休息，注意调整。必要时咨询专业人士!");
        return message;
    }

    /**
     * 用户健康记录删除
     *
     * @param ids 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Long> ids) {
        userHealthMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 用户健康记录修改
     *
     * @param userHealth 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(UserHealth userHealth) {
        userHealthMapper.update(userHealth);
        return ApiResult.success();
    }

    /**
     * 用户健康记录查询
     *
     * @param userHealthQueryDto 查询参数
     * @return Result<List < UserHealthVO>>
     */
    @Override
    public Result<List<UserHealthVO>> query(UserHealthQueryDto userHealthQueryDto) {
        List<UserHealthVO> userHealthVOS = userHealthMapper.query(userHealthQueryDto);
        Integer totalCount = userHealthMapper.queryCount(userHealthQueryDto);
        return PageResult.success(userHealthVOS, totalCount);
    }

    /**
     * 统计模型存量数据
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @Override
    public Result<List<ChartVO>> daysQuery(Integer day) {
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        UserHealthQueryDto userHealthQueryDto = new UserHealthQueryDto();
        userHealthQueryDto.setStartTime(queryDto.getStartTime());
        userHealthQueryDto.setEndTime(queryDto.getEndTime());
        List<UserHealthVO> userHealthVOS = userHealthMapper.query(userHealthQueryDto);
        List<LocalDateTime> localDateTimes = userHealthVOS.stream().map(UserHealthVO::getCreateTime).collect(Collectors.toList());
        List<ChartVO> chartVOS = DateUtil.countDatesWithinRange(day, localDateTimes);
        return ApiResult.success(chartVOS);
    }



}
