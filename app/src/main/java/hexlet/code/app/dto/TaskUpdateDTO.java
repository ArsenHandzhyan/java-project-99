package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskUpdateDTO {
    @NotNull
    private JsonNullable<String> name;
    private JsonNullable<Integer> index;
    private JsonNullable<String> description;
    private JsonNullable<String> taskStatus;
    private JsonNullable<User> assignee;
    private LocalDateTime updatedAt;
}
