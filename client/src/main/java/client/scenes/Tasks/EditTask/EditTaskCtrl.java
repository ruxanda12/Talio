package client.scenes.Tasks.EditTask;

import client.utils.ServerUtils;
import client.scenes.MainCtrl;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.io.IOException;

public class EditTaskCtrl {
    private EditTaskModel editTaskModel;
    private EditTaskView editTaskView;

    @FXML
    private TextField title;

    @FXML
    private TextArea description;

    @FXML
    private VBox subtasksList;

    @FXML
    private VBox tagsList;

    @Inject
    public EditTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        editTaskModel = new EditTaskModel(server, mainCtrl);
        editTaskView = new EditTaskView();
    }

    public void initialize(Task taskEntity) {
        editTaskModel.setTaskEntity(taskEntity);
        title.setText(taskEntity.title);
        description.setText(taskEntity.description);
        editTaskView.initialize(tagsList, subtasksList, editTaskModel.getServer(), editTaskModel.getMainCtrl(), editTaskModel);
    }

    public void cancel() {
        editTaskView.clearFields(title, description);
        editTaskModel.cancel();
    }

    public void submit() {
        try {
            editTaskModel.updateTask(title.getText(), description.getText());
        } catch (IOException e) {
            editTaskView.showError(e.getMessage());
            return;
        }

        editTaskView.clearFields(title, description);
        editTaskModel.getMainCtrl().showBoard(editTaskModel.getTaskEntity().parentFelloList.parentBoard);
    }

    public void addSubTask() {
        editTaskView.addSubTask(editTaskModel.getServer(), editTaskModel.getMainCtrl(), editTaskModel);
    }

    // Add methods to call view methods from the controller
    public void toggleDone() {
        editTaskView.toggleDone();
    }

    public void deleteSubTask() {
        editTaskView.deleteSubTask();
    }

    public void backgroundTask() {
        editTaskView.backgroundTask();
    }

    public void resetBackground() {
        editTaskView.resetBackground();
    }

    public void resetFont() {
        editTaskView.resetFont();
    }

    public void resetBorder() {
        editTaskView.resetBorder();
    }


}
