package org.example.Task_Manager.Repoitory;

import org.example.Task_Manager.DTO.WorkerDTO;
import org.example.Task_Manager.Model.Worker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkerMapper {

    @Mapping(source = "workerStatus", target = "workerStatus")
    WorkerDTO toDTO(Worker worker);

    @Mapping(source = "workerStatus", target = "workerStatus")
    Worker toEntity(WorkerDTO workerDTO);
}
