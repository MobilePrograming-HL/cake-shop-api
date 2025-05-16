# CakeShop API Service
This is a service reserved for authentication and authorization based on JWT.

## Tech stack
- Spring-boot 3.2
- MySql 8
- Redis
- Swagger
- Docker
- Elastic Search 8

## Service configuration
- Database config
    ```
    spring.datasource.url=jdbc:mysql://<db host>:<db port>/<db name>
    spring.datasource.username=<username>
    spring.datasource.password=<password>
    ```

- Redis config
    ```
    redis.host=<redis host>
    redis.port=<redis port>
    redis.password=<redis password>
    ```
- Elastic Search config
    ```
    elasticsearch.uris=http://localhost:<port elasticsearch>
    ```

- Initial data
    Currently all initial data will define on `/resource/liquibase/db.changelog-master.xml`


## BUILD
-  Jar file
```mvn clean package```

- Docker images
``` docker build . --tag [image-tag-name]```
    Ex: ```docker build . --tag user-service-be-v1.0```

## LAUNCH APPLICATION
- From Jar
```java -jar [app jar file] [-Dspring.profiles.active=test]```

- From docker images
```docker run -it -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=[profile-name]" [image-tag-version]```

    Ex: ```docker run -it -p 8080:8080 -e "SPRING_PROFILES_ACTIVE=dev" user-service-be-v1.0```
- Use ngrok for callback payment
```fiserv.callback-url=<ngrok link>```
    Ex: ```fiserv.callback-url=https://e5e7-2402-800-6f2c-8012-9db5-c82-8669-354a.ngrok-free.app```

## Contact point
If you have any problem when rebuild application feel free to contact persons in below:

| Contact name | Email | Position |
| ---         | ---   | ---      |
| Huy Pham    | huyphuocbinh204@gmail.com  | software engineer |
