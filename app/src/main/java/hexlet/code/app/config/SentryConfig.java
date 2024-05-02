package hexlet.code.app.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SentryConfig {

    @Value("${SENTRY_AUTH_TOKEN:default_token}")
    private String sentryAuthToken;

    public void someMethod() {
        System.out.println("Using Sentry Auth Token: " + sentryAuthToken);
        // Здесь можно использовать токен, например, для настройки клиента Sentry
    }
}
