# WUNDERLIST

Hepsiemlak test case for the application.

## Installation
Download Wunderlist Application from DockerHub. Go to the link:
```bash
https://hub.docker.com/r/mkaraa/wunderlist-server
```
Copy the docker image script to pull
```bash
docker pull mkaraa/wunderlist-server
```

Download Wunderlist Couchbase Database from DockerHub. Go to the link:
```bash
https://hub.docker.com/r/mkaraa/wunderlist-couchbase
```
Copy the docker image script to pull
```bash
docker pull mkaraa/wunderlist-couchbase
```

## Usage
Run the script shown below. This script run Couchbase and Wunderlist Application.
```bash
docker-compose up -d
```
###### You can see Couchbase and Wunderlist Application logs container logs with below scripts respectively:

```bash
docker-compose logs he_couchbase
docker-compose logs wunderlist
```
* Couchbase UI will be running on `localhost:8091`. Login page automatically opened. You can login Couchbase Server below credentials: username and password respectively.
```bash
username:  mkaraa-admin
password:  12345678
```
* Wonderlist Application will be running on `port:8081`

###### Note: Wunderlist application will be running in at most two minutes. If you do not see the `Started Server` log immediately, do not worry about it.

## Postman
You can download directly download requests from Postman Collection in:
```bash
https://github.com/mkaraa/Wunderlist/blob/main/Wunderlist.postman_collection.json
```

## Github
You can clone directly the project from Github Repo below:
```bash
https://github.com/mkaraa/Wunderlist
```

## Swagger
You can see the domain of Wunderlist Application and endpoints in:
```bash
http://127.0.0.1:8081/swagger-ui/index.html
```

## Spring Boot Actuator
You can check heartbeats and metrics of Wunderlist Application in:
```bash
http://127.0.0.1:8081/actuator
```

## Details
- `Chain of Responsibility` design pattern is applied for validation
- `Logback-spring.xml` is configured to map all logs
- `HandlerInterceptor` is used for logging all request 
- `CorrelationId` is added to all logs in order to trace logs
- Created `configure-cluster.sh` file to initialize couchbase and it is added to `docker-compose.yml`as entrypoint


## Author
METEHAN KARA
```bash
www.linkedin.com/in/metehankara
```
