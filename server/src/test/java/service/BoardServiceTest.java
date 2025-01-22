package service;

import commons.Board;
import commons.FelloList;
import commons.Palette;
import commons.Tags;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.BoardRepository;
import server.service.BoardService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    private BoardService boardService;

    private Board board1;
    private Board board2;
    private FelloList felloList;
    private FelloList felloList2;

    private Tags tag1;
    private Tags tag2;
    private Palette palette1;
    private Palette palette2;
    private List<FelloList> felloLists;
    private List<FelloList> felloLists2;
    private List<Tags> tags1;
    private List<Tags> tags2;

    private List<Palette> palettes1;
    private List<Palette> palettes2;
    private List<Board> boards;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        felloLists = new ArrayList<>();
        felloLists.add(felloList);
        felloLists2 = new ArrayList<>();
        felloLists2.add(felloList2);
        tags1 = new ArrayList<>();
        tags1.add(tag1);
        tags2 = new ArrayList<>();
        tags2.add(tag2);
        palettes1 = new ArrayList<>();
        palettes1.add(palette1);
        palettes2 = new ArrayList<>();
        palettes2.add(palette2);
        board1 = new Board("title", false, null, felloLists, tags1, palettes1, "blue",
                "black", "red", "blue", "black", "red");
        board2 = new Board("titles", true, "password", felloLists2, tags2, palettes2, "blue",
                "black", "red", "blue", "black", "red");
        board1.id = 1;
        board2.id = 2;
        boards = new ArrayList<>();
        boards.add(board1);
        boards.add(board2);
    }

    @Test
    public void getBoardsTest(){
        when(boardRepository.findAll()).thenReturn(boards);
        List<Board> res = boardService.getBoards();
        assertEquals(2, res.size());
        assertThat("2 boards exist in the database", res.contains(board1) && res.contains(board2));
    }

    @Test
    public void boardExists(){
        when(boardRepository.existsById(1L)).thenReturn(true);
        assertThat("board1 exists in database", boardService.exists(board1.id));
        assertThat("board2 does not exist in database", !boardService.exists(board2.id));
    }

    @Test
    public void getBoardByIdTest(){
        when(boardRepository.findById(board1.id)).thenReturn(Optional.ofNullable(board1));
        Board res = boardService.getBoardById(1L);
        assertThat("Correct board was returned", res.id == board1.id);
    }

    @Test
    public void addBoardTest(){
        when(boardRepository.save(board1)).thenReturn(board1);
        Board res = boardService.addBoard(board1);
        assertThat("Board was saved", res.equals(board1));
    }

    @Test
    public void deleteBoardTest(){
        boardService.deleteBoard(1L);
        verify(boardRepository, times(1)).deleteById(1L);
    }
}
