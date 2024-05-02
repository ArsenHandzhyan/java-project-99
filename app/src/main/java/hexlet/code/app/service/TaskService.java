package hexlet.code.app.service;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.specification.TaskSpecification;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<Task> findTasks(String titleCont, Long assigneeId, String status, Long labelId) {
        Specification<Task> spec = TaskSpecification.getTasksByFilter(titleCont, assigneeId, status, labelId);
        return taskRepository.findAll(spec);
    }

    @Transactional
    public Task create(TaskCreateDTO task) {
        Task createTask =  taskMapper.map(task);
        createTask.setName(task.getName());
        createTask.setIndex(task.getIndex());
        createTask.setDescription(task.getDescription());
        createTask.setTaskStatus(task.getTaskStatus());
        createTask.setAssignee(task.getAssignee());
        createTask.setCreatedAt(LocalDateTime.now());
        return taskRepository.save(createTask);
    }

    @Transactional
    public List<Task> findAll() {
        return (List<Task>) taskRepository.findAll();
    }

    @Transactional
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Task update(Long id, TaskUpdateDTO task) {
        return taskRepository.findById(id)
                .map(updateTask -> {
                    updateTask.setName(task.getName());
                    updateTask.setIndex(task.getIndex());
                    updateTask.setDescription(task.getDescription());
                    updateTask.setTaskStatus(task.getTaskStatus());
                    updateTask.setAssignee(task.getAssignee());
                    updateTask.setUpdatedAt(LocalDateTime.now());
                    return taskRepository.save(updateTask);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public boolean getTaskByName(String initialTask) {
        return taskRepository.findByName(initialTask).isPresent();
    }
}
