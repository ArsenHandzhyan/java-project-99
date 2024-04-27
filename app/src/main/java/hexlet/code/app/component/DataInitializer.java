package hexlet.code.app.component;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.service.LabelService;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.service.TaskStatusService;
import hexlet.code.app.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {
    private final UserService userService;

    private final TaskStatusService taskStatusService;

    private final LabelService labelService;

    private final TaskService taskService;

    public DataInitializer(UserService userService,
                           TaskStatusService taskStatusService,
                           LabelService labelService,
                           TaskService taskService) {
        this.userService = userService;
        this.taskStatusService = taskStatusService;
        this.labelService = labelService;
        this.taskService = taskService;
    }

    @Override
    public void run(ApplicationArguments args) {
        // Создание администратора системы
        UserCreateDTO admin = new UserCreateDTO();
        admin.setEmail("hexlet@example.com");
        admin.setPassword("qwerty");
        admin.setCreatedAt(LocalDateTime.now());
        userService.createUser(admin);

        // Добавление дефолтных статусов задач
        Arrays.asList("Draft", "ToReview", "ToBeFixed", "ToPublish", "Published")
                .forEach(statusName -> {
                    TaskStatusCreateDTO tasStatus = new TaskStatusCreateDTO("New", "new");
                    tasStatus.setName(statusName);
                    tasStatus.setSlug(statusName.toLowerCase().replace(" ", "_"));
                    tasStatus.setCreatedAt(LocalDateTime.now());
                    taskStatusService.createTaskStatus(tasStatus);
                });

        // Создание дефолтных меток
        Arrays.asList("feature", "bug")
                .forEach(labelName -> {
                    LabelCreateDTO label = new LabelCreateDTO();
                    label.setName(labelName);
                    label.setCreatedAt(LocalDateTime.now());
                    labelService.createLabel(label);
                });

        // Создание дефолтных задач
        // Для примера создадим задачу с названием "Initial Task"
        TaskCreateDTO initialTask = new TaskCreateDTO();
        initialTask.setName("Initial Task");
        initialTask.setIndex(1);
        initialTask.setDescription("This is an initial task created at application startup.");
        initialTask.setTaskStatus("Draft"); // Установим статус "Draft"
        // Присвоим задаче дефолтные метки
        Set<Label> labels = new HashSet<>();
        labels.add(labelService.getLabelByName("feature"));
        labels.add(labelService.getLabelByName("bug"));
        // Присвоим задаче администратора в качестве исполнителя
        initialTask.setAssignee(userService.getUserByEmail("hexlet@example.com"));
        taskService.create(initialTask); // Создаем задачу
    }
}
