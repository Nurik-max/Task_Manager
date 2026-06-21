package org.example.Task_Manager.Repository;

import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Model.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Integer>, JpaSpecificationExecutor<Task> {
    List<Task> findByStatus(Status status);

    List<Task> findByDescriptionContainingIgnoreCase(String keyword);

    Page<Task> findByStatus(Status status, Pageable pageable);

    Page<Task> findByDescriptionContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Task> findByStatusAndDescriptionContainingIgnoreCase(
            Status status, String keyword, Pageable pageable);

    Page<Task> findByIsDeletedTrue(Pageable pageable);

    List<Task> findByWorker(Worker worker);

    List<Task> findByWorker_Id(int workerId);

    List<Task> findByWorkerUsernameContainingIgnoreCase(String username);

    Optional<Task> findByIdAndWorkerUsername(int id, String username);
    Page<Task> findByWorkerIdAndIsDeletedTrue(int id, Pageable pageable);
}
