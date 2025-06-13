package cn.saber.pojo.vo;

import cn.saber.pojo.entity.News;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class NewsVO extends News {

    /**
     * 标签名
     */
    private String tagName;

}
