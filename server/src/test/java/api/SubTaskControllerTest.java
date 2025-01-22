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
import server.api.SubTaskController;
import server.service.SubTaskService;
import server.service.TaskService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SubTaskControllerTest {
    @Mock
    private SubTaskService subTaskService;
    @Mock
    private TaskService taskService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private SubTaskController subTaskController;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllTest(){
        List<SubTask> subTasks = Arrays.asList(
                new SubTask("SubTask 1", null, false),
                new SubTask("SubTask 2", null, false)
        );
        when(subTaskService.getSubTasks()).thenReturn(subTasks);
        List<SubTask> result = subTaskController.getAll();
        assertEquals(subTasks, result);
    }

    @Test
    public void getByIdTest(){
        SubTask subTask = new SubTask("Subtask 1", null, false);
        when(subTaskService.exists(1L)).thenReturn(true);
        when(subTaskService.getSubTaskById(1L)).thenReturn(subTask);

        ResponseEntity<SubTask> result = subTaskController.getById(1L);

        assertEquals(subTask, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void getByIdTest_InvalidId(){
        when(subTaskService.exists(-1L)).thenReturn(false);
        ResponseEntity<SubTask> result = subTaskController.getById(-1L);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void addTest() {
        List<FelloList> felloLists = new ArrayList<>();
        List<Tags> tags = new ArrayList<>();
        List<Palette> palettes = new ArrayList<>();
        Board board = new Board("Board", false, " ", felloLists, tags, palettes,
                "blue", "blue", "blue", "blue", "blue", "blue");
        FelloList parent = new FelloList("Title", new ArrayList<Task>(), board,
                "blue", "blue", "blue", false);
        Task task = new Task("Task 1", " ", new ArrayList<>(), parent,
                "blue", "blue", "blue", false);
        SubTask subTask = new SubTask("Subtask 1", task, false);
        when(subTaskService.addSubTask(subTask)).thenReturn(subTask);
        when(taskService.getTaskById(1L)).thenReturn(task);

        ResponseEntity<SubTask> result = subTaskController.add(subTask, 1L);

        assertEquals(subTask, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    @Test
    public void deleteByIdTest() {
        List<FelloList> felloLists = new ArrayList<>();
        List<Tags> tags = new ArrayList<>();
        List<Palette> palettes = new ArrayList<>();
        Board board = new Board("Board", false, " ", felloLists, tags, palettes, "blue",
                "blue", "blue", "blue", "blue", "blue");
        FelloList parent = new FelloList("Title", new ArrayList<Task>(), board, "blue", "blue", "blue", false);
        Task task = new Task("Task 1", " ", new ArrayList<>(), parent, "blue", "blue", "blue", false);
        SubTask subTask = new SubTask("Subtask 1", task, false);
        when(subTaskService.exists(1L)).thenReturn(true);
        when(subTaskService.deleteSubTask(1L)).thenReturn(subTask);

        ResponseEntity<SubTask> result = subTaskController.deleteById(1L);

        assertEquals(subTask, result.getBody());
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void deleteByIdTest_InvalidId(){
        when(subTaskService.exists(-1L)).thenReturn(false);
        ResponseEntity<SubTask> result = subTaskController.deleteById(-1L);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void editTest() {
        List<FelloList> felloLists = new ArrayList<>();
        List<Tags> tags = new ArrayList<>();
        List<Palette> palettes = new ArrayList<>();
        Board board = new Board("Board", false, " ", felloLists, tags, palettes, "blue",
                "blue", "blue", "blue", "blue", "blue");
        FelloList parent = new FelloList("Title", new ArrayList<Task>(), board,
                "blue", "blue", "blue", false);
        Task task = new Task("Task 1", " ", new ArrayList<SubTask>(), parent,
                "blue", "blue", "blue", false);
        SubTask originalSubTask = new SubTask("original title", task, false);
        originalSubTask.id = 1L;

        SubTask updatedSubTask = new SubTask("updated title", task, true);
        updatedSubTask.id = 1L;

        when(subTaskService.exists(anyLong())).thenReturn(true);
        when(subTaskService.addSubTask(any(SubTask.class))).thenReturn(updatedSubTask);

        ResponseEntity<SubTask> responseEntity = subTaskController.edit(originalSubTask.id, updatedSubTask);

        verify(subTaskService).addSubTask(updatedSubTask);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedSubTask, responseEntity.getBody());
    }

    @Test
    public void editTest_InvalidId(){
        when(subTaskService.exists(-1L)).thenReturn(false);
        ResponseEntity<SubTask> result = subTaskController.edit(-1L, null);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }
}
