package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.openapitools.jackson.nullable.JsonNullable;

@Data
@AllArgsConstructor
public class UserDTO {
    private Integer id;
    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;
    private JsonNullable<String> email;
    private JsonNullable<String> password;
    private JsonNullable<String> encryptedPassword;
    private String createdAt;
}
