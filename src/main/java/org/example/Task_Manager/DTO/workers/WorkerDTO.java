    package org.example.Task_Manager.DTO.workers;

    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotEmpty;
    import jakarta.validation.constraints.Size;
    import lombok.Getter;
    import lombok.Setter;
    import org.example.Task_Manager.Model.WorkerStatus;

    import java.rmi.registry.Registry;

    @Setter
    public class WorkerDTO {


        private Integer id;

        @Getter
        @NotBlank
        private String username;

        @Getter
        @NotBlank
        private String surname;

        @Getter
        @NotBlank
        private String position;

        @Getter
        private WorkerStatus workerStatus;

        @Getter
        @NotBlank
        private String email;

        @Getter
        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

        @Getter
        @NotBlank()
        private String confirmPassword;

        public WorkerDTO(String username, String surname ,String position, String email, String password, String confirmPassword) {
            this.username = username;
            this.surname = surname;
            this.position = position;
            this.email = email;
            this.password = password;
            this.confirmPassword = confirmPassword;
        }

        public WorkerDTO() {

        }

        public int getId() {
            return id;
        }

    }
