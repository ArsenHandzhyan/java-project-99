package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserUpdateDTO;
import hexlet.code.app.model.User;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {

    @Autowired
    private PasswordEncoder passwordEncoder;


    public abstract User map(UserCreateDTO dto);

    public abstract User update(UserUpdateDTO dto, @MappingTarget User model);

    public abstract UserDTO map(User model);

    @BeforeMapping
    public void encryptPassword(UserCreateDTO data) {
        var password = data.getPassword();
        String passwordString = password != null ? password.orElse(null) : null;
        String encodedPassword = passwordEncoder.encode(passwordString);
        data.setPassword(JsonNullable.of(encodedPassword));
    }

    // Пользовательский метод маппинга для преобразования JsonNullable<String> в String
    public String map(JsonNullable<String> value) {
        return value != null ? value.orElse(null) : null;
    }
}
