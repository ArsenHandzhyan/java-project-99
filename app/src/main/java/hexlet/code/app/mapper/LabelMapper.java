package hexlet.code.app.mapper;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelPresenceDTO;
import hexlet.code.app.dto.LabelUpdateDTO;
import hexlet.code.app.dto.UserPresenceDTO;
import hexlet.code.app.model.Label;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class LabelMapper {
    @Mapping(target = "name", source = "name", qualifiedByName = "mapPresence")
    public abstract LabelPresenceDTO map(Label label);

    abstract LabelPresenceDTO map(LabelCreateDTO dto);

    abstract LabelPresenceDTO map(LabelUpdateDTO dto);

    public abstract List<LabelPresenceDTO> map(List<Label> dto);

    @Named("mapPresence")
    UserPresenceDTO.Presence mapPresence(String value) {
        return new UserPresenceDTO.Presence(value != null);
    }}
