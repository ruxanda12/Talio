package client.scenes.Boards;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;

import java.util.List;
import java.util.prefs.Preferences;

public class HomeScreenBoardPartCtrl {
    @FXML
    private Label label;

    @FXML
    private Button button;

    private Board board;

    private MainCtrl mainCtrl;

    private ServerUtils server;

    private boolean isAdmin;

    private List<Board> boards;


    public void initialize(MainCtrl mainCtrl, Board board, ServerUtils server, List<Board> boards) {
        this.board = board;
        this.mainCtrl = mainCtrl;
        this.server=server;
        this.boards = boards;
        this.label.setText(this.board.title);
        Preferences pref = Preferences.userRoot().node("board");
        this.isAdmin = pref.getBoolean("admin", false);
        if(isAdmin){
            button.setText("Del");
        }
    }

    public void logInBoard() {
        this.mainCtrl.showBoard(board);
    }

    public void editBoard(){this.mainCtrl.showEditBoard(board);}


    public void leaveBoard() {
        if(isAdmin) {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete");
                alert.setHeaderText("Are you sure you want to delete?");
                alert.setContentText("(This action is irreversible!)");

                if (alert.showAndWait().get() == ButtonType.OK) {
                    var felloListRet = server.deleteBoard(board.id);
                    mainCtrl.showHomeScreen();
                }

            } catch (WebApplicationException e) {
                // Handle any errors from the server
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }

        else {
            boards.remove(board);
            savePreferences();
            mainCtrl.showHomeScreen();
        }
    }

    private void savePreferences() {
        Preferences pref = Preferences.userRoot().node("board");
        pref.putInt("size", boards.size());
        for(int i = 0; i < boards.size(); i++){
            String key = "board" + i;
            pref.putLong(key, boards.get(i).id);
        }
    }
}
