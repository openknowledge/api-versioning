# Api Versioning
Showcase to demonstrate the implementation of backward-compatible APIs and versioning

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

The sample was used during a webinar of open knowledge GmbH.
The recording (in german) can be found here: https://youtu.be/4y6VJOq9jSY
