package org.example.Task_Manager.DTO.workers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.Task_Manager.DTO.tasks.statistics.TaskStatisticsDTO;

@Getter
@Setter
@AllArgsConstructor
public class WorkerProfileResponse {

    private WorkerDTO worker;
    private TaskStatisticsDTO taskStatistics;
}
