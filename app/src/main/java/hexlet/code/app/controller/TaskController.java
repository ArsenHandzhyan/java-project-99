package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        var tasks = taskService.getAllTasks();
        List<TaskDTO> taskDTOs = taskMapper.map(tasks);

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(taskDTOs.size()));

        return ResponseEntity.ok().headers(headers).body(taskDTOs);
    }

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskMapper.map(taskService.getTaskById(id));
    }

    @PostMapping
    public TaskDTO createTask(@RequestBody TaskCreateDTO taskDTO) {
        return taskMapper.map(taskService.createTask(taskDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskDTO) {
        Task updatedTask = taskService.updateTask(id, taskDTO);
        return ResponseEntity.ok(taskMapper.map(updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
