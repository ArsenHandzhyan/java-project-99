package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPresenceDTO extends UserDTO {
    private Long id;
    private Presence email;
    private Presence firstName;
    private Presence lastName;
    private Presence password;
    private Presence encryptedPassword;
    private String createdAt;

    @Getter
    @Setter
    public static class Presence {
        private boolean present;

        public Presence(boolean present) {
            this.present = present;
        }

    }
}
