package com.elasticsearch.esdemo.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.elasticsearch.esdemo.model.Person;

public interface PeopleRepo extends ElasticsearchRepository<Person, String> {

	Page<Person> findByName(String name, Pageable pageable);

	@Query("{\"bool\": {\"must\": [{\"match\": {\"name\": \"?0\"}}]}}")
	Page<Person> findByPersonNameUsingCustomQuery(String name, Pageable pageable);
}
