package org.example.Task_Manager.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.boot.model.source.internal.hbm.AttributesHelper;

import java.util.List;

@Setter
@Getter
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

}
