package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LabelPresenceDTO extends UserDTO {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    @Getter
    @Setter
    public static class Presence {
        private boolean present;

        public Presence(boolean present) {
            this.present = present;
        }

    }
}
