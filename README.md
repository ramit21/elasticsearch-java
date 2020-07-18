# elasticsearch-java
POC to access es cluster using java

Steps to run this POC:

1. mvn install
2. Setup ElsticSearch on your system. If using docker, then run the below command to spin up a container of ElasticSearch:

```
docker run -d --name es762 -p 9200:9200 -e "discovery.type=single-node" elasticsearch:7.6.2
```
3. Invoke insert data controller and see the response on the response. eg:

```
http://localhost:8080/create/Ramit/21
```

