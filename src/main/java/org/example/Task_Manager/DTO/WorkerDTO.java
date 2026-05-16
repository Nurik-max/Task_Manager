    package org.example.Task_Manager.DTO;

    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.validation.constraints.NotEmpty;
    import jakarta.validation.constraints.Size;
    import org.example.Task_Manager.Model.WorkerStatus;

    public class WorkerDTO {


        private int id;

        private String name;

        private String surname;

        private String position;

        private WorkerStatus workerStatus;

        public WorkerDTO(String name, String surname, String position) {
            this.name = name;
            this.surname = surname;
            this.position = position;
        }

        public WorkerDTO() {

        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }
