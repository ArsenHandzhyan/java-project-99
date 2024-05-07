package hexlet.code.app.service;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;
    private final TaskRepository taskRepository;
    private final LabelMapper labelMapper;

    public LabelService(LabelRepository labelRepository,
                        TaskRepository taskRepository,
                        LabelMapper labelMapper) {
        this.labelRepository = labelRepository;
        this.taskRepository = taskRepository;
        this.labelMapper = labelMapper;
    }

    @Transactional
    public List<Label> getAllLabels() {
        return (List<Label>) labelRepository.findAll();
    }

    @Transactional
    public Label createLabel(LabelCreateDTO labelCreateDTO) {
        Label label = labelMapper.map(labelCreateDTO);
        return labelRepository.save(label);
    }

    @Transactional
    public Label updateLabel(Long id, LabelUpdateDTO labelUpdateDTO) {
        var label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found for id " + id));
        labelMapper.update(labelUpdateDTO, label);
        return labelRepository.save(label);
    }

    @Transactional
    public void deleteLabel(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found for id " + id));

        if (!taskRepository.findByLabelsContains(label).isEmpty()) {
            throw new IllegalStateException("Cannot delete label with assigned tasks");
        }

        labelRepository.delete(label);
    }

    @Transactional
    public Label getLabelById(Long id) {
        return labelRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Label getLabelByName(String name) {
        return labelRepository.findByName(name);
    }
}
