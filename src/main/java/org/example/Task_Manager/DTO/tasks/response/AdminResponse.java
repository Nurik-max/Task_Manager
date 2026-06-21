package org.example.Task_Manager.DTO.tasks.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.Task_Manager.Model.Priority;
import org.example.Task_Manager.Model.Status;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
public class AdminResponse {

        private int id;
        private String description;

        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;
        private LocalDateTime deletedAt;

        private Status status;
        private Priority priority;

        private Integer workerId;
        private String workerUsername;
        private String workerSurname;

        public String getWorkerFullName() {
                if (workerUsername != null && workerSurname != null) {
                        return workerUsername + " " + workerSurname;
                }
                return null;
        }

}
