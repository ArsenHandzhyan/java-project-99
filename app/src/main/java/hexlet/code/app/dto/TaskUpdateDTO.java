package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Getter
@Setter
public class TaskUpdateDTO {
    private JsonNullable<String> name;
    private JsonNullable<Integer> index;
    private Set<Long> labelIds;
    private JsonNullable<String> description;
    private JsonNullable<String> taskStatus;
    private JsonNullable<Long> assignee;
}
