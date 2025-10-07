Spring Boot REST API для управления банковскими картами с JWT аутентификацией и ролевым доступом.

Технологии:

* Backend: Java 17, Spring Boot 3.2.0, Spring Security, JPA/Hibernate
* База данных: MySQL 9.4.0, Liquibase
* Безопасность: JWT, BCrypt
* Документация: OpenAPI 3.0, Swagger UI
* Контейнеризация: Docker, Docker Compose

# Запуск приложения

1. Необходим файл .env, со следующмим переменными:

```
DATABASE_ROOT_PASSWORD=00001

DATABASE_NAME=effmob

DATABASE_USER=root

DATABASE_HOST_PORT=7777

DATABASE_CONTAINER_PORT=3306

APPLICATION_HOST_PORT=8080

APPLICATION_CONTAINER_PORT=8080
```

2. Команда для запуска:
`docker compose up`

Приложение доступно по адресу:
http://localhost:APPLICATION_HOST_PORT

# Тестовые пользователи

| Username | Password | Role |
| --- | --- | --- |
| Kororok | 123 | ADMIN |
| max | 777 | USER |

# Основные API Endpoints

Аутентификация

* POST /auth/sing-in - вход в систему
    * Данные: {username, password}

* POST /auth/refresh - обновление токена
    * Данные: {refreshToken}

Регистрация

* POST /register - регистрация пользователя
    * Данные: {username, password, passwordConfirm}

Карты (для пользователей)

* POST /card/show - мои карты (номера маскированы)
    * Данные: {page, size, sortBy, directionSort, ...фильтры}
* POST /card/show-full-number - мои карты (полные номера)
    * Данные: {page, size, ...фильтры}
* POST /card/transfer - перевод между картами
    * Данные: {firstCardNumber, secondCardNumber, amountTransferBetweenCards}

Администраторские endpoints

* GET /user/all - все пользователи
* POST /user/delete - удалить пользователя
    * Данные: {username}
* GET /card/all - все карты с фильтрацией
* POST /card/add - создать карту
    * Данные: {cardNumber, ownerId, validityPeriod, balance}
* POST /card/block - заблокировать карту
    * Данные: {cardNumber}
* POST /card/activate - активировать карту
    * Данные: {cardNumber}
* POST /card/delete - удалить карту
    * Данные: {cardNumber}

# Пример использования

1. Аутентификация

```bash
curl -X POST "http://localhost:8080/auth/sing-in" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "Kororok",
    "password": "123"
  }'
```
Ответ:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

2. Просмотр своих карт

```bash
curl -X POST "http://localhost:8080/card/show" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your_token>" \
  -d '{
    "page": 0,
    "size": 10
  }'
```

Примечание: Для администраторских endpoints требуется роль ADMIN. Все запросы (кроме аутентификации и регистрации) требуют JWT токен в заголовке Authorization: Bearer <token>.

