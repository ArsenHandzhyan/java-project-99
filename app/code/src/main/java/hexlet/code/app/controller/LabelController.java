package hexlet.code.app.controller;

import hexlet.code.app.dto.LabelDTO;
import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.LabelUpdateDTO;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.model.Label;
import hexlet.code.app.service.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
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
@Tag(name = "Labels", description = "Labels management endpoints")
public class LabelController {
    private final LabelService labelService;

    private final LabelMapper labelMapper;

    public LabelController(LabelService labelService, LabelMapper labelMapper) {
        this.labelService = labelService;
        this.labelMapper = labelMapper;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get label by ID", description = "Returns a label by its ID")
    public LabelDTO getLabelById(@PathVariable Long id) {
        return labelMapper.map(labelService.getLabelById(id));
    }

    @GetMapping
    @Operation(summary = "Get all labels", description = "Returns a list of all labels")
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        List<Label> labels = labelService.getAllLabels();
        List<LabelDTO> labelDTO = labelMapper.map(labels);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Total-Count", String.valueOf(labels.size()));
        return ResponseEntity.ok().headers(responseHeaders).body(labelDTO);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    @SecurityRequirement(name = "JWT")
    public LabelDTO createLabel(@RequestBody LabelCreateDTO label) {
        return labelMapper.map(labelService.createLabel(label));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public LabelDTO updateLabel(@PathVariable Long id, @RequestBody LabelUpdateDTO label) {
        return labelMapper.map(labelService.updateLabel(id, label));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "JWT")
    public ResponseEntity<Void> deleteLabel(@PathVariable Long id) {
        labelService.deleteLabel(id);
        return ResponseEntity.noContent().build();
    }
}
