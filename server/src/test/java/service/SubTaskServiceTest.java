package service;

import commons.SubTask;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.SubTaskRepository;
import server.service.SubTaskService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class SubTaskServiceTest {
    @Mock
    private SubTaskRepository subTaskRepository;

    @InjectMocks
    private SubTaskService subTaskService;

    private SubTask subTask1;
    private SubTask subTask2;
    private List<SubTask> subTasks;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        Task parent = new Task();
        subTask1 = new SubTask("test", parent, false);
        subTask2 = new SubTask("test2", parent, true);
        subTasks = new ArrayList<>();
        subTasks.add(subTask1);
        subTasks.add(subTask2);
        subTask1.id = 1;
    }
    @Test
    public void getSubTasks(){
        when(subTaskRepository.findAll()).thenReturn(subTasks);
        List<SubTask> res = subTaskService.getSubTasks();
        assertEquals(2, res.size());
        assertThat("Both subtasks are in the list", res.contains(subTask1) && res.contains(subTask2));
    }

    @Test
    public void subTaskExists(){
        when(subTaskRepository.existsById(1L)).thenReturn(true);
        assertThat("subTask1 exists in database", subTaskService.exists(1));
    }

    @Test
    public void getSubTaskByIdTest(){
        when(subTaskRepository.findById(subTask1.id)).thenReturn(Optional.ofNullable(subTask1));
        SubTask res = subTaskService.getSubTaskById(1L);
        assertThat("Correct subtask was returned", res.id == subTask1.id);
    }

    @Test
    public void addSubTaskTest(){
        when(subTaskRepository.save(subTask1)).thenReturn(subTask1);
        SubTask res = subTaskService.addSubTask(subTask1);
        assertThat("SubTask was saved in database", res.equals(subTask1));
    }

    @Test
    public void deleteSubTaskTest(){
        when(subTaskRepository.findById(subTask1.id)).thenReturn(Optional.ofNullable(subTask1));
        SubTask subTask = subTaskService.deleteSubTask(1L);
        verify(subTaskRepository, times(1)).deleteById(1L);
    }
}
