package cn.chm.Service.solo.impl;

import cn.chm.Service.solo.HeadLineService;
import cn.chm.entity.bo.HeadLine;
import cn.chm.entity.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.simpleframework.core.annotation.Service;

import java.util.List;

@Slf4j
@Service
public class HeadLineServiceImpl implements HeadLineService {
    @Override
    public Result<Boolean> addHeadLine(HeadLine headline) {
        log.info("addHeadLine被执行了");
        return null;
    }

    @Override
    public Result<Boolean> removeHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public Result<Boolean> modifyHeadLine(HeadLine headLine) {
        return null;
    }

    @Override
    public Result<HeadLine> queryHeadLineById(int headLineId) {
        return null;
    }

    @Override
    public Result<List<HeadLine>> queryHead(HeadLine headLineCondition, int pageIndex, int pageSize) {
        return null;
    }
}
