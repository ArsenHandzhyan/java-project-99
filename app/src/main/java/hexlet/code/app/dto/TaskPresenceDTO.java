package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskPresenceDTO {
    private Long id;
    private Integer index;
    private String name;
    private String description;
    private String taskStatus;
    private User assigneeId;
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
