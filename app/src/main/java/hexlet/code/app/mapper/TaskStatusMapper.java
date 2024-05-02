package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusPresenceDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.dto.UserPresenceDTO;
import hexlet.code.app.model.TaskStatus;
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
public abstract class TaskStatusMapper {
    @Mapping(target = "name", source = "name", qualifiedByName = "mapPresence")
    @Mapping(target = "slug", source = "slug", qualifiedByName = "mapPresence")
    public abstract TaskStatusPresenceDTO map(TaskStatus taskStatus);

    abstract TaskStatusPresenceDTO map(TaskStatusCreateDTO dto);

    abstract TaskStatusPresenceDTO map(TaskStatusUpdateDTO dto);

    abstract List<TaskStatusPresenceDTO> map(List<TaskStatus> taskStatuses);

    @Named("mapPresence")
    UserPresenceDTO.Presence mapPresence(String value) {
        return new UserPresenceDTO.Presence(value != null);
    }
}