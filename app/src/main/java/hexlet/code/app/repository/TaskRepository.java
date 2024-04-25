package hexlet.code.app.repository;

import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import hexlet.code.app.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByLabelsContains(Label label);
    List<Task> findByAssignee(User assignee);
}
