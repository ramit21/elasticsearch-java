# elasticsearch-java
POC to access es cluster using java

Steps to run this POC:

1. Setup ElasticSearch on your system. If using docker, then run the below command to spin up a container of ElasticSearch:
```
docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
```
Verify ES installation by hitting the url:
```
http://localhost:9200/
```

You may also choose to setup Kibana using docker with below command, linking it with your elastic search container, and then hit the url to see the UI:
```
docker run --link YOUR_ELASTICSEARCH_CONTAINER_NAME_OR_ID:elasticsearch -p 5601:5601 kibana:6.8.10
http://localhost:5601/
```

2. mvn install, then start the springboot application.
3. Invoke insert data controller and see the response on the browser. eg:
```
http://localhost:8080/create/Ramit/21
```
4. Fetch data from ES:
```
http://localhost:8080/getAll
```
5. Example of search using query builders:
```
http://localhost:8080/getByAge/30/40
```
6. To delete the index entirely:
```
http://localhost:8080/delete
```
7. Spring-data Elasticsearch example:
```
http://localhost:8080/getByName/Ramit
```
ElasticsearchRepository itself extends PagingAndSortingRepository, hence the results are pageable. You can either query from spring-data queries or by custom queries. PeopleController -> getByAgeName() has both functions call (one commented out). You can comment one, uncomment the other to still get the same result.


**References:**

https://github.com/eugenp/tutorials/tree/master/persistence-modules/spring-data-elasticsearch

Theory: https://github.com/ramit21/ELK

ES Searching: https://www.baeldung.com/spring-data-elasticsearch-queries


