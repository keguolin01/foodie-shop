package com.ikgl.service;

import com.ikgl.utils.PagedGridResult;

public interface ItemsESService {

    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);
}
