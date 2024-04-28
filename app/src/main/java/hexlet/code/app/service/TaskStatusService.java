package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    @Transactional
    public TaskStatus createTaskStatus(TaskStatusCreateDTO taskStatusDTO) {
        // Check if a TaskStatus with the same slug already exists
        TaskStatus existingTaskStatus = taskStatusRepository.findBySlug(taskStatusDTO.getSlug());
        if (existingTaskStatus != null) {
            // Handle the case where a TaskStatus with the same slug already exists
            throw new IllegalArgumentException("A TaskStatus with the slug "
                    + taskStatusDTO.getSlug() + " already exists.");
        }
        var taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDTO.getName());
        taskStatus.setSlug(taskStatusDTO.getSlug());
        taskStatus.setUpdatedAt(LocalDateTime.now());
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
    public Set<TaskStatus> getTaskStatusByName(String name) {
        return taskStatusRepository.findByName(name);
    }

    @Transactional
    public TaskStatus updateTaskStatus(Long id, TaskStatusUpdateDTO taskStatus) {
        TaskStatus existingTaskStatus = getTaskStatusById(id);
        if (existingTaskStatus != null) {
            existingTaskStatus.setName(String.valueOf(taskStatus.getName()));
            existingTaskStatus.setSlug(String.valueOf(taskStatus.getSlug()));
            return taskStatusRepository.save(existingTaskStatus);
        }
        return null;
    }

    @Transactional
    public void deleteTaskStatus(Long taskStatusId) {
        taskStatusRepository.deleteById(taskStatusId);
    }
}
