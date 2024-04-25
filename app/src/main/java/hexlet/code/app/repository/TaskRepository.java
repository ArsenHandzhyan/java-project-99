package hexlet.code.app.repository;

import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByLabelsContains(Label label);

    List<Task> findByTaskStatus(TaskStatus taskStatus);

    List<Task> findByAssignee(User assignee);
}
