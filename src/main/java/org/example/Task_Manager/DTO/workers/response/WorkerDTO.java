    package org.example.Task_Manager.DTO.workers.response;

    import lombok.Getter;
    import lombok.NoArgsConstructor;
    import lombok.Setter;
    import org.example.Task_Manager.Model.WorkerStatus;
    import org.hibernate.annotations.CreationTimestamp;

    import java.time.LocalDateTime;


    @Getter
    @Setter
    @NoArgsConstructor
    public class WorkerDTO {


        private Integer id;

        private String username;

        private String surname;


        private String position;

        private WorkerStatus workerStatus;

        private String email;

        private String phone;

        private LocalDateTime createdDate;







    }
