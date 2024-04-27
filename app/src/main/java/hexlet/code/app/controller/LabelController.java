package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.dto.LabelUpdateDTO;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.service.LabelService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@CrossOrigin(exposedHeaders = "X-Total-Count")
public class LabelController {
    private final LabelService labelService;

    private final LabelMapper labelMapper;

    public LabelController(LabelService labelService, LabelMapper labelMapper) {
        this.labelService = labelService;
        this.labelMapper = labelMapper;
    }

    @GetMapping("/{id}")
    public LabelDTO getLabelById(@PathVariable Long id) {
        return labelMapper.map(labelService.getLabelById(id));
    }

    @GetMapping
    public List<LabelDTO> getAllLabels() {
        return labelMapper.map(labelService.getAllLabels());
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public LabelDTO createLabel(@RequestBody LabelCreateDTO label) {
        return labelMapper.map(labelService.createLabel(label));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public LabelDTO updateLabel(@PathVariable Long id, @RequestBody LabelUpdateDTO label) {
        return labelMapper.map(labelService.updateLabel(id, label.getName()));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}
