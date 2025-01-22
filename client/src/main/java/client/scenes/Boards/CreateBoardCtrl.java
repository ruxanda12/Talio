package client.scenes.Boards;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.FelloList;
import commons.Palette;
import commons.Tags;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.prefs.Preferences;

public class CreateBoardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;

    @Inject
    public CreateBoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize() {
        title.getAlignment();
    }

    public void cancel() {
        clearFields();
        mainCtrl.showHomeScreen();
    }

    public void createBoard() {
        try {
            Board board = server.addBoard(getBoard());
            Preferences pref = Preferences.userRoot().node("board");

            int size=pref.getInt("size", -1);
            if(size == -1){
                size = 0;
            }
            String key= "board" + size;
            pref.putLong(key, board.id);
            size++;
            pref.putInt("size", size);

            board.palettes.add(new Palette(board, "Default", true,
                    "#f5f5f5", "black", "black"));
            board = server.editBoard(board);
            mainCtrl.showBoard(board);


        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
    }

    private Board getBoard() {
        var t = title.getText();
        return new Board(t, false, "", new ArrayList<FelloList>(), new ArrayList<Tags>(),
                new ArrayList<Palette>(), "#99cccc", "black", "#808080",
                "#008080", "black", "#808080");
    }

    private void clearFields() {
        title.clear();
    }
}
