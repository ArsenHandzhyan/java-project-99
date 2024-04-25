package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskRepository taskRepository;

    public TaskStatusService(TaskStatusRepository taskStatusRepository, TaskRepository taskRepository) {
        this.taskStatusRepository = taskStatusRepository;
        this.taskRepository = taskRepository;
    }

    public TaskStatus createTaskStatus(TaskStatusCreateDTO taskStatusDTO) {
        var taskStatus = new TaskStatus();
        taskStatus.setName(taskStatusDTO.getName());
        taskStatus.setSlug(taskStatusDTO.getSlug());
        return taskStatusRepository.save(taskStatus);
    }

    public TaskStatus getTaskStatusById(Long id) {
        return taskStatusRepository.findById(id).orElse(null);
    }

    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    public TaskStatus updateTaskStatus(Long id, TaskStatusUpdateDTO taskStatus) {
        TaskStatus existingTaskStatus = getTaskStatusById(id);
        if (existingTaskStatus != null) {
            existingTaskStatus.setName(taskStatus.getName());
            existingTaskStatus.setSlug(taskStatus.getSlug());
            return taskStatusRepository.save(existingTaskStatus);
        }
        return null;
    }

    @Transactional
    public void deleteTaskStatus(Long taskStatusId) {
        if (!taskStatusRepository.existsById(taskStatusId)) {
            throw new ResourceNotFoundException("TaskStatus not found");
        }

        TaskStatus taskStatus = taskStatusRepository.findById(taskStatusId)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus not found"));

        if (!taskRepository.findByTaskStatus(taskStatus).isEmpty()) {
            throw new IllegalStateException("Cannot delete task status with assigned tasks");
        }

        taskStatusRepository.delete(taskStatus);
    }
}
