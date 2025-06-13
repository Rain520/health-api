package cn.saber.service;

import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.extend.NewsSaveQueryDto;
import cn.saber.pojo.entity.NewsSave;
import cn.saber.pojo.vo.NewsSaveVO;

import java.util.List;

public interface NewsSaveService {

    Result<Void> save(NewsSave newsSave);

    Result<Void> batchDelete(List<Long> ids);

    Result<List<NewsSaveVO>> query(NewsSaveQueryDto newsSaveQueryDto);

    Result<Void> operation(NewsSave newsSave);


}
