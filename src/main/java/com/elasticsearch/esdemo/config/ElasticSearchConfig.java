package com.elasticsearch.esdemo.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
public class ElasticSearchConfig {

	@Bean("esClient")
	public RestHighLevelClient client(){
		ClientConfiguration clientConfiguration =
			    ClientConfiguration.builder().connectedTo("localhost:9200").build();
		RestHighLevelClient client = RestClients.create(clientConfiguration).rest();
		return client;
	}
}
