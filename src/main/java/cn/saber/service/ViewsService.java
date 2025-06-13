package cn.saber.service;

import cn.saber.pojo.api.Result;
import cn.saber.pojo.vo.ChartVO;

import java.util.List;

public interface ViewsService {

    Result<List<ChartVO>> staticControls();

}
