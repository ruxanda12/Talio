package client.scenes.Tasks.CreateTask;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.FelloList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;
import java.io.IOException;

public class CreateTaskCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final CreateTaskModel taskModel;

    @FXML
    private TextField title;

    @FXML
    private TextArea description;

    @Inject
    public CreateTaskCtrl(ServerUtils server, MainCtrl mainCtrl, FelloList parentList) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.taskModel = new CreateTaskModel(parentList);
    }

    public void initialize(FelloList parentList) {
        taskModel.setParentList(parentList);
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard(taskModel.getParentList().parentBoard);
    }

    public void createTask() {
        taskModel.setTitle(title.getText());
        taskModel.setDescription(description.getText());

        if(title.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Title is missing");
            alert.showAndWait();
            this.mainCtrl.showCreateTask(taskModel.getParentList());
            return;
        }

        try {
            mainCtrl.addEnd(taskModel.createTask(), taskModel.getParentList());
            taskModel.setParentList(server.editFelloList(taskModel.getParentList(), taskModel.getParentList().id));
        } catch (WebApplicationException e) {
            CreateTaskView view;
            try {
                view = new CreateTaskView();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            view.showError(e.getMessage());
            return;
        }

        clearFields();
        mainCtrl.showBoard(taskModel.getParentList().parentBoard);
    }

    private void clearFields() {
        title.clear();
        description.clear();
    }
}
