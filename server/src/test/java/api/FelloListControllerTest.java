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

import server.api.FelloListController;
import server.service.FelloListService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class FelloListControllerTest {

    @Mock
    private FelloListService felloListService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private FelloListController felloListController;

    private List<FelloList> felloLists;

    private List<Tags> tags;

    private List<Palette> palettes;

    private FelloList felloList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        Task task = new Task("Title", "Description", null, felloList,
                "blue", "blue", "blue", false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        felloLists = new ArrayList<>();
        tags = new ArrayList<Tags>();
        palettes = new ArrayList<Palette>();

        Board board = new Board("Board", false, " ", felloLists, tags, palettes,
                "blue", "blue", "blue", "blue", "blue", "blue");
        felloList = new FelloList("List Title", tasks, board,
                "blue", "blue", "blue", false);
        felloLists.add(felloList);

    }

    @Test
    public void getAllTest(){
        when(felloListService.getAllLists()).thenReturn(felloLists);
        List<FelloList> response = felloListController.getAll();
        assertEquals(felloLists, response);
    }

    @Test
    public void getByIdTest() {
        long id = 1L;
        when(felloListService.exists(id)).thenReturn(true);
        when(felloListService.getList(id)).thenReturn(felloList);

        ResponseEntity<FelloList> response = felloListController.getById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(felloList, response.getBody());
    }

    @Test
    public void getByIdTest_InvalidId() {
        long id = -1L;
        when(felloListService.exists(id)).thenReturn(false);

        ResponseEntity<FelloList> response = felloListController.getById(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void addTest(){
        FelloList newFelloList = new FelloList("New List Title", felloList.tasks,
                felloList.parentBoard, "blue", "blue", "blue", false);
        when(felloListService.addList(newFelloList)).thenReturn(newFelloList);

        ResponseEntity<FelloList> response = felloListController.add(newFelloList);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newFelloList, response.getBody());

        // Verify that simpMessagingTemplate.convertAndSend was called with the expected parameters
        verify(simpMessagingTemplate).convertAndSend("/topic/boards/" + newFelloList.parentBoard.id, newFelloList.parentBoard);
    }

    @Test
    public void addTest_InvalidTitle(){
        FelloList newFelloList = new FelloList(null, felloList.tasks, felloList.parentBoard,
                "blue", "blue", "blue", false);

        ResponseEntity<FelloList> response = felloListController.add(newFelloList);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        // Verify that simpMessagingTemplate.convertAndSend was not called
        verify(simpMessagingTemplate, never()).convertAndSend(anyString(), (Object) any());
    }

    @Test
    public void addTest_InvalidTaskTitle() {
        Board newBoard = new Board("Board", false, " ", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                "blue", "blue", "blue", "blue", "blue", "blue");
        FelloList newList = new FelloList("Title", new ArrayList<>(), newBoard,
                "blue", "blue", "blue", false);
        newBoard.felloLists.add(newList);
        Task badTask = new Task(null, "Task Description", null, newList,
                "blue", "blue", "blue", false);
        newList.tasks.add(badTask);

        ResponseEntity<FelloList> response = felloListController.add(newList);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());

        // Verify that simpMessagingTemplate.convertAndSend was not called
        verify(simpMessagingTemplate, never()).convertAndSend(anyString(), (Object) any());
    }

    @Test
    public void updateTest_ValidList() {
        // Set up the service to return the saved list
        when(felloListService.exists(felloList.id)).thenReturn(true);
        when(felloListService.addList(any(FelloList.class))).thenReturn(felloList);

        // Call the update method
        ResponseEntity<FelloList> response = felloListController.update(felloList.id, felloList);

        // Verify that the service was called with the correct arguments
        verify(felloListService).exists(felloList.id);
        verify(felloListService).addList(felloList);

        // Verify that the message was sent to the correct topic
        verify(simpMessagingTemplate).convertAndSend(eq("/topic/boards/" + felloList.parentBoard.id), eq(felloList.parentBoard));

        // Verify that the response is correct
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(felloList, response.getBody());
    }

    @Test
    public void updateTest_InvalidList() {
        when(felloListService.exists(-1L)).thenReturn(false);

        ResponseEntity<FelloList> response = felloListController.update(-1L, felloList);

        verify(felloListService).exists(-1L);
        verify(simpMessagingTemplate, never()).convertAndSend(anyString(), (Object) any());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void updateTest_InvalidListTitle(){
        when(felloListService.exists(felloList.id)).thenReturn(true);
        felloList.title = "";
        ResponseEntity<FelloList> response = felloListController.update(felloList.id, felloList);
        verify(felloListService).exists(felloList.id);
        verify(simpMessagingTemplate, never()).convertAndSend(anyString(), (Object) any());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateTest_InvalidTaskTitle(){
        when(felloListService.exists(felloList.id)).thenReturn(true);
        felloList.tasks.get(0).title = "";
        ResponseEntity<FelloList> response = felloListController.update(felloList.id, felloList);
        verify(felloListService).exists(felloList.id);
        verify(simpMessagingTemplate, never()).convertAndSend(anyString(), (Object) any());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteTest(){
        when(felloListService.exists(felloList.id)).thenReturn(true);
        when(felloListService.deleteList(felloList.id)).thenReturn(felloList);
        ResponseEntity<FelloList> response = felloListController.deleteById(felloList.id);
        verify(felloListService).exists(felloList.id);
        verify(felloListService).deleteList(felloList.id);

        verify(simpMessagingTemplate).convertAndSend(eq("/topic/boards/" + felloList.parentBoard.id), eq(felloList.parentBoard));

        // Verify that the response is correct
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(felloList, response.getBody());
    }

    @Test
    public void deleteTest_InvalidId(){
        long id = -1L;
        when(felloListService.exists(id)).thenReturn(false);

        ResponseEntity<FelloList> response = felloListController.deleteById(id);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}
