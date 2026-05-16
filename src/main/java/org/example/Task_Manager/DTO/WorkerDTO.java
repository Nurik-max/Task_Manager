    package org.example.Task_Manager.DTO;

    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.NotEmpty;
    import jakarta.validation.constraints.Size;
    import org.example.Task_Manager.Model.WorkerStatus;

    import java.rmi.registry.Registry;

    public class WorkerDTO {


        private Integer id;

        @NotBlank
        private String username;

        @NotBlank
        private String surname;

        @NotBlank
        private String position;

        private WorkerStatus workerStatus;

        @NotBlank
        private String email;

        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password;

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

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public WorkerStatus getWorkerStatus() {
            return workerStatus;
        }

        public void setWorkerStatus(WorkerStatus workerStatus) {
            this.workerStatus = workerStatus;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }
    }
