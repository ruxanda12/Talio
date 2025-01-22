package service;

import commons.FelloList;
import commons.SubTask;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import server.database.TaskRepository;
import server.service.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private List<Task> taskList;

    /**
     * Initializes mock objects and test data before each test
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        task1 = new Task("Task 1", "Description 1", new ArrayList<>(), null, "blue", "black", "red", false);
        task2 = new Task("Task 2", "Description 2",new ArrayList<>(), null, "blue", "black", "red", false);
        task1.id = 0;
        task2.id = 1;
        taskList = new ArrayList<>();
        taskList.add(task1);
        taskList.add(task2);
        Mockito.when(taskRepository.findById(task1.id)).thenReturn(Optional.of(task1));
    }

    /**
     * Tests the getTasks method of the TaskService class
     */
    @Test
    public void testGetTasks() {
        when(taskRepository.findAll()).thenReturn(taskList);
        List<Task> result = taskService.getTasks();
        assertEquals(2, result.size());
        assertTrue(result.contains(task1));
        assertTrue(result.contains(task2));
    }

    /**
     * Tests the exists method of the TaskService class
     */
    @Test
    public void testExists() {
        when(taskRepository.existsById(task1.id)).thenReturn(true);
        assertTrue(taskService.exists(task1.id));
        assertFalse(taskService.exists(Long.MAX_VALUE));
    }

    /**
     * Tests the getTaskById method of the TaskService class
     */
    @Test
    public void testGetTaskById() {
        when(taskRepository.findById(task1.id)).thenReturn(Optional.ofNullable(task1));
        assertEquals(task1, taskService.getTaskById(task1.id));

        Exception exception = assertThrows(Exception.class,
                () -> taskService.getTaskById(Long.MAX_VALUE));
        assertEquals("No value present", exception.getMessage());
    }

    /**
     * Tests the addTask method of the TaskService class
     */
    @Test
    public void testAddTask() {
        when(taskRepository.save(task1)).thenReturn(task1);
        Task result = taskService.addTask(task1);
        assertEquals(task1, result);
        verify(taskRepository, times(1)).save(task1);
    }

    /**
     * Tests the deleteTask method of the TaskService class
     */
    @Test
    public void testDeleteTask() {
        Task taskToDelete = new Task("Title", "Description", new ArrayList<SubTask>(), new FelloList(),
                "blue", "black", "red", false);
        when(taskRepository.save(taskToDelete)).thenReturn(taskToDelete);
        Task result = taskService.addTask(taskToDelete);
        // Create a task to be deleted
        // Call the deleteTask method of the task service
        taskService.deleteTask(taskToDelete.id);

        // Verify that the task repository's deleteById method was called with the correct argument
        Mockito.verify(taskRepository, Mockito.times(1)).deleteById(taskToDelete.id);
    }

}