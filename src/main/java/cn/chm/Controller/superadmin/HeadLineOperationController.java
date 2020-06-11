package cn.chm.Controller.superadmin;

import cn.chm.Service.solo.HeadLineService;
import cn.chm.entity.bo.HeadLine;
import cn.chm.entity.dto.Result;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.inject.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class HeadLineOperationController {

    @Autowired
    private HeadLineService headLineService;

    public Result<Boolean> addHeadLine(HttpServletRequest request, HttpServletResponse response) {
        // TODO:参数校验以及请求参数转化
        return headLineService.addHeadLine(new HeadLine());
    }

    public Result<Boolean> removeHeadLine(HttpServletRequest request, HttpServletResponse response){
        // TODO:参数校验以及请求参数转化
        return headLineService.removeHeadLine(new HeadLine());
    }

    public Result<Boolean> modifyHeadLine(HttpServletRequest request, HttpServletResponse response){
        // TODO:参数校验以及请求参数转化
        return headLineService.modifyHeadLine(new HeadLine());
    }

    public Result<HeadLine> queryHeadLineById(HttpServletRequest request, HttpServletResponse response){
        // TODO:参数校验以及请求参数转化
        return headLineService.queryHeadLineById(1);
    }

    public Result<List<HeadLine>> queryHead(HttpServletRequest request, HttpServletResponse response){
        // TODO:参数校验以及请求参数转化
        return headLineService.queryHead(new HeadLine(), 1, 4);
    }

}
