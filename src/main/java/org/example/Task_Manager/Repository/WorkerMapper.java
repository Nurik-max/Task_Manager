package org.example.Task_Manager.Repository;

import org.example.Task_Manager.DTO.workers.CreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.response.AdminWorkerResponse;
import org.example.Task_Manager.DTO.workers.response.WorkerDTO;
import org.example.Task_Manager.DTO.workers.AdminCreateWorkerDTO;
import org.example.Task_Manager.DTO.workers.UpdateWorkerDTO;
import org.example.Task_Manager.DTO.workers.request.ProfileUpdateDTO;
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

    //Response for Admin
    AdminWorkerResponse toAdminWorkerResponse(Worker worker);

    //Response for Users
    WorkerDTO toUserResponse(Worker worker);

    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateWorkerFromDTO(
            UpdateWorkerDTO dto,
            @MappingTarget Worker worker
    );


    @BeanMapping(
            nullValuePropertyMappingStrategy =
                    NullValuePropertyMappingStrategy.IGNORE
    )
    void updateProfileFromDTO(
            ProfileUpdateDTO dto,
            @MappingTarget Worker worker
    );
}
