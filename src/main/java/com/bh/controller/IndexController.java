package com.bh.controller;

import com.bh.modle.Response;
import com.bh.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", methods =
    {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT})
public class IndexController {
  private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

  @Resource
  ElasticSearchService es;

  @GetMapping(value = "/")
  public Response hello() {
    return new Response(200, "You Know, for Log");
  }

  @GetMapping(value = "/info")
  public String info() throws IOException {
    return es.info();
  }

  @PostMapping(value = "/indexes/{name}")
  public Response createIndex(@PathVariable("name") String indexName) {
    try {
      es.createIndex(indexName);
      return new Response(200, "Created");
    } catch (IOException e) {
      return new Response(500, "Created failed");
    }
  }

  @GetMapping(value = "/indexes/{name}")
  public Response indexExists(@PathVariable("name") String indexName) {
    try {
      boolean isExists = es.isIndexExists(indexName);
      String result = isExists ? "index is exits" : "index is not exits";
      return new Response(200, result, isExists);
    } catch (IOException e) {
      return new Response(500, "query failed");
    }
  }

  @DeleteMapping(value = "/indexes/{name}")
  public Response deleteIndex(@PathVariable("name") String indexName) {
    try {
      boolean isDelete = es.deleteIndex(indexName);
      return new Response(200, "Deleted");
    } catch (IOException e) {
      return new Response(500, "Deleted failed");
    }
  }

  @PostMapping(value = "/doc/{index}")
  public Response addDoc(@PathVariable("index") String index,
                         @RequestBody String doc) {
    try {
      es.addDoc(index, doc);
      return new Response(200, "Added");
    } catch (IOException e) {
      return new Response(500, "Added failed");
    }
  }

  @PostMapping(value = "/docs/{index}")
  public Response addDocs(@PathVariable("index") String index,
                          @RequestBody String docs) {
    try {
      es.addDocs(index, docs);
      return new Response(200, "Added");
    } catch (IOException e) {
      return new Response(500, "Added failed");
    }
  }

  @GetMapping(value = "/docs/{index}/{id}")
  public Response getDocs(@PathVariable("index") String index,
                          @PathVariable("id") String id) {
    try {
      String docs = es.getDoc(index, id);
      return new Response(200, "Query ok", docs);
    } catch (IOException e) {
      return new Response(500, "Query failed");
    }
  }

  @PutMapping(value = "/docs/{index}/{id}")
  public Response updateDocs(@PathVariable("index") String index,
                             @PathVariable("id") String id,
                             @RequestBody String doc) {
    try {
      es.updateDoc(index, id, doc);
      return new Response(200, "Updated ok");
    } catch (IOException e) {
      return new Response(500, "Query failed");
    }
  }

  @DeleteMapping(value = "/docs/{index}/{id}")
  public Response deleteDocs(@PathVariable("index") String index,
                             @PathVariable("id") String id) {
    try {
      String docs = es.getDoc(index, id);
      return new Response(200, "Query ok", docs);
    } catch (IOException e) {
      return new Response(500, "Query failed", "Shit!");
    }
  }


  @GetMapping(value = "/query/{index}")
  public Response queryDocs(@PathVariable("index") String index) {
    try {
      String docs = es.search(index);
      return new Response(200, "Query ok", docs);
    } catch (Exception e) {
      return new Response(500, "Query failed", "Shit!");
    }
  }
}
