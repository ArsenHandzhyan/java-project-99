package hexlet.code.app.dto;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class LabelDTO {
    private Integer id;
    private JsonNullable<String> name;
    private String createdAt;
    private String updateAt;
}
