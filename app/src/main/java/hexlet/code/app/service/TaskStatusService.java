package hexlet.code.app.service;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskStatusMapper;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;
    private final TaskRepository taskRepository;
    private final TaskStatusMapper taskStatusMapper;

    public TaskStatusService(TaskStatusRepository taskStatusRepository,
                             TaskRepository taskRepository,
                             TaskStatusMapper taskStatusMapper) {
        this.taskStatusRepository = taskStatusRepository;
        this.taskRepository = taskRepository;
        this.taskStatusMapper = taskStatusMapper;
    }

    @Transactional
    public TaskStatus createTaskStatus(TaskStatusCreateDTO taskStatusDTO) {
        TaskStatus taskStatus = taskStatusMapper.map(taskStatusDTO);
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

    public Optional<TaskStatus> getBySlug(String slug) {
        return taskStatusRepository.findBySlug(slug);
    }

    @Transactional
    public TaskStatus updateTaskStatus(Long id, TaskStatusUpdateDTO taskStatusUpdateDTO) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found for id " + id));
        taskStatusMapper.update(taskStatusUpdateDTO, taskStatus);
        return taskStatusRepository.save(taskStatus);
    }

    @Transactional
    public void deleteTaskStatus(Long id) {
        TaskStatus taskStatus = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status not found for id " + id));

        if (taskRepository.existsByTaskStatus(String.valueOf(taskStatus))) {
            throw new IllegalStateException("Cannot delete task status with associated tasks");
        }
        taskStatusRepository.delete(taskStatus);
    }
}
