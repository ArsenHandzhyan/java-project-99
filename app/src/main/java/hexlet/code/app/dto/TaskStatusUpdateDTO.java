package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TaskStatusUpdateDTO extends BasePage {
    private JsonNullable<String> name;
    private JsonNullable<String> slug;
    private JsonNullable<LocalDateTime> updatedAt;

    public TaskStatusUpdateDTO(String name, String slug) {
        super();
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
