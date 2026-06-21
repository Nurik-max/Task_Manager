package org.example.Task_Manager.Repoitory;

import org.example.Task_Manager.DTO.TaskDTO;
import org.example.Task_Manager.DTO.tasks.request.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.UpdateTaskRequest;
import org.example.Task_Manager.Model.Task;
import org.mapstruct.*;

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
    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskForAdmin(@MappingTarget Task task, AdminUpdateTaskRequest request);

    //for user
    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskForUser(@MappingTarget Task task, UpdateTaskRequest request);

    // Перевод из DTO в Entity:
    @Mapping(target = "worker", ignore = true)
    Task toEntity(CreateTaskRequest request);

    @Mapping(target = "worker", ignore = true)
    Task adminToEntity(AdminCreateTaskRequest request);


}