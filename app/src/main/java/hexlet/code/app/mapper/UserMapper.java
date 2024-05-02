package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserPresenceDTO;
import hexlet.code.app.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    @Mapping(target = "email", source = "email", qualifiedByName = "mapPresence")
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "mapPresence")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "mapPresence")
    @Mapping(target = "password", source = "password", qualifiedByName = "mapPresence")
    @Mapping(target = "encryptedPassword", source = "password", qualifiedByName = "mapPresence")
    public abstract UserPresenceDTO map(User user);

    public abstract List<UserPresenceDTO> map(List<User> users);

    abstract User map(UserCreateDTO user);

    @Named("mapPresence")
    UserPresenceDTO.Presence mapPresence(String value) {
        return new UserPresenceDTO.Presence(value != null);
    }
}
