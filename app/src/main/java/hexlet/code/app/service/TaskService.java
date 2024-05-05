package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.UserRepository;
import hexlet.code.app.specification.TaskSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;
    private final LabelService labelService;

    @Autowired
    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       TaskMapper taskMapper,
                       LabelService labelService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.taskMapper = taskMapper;
        this.labelService = labelService;
    }

    public List<Task> findTasks(String titleCont, Long assigneeId, String status, Long labelId) {
        Specification<Task> spec = TaskSpecification.getTasksByFilter(titleCont, assigneeId, status, labelId);
        return taskRepository.findAll(spec);
    }

    public Task create(TaskCreateDTO taskCreateDTO) {
        Task task = taskMapper.map(taskCreateDTO);

        if (taskCreateDTO.getLabelIds() != null) {
            Set<Label> labels = taskCreateDTO.getLabelIds().stream()
                    .map(labelService::getLabelById)
                    .collect(Collectors.toSet());
            task.setLabels(labels);
        }

        return taskRepository.save(task);
    }

    @Transactional
    public Task update(Long id, TaskUpdateDTO taskUpdateDTO) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for id " + id));

        taskMapper.update(taskUpdateDTO, task);

        if (taskUpdateDTO.getLabelIds() != null) {
            Set<Label> labels = taskUpdateDTO.getLabelIds().stream()
                    .map(labelService::getLabelById)
                    .collect(Collectors.toSet());
            task.setLabels(labels);
        }

        return taskRepository.save(task);
    }

    @Transactional
    public void getTaskByName(String name) {
        taskRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for name " + name));
    }

    @Transactional
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found for id " + id));
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
