package io.gitub.mat3e.controller;

import io.gitub.mat3e.model.Task;
import io.gitub.mat3e.model.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    TaskRepository repo;
    @Test
    void httpGet_returnAllTasks(){
    int initial = repo.findAll().size();
    repo.save(new Task("foo", LocalDateTime.now()));
    repo.save(new Task("bar", LocalDateTime.now()));


    Task[] result = restTemplate.getForObject("http://localhost:"+port+"/tasks",Task[].class);
    assertThat(result).hasSize(initial+2);


    }

}