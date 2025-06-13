package cn.saber.service;

import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.NewsQueryDto;
import cn.saber.pojo.entity.News;
import cn.saber.pojo.vo.NewsVO;

import java.util.List;

/**
 * 健康资讯业务逻辑接口
 */
public interface NewsService {

    Result<Void> save(News news);

    Result<Void> batchDelete(List<Long> ids);

    Result<Void> update(News news);

    Result<List<NewsVO>> query(NewsQueryDto newsQueryDto);

}
