package hexlet.code.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:8080") // Указывайте здесь актуальные домены для вашего приложения
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type") // Указание разрешенных заголовков
                .exposedHeaders("X-Total-Count") // Убедитесь, что этот заголовок указан для поддержки пагинации
                .allowCredentials(true);
    }
}
