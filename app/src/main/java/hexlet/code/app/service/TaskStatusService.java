package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Transactional
    public TaskStatus createTaskStatus(TaskStatusCreateDTO taskStatusDTO) {
        var taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDTO.getName());
        taskStatus.setSlug(taskStatusDTO.getSlug());
        return taskStatusRepository.save(taskStatus);
    }

    @Transactional
    public TaskStatus getTaskStatusById(Long id) {
        return taskStatusRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<TaskStatus> getAllTaskStatuses() {
        return (List<TaskStatus>) taskStatusRepository.findAll();
    }
    @Transactional
    public Optional<TaskStatus> getTaskStatusByName(String name) {
        return taskStatusRepository.findByName(name);
    }

    public Set<TaskStatus> getBySlug(String slug) {
        return taskStatusRepository.findBySlug(slug);
    }

    @Transactional
    public TaskStatus updateTaskStatus(Long id, TaskStatusUpdateDTO taskStatusUpdateDTO) {
        return taskStatusRepository.findById(id)
                .map(updateTaskStatus -> {
                    updateTaskStatus.setName(String.valueOf(taskStatusUpdateDTO.getName()));
                    updateTaskStatus.setSlug(String.valueOf(taskStatusUpdateDTO.getSlug()));
                    updateTaskStatus.setUpdatedAt(LocalDateTime.now());
                    return taskStatusRepository.save(updateTaskStatus);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional
    public void deleteTaskStatus(Long taskStatusId) {
        taskStatusRepository.deleteById(taskStatusId);
    }
}
