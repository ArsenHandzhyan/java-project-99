package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TaskUpdateDTO extends BasePage {
    private String name;
    private String description;
    private String taskStatus;
    private LocalDateTime updatedAt;
    private User assignee;

    public TaskUpdateDTO() {

    }

    /**
     * Gets the flash message.
     *
     * @return the flash message
     */
    @Override
    public String getFlash() {
        return super.getFlash();
    }

    /**
     * Sets the flash message.
     *
     * @param flash the flash message to set
     */
    @Override
    public void setFlash(String flash) {
        super.setFlash(flash);
    }
}
