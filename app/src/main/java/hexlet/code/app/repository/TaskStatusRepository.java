package hexlet.code.app.repository;

import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TaskStatusRepository extends CrudRepository<TaskStatus, Long> {
    TaskStatus findBySlug(String slug);
    Set<TaskStatus> findByName(String name);
}
