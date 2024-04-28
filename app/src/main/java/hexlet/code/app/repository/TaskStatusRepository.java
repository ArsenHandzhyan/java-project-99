package hexlet.code.app.repository;

import hexlet.code.app.model.TaskStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TaskStatusRepository extends CrudRepository<TaskStatus, Long> {
    Set<TaskStatus> findBySlug(String slug);
    Optional<TaskStatus> findByName(String name);
}
