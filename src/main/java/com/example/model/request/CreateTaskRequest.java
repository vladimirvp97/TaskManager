package com.example.model.request;

import com.example.model.Priority;
import com.example.model.Status;
import com.example.model.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class CreateTaskRequest {

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

    @Email
    private String emailOfExecutor;

    public CreateTaskRequest(String heading, String description, Status status, Priority priority, String emailOfExecutor) {
        this.heading = heading;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.emailOfExecutor = emailOfExecutor;
    }

    public CreateTaskRequest() {
    }
}
