package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
@AllArgsConstructor
public class TaskStatusDTO {
    private Integer id;
    private JsonNullable<String> name;
    private JsonNullable<String> slug;
    private String createdAt;
}
