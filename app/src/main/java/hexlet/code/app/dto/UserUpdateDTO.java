package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserUpdateDTO extends BasePage {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

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
