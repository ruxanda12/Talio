package api;

import commons.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.TaskController;
import server.service.FelloListService;
import server.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Mock
    private FelloListService felloListService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void getAllTasksTest(){
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("Task 1", "Description 1", null, null, "blue", "blue", "blue", false));
        tasks.add(new Task("Task 2", "Description 2", null, null, "blue", "blue", "blue", false));
        when(taskService.getTasks()).thenReturn(tasks);

        List<Task> result = taskController.getAll();
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).title);
        assertEquals("Task 2", result.get(1).title);
    }

    @Test
    public void getTaskByIdTest(){
        Task task = new Task("Task 1", "Description 1", null, null, "blue", "blue", "blue", false);
        when(taskService.exists(1)).thenReturn(true);
        when(taskService.getTaskById(1L)).thenReturn(task);

        ResponseEntity<Task> response = taskController.getById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task 1", Objects.requireNonNull(response.getBody()).title);
    }

    @Test
    public void getTaskByIdTest_BadRequest(){
        ResponseEntity<Task> response = taskController.getById(-1L);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void addTaskTest(){
        List<FelloList> felloLists = new ArrayList<>();
        List<Tags> tags = new ArrayList<>();
        List<Palette> palettes = new ArrayList<>();
        Board board = new Board("Board", false, " ", felloLists, tags, palettes, "blue",
                "blue", "blue", "blue", "blue", "blue");
        List<Task> listOfTasks = new ArrayList<>();
        FelloList list = new FelloList("List 1", listOfTasks, board, "blue", "blue", "blue", false);
        Task task = new Task("Task 1", "Description 1", null, list, "blue", "blue", "blue", false);
        when(felloListService.getList(1)).thenReturn(list);
        when(taskService.addTask(task)).thenReturn(task);
        when(felloListService.addList(list)).thenReturn(list);

        ResponseEntity<Task> response = taskController.add(task, 1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task 1", Objects.requireNonNull(response.getBody()).title);
        assertEquals(1, list.tasks.size());
        assertEquals(task, list.tasks.get(0));
    }

    @Test
    public void addTaskTest_InvalidTitle(){
        Task task = new Task(null, "Description 1", null, null, "blue", "blue", "blue", false);
        ResponseEntity<Task> response = taskController.add(task, 1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteTaskTest() {
        List<FelloList> felloLists = new ArrayList<>();
        List<Tags> tags = new ArrayList<>();
        List<Palette> palettes = new ArrayList<>();
        Board board = new Board("Board", false, " ", felloLists, tags, palettes, "blue",
                "blue", "blue", "blue", "blue", "blue");
        List<Task> listOfTasks = new ArrayList<>();
        FelloList list = new FelloList("List 1", listOfTasks, board, "blue", "blue", "blue", false);
        Task task = new Task("Task 1", "Description 1", null, list, "blue", "blue", "blue", false);
        when(taskService.exists(1)).thenReturn(true);
        when(taskService.deleteTask(1)).thenReturn(task);

        ResponseEntity<Task> response = taskController.deleteById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Task 1", Objects.requireNonNull(response.getBody()).title);
    }

    @Test
    public void deleteTaskTest_NotFound() {
        when(taskService.exists(1)).thenReturn(false);

        ResponseEntity<Task> response = taskController.deleteById(1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void editTaskTest(){
        List<FelloList> felloLists = new ArrayList<>();
        List<Tags> tags = new ArrayList<>();
        List<Palette> palettes = new ArrayList<>();
        Board board = new Board("Board", false, " ", felloLists, tags, palettes, "blue",
                "blue", "blue", "blue", "blue", "blue");
        List<Task> listOfTasks = new ArrayList<>();
        FelloList list = new FelloList("List 1", listOfTasks, board, "blue", "blue", "blue", false);
        Task task = new Task("Task 1", "Description 1", null, list, "blue", "blue", "blue", false);
        task.id = 1;
        when(taskService.exists(1)).thenReturn(true);
        when(taskService.addTask(task)).thenReturn(task);

        ResponseEntity<Task> response = taskController.edit(1, task);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    public void editTaskTest_NotFound(){
        Task task = new Task("Task 1", "Description", null, null, "blue", "blue", "blue", false);
        task.id = 1;
        when(taskService.exists(1)).thenReturn(false);

        ResponseEntity<Task> response = taskController.edit(task.id, task);

        verify(taskService, never()).addTask(task);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
