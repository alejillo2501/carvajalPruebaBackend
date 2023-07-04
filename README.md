# Autor

Oscar Alejandro Londoño Torres

# Spring Security Example

Sample project demonstrating configuration and usage of Spring Security framework

Please see the article [here](https://www.toptal.com/spring/spring-security-tutorial)

## Used Core Libraries

- [spring-boot](https://spring.io/projects/spring-boot)
- [spring-data-mongodb](https://spring.io/projects/spring-data-mongodb)
- [spring-security](https://spring.io/projects/spring-security)
- [mapstruct](https://mapstruct.org)

## Database

Mongo DB

## Project Demonstrates

- Spring Security Configuration
- [JWT](https://jwt.io) Based Stateless Authentication
- Role Based Authorization
- Integration Testing

## APIS Documentation Swagger

{{host}}/swagger-ui/

## Important Notes

- The article is outdated, in the project authentication is implemented using the spring-boot-starter-oauth2-resource-server library. I will update it when I have some free time.

- please change Cors of AllowedOrigin for "*" in /src/main/java/io/example/SecurityConfig.java line 164

## deploy

.mvnw install
docker build -t myorg/myapp:1  