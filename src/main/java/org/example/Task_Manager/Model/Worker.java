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
    private Integer id;

    @NotEmpty(message = "Username should be not empty!")
    @Size(min = 2, message = "Username of description should be not less than 2 character!")
    private String username;

    @NotEmpty(message = "Surname should be not empty!")
    @Size(min = 2, message = "Surname of description should be not less than 2 character!")
    private String surname;

    @Column(name = "email", nullable = false,length = 30)
    private String email;

    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false)
    private UserRole userRole;

    @NotEmpty(message = "Position should be not empty!")
    private String position;

    @OneToMany(mappedBy = "worker")
    private List<Task> tasks;

    @Enumerated(EnumType.STRING)
    private WorkerStatus workerStatus;

    public Worker(String username, String surname, String email, String password, UserRole userRole, String position) {
        this.username = username;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.position = position;
    }

    public Worker() {

    }

    public Integer getId() {
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
