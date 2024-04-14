package hexlet.code.app.config;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            User user = new User();
            user.setEmail("hexlet@example.com");
            user.setPassword("qwerty"); // Убедитесь, что пароль хешируется
            userRepository.save(user);
        };
    }
}
