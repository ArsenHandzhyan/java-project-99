package hexlet.code.app.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.sentry.Sentry;

@RestController
public class TestController {

    @GetMapping("/test-error")
    public String testError() {
        try {
            throw new Exception("This is a test.");
        } catch (Exception e) {
            Sentry.captureException(e);
        }
        return "Error has been sent to Sentry";
    }
}
