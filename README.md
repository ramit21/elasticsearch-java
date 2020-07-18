# elasticsearch-java
POC to access es cluster using java

Steps to run this POC:

1. Setup ElsticSearch on your system. If using docker, then run the below command to spin up a container of ElasticSearch:
```
docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
```
Verify ES installation by hitting the url:
```
http://localhost:9200/
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


