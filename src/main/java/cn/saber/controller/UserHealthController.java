package cn.saber.controller;

import cn.saber.aop.Pager;
import cn.saber.context.LocalThreadHolder;
import cn.saber.pojo.api.Result;
import cn.saber.pojo.dto.query.base.QueryDto;
import cn.saber.pojo.dto.query.extend.UserHealthQueryDto;
import cn.saber.pojo.entity.UserHealth;
import cn.saber.pojo.vo.ChartVO;
import cn.saber.pojo.vo.UserHealthVO;
import cn.saber.service.UserHealthService;
import cn.saber.utils.DateUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户健康记录的 Controller
 */
@RestController
@RequestMapping(value = "/user-health")
public class UserHealthController {

    @Resource
    private UserHealthService userHealthService;


    /**
     * 用户健康记录新增
     *
     * @param userHealths 新增数据
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/save")
    public Result<Void> save(@RequestBody List<UserHealth> userHealths) {
        return userHealthService.save(userHealths);
    }

    /**
     * 用户健康记录删除
     *
     * @param ids 要删除的用户健康记录ID列表
     * @return Result<Void> 通用响应体
     */
    @PostMapping(value = "/batchDelete")
    public Result<Void> batchDelete(@RequestBody List<Long> ids) {
        return userHealthService.batchDelete(ids);
    }

    /**
     * 用户健康记录修改
     *
     * @param userHealth 参数
     * @return Result<Void> 响应
     */
    @PutMapping(value = "/update")
    public Result<Void> update(@RequestBody UserHealth userHealth) {
        return userHealthService.update(userHealth);
    }

    /**
     * 用户健康记录查询
     *
     * @param userHealthQueryDto 查询参数
     * @return Result<List < UserHealthVO>> 通用响应
     */
    @Pager
    @PostMapping(value = "/query")
    public Result<List<UserHealthVO>> query(@RequestBody UserHealthQueryDto userHealthQueryDto) {
        return userHealthService.query(userHealthQueryDto);
    }

    /**
     * 用户健康记录查询
     *
     * @param userHealthQueryDto 查询参数
     * @return Result<List < UserHealthVO>> 通用响应
     */
    @Pager
    @PostMapping(value = "/queryUser")
    public Result<List<UserHealthVO>> queryUser(@RequestBody UserHealthQueryDto userHealthQueryDto) {
        userHealthQueryDto.setUserId(LocalThreadHolder.getUserId());
        return userHealthService.query(userHealthQueryDto);
    }

    /**
     * 用户健康记录查询
     *
     * @param id  健康模型ID
     * @param day 往前查多少天
     * @return Result<List < UserHealthVO>> 通用响应
     */
    @GetMapping(value = "/timeQuery/{id}/{day}")
    public Result<List<UserHealthVO>> timeQuery(@PathVariable Integer id,
                                                @PathVariable Integer day) {

        Integer userId = LocalThreadHolder.getUserId();
        QueryDto queryDto = DateUtil.startAndEndTime(day);
        UserHealthQueryDto userHealthQueryDto = new UserHealthQueryDto();
        userHealthQueryDto.setHealthModelConfigId(id);
        userHealthQueryDto.setUserId(userId);
        userHealthQueryDto.setStartTime(queryDto.getStartTime());
        userHealthQueryDto.setEndTime(queryDto.getEndTime());
        return userHealthService.query(userHealthQueryDto);
    }

    /**
     * 统计模型存量数据
     *
     * @return Result<List < ChartVO>> 响应结果
     */
    @GetMapping(value = "/daysQuery/{day}")
    @ResponseBody
    public Result<List<ChartVO>> query(@PathVariable Integer day) {
        return userHealthService.daysQuery(day);
    }

}
