package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.service.TaskStatusService;
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
@RequestMapping("/api/task_statuses")
public class TaskStatusController {

    private final TaskStatusService taskStatusService;
    private final TaskStatusMapper taskStatusMapper;


    public TaskStatusController(TaskStatusService taskStatusService, TaskStatusMapper taskStatusMapper) {
        this.taskStatusService = taskStatusService;
        this.taskStatusMapper = taskStatusMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskStatus> getTaskStatusById(@PathVariable Long id) {
        TaskStatus taskStatus = taskStatusService.getTaskStatusById(id);
        return ResponseEntity.ok(taskStatus);
    }

    @GetMapping
    public ResponseEntity<List<TaskStatus>> getAllTaskStatuses() {
        List<TaskStatus> taskStatuses = taskStatusService.getAllTaskStatuses();
        return ResponseEntity.ok(taskStatuses);
    }

    @PostMapping
    public ResponseEntity<TaskStatus> createTaskStatus(@RequestBody TaskStatusCreateDTO taskStatus) {
        TaskStatus createdTaskStatus = taskStatusService.createTaskStatus(taskStatusMapper.map(taskStatus));
        return ResponseEntity.ok(createdTaskStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskStatus> updateTaskStatus(@PathVariable Long id,
                                                       @RequestBody TaskStatusUpdateDTO taskStatus) {
        TaskStatus updatedTaskStatus = taskStatusService.updateTaskStatus(id, taskStatusMapper.map(taskStatus));
        taskStatusMapper.update(taskStatus, updatedTaskStatus);
        return ResponseEntity.ok(updatedTaskStatus);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaskStatus(@PathVariable Long id) {
        taskStatusService.deleteTaskStatus(id);
        return ResponseEntity.noContent().build();
    }
}
