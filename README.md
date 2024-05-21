
# Rocket Bank Spring Boot API

A banking CRUD API built using the Spring Boot framework and PostgreSQL.


## Tech Stack

![My Skills](https://skillicons.dev/icons?i=java,spring,docker,maven,postgres)

## Features

- ADD customer objects to DB
- ADD or DELETE account objects to DB
- Cron job to get ending daily balances for all accounts
- Tracks DEPOSIT or WITHDRAW transactions
- API endpoints secured Spring Security


## Requirements

Before deploying the API, you will need to create a **sensitive.properties** file.

```
spring.datasource.url=jdbc:postgresql://<DB Endpoint>:<DB Port>/<DB Table Name>
spring.datasource.username=<DB Username>
spring.datasource.password=<DB Password>

env.jwks.data = <Cognito jwks.json link>
env.proxy.address = http://localhost:3000
env.frontend.address = http://localhost:5173
```
    
## Deployment

This project can be ran locally or in a Docker container. Clone the repository, create **sensitive.properties** file, and run the following commands.

#### Docker

Checkout this blog article for steps to containerize this API --> [HERE](https://dev.to/tevindeale/containerize-a-java-spring-boot-app-1op3)

#### Locally
Ensure that you have **openJDK 21** installed. **These next steps are done on a MAC device. Windows commands could differ**.

```bash
  ./mvnw sprint-boot:run
```

## API Reference

This API works in conjuction with a JWT access token retrieved from a oauth2 user pool. The token has to match the issuer which is defined in the **sensitive.properties** file.

#### Home Controller

These calls are for testing the API.

```http
  GET /home
```
Returns a message:  
Welcome Home!.  
*This api call is bypassed by the security chain to check if the api is up.

---
```http
  GET /hello
```
Return a message:  
Hello, <name>, welcome to your api!  
Your UUID: <uuid>
Username: <username>  
*Values are extracted from JWT token.

---

#### Customer Controller

```http
  POST /api/customer/add
```

| Parameter | Type     | 
| :-------- | :------- | 
| `username` | `string` |
| `uuid` | `string` |
| `fname` | `string` |
| `lname` | `string` |

Returns: "User added"

---

```http
  GET /api/customer
```

Returns:
```json
{
    username: <username>,
    fname: <fname>,
    lname: <lname>,
    uuid: <uuid>
}
```
---
#### Account Controller




## Contributing

Contributions are always welcome!

If you would like to contribute, reach out via X [@FiinnDev](https://x.com/FiinnDev)


