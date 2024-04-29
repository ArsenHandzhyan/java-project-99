package hexlet.code.app.controller;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<TaskDTO>> getAllTasks(
            @RequestParam(required = false) String titleCont,
            @RequestParam(required = false) Long assigneeId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long labelId) {
        List<Task> tasks = taskService.findTasks(titleCont, assigneeId, status, labelId);
        List<TaskDTO> taskDTOs = tasks.stream().map(taskMapper::map).collect(Collectors.toList());
        return ResponseEntity.ok().body(taskDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO taskDTO = taskMapper.map(taskService.findById(id));
        return ResponseEntity.ok().body(taskDTO);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskCreateDTO taskCreateDTO) {
        TaskDTO taskDTO = taskMapper.map(taskService.create(taskCreateDTO));
        return ResponseEntity.ok().body(taskDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskUpdateDTO taskUpdateDTO) {
        Task update = taskService.update(id, taskUpdateDTO);
        TaskDTO taskDTO = taskMapper.map(update);
        return ResponseEntity.ok().body(taskDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
