package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskStatusCreateDTO {
    private String name;
    private String slug;
}
