package org.example.Task_Manager.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.boot.model.source.internal.hbm.AttributesHelper;

import java.util.List;

@Entity
@Table(name = "workers")
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "worker_id")
    private int id;

    @NotEmpty(message = "Name should be not empty!")
    @Size(min = 2, message = "Name of description should be not less than 2 character!")
    private String name;

    @NotEmpty(message = "Surname should be not empty!")
    @Size(min = 2, message = "Surname of description should be not less than 2 character!")
    private String surname;

    @NotEmpty(message = "Position should be not empty!")
    private String position;

    @OneToMany(mappedBy = "worker")
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
    private WorkerStatus workerStatus;

    public Worker(String name, String surname, String position){

        this.name = name;
        this.surname = surname;
        this.position = position;
    }


    public Worker() {

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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
