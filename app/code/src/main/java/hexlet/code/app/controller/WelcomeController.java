package hexlet.code.app.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(exposedHeaders = "X-Total-Count")
public class WelcomeController {

    @GetMapping("/welcome")
    public Map<String, String> welcome() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Welcome to Spring!");
        response.put("email", "example@example.com"); // Пример значения
        return response;
    }

    @GetMapping("/api/login")
    public String showLoginForm() {
        // Возвращаем имя представления, которое содержит форму логина
        return "login";
    }
}
