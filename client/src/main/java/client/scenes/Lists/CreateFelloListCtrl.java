package client.scenes.Lists;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.FelloList;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;


import java.util.ArrayList;

public class CreateFelloListCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private Board parentBoard;

    @FXML
    private TextField title;

    @Inject
    public CreateFelloListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize(Board parentBoard) {
        this.parentBoard = parentBoard;
        title.setText("TO DO");
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard(parentBoard);
    }

    public void createFelloList() {

//        try {
        parentBoard.felloLists.add(getFelloList());
        Board updatedBoard = server.editBoard(parentBoard);
//        } catch (Exception e) {
//            System.out.println("errere");
//            var alert = new Alert(Alert.AlertType.ERROR);
//            alert.initModality(Modality.APPLICATION_MODAL);
//            alert.setContentText(e.getMessage());
//            alert.showAndWait();
//        }
        clearFields();
        mainCtrl.showBoard(updatedBoard);
    }

    private FelloList getFelloList() {
        var t = title.getText();
        return new FelloList(t, new ArrayList<Task>(), parentBoard,
                parentBoard.listBackground, parentBoard.listFont, parentBoard.listBorder, false);
    }

    private void clearFields() {
        title.clear();
    }
}
