package hexlet.code.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.app.dto.TaskCreateDTO;
//import hexlet.code.app.dto.TaskDTO;
//import hexlet.code.app.dto.TaskUpdateDTO;
import hexlet.code.app.exeption.ResourceNotFoundException;
import hexlet.code.app.mapper.TaskMapper;
import hexlet.code.app.model.Task;
//import hexlet.code.app.model.TaskStatus;
import hexlet.code.app.repository.TaskRepository;
import hexlet.code.app.repository.TaskStatusRepository;
import hexlet.code.app.service.TaskService;
import hexlet.code.app.service.TaskStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.openapitools.jackson.nullable.JsonNullable;
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
//import java.util.List;
//import java.util.Optional;

import static com.jayway.jsonpath.internal.path.PathCompiler.fail;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
//import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Autowired
    private TaskMapper taskMapper;

    @MockBean
    private TaskStatusRepository taskStatusRepository;

    @MockBean
    private TaskRepository taskRepository;

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

//    @Test
//    void shouldCreateTaskStatus() throws Exception {
//        mockMvc.perform(post("/api/task_statuses")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"id\":\"10\",\"name\":\"DraftTest\",\"slug\":\"draftTest\"}"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(1))
//                .andExpect(jsonPath("$.name").value("DraftTest"))
//                .andExpect(jsonPath("$.slug").value("draftTest"));
//    }

//    @Test
//    void shouldGetTaskStatusById() throws Exception {
//        TaskStatus taskStatus = new TaskStatus("Draft", "draft");
//        taskStatus.setName("ToReview");
//        taskStatus.setSlug("to_review");
//        taskStatus.setId(10L);
//
//        when(taskStatusRepository.findById(10L)).thenReturn(Optional.of(taskStatus));
//
//        // Используем действительный токен в заголовке авторизации
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/task_statuses/2")
//                        .header("Authorization", "Bearer " + token))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(10))
//                .andExpect(jsonPath("$.name").value("ToReview"))
//                .andExpect(jsonPath("$.slug").value("to_review"));
//    }

//    @Test
//    void shouldUpdateTaskStatus() throws Exception {
//        TaskStatus existingTaskStatus = new TaskStatus();
//        existingTaskStatus.setId(10L);
//        existingTaskStatus.setName("Old Name");
//        existingTaskStatus.setSlug("old-slug");
//
//        when(taskStatusRepository.findById(10L)).thenReturn(Optional.of(existingTaskStatus));
//        when(taskStatusRepository.save(any(TaskStatus.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        mockMvc.perform(put("/api/task_statuses/10")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"New Name\",\"slug\":\"New slug\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(10))
//                .andExpect(jsonPath("$.name").value("JsonNullable[New Name]"))
//                .andExpect(jsonPath("$.slug").value("JsonNullable[New slug]"));
//    }

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

//    @Test
//    public void getTaskByIdTest() throws Exception {
//        TaskCreateDTO task = new TaskCreateDTO();
//        task.setName("Task 1");
//        task.setIndex(3140);
//        task.setDescription("Description of task 1");
//        task.setTaskStatus("to_be_fixed");
//        task.setAssignee(1L);
//        taskService.create(task);
//        Task taskMap = taskMapper.map(task);
//        when(taskService.findById(1L)).thenReturn(taskMap);
//
//        mockMvc.perform(get("/api/tasks/1")
//                        .header("Authorization", "Bearer " + token)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Task 1"))
//                .andExpect(jsonPath("$.index").value(3140))
//                .andExpect(jsonPath("$.description").value("Description of task 1"))
//                .andExpect(jsonPath("$.taskStatus").value("to_be_fixed"))
//                .andExpect(jsonPath("$.createdAt").exists());
//    }

//    @Test
//    void getAllTasks() throws Exception {
//        Task task1 = new Task();
//        task1.setId(10L);
//        task1.setName("Task 1");
//        task1.setIndex(1);
//        task1.setDescription("Description 1");
//        task1.setTaskStatus("to_be_fixed");
//        task1.setAssignee(1L);
//
//        Task task2 = new Task();
//        task2.setId(20L);
//        task2.setName("Task 2");
//        task2.setIndex(2);
//        task2.setDescription("Description 2");
//        task2.setTaskStatus("to_review");
//        task2.setAssignee(1L);
//
//        when(taskService.findTasks(any(), any(), any(), any())).thenReturn(List.of(task1, task2));
//
//        mockMvc.perform(get("/api/tasks")
//                        .header("Authorization", "Bearer " + token)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(header().string("X-Total-Count", "2"))
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].id").value(10))
//                .andExpect(jsonPath("$[9].name").value("Task 1"))
//                .andExpect(jsonPath("$[9].index").value(1))
//                .andExpect(jsonPath("$[9].description").value("Description 1"))
//                .andExpect(jsonPath("$[9].taskStatus").value("to_be_fixed"))
//                .andExpect(jsonPath("$[9].createdAt").exists())
//                .andExpect(jsonPath("$[9].assignee").exists())
//                .andExpect(jsonPath("$[19].id").value(20))
//                .andExpect(jsonPath("$[19].name").value("Task 2"))
//                .andExpect(jsonPath("$[19].index").value(2))
//                .andExpect(jsonPath("$[19].description").value("Description 2"))
//                .andExpect(jsonPath("$[19].taskStatus").value("to_review"))
//                .andExpect(jsonPath("$[19].createdAt").exists())
//                .andExpect(jsonPath("$[19].assignee").exists());
//    }

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
        taskCreateDTO.setName("Test title");
        taskCreateDTO.setDescription("Aaaaaaaa");
        taskCreateDTO.setTaskStatus("draft");

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

//    @Test
//    void updateTaskTest() throws Exception {
//        // Создание объекта TaskUpdateDTO с обновленными данными
//        TaskUpdateDTO taskUpdateDTO = new TaskUpdateDTO();
//        taskUpdateDTO.setName(JsonNullable.of("Updated Task"));
//        taskUpdateDTO.setDescription(JsonNullable.of("Updated description"));
//        taskUpdateDTO.setTaskStatus(JsonNullable.of("in_progress"));
//
//        // Создание существующей задачи
//        Task existingTask = new Task();
//        existingTask.setId(1L);
//        existingTask.setName("Existing Task");
//        existingTask.setDescription("Existing description");
//        existingTask.setTaskStatus("open");
//
//        // Настройка поведения репозитория при поиске задачи по идентификатору
//        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
//
//        // Настройка поведения репозитория при сохранении задачи
//        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        // Настройка поведения сервиса при обновлении задачи
//        when(taskService.update(1L, taskUpdateDTO)).thenReturn(existingTask);
//
//        // Выполнение запроса на обновление задачи
//        MvcResult result = mockMvc.perform(put("/api/tasks/1")
//                        .header("Authorization", "Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(taskUpdateDTO))
//                        .content("{\"name\":\"New Name\",\"slug\":\"New slug\"}"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        // Получение тела ответа
//        String responseBody = result.getResponse().getContentAsString();
//        System.out.println("Response Body: " + responseBody);
//
//        // Проверка, что тело ответа содержит обновленные данные задачи
//        TaskDTO expectedTaskDTO = taskMapper.map(existingTask);
//        TaskDTO actualTaskDTO = objectMapper.readValue(responseBody, TaskDTO.class);
//        assertEquals(expectedTaskDTO, actualTaskDTO);
//    }

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
}
