package com.example.services;

import com.example.dao.TaskDao;
import com.example.dao.UserDao;
import com.example.model.Priority;
import com.example.model.Status;
import com.example.model.Task;
import com.example.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {


    private final TaskDao taskDao;
    private final UserDao userDao;

    public ResponseEntity<String> createTask(String heading, String description,
                                           Status status, Priority priority,
                                           String executorEmail, Principal principal){
        if (!userDao.existsByEmail(executorEmail))
            return new ResponseEntity<String>("Исполнителя с таким email не существует" ,HttpStatus.NOT_FOUND);
        Task task = new Task(heading, description, status, priority,
                userDao.findByEmail(principal.getName()),userDao.findByEmail(executorEmail));
        taskDao.save(task);
        return new ResponseEntity<String>("" ,HttpStatus.OK);
    }

    public ResponseEntity<Void> editTask(Task task, Long taskId, Principal principal){
        if (!taskDao.existsById(taskId))
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        if (!principal.getName().equals(task.getOwner().getEmail()))
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

        task.setId(taskId);
        taskDao.save(task);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public ResponseEntity<Void> updateStatus(Long taskId, Status status, Principal principal){
        if (!taskDao.existsById(taskId))
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);

        if (!principal.getName().equals(taskDao.findById(taskId).get().getOwner().getEmail()) &&
                !principal.getName().equals(taskDao.findById(taskId).get().getExecutor().getEmail()))
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);

        Optional<Task> taskOptional = taskDao.findById(taskId);
        Task task = taskOptional.get();
        task.setStatus(status);

        taskDao.save(task);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    public Page<Task> findTasks(String executorEmail,
                                String ownerEmail,
                                Status status,
                                Priority priority,
                                Pageable pageable){
        if (executorEmail == null && ownerEmail == null) {
            return taskDao.findAll(pageable);
        }

        Task task = new Task();
        task.setExecutor(userDao.findByEmail(executorEmail));
        task.setOwner(userDao.findByEmail(ownerEmail));
        task.setPriority(priority);
        task.setStatus(status);

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues();
        Example<Task> example = Example.of(task, matcher);

        return taskDao.findAll(example, pageable);
    }
}
