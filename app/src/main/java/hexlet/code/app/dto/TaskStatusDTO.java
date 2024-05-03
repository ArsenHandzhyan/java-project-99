package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskStatusDTO {
    private Long id;
    private String name;
    private String slug;
    private LocalDateTime createdAt;
}
