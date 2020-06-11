package cn.chm.Service.combine.impl;

import cn.chm.Service.combine.HeadLineShopCategoryCombineService;
import cn.chm.Service.solo.HeadLineService;
import cn.chm.Service.solo.ShopCategoryService;
import cn.chm.entity.bo.HeadLine;
import cn.chm.entity.bo.ShopCategory;
import cn.chm.entity.dto.MainPageInfoDto;
import cn.chm.entity.dto.Result;
import org.simpleframework.core.annotation.Service;
import org.simpleframework.inject.annotation.Autowired;

import java.util.List;

@Service
public class HeadLineShopCategoryCombineServiceImpl implements HeadLineShopCategoryCombineService {

    @Autowired
    private HeadLineService headLineService;
    @Autowired
    private ShopCategoryService shopCategoryService;

    @Override
    public Result<MainPageInfoDto> getMainPageInfo() {
        // 获取头条列表
        HeadLine headLineCondition = new HeadLine();
        headLineCondition.setEnableStatus(1);
        Result<List<HeadLine>> headLineResult = headLineService.queryHead(headLineCondition, 1, 4);
        // 获取店铺类别列表
        ShopCategory shopCategoryCondition = new ShopCategory();
        Result<List<ShopCategory>> shopCategoryResult = shopCategoryService.queryshopCategory(shopCategoryCondition, 1, 100);
        // 合并两者并返回
        Result<MainPageInfoDto> result = mergeMainPageInfoResult(headLineResult, shopCategoryResult);
        return result;
    }

    private Result<MainPageInfoDto> mergeMainPageInfoResult(
            Result<List<HeadLine>> headLineResult, Result<List<ShopCategory>> shopCategoryResult) {
        return null;
    }
}
