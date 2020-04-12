package com.ikgl.service.Impl;

import com.ikgl.es.pojo.Items;
import com.ikgl.es.pojo.Stu;
import com.ikgl.service.ItemsESService;
import com.ikgl.utils.PagedGridResult;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemsESServiceImpl implements ItemsESService {

    @Autowired
    private ElasticsearchTemplate esTemplate;

//    @Autowired
//    private TransportClient client;

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        String preTag = "<font color='red'>";
        String postTag = "</font>";
//        SortBuilder sortBuilder = new FieldSortBuilder("stuId").order(SortOrder.ASC);
        Pageable pageable = PageRequest.of(page,pageSize);
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("itemName",keywords))
                .withHighlightFields(new HighlightBuilder.Field("itemName"))
                .withPageable(pageable)
//                .withSort(sortBuilder)
                .build();
        List<Items> list = new ArrayList<>();

        AggregatedPage<Items> items = esTemplate.queryForPage(query, Items.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                for(SearchHit hit : hits){
                    HighlightField name = hit.getHighlightFields().get("itemName");
                    String str = name.getFragments()[0].toString();
                    Object itemId = hit.getSourceAsMap().get("itemId");
                    Object imgUrl = hit.getSourceAsMap().get("imgUrl");
                    Object price = hit.getSourceAsMap().get("price");
                    Object sellCounts = hit.getSourceAsMap().get("sellCounts");
                    Items items = new Items();
                    items.setItemId(itemId.toString());
                    items.setImgUrl(imgUrl.toString());
                    items.setPrice(Integer.parseInt(price.toString()));
                    items.setSellCounts(Integer.parseInt(sellCounts.toString()));
                    items.setItemName(str);
                    list.add(items);
                }
                if(list.size()>0){
                    return new AggregatedPageImpl<>((List<T>)list);
                }
                return null;
            }
        });
        List<Items> content = items.getContent();
        for(Items n : content){
            System.out.println(n);
        }
        return PagedGridResult.setPagedGridResult(content,page);
    }
}
