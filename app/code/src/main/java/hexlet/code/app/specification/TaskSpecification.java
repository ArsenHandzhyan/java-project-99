package hexlet.code.app.specification;

import hexlet.code.app.model.Task;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> getTasksByFilter(String titleCont, Long assigneeId, String status, Long labelId) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(titleCont)) {
                predicates.add(criteriaBuilder.like(root.get("title"), "%" + titleCont + "%"));
            }
            if (assigneeId != null) {
                predicates.add(criteriaBuilder.equal(root.get("assignee").get("id"), assigneeId));
            }
            if (StringUtils.hasText(status)) {
                predicates.add(criteriaBuilder.equal(root.get("status").get("slug"), status));
            }
            if (labelId != null) {
                predicates.add(criteriaBuilder.isMember(labelId, root.get("labels")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
