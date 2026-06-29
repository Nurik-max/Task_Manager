package org.example.Task_Manager.Repository;

import org.example.Task_Manager.DTO.tasks.request.AdminCreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.AdminUpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.CreateTaskRequest;
import org.example.Task_Manager.DTO.tasks.request.UpdateTaskRequest;
import org.example.Task_Manager.DTO.tasks.response.AdminResponse;
import org.example.Task_Manager.DTO.tasks.response.UserResponse;
import org.example.Task_Manager.Model.Task;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    // USER VIEW
    @Mapping(target = "workerUsername", source = "worker.username")
    UserResponse toUserResponse(Task task);

    // ADMIN VIEW
    @Mapping(target = "workerId", source = "worker.id")
    @Mapping(target = "username", source = "worker.username")
    @Mapping(target = "surname", source = "worker.surname")
    AdminResponse toAdminResponse(Task task);

    // CREATE
    @Mapping(target = "worker", ignore = true)
    Task toEntity(CreateTaskRequest request);

    @Mapping(target = "worker", ignore = true)
    Task toEntity(AdminCreateTaskRequest request);


    //for admin
    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskForAdmin(@MappingTarget Task task, AdminUpdateTaskRequest request);

    //for user
    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    void updateTaskForUser(@MappingTarget Task task, UpdateTaskRequest request);

}