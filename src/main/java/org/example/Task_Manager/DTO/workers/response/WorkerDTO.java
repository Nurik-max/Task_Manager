    package org.example.Task_Manager.DTO.workers.response;

    import lombok.Getter;
    import lombok.Setter;
    import org.example.Task_Manager.Model.WorkerStatus;
    import org.hibernate.annotations.CreationTimestamp;

    import java.time.LocalDateTime;


    @Getter
    @Setter
    public class WorkerDTO {


        private Integer id;

        private String username;

        private String surname;


        private String position;

        private WorkerStatus workerStatus;

        private String email;

        @CreationTimestamp
        private LocalDateTime createdDate;


        public WorkerDTO(String username, String surname ,String position, String email) {
            this.username = username;
            this.surname = surname;
            this.position = position;
            this.email = email;
            this.createdDate = LocalDateTime.now();

        }

        public WorkerDTO() {

        }



    }
