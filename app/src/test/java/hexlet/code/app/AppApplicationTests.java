package hexlet.code.app;

import hexlet.code.app.config.EncodersConfig;
import hexlet.code.app.config.SecurityConfig;
import hexlet.code.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.core.userdetails.User;

@SpringBootTest(classes = SecurityConfig.class)
@Import({EncodersConfig.class})
@AutoConfigureMockMvc
class AppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserService userService;

    @BeforeEach
    public void setup() {
        UserDetails userDetails = User.builder()
                .username("hexlet@example.com")
                .password(passwordEncoder.encode("qwerty"))
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void getWelcomeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/welcome")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.email", is("Welcome to Spring!")));
    }

    @Test
    public void getAllUsersTest() throws Exception {
        hexlet.code.app.model.User user1 = new hexlet.code.app.model.User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");

        hexlet.code.app.model.User user2 = new hexlet.code.app.model.User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        List<hexlet.code.app.model.User> users = Arrays.asList(user1, user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect((ResultMatcher) jsonPath("$[0].email", is("user1@example.com")))
                .andExpect((ResultMatcher) jsonPath("$[1].email", is("user2@example.com")));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        hexlet.code.app.model.User user = new hexlet.code.app.model.User();
        user.setId(1L);
        user.setEmail("user1@example.com");

        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.email", is("user1@example.com")));
    }

    @Test
    public void createUserTest() throws Exception {
        hexlet.code.app.model.User user = new hexlet.code.app.model.User();
        user.setEmail("user1@example.com");

        when(userService.createUser(any(hexlet.code.app.model.User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user1@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect((ResultMatcher) jsonPath("$.email", is("user1@example.com")));
    }

    @Test
    public void updateUserTest() throws Exception {
        hexlet.code.app.model.User user = new hexlet.code.app.model.User();
        user.setId(1L);
        user.setEmail("user1@example.com");

        when(userService.updateUser(1L, any(hexlet.code.app.model.User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user1@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.email", is("user1@example.com")));
    }

    @Test
    public void deleteUserTest() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isNoContent());
    }
}
