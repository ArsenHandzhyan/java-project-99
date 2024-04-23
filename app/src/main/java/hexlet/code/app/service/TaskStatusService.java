package hexlet.code.app.service;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    public TaskStatusService(TaskStatusRepository taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    public TaskStatus createTaskStatus(TaskStatus taskStatus) {
        return taskStatusRepository.save(taskStatus);
    }

    public TaskStatus getTaskStatusById(Long id) {
        return taskStatusRepository.findById(id).orElse(null);
    }

    public List<TaskStatus> getAllTaskStatuses() {
        return taskStatusRepository.findAll();
    }

    public TaskStatus updateTaskStatus(Long id, TaskStatus taskStatus) {
        TaskStatus existingTaskStatus = getTaskStatusById(id);
        if (existingTaskStatus != null) {
            existingTaskStatus.setName(taskStatus.getName());
            existingTaskStatus.setSlug(taskStatus.getSlug());
            return taskStatusRepository.save(existingTaskStatus);
        }
        return null;
    }

    public void deleteTaskStatus(Long id) {
        taskStatusRepository.deleteById(id);
    }
}
