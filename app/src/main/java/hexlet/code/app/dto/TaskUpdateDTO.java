package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, message = "Name must be at least 1 character long")
    private JsonNullable<String> title;

    private JsonNullable<Integer> index;

    private JsonNullable<Set<Long>> taskLabelIds;

    private JsonNullable<String> content;

    @NotBlank(message = "Task status is mandatory")
    private JsonNullable<String> status;

    private JsonNullable<Long> assignee_id;
}
