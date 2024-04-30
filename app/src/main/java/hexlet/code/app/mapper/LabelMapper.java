package hexlet.code.app.mapper;

import hexlet.code.app.dto.*;
import hexlet.code.app.model.Label;
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
public interface LabelMapper {
    @Mapping(target = "name", source = "name", qualifiedByName = "mapPresence")

    LabelPresenceDTO map(Label label);

    LabelDTO map(LabelCreateDTO dto);

    LabelDTO map(LabelUpdateDTO dto);

    List<LabelDTO> map(List<Label> allLabels);

    @Named("mapPresence")
    default LabelPresenceDTO.Presence mapPresence(String value) {
        return new LabelPresenceDTO.Presence(value != null);
    }
}
