package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AppApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskStatusRepository taskStatusRepository;


    private String token;

    @BeforeEach
    void setUp() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"hexlet@example.com\",\"password\":\"qwerty\"}"))
                .andExpect(status().isOk()) // Проверяем, что статус ответа равен 200 OK
                .andReturn();

        token = result.getResponse().getContentAsString();
    }

    @Test
    public void getWelcomeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/welcome")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Welcome to Spring!")));
    }

    @Test
    public void getAllUsersTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email", is("hexlet@example.com")));
    }

    @Test
    public void getUserByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("hexlet@example.com")));
    }

    @Test
    public void createUserTest() throws Exception {
        String uniqueEmail = "hexlet" + System.currentTimeMillis() + "@example.com";
        if (token.isEmpty()) {
            fail("Token is null or empty. Ensure the getAccessToken method is correctly implemented.");
        }

        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + uniqueEmail + "\", \"password\":\"hexlet\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(uniqueEmail)))
                // Expect the hashed password instead of the plain text password
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    public void updateUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user1@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("user1@example.com")));
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCreateTaskStatus() throws Exception {
        TaskStatusCreateDTO taskStatusCreateDTO = new TaskStatusCreateDTO("New", "new");
        TaskStatus taskStatus = new TaskStatus("Draft", "draft");
        taskStatus.setId(1L);

        when(taskStatusRepository.save(any(TaskStatus.class))).thenReturn(taskStatus);

        mockMvc.perform(post("/api/task_statuses")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskStatusCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Draft"))
                .andExpect(jsonPath("$.slug").value("draft"));
    }

    @Test
    void shouldGetTaskStatusById() throws Exception {
        TaskStatus taskStatus = new TaskStatus("Draft", "draft");
        taskStatus.setName("ToReview");
        taskStatus.setSlug("to_review");
        taskStatus.setId(2L);

        when(taskStatusRepository.findById(2L)).thenReturn(Optional.of(taskStatus));

        // Используем действительный токен в заголовке авторизации
        mockMvc.perform(MockMvcRequestBuilders.get("/api/task_statuses/2")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("ToReview"))
                .andExpect(jsonPath("$.slug").value("to_review"));
    }

    @Test
    void shouldUpdateTaskStatus() throws Exception {
        TaskStatusUpdateDTO taskStatusUpdateDTO = new TaskStatusUpdateDTO("New", "new");
        TaskStatus taskStatus = new TaskStatus("Draft", "draft");
        taskStatus.setName("New");
        taskStatus.setSlug("new");
        taskStatus.setId(1L);

        when(taskStatusRepository.findById(1L)).thenReturn(Optional.of(taskStatus));
        when(taskStatusRepository.save(any(TaskStatus.class))).thenReturn(taskStatus);

        // Используем действительный токен в заголовке авторизации
        mockMvc.perform(MockMvcRequestBuilders.put("/api/task_statuses/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New\",\"slug\":\"new\"}")
                        .content(new ObjectMapper().writeValueAsString(taskStatusUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New"))
                .andExpect(jsonPath("$.slug").value("new"));
    }

    @Test
    void shouldDeleteTaskStatus() throws Exception {
        doNothing().when(taskStatusRepository).deleteById(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/task_statuses/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
