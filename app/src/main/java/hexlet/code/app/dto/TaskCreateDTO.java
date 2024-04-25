package hexlet.code.app.dto;

import hexlet.code.app.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskCreateDTO extends BasePage {
    private String name;
    private Integer index;
    private String description;
    private String taskStatus;
    private LocalDateTime createdAt;
    private User assignee;

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
