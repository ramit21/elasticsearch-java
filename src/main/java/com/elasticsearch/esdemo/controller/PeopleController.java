package com.elasticsearch.esdemo.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.elasticsearch.esdemo.model.Person;

@Controller
public class PeopleController {
	
	@Autowired
	@Qualifier("esClient")
	private RestHighLevelClient client;
	
	@RequestMapping(value="/create/{name}/{age}")
	@ResponseBody
	public IndexResponse createPerson(@Param("name") String name, @Param("age") String age){
		return createDataintoEs(name, age);
	}
	
	@RequestMapping(value="/getAll")
	@ResponseBody
	public List<Person> getAllPeople() throws IOException{
		SearchRequest searchRequest = new SearchRequest();
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		//The results returned by the search() method are called Hits, 
		//each Hit refers to a JSON document matching a search request.
		SearchHit[] searchHits = response.getHits().getHits();
		List<Person> results = null;
		results = 
		  Arrays.stream(searchHits)
		    .map(hit -> JSON.parseObject(hit.getSourceAsString(), Person.class))
		    .collect(Collectors.toList());
		return results;
	}

	private IndexResponse createDataintoEs(String name, String age){
		System.out.println("Inserting json data into ES .... ");
		try{
		  //Build a json object	
		  XContentBuilder builder = XContentFactory.jsonBuilder()
				  .startObject()
				  .field("fullName", name)
				  .field("age", age)
				  .field("dtCreated", new Date())
				  .endObject();
				 
		  IndexRequest indexRequest = new IndexRequest("people");
		  indexRequest.source(builder);
		 
		  IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
		  System.out.println("Data inserted: result = "+response.getResult()+", version = "+response.getVersion());
		  return response;
		}
		catch(Exception e){
			System.out.println("EXCEPTION= "+e.getMessage());
		}
		return null;
	}
}
