package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusPresenceDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.model.TaskStatus;
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
public interface TaskStatusMapper {
    @Mapping(target = "name", source = "name", qualifiedByName = "mapPresence")
    @Mapping(target = "slug", source = "slug", qualifiedByName = "mapPresence")
    TaskStatusPresenceDTO map(TaskStatus taskStatus);

    TaskStatusPresenceDTO map(TaskStatusCreateDTO dto);

    TaskStatusPresenceDTO map(TaskStatusUpdateDTO dto);

    List<TaskStatusPresenceDTO> map(List<TaskStatus> taskStatuses);

    @Named("mapPresence")
    default TaskStatusPresenceDTO.Presence mapPresence(String value) {
        return new TaskStatusPresenceDTO.Presence(value != null);
    }
}