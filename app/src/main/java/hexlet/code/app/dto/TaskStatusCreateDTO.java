package hexlet.code.app.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor(onConstructor = @__(@JsonCreator))
public class TaskStatusCreateDTO extends BasePage {
    private String name;
    private String slug;
    private LocalDateTime createdAt;

    @JsonCreator
    public TaskStatusCreateDTO(String name, String slug) {
        this.name = name;
        this.slug = slug;
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
