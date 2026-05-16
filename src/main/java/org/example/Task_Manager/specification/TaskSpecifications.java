package org.example.Task_Manager.specification;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.springframework.data.jpa.domain.Specification;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaskSpecifications {

    /**
     * Фильтр по статусу.
     * SQL аналог: WHERE status = 'ENUM_VALUE'
     */
    public static Specification<Task> hasStatus(Status status){

        return (root, query, criteriaBuilder) -> {
// Если статус не передан, возвращаем "conjunction" (пустое условие 1=1)
            if(status == null){
                return criteriaBuilder.conjunction();
            }
            // Генерируем условие равенства: колонки "status" значению переменной status
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    /**
     * Поиск по ключевому слову в описании (без учета регистра).
     * SQL аналог: WHERE LOWER(description) LIKE '%keyword%'
     */
    public static Specification<Task> hasKeyword(String keyword){

        return (root, query, criteriaBuilder) -> {

            if(keyword == null || keyword.isBlank()){
                criteriaBuilder.conjunction();
            }

            // 1. Берем поле "description"
            // 2. Приводим его к нижнему регистру: cb.lower(...)
            // 3. Сравниваем по маске LIKE: %слово%
            return criteriaBuilder.like(criteriaBuilder.
                    lower(root.get("description")), "%" + keyword.toLowerCase() + "%");
        };

    }

    public static Specification<Task> hasWorker(String name){
        return ((root, query, criteriaBuilder) -> {

            if(name == null || name.isBlank()){
               return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(criteriaBuilder.
                    lower(root.join("worker").get("name")),"%" + name.toLowerCase() + "%");
        });
        }


    //Methods for sorting by date
    public static Specification<Task> createdAfter(LocalDateTime start){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), start));
    }

    public static Specification<Task> createdBefore(LocalDateTime end){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("createdDate"), end));
    }

    public static Specification<Task> isDeleted(Boolean isDeleted){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isDeleted"), isDeleted));
    }
}
