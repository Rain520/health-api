package cn.saber.mapper;

import cn.saber.pojo.dto.query.extend.NewsQueryDto;
import cn.saber.pojo.entity.News;
import cn.saber.pojo.vo.NewsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 标签持久化接口
 */
@Mapper
public interface NewsMapper {

    void save(News news);

    void update(News news);

    void batchDelete(@Param(value = "ids") List<Long> ids);

    List<NewsVO> query(NewsQueryDto newsQueryDto);

    Integer queryCount(NewsQueryDto newsQueryDto);

}
