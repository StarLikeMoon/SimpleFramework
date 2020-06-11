package cn.chm.Service.solo;

import cn.chm.entity.bo.HeadLine;
import cn.chm.entity.bo.ShopCategory;
import cn.chm.entity.dto.Result;

import java.util.List;

public interface ShopCategoryService {

    Result<Boolean> addShopCategory(ShopCategory shopCategory);

    Result<Boolean> removeShopCategory(ShopCategory shopCategory);

    Result<Boolean> modifyshopCategory(ShopCategory shopCategory);

    Result<ShopCategory> queryshopCategoryById(int headLineId);

    Result<List<ShopCategory>> queryshopCategory(ShopCategory shopCategoryCondition, int pageIndex, int pageSize);

}
