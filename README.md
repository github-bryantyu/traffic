# Traffic 

## Approach

Two containers are used to construct the solution. One is the PosgreSQL and another one is a application made from OpenJDK with spring boot. 5 minute schedule job will pull traffic information from https://data.gov.hk/en-data/dataset/hk-td-sm_1-traffic-speed-map and store the latest information in PostgreSQL



I used Eclipse and the Spring Tools from Eclipse marketplace to quick form the project skeleton. 



## Setup 

Download docker images 

```bash
docker pull bryantyu/trafficmonitor:v0.1  
docker pull postgres 
```

Prepare a new docker network `tsm-net` to allow container communication  

```bash
docker network create tsm-net
```

Create PG container

```bash
docker run -d --name postgres -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword --net tsm-net postgres
```

Create the Traffic Monitor container

```bash
docker run -d --name mytrafficmonitor -p 8080:8080 --net tsm-net bryantyu/trafficmonitor:v0.1
```



## Check database content 

```sql
# Go into container prompt
docker exec -it postgres /bin/bash

# Connect PG 
psql -U postgres

# Select all traffic speed records
select * from tsm;
```



## Limitation

Due to time constraint, followings have not been implemented: 

+ Kafka topics
+ RESTFUL API to start and stop the fetch traffic XML job  



## Further improvement

With more time, the deliverables can be further improved. 

- Encountered a keystore problem, work around by ignoring HTTPS certification check  
- Replace `RestTemplate` with `WebClient` to fetch the XML data
- During consuming the incoming XML traffic data, the spring message converter did not work well. Now, I simply load the XML as a string to a Java XML Dom. Performance is not good when the XML growth further 
- Simple docker environment is used and communications between containers need to done by creating a new docker network. If deployed in K8 environment, believe using K8 service will simplify deployment. POD probe can perform health monitoring and self-healing as well  



## Cleanup 

```bash
docker stop mytrafficmonitor
docker rm mytrafficmonitor

docker stop postgres
docker rm postgres

docker rmi bryantyu/trafficmonitor:v0.1
docker rmi postgres

```

