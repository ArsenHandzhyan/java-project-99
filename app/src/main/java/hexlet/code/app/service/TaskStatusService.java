package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

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
        return taskStatusRepository.save(taskStatus);
    }

    public TaskStatus getTaskStatusById(Long id) {
        return taskStatusRepository.findById(id).orElse(null);
    }

    public List<TaskStatus> getAllTaskStatuses() {
        return (List<TaskStatus>) taskStatusRepository.findAll();
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
        taskStatusRepository.deleteById(taskStatusId);
    }
}
