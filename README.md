# RSO: Polnilnice microservice

## Prerequisites

```bash
docker run -d --name pg-image-metadata -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=image-metadata -p 5432:5432 postgres:13
```

## Build and run commands
```bash
mvn clean package
cd api/target
java -jar image-catalog-api-1.0.0-SNAPSHOT.jar
```

## Run in IntelliJ IDEA
Add new Run configuration and select the Application type. In the next step, select the module api and for the main class com.kumuluz.ee.EeApplication.

Available at: localhost:8080/v1/images

## Docker commands
```bash
docker build -t polnilnice .   
docker images
docker run polnilnice    
docker tag polnilnice kkklemennn/polnilnice   
docker push kkklemennn/polnilnice
docker ps
```

## URLs
App: localhost:8080/v1/polnilnice

Health: localhost:8080/health/live

Metrics:localhost:8080/health/metrics

GraphQL: localhost:8080/graphiql

OpenAPI: localhost:8080/api-specs/ui/
