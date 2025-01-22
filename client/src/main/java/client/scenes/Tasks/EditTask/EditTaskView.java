package client.scenes.Tasks.EditTask;

import client.MyFXML;
import client.MyModule;
import client.scenes.MainCtrl;
import client.scenes.Subtask.SubTaskCtrl;
import client.scenes.Tags.Tag.TagCtrl;
import client.scenes.Tasks.TaskCtrl;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.SubTask;
import commons.Task;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class EditTaskView {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private List<Node> subtasks = new ArrayList<>();
    private List<Node> tags = new ArrayList<>();
    private TaskCtrl taskCtrl;
    private ColorPicker backc;

    /**
     * Initializes the subtasks list in the edit task view based on the given parameters. Clears
     * the subtasks list and loads each subtask from the edit task model's task entity. Creates a
     * new subtask controller for each subtask and adds it to the subtasks list.
     * @param subtasksList the VBox that will contain the subtasks
     * @param server the ServerUtils object used for server communication
     * @param mainCtrl the MainCtrl object controlling the main view
     * @param editTaskModel the EditTaskModel object containing the task entity to be edited
     */

    public void initialize(VBox tagsList, VBox subtasksList, ServerUtils server, MainCtrl mainCtrl, EditTaskModel editTaskModel) {
        subtasksList.getChildren().clear();
        tagsList.getChildren().clear();

        for (var subtask : editTaskModel.getTaskEntity().subTasks) {
            var pair = FXML.load(SubTaskCtrl.class, "client", "scenes", "SubTask.fxml");
            var currSubtask = new AnchorPane(pair.getValue());
            SubTaskCtrl subTaskCtrl = pair.getKey();
            subTaskCtrl.initialize(mainCtrl, server, subtask, editTaskModel.getTaskEntity());

            subtasksList.getChildren().add(currSubtask);
            subtasks.add(currSubtask);
        }

        for(var tag : editTaskModel.getTaskEntity().parentFelloList.parentBoard.tags){
            var pair = FXML.load(TagCtrl.class, "client", "scenes", "Tag.fxml");
            var currTag = new AnchorPane(pair.getValue());
            TagCtrl tagCtrl = pair.getKey();
            tagCtrl.initialize(mainCtrl, server, tag, editTaskModel.getTaskEntity());

            tagsList.getChildren().add(currTag);
            tags.add(currTag);
        }
    }

    public void clearFields(TextField title, TextArea description) {
        title.clear();
        description.clear();
    }

    public void showError(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }


    /**
     * Adds a new subtask to the task entity in the edit task model and updates the task on the
     * server. The new subtask is added with an empty title, a done status of false, and
     * associated with the task in the edit task model. After the task is updated, the edit task
     * view is displayed with the updated task.
     *
     * @param server the ServerUtils object used for server communication
     * @param mainCtrl the MainCtrl object controlling the main view
     * @param editTaskModel the EditTaskModel object containing the task entity to be edited
     */

    public void addSubTask(ServerUtils server, MainCtrl mainCtrl, EditTaskModel editTaskModel) {
        SubTask emptyTask = new SubTask("", editTaskModel.getTaskEntity(), false);
        editTaskModel.getTaskEntity().subTasks.add(emptyTask);
        Task updatedTask = server.editTask(editTaskModel.getTaskEntity(), editTaskModel.getTaskEntity().id);
        editTaskModel.updateTaskEntity(updatedTask);

        mainCtrl.showEditTask(editTaskModel.getTaskEntity());
    }

    public void toggleDone() {
        // TODO
    }

    public void deleteSubTask() {
        // TODO

    }

    public void backgroundTask() {
        Color color = backc.getValue();

    }

    public void resetBackground() {
        // TODO
    }

    public void resetFont() {
        // TODO
    }

    public void resetBorder() {
        // TODO
    }
}
