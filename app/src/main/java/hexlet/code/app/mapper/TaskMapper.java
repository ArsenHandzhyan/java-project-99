package hexlet.code.app.mapper;

import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        // Подключение JsonNullableMapper
        uses = { JsonNullableMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
@Component
public abstract class TaskMapper {
    public abstract void map(TaskCreateDTO dto, @MappingTarget Task model);
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task model);
    public abstract void map(TaskDTO dto, @MappingTarget Task model);
    public abstract void map(Task dto, @MappingTarget TaskDTO model);
    public abstract void map(Task dto, @MappingTarget TaskCreateDTO model);

    // Добавьте реализацию метода taskDTOToTask, если он отсутствует
    protected Task taskDTOToTask(TaskDTO taskDTO) {
        if (taskDTO == null) {
            return null;
        }

        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setName(taskDTO.getName());
        task.setIndex(taskDTO.getIndex());
        task.setDescription(taskDTO.getDescription());
        task.setTaskStatus(taskDTO.getTaskStatus());
        task.setCreatedAt(taskDTO.getCreatedAt());

        return task;
    }

    // Переписанный метод map для списка
    public void map(List<TaskDTO> dtoList, List<Task> modelList) {
        if (dtoList == null) {
            return;
        }

        // Создаем новый список для преобразованных элементов
        List<Task> newModelList = new ArrayList<>();
        for (TaskDTO taskDTO : dtoList) {
            newModelList.add(taskDTOToTask(taskDTO));
        }

        // Заменяем старый список на новый
        modelList.clear();
        modelList.addAll(newModelList);
    }
}
