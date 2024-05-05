package hexlet.code.app.dto;

import hexlet.code.app.model.Task;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;
    private JsonNullable<String> email;
    private JsonNullable<String> password;
    private JsonNullable<List<Task>> tasks;
    private String createdAt;
    private String updatedAt;
}
