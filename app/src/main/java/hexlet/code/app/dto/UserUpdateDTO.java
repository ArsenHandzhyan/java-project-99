package hexlet.code.app.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserUpdateDTO {
    private JsonNullable<String> firstName;
    private JsonNullable<String> lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private LocalDateTime updateAt;
}
