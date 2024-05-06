package hexlet.code.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class TaskCreateDTO {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, message = "Name must be at least 1 character long")
    private String title;

    private Integer index;

    private Set<Long> taskLabelIds;

    private String content;

    @NotBlank(message = "Task status is mandatory")
    private String status;

    private Long assignee_id;
}
