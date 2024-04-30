package hexlet.code.app.mapper;

import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.dto.UserDTO;
import hexlet.code.app.dto.UserPresenceDTO;
import hexlet.code.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "email", source = "email", qualifiedByName = "mapPresence")
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "mapPresence")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "mapPresence")
    @Mapping(target = "password", source = "password", qualifiedByName = "mapPresence")
    @Mapping(target = "encryptedPassword", source = "password", qualifiedByName = "mapPresence") // Используем password для примера
    UserPresenceDTO map(User user);

    UserDTO mapUserToUserDTO(User user);


    List<UserPresenceDTO> map(List<User> users);

    User map(UserCreateDTO user);

    UserDTO mapToUserDTO(User user);

    @Named("mapPresence")
    default UserPresenceDTO.Presence mapPresence(String value) {
        return new UserPresenceDTO.Presence(value != null);
    }
}
