package org.example.Task_Manager.DTO.tasks.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TaskStatisticsDTO {
    private long totalTasks;
    private long completedTasks;
    private long inProgressTasks;
    private long newTasks;

}
