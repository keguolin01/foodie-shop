package com.ikgl.pojo.vo;

import com.ikgl.pojo.Items;
import com.ikgl.pojo.ItemsImg;
import com.ikgl.pojo.ItemsParam;
import com.ikgl.pojo.ItemsSpec;
import lombok.Data;

import java.util.List;

@Data
public class ItemInfoVO {
    private Items item;

    private List<ItemsImg> itemImgList;

    private List<ItemsSpec> itemSpecList;

    private ItemsParam itemParams;

}
