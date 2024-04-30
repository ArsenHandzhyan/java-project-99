package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskStatusUpdateDTO {
    private JsonNullable<String> name;
    private JsonNullable<String> slug;
    private JsonNullable<LocalDateTime> updatedAt;
}
