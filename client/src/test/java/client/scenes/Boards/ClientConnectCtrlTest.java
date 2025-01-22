package client.scenes.Boards;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class ClientConnectCtrlTest {

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private ClientConnectCtrl clientConnectCtrl;

    @BeforeEach
    public void setUp() {
        server = mock(ServerUtils.class);
        mainCtrl = mock(MainCtrl.class);
        clientConnectCtrl = new ClientConnectCtrl(server, mainCtrl);
        clientConnectCtrl.serverAddress = Mockito.mock(TextField.class);
    }

//    @Test
//    public void testJoinSuccessful() {
//        when(clientConnectCtrl.serverAddress.getText()).thenReturn("http://localhost:8080");
//        doNothing().when(server).setServer("http://localhost:8080");
//
//        clientConnectCtrl.join();
//
//        // Verify the setServer and showHomeScreen methods are called with the correct arguments
//        verify(server).setServer("http://localhost:8080");
//        verify(mainCtrl).showHomeScreen();
//    }

//    @Test
//    public void testJoinFailed() {
//        when(clientConnectCtrl.serverAddress.getText()).thenReturn("http://invalidurl:8080");
//        doThrow(new RuntimeException("Sever not found")).when(server).setServer("http" +
//                "://invalidurl:8080");
//
//        clientConnectCtrl.join();
//
//        // Verify the setServer and showClientConnect methods are called with the correct arguments
//        verify(server).setServer("http://invalidurl:8080");
//        verify(mainCtrl).showClientConnect();
//
//        when(clientConnectCtrl.serverAddress.getText()).thenReturn("http://invalidurl:8080");
//        doThrow(new RuntimeException("Server not found")).when(server).setServer("http" +
//                "://invalidurl:8080");
//    }
}
