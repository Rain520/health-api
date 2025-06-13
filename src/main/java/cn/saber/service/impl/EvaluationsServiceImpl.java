package cn.saber.service.impl;

import cn.saber.context.LocalThreadHolder;
import cn.saber.mapper.EvaluationsMapper;
import cn.saber.mapper.UserMapper;
import cn.saber.pojo.api.ApiResult;
import cn.saber.pojo.api.PageResult;
import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.EvaluationsQueryDto;
import cn.saber.pojo.dto.query.extend.UserQueryDto;
import cn.saber.pojo.entity.*;
import cn.saber.pojo.vo.CommentChildVO;
import cn.saber.pojo.vo.CommentParentVO;
import cn.saber.pojo.vo.EvaluationsVO;
import cn.saber.service.EvaluationsService;
import cn.saber.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 评论服务实现类
 */
@Service
public class EvaluationsServiceImpl implements EvaluationsService {

    @Resource
    private EvaluationsMapper evaluationsMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private MessageService messageService;

    /**
     * 评论
     *
     * @return Result<String>
     */
    @Override
    public Result<Object> insert(Evaluations evaluations) {
        evaluations.setCommenterId(LocalThreadHolder.getUserId());
        User queryConditionEntity = User.builder().id(LocalThreadHolder.getUserId()).build();
        User user = userMapper.getByActive(queryConditionEntity);
        if (user.getIsWord()) {
            return ApiResult.error("账户已被禁言");
        }

        // 发通知
        if (evaluations.getParentId() != null && !Objects.equals(evaluations.getReplierId(), evaluations.getCommenterId()))
            messageService.evaluationsReplySave(sendMessage(evaluations));


        evaluations.setCreateTime(LocalDateTime.now());
        evaluationsMapper.save(evaluations);
        return ApiResult.success("评论成功");
    }

    /**
     * 处理符合消息通知的用户健康记录
     *
     * @param evaluations 评论记录
     * @return Message
     */
    private Message sendMessage(Evaluations evaluations) {
        Message message = new Message();
        UserQueryDto userQueryDto = new UserQueryDto();

        // 接收者
        if (evaluations.getReplierId() == null) {
            message.setReceiverId(queryIdByParentId(evaluations.getParentId()));
            userQueryDto.setId(queryIdByParentId(evaluations.getParentId()));
        } else {
            message.setReceiverId(evaluations.getReplierId());
//            userQueryDto.setId(evaluations.getReplierId());
            userQueryDto.setId(LocalThreadHolder.getUserId());
        }
        //发送者
        message.setSenderId(LocalThreadHolder.getUserId());
        // 消息体
        List<User> query = userMapper.query(userQueryDto);
        message.setContent("你的发言被【" + query.get(0).getUserName() + "】评论了：【" + evaluations.getContent()+ "】");
        return message;
    }

    /**
     * 查询接受者ID
     *
     * @return Integer
     */
    private Integer queryIdByParentId(Integer parentId) {
        return evaluationsMapper.queryIdByParentId(parentId);
    }


    /**
     * 查询全部评论
     *
     * @return Result<String>
     */
    @Override
    public Result<Object> list(Integer contentId, String contentType) {
        List<CommentParentVO> parentComments = evaluationsMapper.getParentComments(contentId, contentType);
        setUpvoteFlag(parentComments);
        Integer count = evaluationsMapper.totalCount(contentId, contentType);
        return ApiResult.success(new EvaluationsVO(count, parentComments));
    }

    /**
     * 设置点赞状态
     *
     * @param parentComments 评论数据列表
     */
    private void setUpvoteFlag(List<CommentParentVO> parentComments) {
        String userId = LocalThreadHolder.getUserId().toString(); // 预先获取用户ID
        parentComments.forEach(parentComment -> {
            parentComment.setUpvoteFlag(isUserUpvote(parentComment.getUpvoteList(), userId));
            parentComment.setUpvoteCount(countVotes(parentComment.getUpvoteList()));
            Optional.ofNullable(parentComment.getCommentChildVOS())
                    .orElse(Collections.emptyList())
                    .forEach(child -> {
                        child.setUpvoteFlag(isUserUpvote(child.getUpvoteList(), userId));
                        child.setUpvoteCount(countVotes(child.getUpvoteList()));
                    });
        });
    }

    /**
     * 判断用户是否已点赞
     *
     * @param voteStr 点赞用户ID字符串（逗号分隔）
     * @param userId  用户ID
     * @return 是否已点赞
     */
    private boolean isUserUpvote(String voteStr, String userId) {
        return Optional.ofNullable(voteStr)
                .map(s -> Arrays.asList(s.split(",")))
                .orElse(Collections.emptyList())
                .contains(userId);
    }

    /**
     * 计算点赞数
     *
     * @param voteStr 点赞用户ID字符串（逗号分隔）
     * @return 点赞数
     */
    private int countVotes(String voteStr) {
        return Optional.ofNullable(voteStr)
                .map(s -> s.split(",").length)
                .orElse(0);
    }

    /**
     * 分页查询评论
     *
     * @return Result<String>
     */
    @Override
    public Result<Object> query(EvaluationsQueryDto evaluationsQueryDto) {
        List<CommentChildVO> list = evaluationsMapper.query(evaluationsQueryDto);
        Integer totalPage = evaluationsMapper.queryCount(evaluationsQueryDto);
        return PageResult.success(list, totalPage);
    }

    /**
     * 批量删除评论数据
     *
     * @return Result<String>
     */
    @Override
    public Result<Object> batchDelete(List<Integer> ids) {
        evaluationsMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 评论删除
     *
     * @return Result<String>
     */
    @Override
    public Result<String> delete(Integer id) {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(id);
        evaluationsMapper.batchDelete(ids);
        if (!evaluationsMapper.queryIds(id).isEmpty())
            evaluationsMapper.batchDelete(evaluationsMapper.queryIds(id));
        return ApiResult.success();
    }


    /**
     * 评论修改
     *
     * @return Result<String>
     */
    @Override
    public Result<Void> update(Evaluations evaluations) {
        // TODO 点赞需要做通知
        if (!evaluations.getFlag()) {
            Message message = new Message();
            UserQueryDto userQueryDto = new UserQueryDto();
            userQueryDto.setId(LocalThreadHolder.getUserId());
            message.setReceiverId(evaluationsMapper.queryIdByParentId(evaluations.getId()));

            message.setSenderId(LocalThreadHolder.getUserId());


            List<User> query = userMapper.query(userQueryDto);

            message.setContent("你的评论:【"+evaluations.getContent()+"】被【" + query.get(0).getUserName() + "】点赞了");
            if (!Objects.equals(LocalThreadHolder.getUserId(), evaluationsMapper.queryIdByParentId(evaluations.getId())))
                messageService.evaluationsUpvoteSave(message);
        }


        evaluationsMapper.update(evaluations);
        return ApiResult.success("点赞成功");
    }

}
