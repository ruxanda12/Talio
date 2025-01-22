package client.scenes.Boards;

import client.scenes.Boards.BoardCtrl.BoardCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BoardCtrlTest {
    private BoardCtrl boardCtrl;
    private MainCtrl mainCtrl;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        ServerUtils serverUtils = mock(ServerUtils.class);
        mainCtrl = mock(MainCtrl.class);

        // Create BoardCtrl with mocked dependencies
        boardCtrl = new BoardCtrl(serverUtils, mainCtrl);
    }

    @Test
    void testAddFelloList() {
        BoardCtrl spyBoardCtrl = Mockito.spy(boardCtrl);

        // Call addFelloList method
        spyBoardCtrl.addFelloList();

        // Verify if showCreateFelloList method is called on mainCtrl with proper argument
        verify(spyBoardCtrl.getMainCtrl(), times(1)).showCreateFelloList(any(Board.class));
    }

    @Test
    void testGoBackHomeScreen() {
        BoardCtrl spyBoardCtrl = Mockito.spy(boardCtrl);

        // Call goBackHomeScreen method
        spyBoardCtrl.goBackHomeScreen();

        // Verify if showHomeScreen method is called on mainCtrl
        verify(spyBoardCtrl.getMainCtrl(), times(1)).showHomeScreen();
    }
}
