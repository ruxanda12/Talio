package client.scenes.Boards;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class EditBoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;

    private Board boardEntity;

    @Inject
    public EditBoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize(Board boardEntity) {
        this.boardEntity=boardEntity;
        title.setText(boardEntity.getTitle());
    }

    public void editBoard(){
        try {
            boardEntity.title = title.getText();
            boardEntity = server.editBoard(boardEntity);
            title.clear();
            mainCtrl.showHomeScreen();
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void cancel() {
        title.clear();
        mainCtrl.showHomeScreen();
    }
}
