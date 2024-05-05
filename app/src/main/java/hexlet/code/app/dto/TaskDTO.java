package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.Set;

@Data
@AllArgsConstructor
public class TaskDTO {
    private Integer id;
    private JsonNullable<Set<Long>> labelIds;
    private JsonNullable<Integer> index;
    private String createdAt;
    private JsonNullable<Long> assigneeId;
    private JsonNullable<String> title;
    private JsonNullable<String> content;
    private JsonNullable<String> status;
    private String updateAt;
}
