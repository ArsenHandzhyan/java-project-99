package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(exposedHeaders = "X-Total-Count")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        var tasks = taskService.findAll();
        List<TaskDTO> taskDTO = new ArrayList<>();
        taskMapper.map(taskDTO, tasks);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(taskDTO.size()));

        return ResponseEntity.ok().headers(headers).body(taskDTO);
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        TaskDTO taskDTO = new TaskDTO();
        taskMapper.map(taskDTO, taskService.findById(id));
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(taskDTO.getName()));
        return taskDTO;
    }

    @PostMapping
    public TaskDTO createTask(@RequestBody TaskCreateDTO taskCreateDTO) {
        TaskDTO taskDTO = new TaskDTO();
        taskMapper.map(taskDTO, taskService.create(taskCreateDTO));
        return taskDTO;
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO) {
        TaskDTO taskDTO = new TaskDTO();
        Task update = taskService.update(id, taskUpdateDTO);
        taskMapper.map(taskDTO, update);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(taskDTO.getName()));
        return taskDTO;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
