package com.ikgl.pojo.vo;

import com.ikgl.pojo.Items;
import com.ikgl.pojo.ItemsImg;
import com.ikgl.pojo.ItemsParam;
import com.ikgl.pojo.ItemsSpec;

import java.util.List;

public class ItemInfoVO {
    private Items item;

    private List<ItemsImg> itemImgList;

    private List<ItemsSpec> itemSpecList;

    private ItemsParam itemParams;

    public Items getItem() {
        return item;
    }

    public void setItem(Items item) {
        this.item = item;
    }

    public List<ItemsImg> getItemImgList() {
        return itemImgList;
    }

    public void setItemImgList(List<ItemsImg> itemImgList) {
        this.itemImgList = itemImgList;
    }

    public List<ItemsSpec> getItemSpecList() {
        return itemSpecList;
    }

    public void setItemSpecList(List<ItemsSpec> itemSpecList) {
        this.itemSpecList = itemSpecList;
    }

    public ItemsParam getItemParams() {
        return itemParams;
    }

    public void setItemParams(ItemsParam itemParams) {
        this.itemParams = itemParams;
    }
}
