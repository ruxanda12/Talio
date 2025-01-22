package client.scenes.Subtask;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.SubTask;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;


public class SubTaskCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils server;
    private SubTask subTaskEntity;
    private Task parentTask;

    @FXML
    private TextField title;

    @FXML
    private CheckBox done;




    @Inject
    public SubTaskCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize(MainCtrl mainCtrl, ServerUtils server, SubTask subTaskEntity,
                           Task parentTask) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.subTaskEntity = subTaskEntity;
        this.parentTask = parentTask;


        if(subTaskEntity.done){
            done.setSelected(true);
        }
        title.setText(subTaskEntity.title);
        done.setSelected(done.isSelected());
    }

    public void editSubTask(){
        try {
            subTaskEntity.title = title.getText();
            server.editSubtask(subTaskEntity);
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        mainCtrl.showEditTask(parentTask);
    }

    public void toggleDone(){
        if(done.isSelected()){
            subTaskEntity.done = true;
            server.editSubtask(subTaskEntity);
        } else {
            subTaskEntity.done = false;
            server.editSubtask(subTaskEntity);
        }

    }

    public void deleteSubTask() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you sure you want to delete ?");
            alert.setContentText("(This action is irreversible!)");

            if (alert.showAndWait().get() == ButtonType.OK) {
                parentTask.subTasks.remove(subTaskEntity);
                server.deleteSubTask(subTaskEntity.id);
                mainCtrl.showEditTask(parentTask);
            }
        } catch (WebApplicationException e) {
            // Handle any errors from the server
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        mainCtrl.showEditTask(parentTask);
    }
}
