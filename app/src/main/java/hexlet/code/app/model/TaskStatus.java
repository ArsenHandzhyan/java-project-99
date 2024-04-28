package hexlet.code.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "task_status")
@Getter
@Setter
@AllArgsConstructor
public class TaskStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, message = "Name must be at least 1 character long")
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Slug is mandatory")
    @Size(min = 1, message = "Slug must be at least 1 character long")
    private String slug;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @ManyToMany(mappedBy = "taskStatuses")
    private Set<Task> tasks = new HashSet<>();

    public TaskStatus(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public TaskStatus() {

    }
}
