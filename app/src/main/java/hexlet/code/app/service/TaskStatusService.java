package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
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
    private final TaskStatusMapper taskStatusMapper;

    public TaskStatusService(TaskStatusRepository taskStatusRepository, TaskStatusMapper taskStatusMapper) {
        this.taskStatusRepository = taskStatusRepository;
        this.taskStatusMapper = taskStatusMapper;
    }

    @Transactional
    public TaskStatus createTaskStatus(TaskStatusCreateDTO taskStatusDTO) {
        TaskStatus taskStatus = taskStatusMapper.map(taskStatusDTO);
        taskStatus.setCreatedAt(LocalDateTime.now());
        return taskStatusRepository.save(taskStatus);
    }

    @Transactional
    public TaskStatus getTaskStatusById(Long id) {
        return taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found for id " + id));
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
                .map(updateTask -> {
                    updateTask.setName(String.valueOf(taskStatusUpdateDTO.getName()));
                    updateTask.setSlug(String.valueOf(taskStatusUpdateDTO.getSlug()));
                    updateTask.setUpdatedAt(LocalDateTime.now());
                    return taskStatusRepository.save(updateTask);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found for id " + id));
    }

    @Transactional
    public void deleteTaskStatus(Long taskStatusId) {
        taskStatusRepository.deleteById(taskStatusId);
    }
}
