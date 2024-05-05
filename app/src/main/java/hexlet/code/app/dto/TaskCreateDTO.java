package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    private String name;
    private Integer index;
    private Set<Long> labelIds;
    private String description;
    private String taskStatus;
    private Long assignee;
}
