package hexlet.code.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskStatusUpdateDTO {
    @NotNull
    private JsonNullable<String> name;
    @NotNull
    private JsonNullable<String> slug;
    private JsonNullable<LocalDateTime> updatedAt;
}
