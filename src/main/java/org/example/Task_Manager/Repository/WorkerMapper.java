package org.example.Task_Manager.Repository;

import org.example.Task_Manager.DTO.workers.CreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.WorkerDTO;
import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.Model.Worker;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WorkerMapper {

    @Mapping(target = "userRole", constant = "USER")
    @Mapping(target = "workerStatus", constant = "WORKS")
    Worker toEntity(CreateWorkerDTO dto);

//    @Mapping(source = "userRole", target = "userRole")
@Mapping(target = "workerStatus", constant = "WORKS")
    Worker adminCreateWorkerFromDTO(AdminCreateWorkerDTO dto);

    WorkerDTO toDTO(Worker worker);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateWorkerFromDTO(
            UpdateWorkerDTO dto,
            @MappingTarget Worker worker
    );
}
