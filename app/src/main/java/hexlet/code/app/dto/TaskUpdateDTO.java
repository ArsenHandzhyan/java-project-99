package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskUpdateDTO {
    private String name;
    private Integer index;
    private String description;
    private String taskStatus;
    private LocalDateTime updatedAt;
    private User assignee;
}
