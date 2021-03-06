package com.elasticsearch.esdemo.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.elasticsearch.esdemo.model.Person;
import com.elasticsearch.esdemo.repo.PeopleRepo;

@Controller
public class PeopleController {
	
	private final String indexName = "people-idx";
	
	@Autowired
	private PeopleRepo repo;
	
	@Autowired
	@Qualifier("esClient")
	private RestHighLevelClient client;
	
	@RequestMapping(value="/create/{name}/{age}")
	@ResponseBody
	public IndexResponse createPerson(
			@PathVariable("name") String name, @PathVariable("age") String age){
		return createDataintoEs(name, age);
	}
	
	@RequestMapping(value="/getAll")
	@ResponseBody
	public List<Person> getAllPeople() throws IOException{
		SearchRequest searchRequest = new SearchRequest(indexName);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		//The results returned by the search() method are called Hits, 
		//each Hit refers to a JSON document matching a search request.
		SearchHit[] searchHits = response.getHits().getHits();
		List<Person> results = Arrays.stream(searchHits)
					    .map(hit -> JSON.parseObject(hit.getSourceAsString(), Person.class))
					    .collect(Collectors.toList());
		return results;
	}

	//Search using QueryBuilders
	@RequestMapping(value="/getByAge/{age1}/{age2}")
	@ResponseBody
	public List<Person> getByAgeRange(
			@PathVariable("age1") Integer age1, @PathVariable("age2") Integer age2) throws IOException{
		
		SearchSourceBuilder builder = new SearchSourceBuilder()
				  .postFilter(QueryBuilders.rangeQuery("age").from(age1).to(age2));		 
		SearchRequest searchRequest = new SearchRequest(indexName);
		searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
		searchRequest.source(builder);
		SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
		SearchHit[] searchHits = response.getHits().getHits();
		List<Person> results = Arrays.stream(searchHits)
					    .map(hit -> JSON.parseObject(hit.getSourceAsString(), Person.class))
					    .collect(Collectors.toList());
		return results;
	}
	
	//Search using spring-data-elastisearch
	@RequestMapping(value="/getByName/{name}")
	@ResponseBody
	public List<Person> getByAgeName(@PathVariable("name") String name) throws IOException{
		repo.findByName(name, PageRequest.of(0, 10));
		//Page<Person> result = repo.findByName(name, PageRequest.of(0, 10));
		Page<Person> result = repo.findByPersonNameUsingCustomQuery(name, PageRequest.of(0, 10));
		return result.toList();
	}
	
	//Delete the index entirely
	@RequestMapping(value="/delete")
	@ResponseBody
	public AcknowledgedResponse deleteAll() throws IOException{
		DeleteIndexRequest deleteRequest = new DeleteIndexRequest(indexName);
		AcknowledgedResponse deleteIndexResponse = client.indices().delete(deleteRequest, RequestOptions.DEFAULT);
		return deleteIndexResponse;
	}
	
	private IndexResponse createDataintoEs(String name, String age){
		System.out.println("Inserting json data into ES .... ");
		
		try{
		  //Build a json object	
		  XContentBuilder builder = XContentFactory.jsonBuilder()
				  .startObject()
				  .field("name", name)
				  .field("age", age)
				  .field("dtCreated", new Date())
				  .endObject();
		  //note that first insert will also create the index if it does not exist		 
		  IndexRequest indexRequest = new IndexRequest(indexName);
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
