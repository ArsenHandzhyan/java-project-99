package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.service.TaskStatusService;
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


import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/task_statuses")
@CrossOrigin(exposedHeaders = "X-Total-Count")
public class TaskStatusController {

    private final TaskStatusService taskStatusService;
    private final TaskStatusMapper taskStatusMapper;


    public TaskStatusController(TaskStatusService taskStatusService, TaskStatusMapper taskStatusMapper) {
        this.taskStatusService = taskStatusService;
        this.taskStatusMapper = taskStatusMapper;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<TaskStatusDTO> getTaskStatusById(@PathVariable Long id) {
        var taskStatus = taskStatusService.getTaskStatusById(id);
        return ResponseEntity.ok(taskStatusMapper.map(taskStatus));
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<TaskStatusDTO>> getAllTaskStatuses() {
        var taskStatuses = taskStatusService.getAllTaskStatuses();
        return ResponseEntity.ok(Collections.singletonList((TaskStatusDTO) taskStatusMapper.map(taskStatuses)));
    }
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<TaskStatusDTO> createTaskStatus(@RequestBody TaskStatusCreateDTO taskStatus) {
        var createdTaskStatus = taskStatusService.createTaskStatus(taskStatus);
        return ResponseEntity.ok(taskStatusMapper.map(createdTaskStatus));
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusDTO> updateTaskStatus(@PathVariable Long id,
                                                       @RequestBody TaskStatusUpdateDTO taskStatus) {
        var updatedTaskStatus = taskStatusService.updateTaskStatus(id, taskStatus);
        taskStatusMapper.update(taskStatus, updatedTaskStatus);
        return ResponseEntity.ok(taskStatusMapper.map(updatedTaskStatus));
    }
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
        return ResponseEntity.noContent().build();
    }
}
