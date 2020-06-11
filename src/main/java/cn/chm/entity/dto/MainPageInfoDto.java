package cn.chm.entity.dto;

import cn.chm.entity.bo.HeadLine;
import cn.chm.entity.bo.ShopCategory;
import lombok.Data;

import java.util.List;

@Data
public class MainPageInfoDto {

    private List<HeadLine> headLineList;
    private List<ShopCategory> shopCategoryList;

}
