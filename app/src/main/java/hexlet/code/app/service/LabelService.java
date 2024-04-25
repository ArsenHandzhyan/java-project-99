package hexlet.code.app.service;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import hexlet.code.app.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {
    private final LabelRepository labelRepository;
    private final TaskRepository taskRepository;

    public LabelService(LabelRepository labelRepository, TaskRepository taskRepository) {
        this.labelRepository = labelRepository;
        this.taskRepository = taskRepository;
    }


    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public Label createLabel(LabelCreateDTO labelCreateDTO) {
        Label label = new Label();
        label.setName(labelCreateDTO.getName());
        return labelRepository.save(label);
    }

    public Label updateLabel(Long id, String name) {
        Label label = labelRepository.findById(id).orElseThrow();
        label.setName(name);
        return labelRepository.save(label);
    }

    @Transactional
    public void deleteLabel(Long labelId) {
        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new EntityNotFoundException("Label not found"));

        if (!taskRepository.findByLabelsContains(label).isEmpty()) {
            throw new IllegalStateException("Cannot delete label with assigned tasks");
        }

        labelRepository.delete(label);
    }

    public Label getLabelById(Long id) {
        return labelRepository.findById(id).orElseThrow();
    }

    public Label getLabelByName(String name) {
        return labelRepository.findByName(name);
    }
}
