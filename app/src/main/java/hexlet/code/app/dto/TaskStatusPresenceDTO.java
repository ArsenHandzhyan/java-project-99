package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusPresenceDTO {
    private Long id;
    private Presence name;
    private Presence slug;
    private String createdAt;
    private String updatedAt;

    @Getter
    @Setter
    public static class Presence {
        private boolean present;

        public Presence(boolean present) {
            this.present = present;
        }
    }
}