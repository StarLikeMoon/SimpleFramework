package cn.chm.Service.solo;

import cn.chm.entity.bo.HeadLine;
import cn.chm.entity.dto.Result;

import java.util.List;

public interface HeadLineService {

    /**
     * 添加头条
     */
    Result<Boolean> addHeadLine(HeadLine headline);

    /**
     * 删除头条
     */
    Result<Boolean> removeHeadLine(HeadLine headLine);

    /**
     * 修改头条
     */
    Result<Boolean> modifyHeadLine(HeadLine headLine);


    Result<HeadLine> queryHeadLineById(int headLineId);

    Result<List<HeadLine>> queryHead(HeadLine headLineCondition, int pageIndex, int pageSize);
}
