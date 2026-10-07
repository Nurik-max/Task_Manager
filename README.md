# Task Manager

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

Веб-приложение для управления задачами на Spring Boot.

## Технологии 
- Java 21 
- Spring Boot
- Spring Security 
- Spring Data JPA 
- PostgreSQL 
- Thymeleaf 
- Docker
- Swagger/OpenAPI.

## Основные возможности
* реализовал регистрацию и аутентификацию пользователей;
* настроил разграничение ролей ADMIN и USER;
* разработал CRUD-функциональность для задач;
* реализовал фильтрацию задач по статусу, приоритету и диапазону дат;
* разработал REST API и документацию Swagger/OpenAPI;
* настроил запуск приложения и PostgreSQL через Docker Compose;
* использовал JPA/Hibernate для работы с базой данных.
  
## Статус проекта
Проект находится в рабочем состоянии. Основной функционал реализован и протестирован.
Реализовано:

* REST API и MVC-интерфейс;
* аутентификация и авторизация;
* управление работниками и задачами;
* CRUD-операции;
* фильтрация задач;
* soft delete, восстановление и корзина;
* валидация и обработка ошибок;
* Swagger/OpenAPI;
* Docker Compose и PostgreSQL;
* автоматические тесты основных компонентов и REST endpoints.

В дальнейшем возможны дополнительные улучшения и расширение функциональности проекта.

## Быстрый старт

```bash
git clone https://github.com/Nurik-max/Task_Manager.git
cd Task_Manager
docker compose up --build
```
Приложение: http://localhost:8080

Страница входа: http://localhost:8080/login

Swagger UI: http://localhost:8080/swagger-ui/index.html

## Скриншоты

### Главная страница с задачами
<img width="1844" height="977" alt="Screenshot From 2026-08-06 07-23-09" src="https://github.com/user-attachments/assets/56b6990c-3cef-43aa-8be8-1d4fa0d05f38" />

### Страница со списком сотрудников
<img width="1844" height="977" alt="Screenshot From 2026-08-06 07-25-21" src="https://github.com/user-attachments/assets/2ec79212-5ec8-4fec-8c67-3f93e08d01a1" />

### REST API версия
<img width="1844" height="977" alt="Screenshot From 2026-08-06 07-54-17" src="https://github.com/user-attachments/assets/8bc17eb4-b178-4586-965c-2361a131080d" />
<img width="1844" height="977" alt="Screenshot From 2026-08-06 07-59-52" src="https://github.com/user-attachments/assets/27c05b79-1fa4-44c6-a777-cc7df05aa12c" />



