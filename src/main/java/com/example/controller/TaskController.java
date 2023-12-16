package com.example.controller;

import com.example.model.Priority;
import com.example.model.Status;
import com.example.model.Task;
import com.example.model.request.CreateTaskRequest;
import com.example.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/tasks")
    @Operation(summary = "Получить задачи в зависимости от переданных параметров", description = "1) При отсутствии переданных параметров " +
            "сервер отдаст все существующие задачи 2) При указании любого количества из 4ех параметров фильтрации(email создателя задачи, " +
            "email исполнителя задачи, статус задачи, приоритет задачи) сервер отдаст все задачи соответсвующие этим параметрам")
    public Page<Task> findTasks(@RequestParam(required = false)@Parameter(description = "Email исполнителя") String executorEmail,
                                @RequestParam(required = false)@Parameter(description = "Email создателя") String ownerEmail,
                                @RequestParam(required = false)@Parameter(description = "Статус задачи") Status status,
                                @RequestParam(required = false)@Parameter(description = "Приоритет задачи") Priority priority,
                                Pageable pageable,
                                Principal principal) {
        return taskService.findTasks(executorEmail, ownerEmail, status, priority, pageable);
    }

    @PostMapping("/tasks")
    @Operation(summary = "Создать задачу", description = "Обязательно указание всех полей задачи. Email заказчика " +
            "указывать не нужно, он подкрепится автоматически")
    public ResponseEntity<String> createTask(@Valid @RequestBody @Parameter(description = "Шаблон задачи") CreateTaskRequest task,
                                           Principal principal) {
        return taskService.createTask(task.getHeading(), task.getDescription(), task.getStatus(), task.getPriority(), task.getEmailOfExecutor(), principal);
    }

    @PutMapping("/tasks/{taskId}")
    @Operation(summary = "Изменить существующую задачу")
    public ResponseEntity<Void> editTask(@PathVariable Long taskId,
                                         @Valid @RequestBody Task task,
                                         Principal principal) {
        return taskService.editTask(task, taskId, principal);
    }

    @PatchMapping ("/tasks/{taskId}")
    @Operation(summary = "Изменить статус существующей задачи.", description = "Изменять статус " +
            "может либо исполнитель, либо создатель задачи указанный в выбранной задаче, никто более.")
    public ResponseEntity<Void> updateStatusOfTask(@PathVariable Long taskId,
                                                   @RequestParam @Parameter(description = "Статус задачи") Status status,
                                                   Principal principal) {
        return taskService.updateStatus(taskId, status, principal);
    }

}
