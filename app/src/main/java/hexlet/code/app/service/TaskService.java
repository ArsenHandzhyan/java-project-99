package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.specification.TaskSpecification;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
    }

    public List<Task> findTasks(String titleCont, Long assigneeId, String status, Long labelId) {
        Specification<Task> spec = TaskSpecification.getTasksByFilter(titleCont, assigneeId, status, labelId);
        return taskRepository.findAll(spec);
    }

    @Transactional
    public Task create(TaskCreateDTO task) {
        Task createTask = taskMapper.map(task);
        createTask.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(createTask);
    }

    @Transactional
    public List<Task> findAll() {
        return (List<Task>) taskRepository.findAll();
    }

    @Transactional
    public Task getTaskByName(String name) {
        return taskRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for name " + name));
    }

    @Transactional
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for id " + id));
    }

    @Transactional
    public Task update(Long id, @Valid TaskUpdateDTO data) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setName(data.getName().get());
                    task.setIndex(data.getIndex().get());
                    task.setTaskStatus(data.getTaskStatus().get());
                    task.setDescription(data.getDescription().get());
                    task.setUpdatedAt(LocalDateTime.now());
                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for id" + id));
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for id " + id));

        if (!user.getTasks().isEmpty()) {
            throw new IllegalStateException("Cannot delete user with associated tasks");
        }
        userRepository.delete(user);
    }
}
