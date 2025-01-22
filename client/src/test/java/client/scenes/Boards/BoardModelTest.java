package client.scenes.Boards;

import client.scenes.Boards.BoardCtrl.BoardModel;
import commons.Board;
import commons.FelloList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardModelTest {
    private BoardModel boardModel;
    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        boardModel = new BoardModel(board);
    }

    @Test
    void testGetBoardEntity() {
        assertEquals(board, boardModel.getBoardEntity(), "Should return the same board entity");
    }

    @Test
    void testSetBoardEntity() {
        Board newBoard = new Board();
        boardModel.setBoardEntity(newBoard);
        assertEquals(newBoard, boardModel.getBoardEntity(), "Should return the new board entity");
    }

    @Test
    void testRemoveFelloList() {
        FelloList list1 = new FelloList();
        FelloList list2 = new FelloList();
        board.felloLists = new ArrayList<>();
        board.felloLists.add(list1);
        board.felloLists.add(list2);

        boardModel.removeFelloList(0);

        assertEquals(boardModel.getBoardEntity().felloLists.size(),1, "These should only be one " +
                "list");
        assertTrue(boardModel.getBoardEntity().felloLists.contains(list2), "List2 should not be removed");
    }

    @Test
    void testRemoveFelloListWithInvalidIndex() {
        FelloList list1 = new FelloList();
        FelloList list2 = new FelloList();
        board.felloLists = new ArrayList<>();
        board.felloLists.add(list1);
        board.felloLists.add(list2);

        assertDoesNotThrow(() -> boardModel.removeFelloList(-1), "Should not throw an exception");
        assertDoesNotThrow(() -> boardModel.removeFelloList(2), "Should not throw an exception");

        assertTrue(boardModel.getBoardEntity().felloLists.contains(list1), "List1 should not be removed");
        assertTrue(boardModel.getBoardEntity().felloLists.contains(list2), "List2 should not be removed");
    }
}
