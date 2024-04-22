package hexlet.code.app.config;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @Profile("!data-initializer")
    public CommandLineRunner initDatabase() {
        return args -> {
            User user = new User();
            user.setEmail("hexlet@example.com");
            user.setPassword(passwordEncoder.encode("qwerty"));
            userRepository.save(user);
        };
    }
}
