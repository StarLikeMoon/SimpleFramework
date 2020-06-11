package cn.chm.Controller.superadmin;

import cn.chm.Service.solo.HeadLineService;
import cn.chm.Service.solo.ShopCategoryService;
import cn.chm.entity.bo.HeadLine;
import cn.chm.entity.bo.ShopCategory;
import cn.chm.entity.dto.Result;
import org.simpleframework.core.annotation.Controller;
import org.simpleframework.inject.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
public class ShopCategoryOperationController {

    @Autowired
    private ShopCategoryService shopCategoryService;

    public Result<Boolean> addShopCategory(HttpServletRequest request, HttpServletResponse response) {
        // TODO:参数校验以及请求参数转化
        return shopCategoryService.addShopCategory(new ShopCategory());
    }

    public Result<Boolean> removeShopCategory(HttpServletRequest request, HttpServletResponse response){
        // TODO:参数校验以及请求参数转化
        return shopCategoryService.removeShopCategory(new ShopCategory());
    }

    public Result<Boolean> modifyShopCategory(HttpServletRequest request, HttpServletResponse response){
        // TODO:参数校验以及请求参数转化
        return shopCategoryService.modifyshopCategory(new ShopCategory());
    }

    public Result<ShopCategory> queryShopCategoryById(HttpServletRequest request, HttpServletResponse response){
        // TODO:参数校验以及请求参数转化
        return shopCategoryService.queryshopCategoryById(1);
    }

    public Result<List<ShopCategory>> queryShopCategory(HttpServletRequest request, HttpServletResponse response){
        // TODO:参数校验以及请求参数转化
        return shopCategoryService.queryshopCategory(new ShopCategory(), 1, 4);
    }

}
