# Task Manager

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

Веб-приложение для управления задачами на Spring Boot.

### Task Manager — веб-приложение для управления задачами

**GitHub:** https://github.com/Nurik-max/Task_Manager

Технологии: Java 21, Spring Boot, Spring Security, Spring Data JPA, PostgreSQL, Thymeleaf, Docker, Swagger/OpenAPI.

* реализовал регистрацию и аутентификацию пользователей;
* настроил разграничение ролей ADMIN и USER;
* разработал CRUD-функциональность для задач;
* реализовал фильтрацию задач по статусу, приоритету и диапазону дат;
* разработал REST API и документацию Swagger/OpenAPI;
* настроил запуск приложения и PostgreSQL через Docker Compose;
* использовал JPA/Hibernate для работы с базой данных.

## Структура проекта
src/main/java/org/example/Task_Manager
├── Controller
├── DTO
├── Entity
├── Repository
├── Security
├── Service
└── Config

## Статус проекта

Проект находится в активной разработке.

Планируется:

улучшение интерфейса;
пагинация и сортировка;
дополнительные REST endpoints;
расширение тестового покрытия.

## Быстрый старт

```bash
git clone https://github.com/Nurik-max/Task_Manager.git
cd Task_Manager
docker compose up --build
```

Приложение: http://localhost:8080/login

Swagger UI: http://localhost:8080/swagger-ui/index.html
## Скриншоты

# Главная страница с задачами
<img width="1844" height="977" alt="Screenshot From 2026-08-06 07-23-09" src="https://github.com/user-attachments/assets/56b6990c-3cef-43aa-8be8-1d4fa0d05f38" />

# Страница со списком сотрудников
<img width="1844" height="977" alt="Screenshot From 2026-08-06 07-25-21" src="https://github.com/user-attachments/assets/2ec79212-5ec8-4fec-8c67-3f93e08d01a1" />
