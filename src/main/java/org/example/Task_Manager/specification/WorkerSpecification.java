package org.example.Task_Manager.specification;

import org.example.Task_Manager.Model.Worker;
import org.example.Task_Manager.Model.WorkerStatus;
import org.springframework.data.jpa.domain.Specification;

public class WorkerSpecification {

    public static Specification<Worker> isNotFired() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("workerStatus"), WorkerStatus.FIRED);
    }

    // Поиск по части имени (like)
    public static Specification<Worker> hasName(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    // Поиск по части фамилии (like)
    public static Specification<Worker> hasSurname(String surname) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("surname")), "%" + surname.toLowerCase() + "%");
    }

    // Поиск по должности (точное совпадение)
    public static Specification<Worker> hasPosition(String position) {
        return (root, query, cb) -> cb.equal(root.get("position"), position);
    }

    // Поиск по статусу
    public static Specification<Worker> hasStatus(WorkerStatus workerStatus) {
        return (root, query, cb) -> cb.equal(root.get("workerStatus"), workerStatus);
    }
    public static Specification<Worker> isFired() {
        return (root, query, cb) -> cb.equal(root.get("workerStatus"), WorkerStatus.FIRED);
    }
}
