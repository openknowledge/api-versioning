# Optimistic Locking with REST
Showcase to demonstrate the implementation of optimistic locking with JAX-RS.

## Run via Docker
Run
```
docker build -t customer-service customer-service/
docker run -p8080:8080 customer-service
```
to build and run the sample.

## Run without Docker
You need Apache Maven installed to run the sample without Docker. Run
```
cd customer-service
mvn package meecrowave:run
```

## Accessing the UI
After the server is running, you can access the Swagger UI at
http://localhost:8080/index.html
