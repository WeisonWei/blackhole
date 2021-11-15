package com.bh.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bh.util.JsonUtil;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ElasticSearchService {
  private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

  @Resource
  RestHighLevelClient restHighLevelClient;

  public String info() throws IOException {
    return JsonUtil.toJson(restHighLevelClient.info(RequestOptions.DEFAULT));
  }

  public void createIndex(String index) throws IOException {
    CreateIndexRequest request = new CreateIndexRequest(index);
    buildSetting(request);
    restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
  }

  public boolean isIndexExists(String index) throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest(index).local(false)
        .humanReadable(true).includeDefaults(false);
    return restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
  }

  public boolean deleteIndex(String index) throws IOException {
    DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(index);
    deleteIndexRequest.indicesOptions(IndicesOptions.LENIENT_EXPAND_OPEN);
    AcknowledgedResponse delete = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
    return delete.isAcknowledged();
  }

  public boolean addDoc(String index, String document) throws IOException {
    byte[] bytes = JsonUtil.getBytes(document);
    IndexRequest request = new IndexRequest(index).source(bytes, XContentType.JSON);
    restHighLevelClient.index(request, RequestOptions.DEFAULT);
    return true;
  }

  public boolean addDocs(String index, String documents) throws IOException {
    BulkRequest bulkRequest = new BulkRequest();
    List list = JsonUtil.str2List(documents);
    for (Object obj : list) {
      byte[] bytes = JsonUtil.toJson(obj).getBytes();
      IndexRequest request = new IndexRequest(index).source(bytes, XContentType.JSON);
      bulkRequest.add(request);
    }
    BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    RestStatus restStatus = bulkResponse.status();
    return 200 == restStatus.getStatus();
  }

  public String getDoc(String index, String id) throws IOException {
    GetRequest getRequest = new GetRequest(index).id(id);
    return restHighLevelClient.get(getRequest, RequestOptions.DEFAULT).toString();
  }

  public boolean updateDoc(String index, String id, String doc) throws IOException {
    byte[] bytes = JsonUtil.getBytes(doc);
    UpdateRequest updateRequest = new UpdateRequest(index, id).doc(bytes, XContentType.JSON);
    UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    return updateResponse.getResult() == DocWriteResponse.Result.UPDATED;
  }

  public boolean deleteDoc(String index, String id) throws IOException {
    DeleteRequest deleteRequest = new DeleteRequest(index, id);
    DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
    return deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND;
  }

  public String search(String index) throws Exception {
    SearchRequest searchRequest = new SearchRequest(index);
    SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
    sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
    searchRequest.source(sourceBuilder);
    SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    SearchHits hits = response.getHits();
    JSONArray array = new JSONArray();
    for (SearchHit hit : hits) {
      JSONObject json = JSON.parseObject(hit.getSourceAsString());
      array.add(json);
    }
    return array.toJSONString();
  }

  private void buildSetting(CreateIndexRequest request) {
    Settings.Builder sb = Settings.builder()
        .put("index.number_of_shards", 3)
        .put("index.number_of_replicas", 2);
    request.settings(sb);
  }

}
