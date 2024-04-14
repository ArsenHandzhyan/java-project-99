package hexlet.code.app;

import hexlet.code.app.model.User;
import hexlet.code.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
    }

    @Test
    public void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"id\":1,\"email\":\"john.doe@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}]"));
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"email\":\"john.doe@example.com\",\"firstName\":\"John\",\"lastName\":\"Doe\"}"));
    }

    @Test
    public void testCreateUser() throws Exception {
        User newUser = new User();
        newUser.setEmail("jack@google.com");
        newUser.setFirstName("Jack");
        newUser.setLastName("Jons");
        newUser.setPassword("some-password");

        // Предполагаем, что после сохранения пользователя в базе данных, ему присваивается ID.
        // Для теста мы мокируем метод save так, чтобы он возвращал newUser с присвоенным ID.
        when(userRepository.save(newUser)).thenReturn(newUser);

        // Добавляем ID в ожидаемый JSON, так как он должен быть возвращен сервером.
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"jack@google.com\",\"firstName\":\"Jack\",\"lastName\":\"Jons\",\"password\":\"some-password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"email\":\"jack@google.com\",\"firstName\":\"Jack\",\"lastName\":\"Jons\",\"password\":\"some-password\"}"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setId(3L);
        updatedUser.setEmail("jack@yahoo.com");
        updatedUser.setFirstName("Jack");
        updatedUser.setLastName("Jons");
        updatedUser.setPassword("new-password");

        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"jack@yahoo.com\",\"password\":\"new-password\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":3,\"email\":\"jack@yahoo.com\",\"firstName\":\"Jack\",\"lastName\":\"Jons\"}"));
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isOk());
    }
}