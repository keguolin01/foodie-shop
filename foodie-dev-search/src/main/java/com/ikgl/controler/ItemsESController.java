package com.ikgl.controler;

import com.ikgl.service.ItemsESService;
import com.ikgl.utils.ResponseJSONResult;
import com.ikgl.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemsESController {

    @Autowired
    private ItemsESService itemsESService;


    @GetMapping("search")
    public ResponseJSONResult search(
            String keywords,
            String sort,
            Integer page,
            Integer pageSize){
        if(StringUtils.isBlank(keywords)){
            return ResponseJSONResult.errorMsg("请输入搜索名称");
        }
        if(page == null){
            page = 1;
        }
        page--;
        if(pageSize == null){
            pageSize = 20;
        }
        PagedGridResult pagedGridResult = itemsESService.searchItems(keywords, sort, page, pageSize);
        return ResponseJSONResult.ok(pagedGridResult);
    }
}
