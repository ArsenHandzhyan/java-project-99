package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.TaskStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/task_statuses")
@CrossOrigin(exposedHeaders = "X-Total-Count")
@Tag(name = "Task Statuses", description = "Task statuses management endpoints")
public class TaskStatusController {

    private final TaskStatusService taskStatusService;
    private final TaskStatusMapper taskStatusMapper;


    public TaskStatusController(TaskStatusService taskStatusService, TaskStatusMapper taskStatusMapper) {
        this.taskStatusService = taskStatusService;
        this.taskStatusMapper = taskStatusMapper;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get taskStatus by ID", description = "Returns a taskStatus by its ID")
    public ResponseEntity<TaskStatusDTO> getTaskStatusById(@PathVariable Long id) {
        var taskStatus = taskStatusService.getTaskStatusById(id);
        return ResponseEntity.ok().body(taskStatusMapper.map(taskStatus));
    }

    @GetMapping
    @Operation(summary = "Get all taskStatuses", description = "Returns a list of all taskStatuses")
    public ResponseEntity<List<TaskStatusDTO>> getAllTaskStatuses() {
        List<TaskStatus> taskStatuses = taskStatusService.getAllTaskStatuses();
        List<TaskStatusDTO> taskStatusDTOs = taskStatuses.stream()
                .map(taskStatusMapper::map)
                .collect(Collectors.toList());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(taskStatuses.size()));
        return ResponseEntity.ok().headers(responseHeaders).body(taskStatusDTOs);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TaskStatusDTO> createTaskStatus(@RequestBody TaskStatusCreateDTO taskStatusDTO) {
        Optional<TaskStatus> taskStatusBySlug = taskStatusService.getBySlug(taskStatusDTO.getSlug());
        if (taskStatusBySlug.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        TaskStatus createdTaskStatus = taskStatusService.createTaskStatus(taskStatusDTO);
        TaskStatusDTO responseTaskStatusDTO = taskStatusMapper.map(createdTaskStatus);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(responseTaskStatusDTO.hashCode()));
        return ResponseEntity.status(HttpStatus.CREATED).headers(responseHeaders).body(responseTaskStatusDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<TaskStatusDTO> updateTaskStatus(@PathVariable Long id,
                                                          @RequestBody TaskStatusUpdateDTO taskStatusUpdateDTO) {
        Optional<TaskStatus> taskStatusBySlug = taskStatusService.getBySlug(taskStatusUpdateDTO.getSlug().get());
        if (taskStatusBySlug.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        TaskStatus taskStatus = taskStatusService.updateTaskStatus(id, taskStatusUpdateDTO);
        TaskStatusDTO taskStatusDTO = taskStatusMapper.map(taskStatus);
        return ResponseEntity.ok().body(taskStatusDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<String> deleteTaskStatus(@PathVariable Long id) {
        try {
            taskStatusService.deleteTaskStatus(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
