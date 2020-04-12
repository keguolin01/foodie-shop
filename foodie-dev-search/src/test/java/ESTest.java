import com.ikgl.Application;
import com.ikgl.es.pojo.Stu;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ESTest {

    @Autowired
    private ElasticsearchTemplate esTemplate;
//    @Autowired
//    private TransportClient client;

//    @After
//    public void closeConnect() {
//        try {
//            if (null != client) {
//                client.close();
//            }
//            System.out.println("执行after方法");
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//
//    }
    @Test
    public void creatIndexStu() throws IOException {
//        IndexResponse response = client.prepareIndex("index_1", "type_1", "1").setSource(XContentFactory.jsonBuilder()
//                .startObject().field("userName", "张三").field("sendDate", new Date()).field("msg", "你好李四").endObject())
//                .get();
//        System.out.println("索引名称:" + response.getIndex() + "\n类型:" + response.getType() + "\n文档ID:" + response.getId()
//                + "\n当前实例状态:" + response.status());
        Stu stu = new Stu();
        stu.setStuId(1005L);
        stu.setAge("19");
        stu.setName("wangwu");
        IndexQuery query = new IndexQueryBuilder().withObject(stu).build();
        esTemplate.index(query);
    }
    @Test
    public void deleteIndex(){
        esTemplate.deleteIndex(Stu.class);
    }

    @Test
    public void updateStuDoc(){
        Map<String,Object> source = new HashMap();
        source.put("name","hh");
        source.put("age","80");
        IndexRequest request = new IndexRequest();
        request.source(source);
        UpdateQuery query = new UpdateQueryBuilder().withClass(Stu.class)
        .withIndexRequest(request).withId("1001").build();
        esTemplate.update(query);
    }

    @Test
    public void getStuDoc(){
        GetQuery query = new GetQuery();
        query.setId("1005");
        Stu stu = esTemplate.queryForObject(query, Stu.class);
        System.out.println(stu);
    }
    @Test
    public void deleteStuDoc(){
        String delete = esTemplate.delete(Stu.class, "1001");
        System.out.println(delete);
    }

    @Test
    public void searchStuPage(){
        Pageable pageable = PageRequest.of(0,4);
       SearchQuery query = new NativeSearchQueryBuilder()
               .withQuery(QueryBuilders.matchQuery("name","super man wangwu"))
               .withPageable(pageable).build();
        AggregatedPage<Stu> stus = esTemplate.queryForPage(query, Stu.class);
        List<Stu> content = stus.getContent();
        for(Stu n : content){
            System.out.println(n);
        }
    }

    @Test
    public void highLightStuDoc(){
        String preTag = "<font color='red'>";
        String postTag = "</font>";
        SortBuilder sortBuilder = new FieldSortBuilder("stuId").order(SortOrder.ASC);
        Pageable pageable = PageRequest.of(0,4);
        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchQuery("name","super man wangwu"))
                .withHighlightFields(new HighlightBuilder.Field("name"))
                .withPageable(pageable)
                .withSort(sortBuilder)
                .build();
        List<Stu> list = new ArrayList<>();

        AggregatedPage<Stu> stus = esTemplate.queryForPage(query, Stu.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                SearchHits hits = searchResponse.getHits();
                for(SearchHit hit : hits){
                    HighlightField name = hit.getHighlightFields().get("name");
                    String str = name.getFragments()[0].toString();
                    Object stuId = hit.getSourceAsMap().get("stuId");
                    Object age = hit.getSourceAsMap().get("age");
                    Stu s = new Stu();
                    s.setName(str);
                    s.setAge(String.valueOf(age));
                    s.setStuId(Long.valueOf(stuId.toString()));
                    list.add(s);
                }
                if(list.size()>0){
                    return new AggregatedPageImpl<>((List<T>)list);
                }
                return null;
            }
        });
        List<Stu> content = stus.getContent();
        for(Stu n : content){
            System.out.println(n);
        }
    }
    @Test
    public void test() throws Exception {
//        String password = "kgl19960702.";
//        ConfigTools.main(new String[]{password});
        // jasypt
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword("salt");
        // 加密
        String password = textEncryptor.encrypt("imooc");
        System.out.println("^0^===password:"+ password);
        // 解密
        String originPwd = textEncryptor.decrypt("u+eEBYi6ZFY5de5Mv5fi8A==");
        System.out.println("^0^===originPwd:"+ originPwd);
    }

}
