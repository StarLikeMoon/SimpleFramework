package cn.chm.Service.combine;

import cn.chm.entity.dto.MainPageInfoDto;
import cn.chm.entity.dto.Result;

/**
 * 将solo中的HeadLine和ShopCategory组合起来
 */
public interface HeadLineShopCategoryCombineService {

    /**
     * 获取首页所需头条以及店铺类别列表
     */
    Result<MainPageInfoDto> getMainPageInfo();

}
