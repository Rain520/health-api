package cn.saber.pojo.dto.query.extend;

import cn.saber.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagsQueryDto extends QueryDto {

    /**
     * 标签名
     */
    private String name;

}
