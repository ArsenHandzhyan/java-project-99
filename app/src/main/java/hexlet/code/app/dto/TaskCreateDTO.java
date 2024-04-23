package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskCreateDTO {
    private String name;
    private Integer index;
    private String description;
    private String taskStatus;
    private LocalDateTime createdAt;
    private User assignee;

    public TaskCreateDTO() {

    }
}
