package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(exposedHeaders = "X-Total-Count")
@Tag(name = "Tasks", description = "Tasks management endpoints")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get all tasks", description = "Returns a list of all tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @RequestParam(required = false) String titleCont,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long labelId) {
        List<Task> tasks = taskService.findTasks(titleCont, assigneeId, status, labelId);
        List<TaskDTO> taskDTOs = tasks.stream().map(taskMapper::map).collect(Collectors.toList());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(tasks.size()));
        return ResponseEntity.ok().headers(responseHeaders).body(taskDTOs);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    @Operation(summary = "Get task by ID", description = "Returns a task by its ID")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO taskDTO = taskMapper.map(taskService.findById(id));
        return ResponseEntity.ok().body(taskDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TaskDTO> createTask(@RequestBody  @Valid TaskCreateDTO taskCreateDTO) {
        Task createdTask = taskService.create(taskCreateDTO);
        TaskDTO taskDTO = taskMapper.map(createdTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(taskDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO) {
        Task updatedTask = taskService.update(id, taskUpdateDTO);
        TaskDTO taskDTO = taskMapper.map(updatedTask);
        return ResponseEntity.ok(taskDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        try {
            taskService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
