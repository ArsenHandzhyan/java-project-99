package hexlet.code.app.component;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.model.Label;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.service.LabelService;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.service.TaskStatusService;
import hexlet.code.app.service.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

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
        log.info("Starting data initialization...");

        try {
            // Создание администратора системы, если он еще не создан
            String adminEmail = "hexlet@example.com";
            if (userService.getUserByEmail(adminEmail) == null) {
                UserCreateDTO admin = new UserCreateDTO();
                admin.setEmail(adminEmail);
                admin.setPassword("qwerty");
                userService.createUser(admin);
                log.info("Admin user created");
            }

            // Добавление дефолтных статусов задач
            Arrays.asList("Draft", "ToReview", "ToBeFixed", "ToPublish", "Published")
                    .forEach(statusName -> {
                        if (taskStatusService.getTaskStatusByName(statusName) == null) {
                            TaskStatusCreateDTO taskStatus = new TaskStatusCreateDTO(statusName, statusName.toLowerCase().replace(" ", "_"));
                            taskStatusService.createTaskStatus(taskStatus);
                            log.info("Task status '{}' created", statusName);
                        }
                    });

            // Создание дефолтных меток
            Arrays.asList("feature", "bug")
                    .forEach(labelName -> {
                        if (labelService.getLabelByName(labelName) == null) {
                            LabelCreateDTO label = new LabelCreateDTO();
                            label.setName(labelName);
                            labelService.createLabel(label);
                            log.info("Label '{}' created", labelName);
                        }
                    });

            // Создание дефолтных задач
            if (taskService.getTaskByName("Initial Task")) {
                TaskCreateDTO initialTask = new TaskCreateDTO();
                initialTask.setName("Initial Task");
                initialTask.setIndex(1);
                initialTask.setDescription("This is an initial task created at application startup.");
//                initialTask.setTaskStatus(taskStatusService.getTaskStatusByName("Draft"));
                Set<Label> labels = new HashSet<>();
                labels.add(labelService.getLabelByName("feature"));
                labels.add(labelService.getLabelByName("bug"));
                initialTask.setAssignee(userService.getUserByEmail(adminEmail));
                taskService.create(initialTask);
                log.info("Initial task created");
            }
        } catch (Exception e) {
            log.error("Error during data initialization: {}", e.getMessage(), e);
        }

        log.info("Data initialization completed successfully.");
    }
}
