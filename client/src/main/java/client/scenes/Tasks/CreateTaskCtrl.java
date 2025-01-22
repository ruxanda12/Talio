package client.scenes.Tasks;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.FelloList;
import commons.SubTask;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import java.util.ArrayList;

public class CreateTaskCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private FelloList parentList;

    @FXML
    private TextField title;

    @FXML
    private TextArea description;

    @Inject
    public CreateTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize(FelloList parentList){
        this.parentList = parentList;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard(parentList.parentBoard);
    }

    public void createTask() {
        try {
            mainCtrl.addEnd(getTask(), parentList);
            parentList = server.editFelloList(parentList, parentList.id);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showBoard(parentList.parentBoard);
    }
    //test
    private Task getTask() {
        var t = title.getText();
        var d = description.getText();
        return new Task(t, d, new ArrayList<SubTask>(), parentList, "blue", "black", "green", false);
    }

    private void clearFields() {
        title.clear();
        description.clear();
    }
}
