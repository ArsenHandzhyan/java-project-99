package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.model.Task;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.service.TaskStatusService;
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

import java.util.Collections;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AppApplicationTests {

    @MockBean
    private TaskService taskService;

    @MockBean
    private TaskStatusService taskStatusService;


    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].email", is("hexlet@example.com")))
                .andExpect(jsonPath("$[1].email", is("hexlet2@example.com")));
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(uniqueEmail)));
    }

    @Test
    public void updateUserTest() throws Exception {
        mockMvc.perform(put("/api/users/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user1@example.com\",\"password\":\"newpassword\"}"))
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
    public void shouldDeleteTaskStatus() throws Exception {
        Long taskStatusId = 1L;
        doThrow(new ResourceNotFoundException("Task status not found for id " + taskStatusId))
                .when(taskStatusService).deleteTaskStatus(taskStatusId);

        mockMvc.perform(delete("/api/task_statuses/{id}", taskStatusId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Task status not found for id " + taskStatusId));
    }

    @Test
    void getAllTasksEmpty() throws Exception {
        when(taskService.findTasks(any(), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "0"))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void createTaskTest() throws Exception {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setIndex(12);
        taskCreateDTO.setTitle("Test title");
        taskCreateDTO.setContent("Aaaaaaaa");
        taskCreateDTO.setStatus("draft");

        Task task = new Task();
        task.setId(31L);
        task.setIndex(12);
        task.setName("Test title");
        task.setDescription("Test content");
        task.setTaskStatus("draft");

        when(taskService.create(any(TaskCreateDTO.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(31))
                .andExpect(jsonPath("$.index").value(12))
                .andExpect(jsonPath("$.title").value("Test title"))
                .andExpect(jsonPath("$.content").value("Test content"))
                .andExpect(jsonPath("$.status").value("draft"));
    }

    @Test
    public void deleteTaskTest() throws Exception {
        // Проверка успешного удаления задачи
        doNothing().when(taskService).delete(31L);

        mockMvc.perform(delete("/api/tasks/31")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Проверка невозможности удаления задачи, если связанный пользователь не может быть удален
        Long taskIdWithAssociatedUser = 32L;
        doThrow(new IllegalStateException("Cannot delete user with associated tasks"))
                .when(taskService).delete(taskIdWithAssociatedUser);

        mockMvc.perform(delete("/api/tasks/" + taskIdWithAssociatedUser)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Cannot delete user with associated tasks")));

        // Проверка невозможности удаления задачи, если связанный статус не может быть удален
        Long taskIdWithAssociatedStatus = 33L;
        doThrow(new IllegalStateException("Cannot delete task status with associated tasks"))
                .when(taskService).delete(taskIdWithAssociatedStatus);

        mockMvc.perform(delete("/api/tasks/" + taskIdWithAssociatedStatus)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Cannot delete task status with associated tasks")));
    }

    @Test
    public void createUserWithInvalidEmailTest() throws Exception {
        String invalidEmail = "not_an_email";
        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"" + invalidEmail + "\", \"password\":\"password\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createTaskWithInvalidStatusTest() throws Exception {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setIndex(13);
        taskCreateDTO.setContent("Invalid Status Test");
        taskCreateDTO.setStatus("invalid_status");

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void accessProtectedRouteWithoutTokenTest() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isFound());
    }

    @Test
    public void accessProtectedRouteWithInvalidTokenTest() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void updateUserInvalidDataTest() throws Exception {
        mockMvc.perform(put("/api/users/1")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid_email\",\"password\":\"newpassword\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteNonExistentUserTest() throws Exception {
        mockMvc.perform(delete("/api/users/999")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }
}
