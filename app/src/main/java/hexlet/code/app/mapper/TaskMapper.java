package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskPresenceDTO;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TaskMapper {
    @Mapping(target = "index", source = "index", qualifiedByName = "mapPresence")
    @Mapping(target = "name", source = "name", qualifiedByName = "mapPresence")
    @Mapping(target = "description", source = "description", qualifiedByName = "mapPresence")
    @Mapping(target = "taskStatus", source = "taskStatus", qualifiedByName = "mapPresence")
    @Mapping(target = "assigneeId", source = "assigneeId", qualifiedByName = "mapPresence")
    TaskPresenceDTO map(Task task);

    Task map(TaskCreateDTO taskCreateDTO);

    List<TaskDTO> map(List<Task> tasks);

    @Named("mapPresence")
    default TaskPresenceDTO.Presence mapPresence(String value) {
        return new TaskPresenceDTO.Presence(value != null);
    }
}
