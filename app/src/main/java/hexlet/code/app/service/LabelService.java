package hexlet.code.app.service;

import hexlet.code.app.model.Label;
import hexlet.code.app.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public Label createLabel(String name) {
        Label label = new Label();
        label.setName(name);
        label.setCreatedAt(LocalDateTime.now());
        return labelRepository.save(label);
    }

    public Label updateLabel(Long id, String name) {
        Label label = labelRepository.findById(id).orElseThrow();
        label.setName(name);
        return labelRepository.save(label);
    }

    public void deleteLabel(Long id) {
        Label label = labelRepository.findById(id).orElseThrow();
        labelRepository.delete(label);
    }

    public Label getLabelById(Long id) {
        return labelRepository.findById(id).orElseThrow();
    }

    public Label getLabelByName(String name) {
        return labelRepository.findByName(name);
    }
}
