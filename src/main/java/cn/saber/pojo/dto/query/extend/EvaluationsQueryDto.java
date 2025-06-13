package cn.saber.pojo.dto.query.extend;

import cn.saber.pojo.dto.query.base.QueryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评论查询参数Dto
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationsQueryDto extends QueryDto {
    /**
     * ID
     */
    private Integer id;
    /**
     * 内容类型
     */
    private String contentType;
    /**
     * 评论的内容
     */
    private String content;


}
