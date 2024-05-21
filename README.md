
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

```yaml
spring.datasource.url=jdbc:postgresql://<DB Endpoint>:<DB Port>/<DB Table Name>
spring.datasource.username=<DB Username>
spring.datasource.password=<DB Password>

env.jwks.data = <Cognito jwks.json link>
env.proxy.address = http://localhost:3000
env.frontend.address = http://localhost:5173
```

## Deployment

This project can be ran locally or in a Docker container. Clone the repository, create **sensitive.properties** file, and run the following commands.

### Docker

Checkout this blog article for steps to containerize this API --> [HERE](https://dev.to/tevindeale/containerize-a-java-spring-boot-app-1op3)

### Locally

Ensure that you have **openJDK 21** installed. **These next steps are done on a MAC device. Windows commands could differ**.

```bash
  ./mvnw sprint-boot:run
```

## API Reference

This API works in conjuction with a JWT access token retrieved from a oauth2 user pool. The token has to match the issuer which is defined in the **sensitive.properties** file.

### Home Controller

```http
  GET /home
```

Checks to see if API is reachable.

---

```http
  GET /hello
```

Checks to see if user token is valid. Will return the UUID from token.

---

### Customer Controller

```http
  POST /api/customer/add
```

| Parameter | Type     |
| :-------- | :------- |
| `username` | `string` |
| `uuid` | `string` |
| `fname` | `string` |
| `lname` | `string` |

Adds customer to DB

---

```http
  GET /api/customer
```

Returns customer object based off UUID in access token.

---

#### Account Controller

```http
  POST /api/account/add
```

| Parameter | Type     |
| :-------- | :------- |
| `type` | `CHECKING or SAVINGS` |
| `amount` | `string` |

Opens new account and adds initial deposit.

---

```http
  POST /api/account/delete
```

| Parameter | Type     |
| :-------- | :------- |
| `account_number` | `number` |

Closes account as long as the balance is 0.

---

```http
  POST /api/account
```

| Parameter | Type     |
| :-------- | :------- |
| `account_number` | `number` |

Gets a single account.

---

```http
  GET /api/account/getall
```

Gets all accounts that belong to customer.

---

```http
  POST /api/account/deposit
```

| Parameter | Type     |
| :-------- | :------- |
| `account_number` | `number` |
| `amount` | `string` |

Adds money to account balance.

---

```http
  POST /api/account/withdraw
```

| Parameter | Type     |
| :-------- | :------- |
| `account_number` | `number` |
| `amount` | `string` |

Removes money from account balance.

---

### Transaction Controller

## Contributing

Contributions are always welcome!

If you would like to contribute, reach out via X [@FiinnDev](https://x.com/FiinnDev)
