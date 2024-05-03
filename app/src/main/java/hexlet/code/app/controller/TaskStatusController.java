package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.TaskStatusService;
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

    @GetMapping
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
    public ResponseEntity<TaskStatusDTO> createTaskStatus(@RequestBody TaskStatusCreateDTO taskStatusDTO) {
        Optional<TaskStatus> taskStatusByName = taskStatusService.getTaskStatusByName(taskStatusDTO.getName());
        if (taskStatusByName.isPresent())
            if (taskStatusByName.get().getTasks().isEmpty()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        TaskStatus createdTaskStatus = taskStatusService.createTaskStatus(taskStatusDTO);
        TaskStatusDTO responseTaskStatusDTO = taskStatusMapper.map(createdTaskStatus);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseTaskStatusDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<TaskStatusDTO> updateTaskStatus(@PathVariable Long id,
                                                          @RequestBody TaskStatusUpdateDTO taskStatus) {
        TaskStatus taskStatus1 = taskStatusService.updateTaskStatus(id, taskStatus);
        TaskStatusDTO taskStatusDTO = taskStatusMapper.map(taskStatus1);
        return ResponseEntity.ok(taskStatusDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
        return ResponseEntity.noContent().build();
    }
}
