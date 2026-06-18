package org.example.Task_Manager.Repoitory;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.DTO.tasks.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.UpdateTaskRequest;
import org.example.Task_Manager.Model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // Перевод из Entity в DTO:
    @Mapping(target = "workerId", source = "worker.id")
    // Вот здесь MapStruct сам возьмет имя из worker.name и положит в workerName
    @Mapping(target = "workerUsername", source = "worker.username")
    @Mapping(target = "workerSurname", source = "worker.surname")
    @Mapping(target = "deletedAt", source = "deletedAt")
    @Mapping(target = "createdDate", source = "createdDate")
    TaskDTO toDTO(Task task);

    //for admin
    void updateTaskForAdmin(@MappingTarget Task task, AdminUpdateTaskRequest request);

    //for user
    void updateTaskForUser(@MappingTarget Task task, UpdateTaskRequest request);

    // Перевод из DTO в Entity:
    @Mapping(target = "worker", ignore = true)
    Task toEntity(AdminCreateTaskRequest request);
}