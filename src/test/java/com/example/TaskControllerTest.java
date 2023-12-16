package com.example;

import com.example.dao.TaskDao;
import com.example.model.Priority;
import com.example.model.Status;
import com.example.model.Task;
import com.example.model.request.CreateTaskRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;
import java.util.Optional;

import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TaskDao taskDao;


	@Test
	@WithMockUser
	public void testGetTasksWithoutParams() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/tasks"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser
	public void testGetTasksWithStatusParam() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/tasks")
						.param("status", "AT_WORK"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(username = "colinhall@yahoo.com") // Username взят из тестового набора данных в init.sql
	public void testCreateTaskWithExistingExecutor() throws Exception {
		CreateTaskRequest newTask = new CreateTaskRequest("Header", "Description", Status.AT_WORK, Priority.HIGH, "xli@gmail.com"); //Email взят из тестового набора данных в init.sql
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(newTask);

		mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(username = "colinhall@yahoo.com") // Username взят из тестового набора данных в init.sql
	public void testCreateTaskWithNotExistingExecutor() throws Exception {
		CreateTaskRequest newTask = new CreateTaskRequest("Header", "Description", Status.AT_WORK, Priority.HIGH, "noname@gmail.com");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(newTask);

		mockMvc.perform(MockMvcRequestBuilders.post("/tasks")
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@WithMockUser(username = "xli@gmail.com") // Owner задачи с id == 1
	public void testEditTaskWithCorrectOwner() throws Exception {
		Optional<Task> optionalTask = taskDao.findById(1L);
		Task task = optionalTask.get();
		task.setDescription("Change for test");

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(task);

		mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{taskId}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(username = "test@gmail.com")
	public void testEditTaskWithIncorrectOwner() throws Exception {
		Long taskId = 1L;

		Optional<Task> optionalTask = taskDao.findById(taskId);
		Task task = optionalTask.get();
		task.setDescription("Change for test");

		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(task);

		mockMvc.perform(MockMvcRequestBuilders.put("/tasks/{taskId}", taskId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(json))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@WithMockUser(username = "xli@gmail.com") // Это username owner первой задачи
	public void testUpdateStatusOfTaskWithCorrectOwner() throws Exception {
		Long taskId = 1L;
		Status newStatus = Status.AT_WORK;

		mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/{taskId}", taskId)
						.param("status", String.valueOf(newStatus)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(username = "xli@gmail.com") // Это username owner первой задачи
	public void testUpdateStatusOfTaskWithCorrectOwnerButNotCorrectIdOfTask() throws Exception {
		Long taskId = 9999L;
		Status newStatus = Status.AT_WORK;

		mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/{taskId}", taskId)
						.param("status", String.valueOf(newStatus)))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@WithMockUser(username = "test@gmail.com")
	public void testUpdateStatusOfTaskWithIncorrectOwner() throws Exception {
		Long taskId = 1L;
		Status newStatus = Status.AT_WORK;

		mockMvc.perform(MockMvcRequestBuilders.patch("/tasks/{taskId}", taskId)
						.param("status", String.valueOf(newStatus)))
				.andExpect(MockMvcResultMatchers.status().isForbidden());
	}





}
