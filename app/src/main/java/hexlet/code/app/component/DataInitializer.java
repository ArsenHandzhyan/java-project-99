package hexlet.code.app.component;

import hexlet.code.app.dto.LabelCreateDTO;
import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.UserCreateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DataInitializer.class);

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
        LOGGER.info("Starting data initialization...");

        try {
            // Создание администратора системы, если он еще не создан
            String admin1Email = "hexlet@example.com";
            if (userService.getUserByEmail(admin1Email) == null) {
                UserCreateDTO admin1 = new UserCreateDTO();
                admin1.setEmail(admin1Email);
                admin1.setPassword("qwerty");
                userService.createUser(admin1);
                LOGGER.info("Admin1 user created");
            }

            String admin2Email = "hexlet2@example.com";
            if (userService.getUserByEmail(admin2Email) == null) {
                UserCreateDTO admin2 = new UserCreateDTO();
                admin2.setEmail(admin2Email);
                admin2.setPassword("qwerty");
                userService.createUser(admin2);
                LOGGER.info("Admin2 user created");
            }

            // Добавление дефолтных статусов задач
            Arrays.asList("Draft", "ToReview", "ToBeFixed", "ToPublish", "Published")
                    .forEach(statusName -> {
                        if (taskStatusService.getTaskStatusByName(statusName).isEmpty()) {
                            TaskStatusCreateDTO taskStatus = new TaskStatusCreateDTO();
                            taskStatus.setName(statusName);
                            taskStatus.setSlug(statusName.toLowerCase().replace(" ", "_"));
                            taskStatusService.createTaskStatus(taskStatus);
                            LOGGER.info("Task status '{}' created", statusName);
                        }
                    });

            // Создание дефолтных меток
            Arrays.asList("feature", "bug")
                    .forEach(labelName -> {
                        if (labelService.getLabelByName(labelName) == null) {
                            LabelCreateDTO label = new LabelCreateDTO();
                            label.setName(labelName);
                            labelService.createLabel(label);
                            LOGGER.info("Label '{}' created", labelName);
                        }
                    });

            // Создание дефолтных задач
            try {
                taskService.getTaskByName("Initial Task");
                LOGGER.info("Initial task already exists");
            } catch (ResourceNotFoundException e) {
                TaskCreateDTO initialTask = new TaskCreateDTO();
                initialTask.setTitle("Initial Task");
                initialTask.setIndex(1);
                initialTask.setContent("This is an initial task created at application startup.");

                // Установка статуса задачи
                String taskStatusName = "Draft"; // Замените на нужный статус задачи
                initialTask.setStatus(taskStatusName);

                // Установка меток задачи
                Set<Long> labelIds = new HashSet<>();
                labelIds.add(labelService.getLabelByName("feature").getId());
                labelIds.add(labelService.getLabelByName("bug").getId());
                initialTask.setTaskLabelIds(labelIds);

                initialTask.setAssignee_id(userService.getUserByEmail(admin1Email).getId());
                taskService.create(initialTask);
                LOGGER.info("Initial task created");
            }
        } catch (Exception e) {
            LOGGER.error("Error during data initialization: {}", e.getMessage(), e);
        }

        LOGGER.info("Data initialization completed successfully.");
    }
}
