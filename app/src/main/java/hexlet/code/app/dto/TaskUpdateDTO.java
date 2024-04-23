package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskUpdateDTO {
    private Long id;
    private String name;
    private String description;
    private String taskStatus;
    private LocalDateTime createdAt;
    private User assignee;

    public TaskUpdateDTO() {
        this.createdAt = null;
        this.assignee = null;
        this.taskStatus = null;
        this.description = null;
        this.name = null;
        this.id = null;
    }
}
