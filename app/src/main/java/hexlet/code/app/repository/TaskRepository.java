package hexlet.code.app.repository;

import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByLabelsContains(Label label);
    List<Task> findByAssignee(Long assignee);
    Optional<Task> findByName(String name);
    List<Task> findAll(Specification<Task> spec);
    boolean existsByTaskStatus(@NotBlank(message = "Task status is mandatory") String taskStatus);
}
