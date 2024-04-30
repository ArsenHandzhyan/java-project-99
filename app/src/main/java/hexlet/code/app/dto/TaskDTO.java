package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskDTO {
    private Long id;
    private Integer index;
    private String name;
    private String description;
    private String taskStatus;
    private User assigneeId;
    private LocalDateTime createdAt;
}
