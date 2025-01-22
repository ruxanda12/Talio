package api;

import commons.Board;
import commons.FelloList;
import commons.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.BoardController;
import server.service.BoardService;
import server.service.FelloListService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BoardControllerTest {

    @InjectMocks
    private BoardController boardController;

    @Mock
    private BoardService boardService;

    @Mock
    private FelloListService felloListService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    private Board testBoard;

    @BeforeEach
    void setUp() {
        testBoard = new Board();
        testBoard.id = 1L;
        testBoard.title = "Test Board";
        testBoard.felloLists = new ArrayList<>();
        testBoard.tags = new ArrayList<>();
    }

    @Test
    public void testGetAll() {
        when(boardService.getBoards()).thenReturn(Collections.singletonList(testBoard));
        List<Board> boards = boardController.getAll();
        assertEquals(1, boards.size());
        assertEquals(testBoard, boards.get(0));
    }

    @Test
    public void testGetById() {
        when(boardService.exists(1L)).thenReturn(true);
        when(boardService.getBoardById(1L)).thenReturn(testBoard);
        ResponseEntity<Board> response = boardController.getById(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(testBoard, response.getBody());
    }

    @Test
    public void testGetByIdNotFound() {
        when(boardService.exists(1L)).thenReturn(false);
        ResponseEntity<Board> response = boardController.getById(1L);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }

    @Test
    public void testAdd() {
        Board newBoard = new Board();
        newBoard.title = "New Board";
        newBoard.felloLists = new ArrayList<>();

        when(boardService.addBoard(newBoard)).thenReturn(newBoard);

        ResponseEntity<Board> response = boardController.add(newBoard);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(newBoard, response.getBody());
    }

    @Test
    public void testAddInvalidTitle() {
        Board newBoard = new Board();
        newBoard.title = "";
        newBoard.felloLists = new ArrayList<>();

        ResponseEntity<Board> response = boardController.add(newBoard);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteById() {
        when(boardService.exists(1L)).thenReturn(true);
        when(boardService.getBoardById(1L)).thenReturn(testBoard);
        doNothing().when(boardService).deleteBoard(1L);

        ResponseEntity<Board> response = boardController.deleteById(1L);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(testBoard, response.getBody());
    }

    @Test
    public void testDeleteByIdNotFound() {
        when(boardService.exists(1L)).thenReturn(false);

        ResponseEntity<Board> response = boardController.deleteById(1L);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdate() {
        FelloList newFelloList = new FelloList();
        newFelloList.title = "New FelloList";
        newFelloList.tasks = new ArrayList<>();
        newFelloList.parentBoard = testBoard;

        when(boardService.exists(1L)).thenReturn(true);
        when(boardService.addBoard(testBoard)).thenReturn(testBoard);

        ResponseEntity<Board> response = boardController.update(testBoard.id, testBoard);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(testBoard, response.getBody());
    }

    @Test
    public void testUpdateInvalidTitle() {
        FelloList newFelloList = new FelloList();
        newFelloList.title = "";
        newFelloList.tasks = new ArrayList<>();
        newFelloList.parentBoard = testBoard;

//        when(boardService.exists(1L)).thenReturn(true);
//        when(boardService.getBoardById(1L)).thenReturn(testBoard);

        ResponseEntity<Board> response = boardController.update(testBoard.id, testBoard);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateNotFound() {
        FelloList newFelloList = new FelloList();
        newFelloList.title = "New FelloList";
        newFelloList.tasks = new ArrayList<>();
        newFelloList.parentBoard = testBoard;

        when(boardService.exists(1L)).thenReturn(false);

        ResponseEntity<Board> response = boardController.update(testBoard.id, testBoard);
        assertTrue(response.getStatusCode().is4xxClientError());
        assertNull(response.getBody());
    }


}
