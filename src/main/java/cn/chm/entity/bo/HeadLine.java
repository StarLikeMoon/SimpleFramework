package cn.chm.entity.bo;

import lombok.Data;

import java.util.Date;

@Data
public class HeadLine {
    private Long lineId;
    private String lineName;
    private String lineLink;
    private String lineImg;
    // 头条的权重
    private Integer priority;
    // 限制状态 0不可用 1可用
    private Integer enableStatus;
    private Date createTime;
    private Date lastEditTime;

}
