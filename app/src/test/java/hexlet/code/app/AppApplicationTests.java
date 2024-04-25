package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.TaskCreateDTO;
import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
import hexlet.code.app.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import hexlet.code.app.dto.TaskStatusCreateDTO;
import hexlet.code.app.dto.TaskStatusUpdateDTO;
import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.model.User;
import hexlet.code.app.repository.TaskStatusRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AppApplicationTests {

    @MockBean
    private TaskService taskService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskMapper taskMapper;

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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is(uniqueEmail)));
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

    @Test
    public void getTaskByIdTest() throws Exception {
        TaskCreateDTO task = new TaskCreateDTO();
        task.setName("Task 1");
        task.setIndex(3140);
        task.setDescription("Description of task 1");
        task.setTaskStatus("to_be_fixed");
        task.setCreatedAt(LocalDateTime.now());
        task.setAssignee(new User());
        taskService.createTask(task);
        when(taskService.getTaskById(1L)).thenReturn(taskMapper.toEntity(task));

        mockMvc.perform(get("/api/tasks/1")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Task 1"))
                .andExpect(jsonPath("$.index").value(3140))
                .andExpect(jsonPath("$.description").value("Description of task 1"))
                .andExpect(jsonPath("$.taskStatus").value("to_be_fixed"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    public void getAllTasksTest() throws Exception {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setName("Task 1");
        task1.setIndex(3140);
        task1.setDescription("Description of task 1");
        task1.setTaskStatus("to_be_fixed");
        task1.setCreatedAt(LocalDateTime.now());
        task1.setAssignee(new User());

        Task task2 = new Task();
        task2.setId(2L);
        task2.setName("Task 2");
        task2.setIndex(3161);
        task2.setDescription("Description of task 2");
        task2.setTaskStatus("to_review");
        task2.setCreatedAt(LocalDateTime.now());
        task2.setAssignee(new User());

        when(taskService.getAllTasks()).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Task 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Task 2"));
    }

    @Test
    public void createTaskTest() throws Exception {
        TaskCreateDTO taskCreateDTO = new TaskCreateDTO();
        taskCreateDTO.setIndex(12);
        taskCreateDTO.setName("Test title");
        taskCreateDTO.setDescription("Aaaaaaaa");
        taskCreateDTO.setTaskStatus("draft");

        Task task = new Task();
        task.setId(31L);
        task.setIndex(12);
        task.setName("Test title");
        task.setDescription("Test content");
        task.setTaskStatus("draft");

        when(taskService.createTask(any(TaskCreateDTO.class))).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(31))
                .andExpect(jsonPath("$.index").value(12))
                .andExpect(jsonPath("$.name").value("Test title"))
                .andExpect(jsonPath("$.description").value("Test content"))
                .andExpect(jsonPath("$.taskStatus").value("draft"));
    }

    @Test
    public void updateTaskTest() throws Exception {
        // Prepare the test data
        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
        taskUpdateDTO.setName("New title");
        taskUpdateDTO.setDescription("New content");

        // Mock the service layer
        Task task = new Task();
        task.setId(31L);
        task.setName("New title");
        task.setDescription("New content");
        task.setTaskStatus("draft");
        task.setCreatedAt(LocalDateTime.now());
        task.setAssignee(new User()); // Assuming User is a valid entity

        when(taskService.updateTask(31L, taskUpdateDTO)).thenReturn(task);

        // Perform the request
        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/31")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New title\",\"description\":\"New content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(31))
                .andExpect(jsonPath("$.name").value("New title"))
                .andExpect(jsonPath("$.description").value("New content"))
                .andExpect(jsonPath("$.taskStatus").value("draft"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    public void deleteTaskTest() throws Exception {
        doNothing().when(taskService).deleteTask(31L);

        mockMvc.perform(delete("/api/tasks/31")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
