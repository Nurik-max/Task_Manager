package org.example.Task_Manager.Repository;

import org.example.Task_Manager.Model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer>, JpaSpecificationExecutor<Worker> {

    Optional<Worker> findByUsername (String username);
    List<Worker> findBySurname (String surname);
    List<Worker> findByPosition (String position);
    Boolean existsWorkerById(int id);
    Boolean existsWorkerByUsername(String username);
    List<Worker> findByIdNot(int id);


}
