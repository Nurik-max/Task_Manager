package org.example.Task_Manager.Sevice;

import org.example.Task_Manager.DTO.tasks.statistics.TaskStatisticsDTO;
import org.example.Task_Manager.Model.Status;
import org.example.Task_Manager.Model.Task;
import org.example.Task_Manager.Repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatisticsService {

    private TaskRepository taskRepository;

    public TaskStatisticsService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public long countTotalTasks(int workerId) {

        List<Task> totalTasks = taskRepository.findByWorker_Id(workerId);
        return totalTasks.size();

    }

    public long countCompletedTasks(int workerId) {
        int completedTasks = 0;
        List<Task> Tasks = taskRepository.findByWorker_Id(workerId);

        for (Task task : Tasks) {
            if(task.getStatus() == Status.DONE){
                completedTasks++;
            }
        }

        return completedTasks;
    }

    public long countInProgressTasks(int workerId) {

        int inProgressTasks = 0;
        List<Task> Tasks = taskRepository.findByWorker_Id(workerId);

        for (Task task : Tasks) {
            if(task.getStatus() == Status.IN_PROGRESS){
               inProgressTasks++;
            }
        }

        return inProgressTasks;
    }

    public long countNewTasks(int workerId) {
        int newTasks = 0;
        List<Task> Tasks = taskRepository.findByWorker_Id(workerId);
        for (Task task : Tasks) {
            if(task.getStatus() == Status.NEW){

                newTasks++;
            }
        }
        return newTasks;
    }

    public TaskStatisticsDTO getStatistics(int workerId) {

        TaskStatisticsDTO dto = new TaskStatisticsDTO();

        dto.setTotalTasks(countTotalTasks(workerId));
        dto.setCompletedTasks(countCompletedTasks(workerId));
        dto.setInProgressTasks(countInProgressTasks(workerId));
        dto.setNewTasks(countNewTasks(workerId));

        return dto;
    }
}
