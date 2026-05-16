package org.example.Task_Manager.Repoitory;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.Model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // Перевод из Entity в DTO:
    @Mapping(target = "workerId", source = "worker.id")
    // Вот здесь MapStruct сам возьмет имя из worker.name и положит в workerName
    @Mapping(target = "workerName", source = "worker.name")
    @Mapping(target = "workerSurname", source = "worker.surname")
    TaskDTO toDTO(Task task);

    // Перевод из DTO в Entity:
    @Mapping(target = "worker", ignore = true)
    Task toEntity(TaskDTO taskDTO);
}