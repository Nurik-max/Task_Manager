package org.example.Task_Manager.Repository.Worker;


import org.example.Task_Manager.Repository.WorkerMapper;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class WorkMapperTest {


    private final WorkerMapper workerMapper = Mappers.getMapper(WorkerMapper.class);
//    @Test
//    void testToDTO(){
//
//        Worker worker = new Worker();
//        worker.setName("William");
//
//        WorkerDTO workerDTO = workerMapper.toDTO(worker);
//
//        assertEquals("William", workerDTO.getName());
//
//    }
}
