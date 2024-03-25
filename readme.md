# JWT Implementation with Spring Boot 3 and Spring Security 6

This repository showcases a project that demonstrates the implementation of JSON Web Tokens (JWT) with Spring Boot 3 and Spring Security 6. The project includes the following functionalities:

- User Registration and Login with JWT Authentication
- Role-Based Authorization with authorities
- Customized Access Denied Handling
- OpenAPI Documentation Integration (Swagger)
- Unit testing

# Technologies

- Spring Boot
- Spring Security
- Spring Data JPA
- Spring Boot custom Validation
- JSON Web Tokens(JWT)
- BCrypt
- Maven
- OpenAPI(SpringDoc Impl)
- Lombok
- JUnit

# Getting Started
To get started with this project, you will need to have the following installed on your local machine:
- JDK 17+
- Maven 3+
## Configure Spring Datasource, JPA, App properties
1. Clone the repository
2. Open src/main/resources/application.properties
```
spring:
  datasource:
    url: jdbc:h2:mem:users-db
  h2.console.enabled: true
server:
  port: 9090
application:
  security:
    jwt:
      secret-key: 586B633834416E396D7436753879382F423F4428482B4C6250655367566B5970
      expiration: 86400000 # a day
      cookie-name: jwt-cookie
      refresh-token:
        expiration: 1296000000 # 15 days
        cookie-name: refresh-jwt-cookie
        
        


```
## Build and run the Project

The application will be available at http://localhost:9090.

# Test project
## User registration endpoint


##1. Upload du fichier utilisateurs et création des utilisateurs en base de données
```method: POST
url: /api/users/batch
content-type: multipart/form-data
secured: no
parameters:
- file: multipart-filer
 ```

##2. Connexion utilisateur + génération JWT
```
method: POST
url: /api/auth
content-type: application/json
request-body:
- username: string
- password: string
```

##3. Consultation de mon profil
```
method: GET
url: /api/users/me
secured: yes
```


##4. Consultation d'autre profil
```
method: GET
url: /api/users/{username}
sécurisée: oui
```

For detailed documentation and testing of the APIs, access the Swagger UI by visiting:
```
http://localhost:9090/swagger-ui.html
```