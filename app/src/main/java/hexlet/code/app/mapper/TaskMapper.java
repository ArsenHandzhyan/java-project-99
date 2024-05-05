package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        uses = {JsonNullableMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {
    @Mapping(target = "labelIds", source = "labels", qualifiedByName = "mapLabelsToIds")
    @Mapping(target = "assigneeId", source = "assignee")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus")
    public abstract TaskDTO map(Task task);

    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "mapIdsToLabels")
    public abstract Task map(TaskCreateDTO taskCreateDTO);

    @Mapping(target = "labels", source = "labelIds", qualifiedByName = "mapIdsToLabels")
    public abstract void update(TaskUpdateDTO taskUpdateDTO, @MappingTarget Task task);

    @Named("mapLabelsToIds")
    Set<Long> mapLabelsToIds(Set<Label> labels) {
        return labels.stream()
                .map(Label::getId)
                .collect(Collectors.toSet());
    }

    @Named("mapIdsToLabels")
    Set<Label> mapIdsToLabels(Set<Long> labelIds) {
        if (labelIds == null) {
            return Collections.emptySet();
        }
        return labelIds.stream()
                .map(id -> {
                    Label label = new Label();
                    label.setId(id);
                    return label;
                })
                .collect(Collectors.toSet());
    }

    public abstract List<TaskDTO> map(List<Task> tasks);
}
