package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskStatusUpdateDTO {

    private String name;
    private String slug;
}
