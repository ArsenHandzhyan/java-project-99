package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private Integer index;
    private String name;
    private String description;
    private String taskStatus;
    private User assignee;
    private LocalDateTime createdAt;
}
