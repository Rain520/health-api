package cn.saber.service.impl;

import cn.saber.mapper.TagsMapper;
import cn.saber.pojo.api.ApiResult;
import cn.saber.pojo.api.PageResult;
import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.TagsQueryDto;
import cn.saber.pojo.entity.Tags;
import cn.saber.service.TagsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 标签业务逻辑实现
 */
@Service
public class TagsServiceImpl implements TagsService {

    @Resource
    private TagsMapper tagsMapper;

    /**
     * 标签新增
     *
     * @param tags 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> save(Tags tags) {
        TagsQueryDto tagsQueryDto = new TagsQueryDto();
        tagsQueryDto.setName(tags.getName());
        Integer totalCount = tagsMapper.queryCount(tagsQueryDto);
        if (totalCount > 0) {
            return ApiResult.error("标签名重复！请换一个！");
        }
        tagsMapper.save(tags);
        return ApiResult.success();
    }

    /**
     * 标签删除
     *
     * @param ids 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> batchDelete(List<Long> ids) {
        tagsMapper.batchDelete(ids);
        return ApiResult.success();
    }

    /**
     * 标签修改
     *
     * @param tags 参数
     * @return Result<Void>
     */
    @Override
    public Result<Void> update(Tags tags) {
        TagsQueryDto tagsQueryDto = new TagsQueryDto();
        tagsQueryDto.setName(tags.getName());
        List<Tags> list = tagsMapper.query(tagsQueryDto);
        if(list.isEmpty()){
            tagsMapper.update(tags);
            return ApiResult.success();
        } else if (list.get(0).getId()  != tags.getId()) {
            return ApiResult.error("标签重复，请换一个！");
        }
        return ApiResult.success();
    }

    /**
     * 标签查询
     *
     * @param tagsQueryDto 查询参数
     * @return Result<List < Tags>>
     */
    @Override
    public Result<List<Tags>> query(TagsQueryDto tagsQueryDto) {
        List<Tags> tagsList = tagsMapper.query(tagsQueryDto);
        Integer totalCount = tagsMapper.queryCount(tagsQueryDto);
        return PageResult.success(tagsList, totalCount);
    }


}
