package org.example.Task_Manager.Repoitory;

import org.example.Task_Manager.Model.Worker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository extends JpaRepository<Worker, Integer>, JpaSpecificationExecutor<Worker> {

    List<Worker> findByName (String name);
    List<Worker> findBySurname (String surname);
    List<Worker> findByPosition (String position);

    Boolean existsWorkerById(int id);


}
