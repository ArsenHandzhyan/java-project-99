package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskPresenceDTO;
import hexlet.code.app.dto.UserPresenceDTO;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Mapping(target = "index", source = "index", qualifiedByName = "mapPresence")
    @Mapping(target = "name", source = "name", qualifiedByName = "mapPresence")
    @Mapping(target = "description", source = "description", qualifiedByName = "mapPresence")
    @Mapping(target = "taskStatus", source = "taskStatus", qualifiedByName = "mapPresence")
    @Mapping(target = "assignee", source = "assignee", qualifiedByName = "mapPresence")
    public abstract TaskPresenceDTO map(Task task);

    abstract Task map(TaskCreateDTO taskCreateDTO);

    abstract List<TaskPresenceDTO> map(List<Task> tasks);

    @Named("mapPresence")
    UserPresenceDTO.Presence mapPresence(String value) {
        return new UserPresenceDTO.Presence(value != null);
    }
}
