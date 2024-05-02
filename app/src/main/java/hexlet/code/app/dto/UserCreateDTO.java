package hexlet.code.app.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserCreateDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime createdAt;
}
