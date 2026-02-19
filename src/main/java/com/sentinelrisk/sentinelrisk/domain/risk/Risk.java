package com.sentinelrisk.sentinelrisk.domain.risk;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "risks")
@EntityListeners(AuditingEntityListener.class)
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;


    private String description;

    @Column(length = 50, nullable = true)
    //@Size(min = 3, max = 50)
    private String observation;


    //@NotNull(message = "Priority is mandatory")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    private String owner;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Risk() {

    }

    public Risk(String title, String description, Priority priority, Status status, String owner, String observation) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.owner = owner;
        this.observation = observation;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getOwner() {return owner;}
    public void setOwner(String owner) {this.owner = owner;}
    public LocalDateTime getCreatedAt() {return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt) {this.createdAt = createdAt;}

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }
}
