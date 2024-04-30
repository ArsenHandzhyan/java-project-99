package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskCreateDTO {
    private String name;
    private Integer index;
    private String description;
    private String taskStatus;
    private LocalDateTime createdAt;
    private User assignee;
}
