package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private final TaskRepository taskRepository;

    public Task createTask(TaskCreateDTO taskDTO) {
        Task task = new Task();
        task.setName(taskDTO.getName());
        task.setIndex(taskDTO.getIndex());
        task.setDescription(taskDTO.getDescription());
        task.setTaskStatus(taskDTO.getTaskStatus());
        task.setAssignee(taskDTO.getAssignee());
        task.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, TaskUpdateDTO taskDTO) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setName(taskDTO.getName());
                    task.setDescription(taskDTO.getDescription());
                    task.setTaskStatus(taskDTO.getTaskStatus());
                    task.setAssignee(taskDTO.getAssignee());
                    return taskRepository.save(task);
                })
                .orElse(null);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
