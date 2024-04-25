package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public Task create(TaskCreateDTO task) {
        Task taskEntity = taskMapper.toEntity(task);
        taskEntity.setName(task.getName());
        taskEntity.setIndex(task.getIndex());
        taskEntity.setDescription(task.getDescription());
        taskEntity.setTaskStatus(task.getTaskStatus());
        taskEntity.setAssignee(task.getAssignee());
        taskEntity.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(taskEntity);
    }

    public List<Task> findAll() {
        return (List<Task>) taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    public Task update(Long id, TaskUpdateDTO task) {
        return taskRepository.findById(id)
                .map(existingTask -> {
                    existingTask.setName(task.getName());
                    existingTask.setIndex(task.getIndex());
                    existingTask.setDescription(task.getDescription());
                    existingTask.setTaskStatus(task.getTaskStatus());
                    existingTask.setAssignee(task.getAssignee());
                    existingTask.setCreatedAt(LocalDateTime.now());
                    return taskRepository.save(existingTask);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
