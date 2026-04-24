# Banking Cards Management API

Spring Boot REST API для управления банковскими картами с JWT-аутентификацией, ролевым доступом, CRUD-операциями, переводами между картами, фильтрацией, пагинацией, миграциями базы данных и Swagger/OpenAPI-документацией.

Проект реализован как backend-приложение на Java/Spring и построен по многослойной архитектуре: controller → service → repository → database.

## О проекте

Приложение моделирует backend-сервис для управления банковскими картами.

В системе есть два типа пользователей:

- `USER` — обычный пользователь;
- `ADMIN` — администратор.

Обычный пользователь может просматривать свои карты, видеть маскированные или полные номера своих карт и выполнять переводы между собственными картами.

Администратор может управлять пользователями и картами: просматривать список пользователей, создавать карты, блокировать, активировать и удалять карты.

## Функциональность

### Аутентификация и безопасность

- Регистрация пользователей.
- Аутентификация через JWT.
- Поддержка access token и refresh token.
- Ролевая модель доступа `USER` / `ADMIN`.
- Защита endpoint’ов через Spring Security.
- Хеширование паролей через BCrypt.
- Передача JWT через заголовок `Authorization: Bearer your-access-token`.

### Работа с банковскими картами

- Создание банковской карты администратором.
- Просмотр карт пользователя.
- Просмотр карт с маскированными номерами.
- Просмотр карт с полными номерами.
- Блокировка карты.
- Активация карты.
- Удаление карты.
- Фильтрация и пагинация списка карт.
- Перевод средств между картами.

### Работа с пользователями

- Получение списка пользователей.
- Удаление пользователя.
- Разделение доступа между обычным пользователем и администратором.

### Работа с базой данных

- Использование MySQL.
- Работа с сущностями через Hibernate / Spring Data JPA.
- Миграции схемы БД через Liquibase.
- Использование DTO для разделения внутренней модели приложения и данных API.

## Tech Stack

**Language:** Java 17

**Backend:** Spring Boot 3.2.0, Spring Web, Spring Security, Spring Data JPA, Hibernate, QueryDSL

**Database:** MySQL, Liquibase

**Security:** JWT, Spring Security, BCrypt

**Documentation:** Swagger UI, OpenAPI 3

**Build:** Maven

**Containerization:** Docker, Docker Compose

**Testing:** JUnit, Spring Boot Test, Spring Security Test

## Структура проекта

```text
src/main/java/com/example/bankcards
├── config        # Конфигурация Spring Security, Swagger, QueryDSL
├── controller    # REST-контроллеры
├── dto           # DTO-классы для запросов и ответов API
├── entity        # JPA-сущности
├── exception     # Исключения и обработка ошибок
├── repository    # Spring Data JPA repositories и custom repository
├── security      # JWT-фильтр и UserDetailsService
├── service       # Бизнес-логика приложения
├── util          # Вспомогательные классы
└── Application.java
```

## API Endpoints

### Authentication

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/auth/sing-in` | Вход в систему | Public |
| `POST` | `/auth/refresh` | Обновление access token | Public |

### Registration

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/register` | Регистрация нового пользователя | Public |

### Cards — User

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `POST` | `/card/show` | Получить свои карты с маскированными номерами | USER |
| `POST` | `/card/show-full-number` | Получить свои карты с полными номерами | USER |
| `POST` | `/card/transfer` | Перевод между картами | USER |

### Cards — Admin

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/card/all` | Получить все карты с фильтрацией и пагинацией | ADMIN |
| `POST` | `/card/add` | Создать карту | ADMIN |
| `POST` | `/card/block` | Заблокировать карту | ADMIN |
| `POST` | `/card/activate` | Активировать карту | ADMIN |
| `POST` | `/card/delete` | Удалить карту | ADMIN |

### Users — Admin

| Method | Endpoint | Description | Access |
|---|---|---|---|
| `GET` | `/user/all` | Получить список всех пользователей | ADMIN |
| `POST` | `/user/delete` | Удалить пользователя | ADMIN |

## Запуск через Docker Compose

### Требования

Для запуска проекта нужны:

- Docker;
- Docker Compose.

### Запуск

1. Склонировать репозиторий:

```bash
git clone https://github.com/KororokisRock/Spring-Boot-Application.git
cd Spring-Boot-Application
```

2. Создать файл `.env` в корне проекта.

Пример:

```env
DATABASE_ROOT_PASSWORD=change_me
DATABASE_NAME=bank_cards
DATABASE_USER=root
DATABASE_HOST_PORT=7777
DATABASE_CONTAINER_PORT=3306

APPLICATION_HOST_PORT=8080
APPLICATION_CONTAINER_PORT=8080
```

3. Запустить приложение:

```bash
docker compose up --build
```

4. После запуска приложение будет доступно по адресу:

```text
http://localhost:8080
```

Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI docs:

```text
http://localhost:8080/v3/api-docs
```

## Тестовые пользователи

После применения миграций в базе доступны тестовые пользователи:

| Username | Password | Role |
|---|---|---|
| `Kororok` | `123` | `ADMIN` |
| `max` | `777` | `USER` |

> Эти пользователи предназначены для локального тестирования проекта.

## Примеры запросов

### Авторизация

```bash
curl -X POST "http://localhost:8080/auth/sing-in" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Kororok",
    "password": "123"
  }'
```

Пример ответа:

```json
{
  "token": "access-token-example",
  "refreshToken": "refresh-token-example"
}
```

### Обновление токена

```bash
curl -X POST "http://localhost:8080/auth/refresh" \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "your-refresh-token"
  }'
```

### Просмотр своих карт

```bash
curl -X POST "http://localhost:8080/card/show" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-access-token" \
  -d '{
    "page": 0,
    "size": 10,
    "sortBy": "id",
    "directionSort": "ASC"
  }'
```

### Перевод между картами

```bash
curl -X POST "http://localhost:8080/card/transfer" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-access-token" \
  -d '{
    "firstCardNumber": "1111222233334444",
    "secondCardNumber": "5555666677778888",
    "amountTransferBetweenCards": 100
  }'
```

### Создание карты администратором

```bash
curl -X POST "http://localhost:8080/card/add" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer admin-access-token" \
  -d '{
    "cardNumber": "1111222233334444",
    "ownerId": 1,
    "validityPeriod": "2028-12-31",
    "balance": 1000
  }'
```

## Swagger / OpenAPI

В проекте используется Swagger/OpenAPI.

После запуска приложения документация доступна по адресу:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI-спецификация находится в директории:

```text
docs/api-docs.yaml
```

Дополнительное описание API:

```text
docs/Доп. описания API.md
```

## Тестирование

Для запуска тестов:

```bash
mvn test
```

В проекте используются:

- JUnit;
- Spring Boot Test;
- Spring Security Test.

Тесты находятся в директории:

```text
src/test/java/com/example/bankcards
```

## Docker

Проект содержит multi-stage Dockerfile.

На первом этапе используется Maven-образ для сборки приложения.

На втором этапе используется runtime-образ `amazoncorretto:17-alpine`, куда копируется собранный jar-файл.

Docker Compose поднимает два сервиса:

- `spring-boot-app` — backend-приложение;
- `db` — MySQL database.

Для MySQL настроен healthcheck, чтобы приложение запускалось после готовности базы данных.
