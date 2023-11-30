# crypto-investment


## Tech
  - Java 17, Quarkus, PostgreSQL, Docker, Caffeine Cache, Smallrye fault tolerance (Rate limiting - experimental)

## Prerequisites
 - JDK 17
 - Docker Daemon (e.g. free:  Rancher Desktop)

## Api

Rate limiting (experimental): max 15 requests / 2min
 
OpenAPI: http://localhost:8080/q/swagger-ui/

Endpoints:
 - GET - http://localhost:8080/stats/{symbol}
 - GET - http://localhost:8080/stats/normalized-range-desc
 - GET - http://localhost:8080/stats/normalized-range-desc/{date}

Where: 
- {symbol} - [BTC, ETH, DOGE, LTC, XRP] ;
- {date: yyyy-MM-dd} - CSV data is between 2022-01-01 - 2022-01-31

## How to run

Use a terminal, make sure you are in the folder of the application.

### Package the application

```shell script
 ./mvnw package -DskipTests
```

### Run using Docker

This will start containers for: application, PostgreSQL and pgadmin.

```shell
 docker-compose up -d --build
```

### Stop the applications

```shell
 docker-compose down
```

## Flow

### Startup
![Alt text](src/main/resources/readme/d1.png?raw=true "Startup")

### Live

![Alt text](src/main/resources/readme/d2.png?raw=true "Startup")

