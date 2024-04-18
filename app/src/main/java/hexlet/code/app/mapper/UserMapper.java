package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    public abstract User map(UserCreateDTO dto);
    public abstract User map(UserUpdateDTO dto);
    public abstract UserDTO map(User model);

    // Добавленный метод преобразования
    public String map(JsonNullable<String> value) {
        return value != null ? value.orElse(null) : null;
    }
}
