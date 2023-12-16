package com.example.model;

import com.fasterxml.jackson.databind.annotation.EnumNaming;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Heading cannot be blank")
    private String heading;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Status cannot be null")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Priority cannot be null")
    @Enumerated(EnumType.STRING)
    private Priority priority;

    @NotNull(message = "Owner cannot be null")
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @NotNull(message = "Executor cannot be null")
    @ManyToOne
    @JoinColumn(name = "executor_id")
    private User executor;

    public Task(String heading, String description, Status status, Priority priority, User owner, User executor) {
        this.heading = heading;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.owner = owner;
        this.executor = executor;
    }

    public Task() {
    }
}
