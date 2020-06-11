package cn.chm.Service.solo.impl;

import cn.chm.Service.solo.ShopCategoryService;
import cn.chm.entity.bo.ShopCategory;
import cn.chm.entity.dto.Result;
import org.simpleframework.core.annotation.Service;

import java.util.List;

@Service
public class ShopCategoryServiceImpl implements ShopCategoryService {
    @Override
    public Result<Boolean> addShopCategory(ShopCategory shopCategory) {
        return null;
    }

    @Override
    public Result<Boolean> removeShopCategory(ShopCategory shopCategory) {
        return null;
    }

    @Override
    public Result<Boolean> modifyshopCategory(ShopCategory shopCategory) {
        return null;
    }

    @Override
    public Result<ShopCategory> queryshopCategoryById(int headLineId) {
        return null;
    }

    @Override
    public Result<List<ShopCategory>> queryshopCategory(ShopCategory shopCategoryCondition, int pageIndex, int pageSize) {
        return null;
    }
}
