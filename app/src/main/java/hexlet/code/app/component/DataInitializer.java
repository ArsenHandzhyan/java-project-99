package hexlet.code.app.component;

import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.service.TaskStatusService;
import hexlet.code.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Создание администратора системы
        User admin = new User();
        admin.setEmail("hexlet@example.com");
        admin.setPassword("qwerty");
        userService.createUser(admin);

        // Добавление дефолтных статусов задач
        Arrays.asList("Draft", "ToReview", "ToBeFixed", "ToPublish", "Published")
                .forEach(statusName -> {
                    TaskStatus status = new TaskStatus();
                    status.setName(statusName);
                    status.setSlug(statusName.toLowerCase().replace(" ", "_"));
                    status.setCreatedAt(LocalDateTime.now());
                    taskStatusService.createTaskStatus(status);
                });
    }
}