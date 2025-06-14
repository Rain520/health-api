package cn.saber.service.impl;

import cn.saber.context.LocalThreadHolder;
import cn.saber.mapper.NewsSaveMapper;
import cn.saber.pojo.api.ApiResult;
import cn.saber.pojo.api.PageResult;
import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.NewsSaveQueryDto;
import cn.saber.pojo.entity.NewsSave;
import cn.saber.pojo.vo.NewsSaveVO;
import cn.saber.service.NewsSaveService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 健康资讯收藏业务逻辑实现
 */
@Service
public class NewsSaveServiceImpl implements NewsSaveService {

    @Resource
    private NewsSaveMapper newsSaveMapper;

    /**
     * 健康资讯收藏新增
     *
     * @param newsSave 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(NewsSave newsSave) {
        newsSave.setCreateTime(LocalDateTime.now());
        newsSaveMapper.save(newsSave);
        return ApiResult.success();
    }

    /**
     * 健康资讯收藏删除
     *
     * @param ids 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Long> ids) {
        newsSaveMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 健康资讯收藏查询
     *
     * @param newsSaveQueryDto 查询参数
     * @return Result<List < NewsSaveVO>>
     */
    @Override
    public Result<List<NewsSaveVO>> query(NewsSaveQueryDto newsSaveQueryDto) {
        List<NewsSaveVO> tagsList = newsSaveMapper.query(newsSaveQueryDto);
        Integer totalCount = newsSaveMapper.queryCount(newsSaveQueryDto);
        return PageResult.success(tagsList, totalCount);
    }

    /**
     * 资讯收藏或取消收藏
     *
     * @param newsSave 资讯收藏实体
     * @return Result<Void>
     */
    @Override
    public Result<Void> operation(NewsSave newsSave) {
        // 收藏----新增；取消收藏----删除
        newsSave.setUserId(LocalThreadHolder.getUserId());
        NewsSaveQueryDto newsSaveQueryDto = new NewsSaveQueryDto();
        newsSaveQueryDto.setUserId(LocalThreadHolder.getUserId());
        newsSaveQueryDto.setNewsId(newsSave.getNewsId());
        List<NewsSaveVO> newsSaveVOS = newsSaveMapper.query(newsSaveQueryDto);
        if (newsSaveVOS.isEmpty()) {
            newsSave.setCreateTime(LocalDateTime.now());
            newsSaveMapper.save(newsSave);
        } else {
            List<Long> ids = Collections.singletonList(Long.valueOf(newsSaveVOS.get(0).getId()));
            newsSaveMapper.batchDelete(ids);
        }
        return ApiResult.success();
    }
}
