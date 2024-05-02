package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LabelCreateDTO {
    private String name;
    private LocalDateTime createdAt;
}
