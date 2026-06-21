package org.example.Task_Manager.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;


import java.time.LocalDateTime;
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Getter
    @Column(nullable = false, length = 255)
    private String description;

//    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    private LocalDateTime deletedAt;

    @Column(name = "isDeleted")
    private Boolean isDeleted = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
     private Priority priority;

    @ManyToOne
    @JoinColumn(name = "worker_id")
    private Worker worker;


    public Task(String description, LocalDateTime createdDate, LocalDateTime updatedAt, Status status, Priority priority) {

        this.description = description;
        this.createdDate = createdDate;
        this.updatedDate = updatedAt;
        this.status = status;
        this.priority = priority;
    }
    public Task(){

    };

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedDate;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedDate = updatedAt;
    }
}
