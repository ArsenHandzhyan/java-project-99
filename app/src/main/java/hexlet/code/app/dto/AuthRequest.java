package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthRequest extends BasePage {
    private String username;
    private String password;
}
