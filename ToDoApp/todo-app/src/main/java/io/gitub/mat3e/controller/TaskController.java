package io.gitub.mat3e.controller;

import io.gitub.mat3e.model.Task;
import io.gitub.mat3e.model.TaskRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.awt.print.Pageable;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.List;

@RestController
public class TaskController {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;


    TaskController(final TaskRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/tasks")
    ResponseEntity<Task> createTask(@RequestBody @Valid Task toCreate){
        Task result = repository.save(toCreate);
        return ResponseEntity.created(URI.create("/"+result.getId())).body(result);
    }
    @GetMapping(value ="/tasks", params = {"!sort","!page","!size"})
    ResponseEntity<List<Task>> readAllTasks(){

        logger.warn("Exposing all the tasks");

        return  ResponseEntity.ok(repository.findAll());
    }

    @GetMapping(value ="/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){

        logger.info("Custom pageable");

        return  ResponseEntity.ok(repository.findAll(page).getContent());
    }
    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/tasks/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id,@RequestBody @Valid Task toUpdate){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom(toUpdate);
                    repository.save(task);
                });

        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/tasks/{id}")
    public ResponseEntity<?> toggleTask(@PathVariable int id){
        if(!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setDone(!task.isDone()));

        return ResponseEntity.noContent().build();
    }

}