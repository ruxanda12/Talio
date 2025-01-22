package client.scenes.Tasks;

import client.MyFXML;
import client.MyModule;
import client.scenes.MainCtrl;
import client.scenes.Subtask.SubTaskCtrl;
import client.scenes.Tags.Tag.TagCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.SubTask;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class EditTaskCtrl {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private Task taskEntity;
    private   TaskCtrl taskCtrl;

    private List<Node> tags = new ArrayList<>();

    @FXML
    private TextField title;

    @FXML
    private TextArea description;
    @FXML
    private VBox subtasksList;
    @FXML
    private VBox tagsList;

    private List<Node> subtasks = new ArrayList<>();

    @FXML
    private ColorPicker backc;

    @FXML
    private ColorPicker fontc;

    @FXML
    private ColorPicker borderc;

    @FXML
    private AnchorPane window;


    /**
     * Creates a new instance of the EditTaskCtrl class.
     *
     * @param server   The ServerUtils instance used to communicate with the server.
     * @param mainCtrl The MainCtrl instance used to switch between scenes.
     */
    @Inject
    public EditTaskCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize(Task taskEntity, TaskCtrl taskCtrl){
        this.taskEntity = taskEntity;
        title.setText(taskEntity.title);
        description.setText(taskEntity.description);
        this.taskCtrl = taskCtrl;

        this.subtasksList.getChildren().clear();
        this.tagsList.getChildren().clear();

        for(var subtask: taskEntity.subTasks) {
            var pair = FXML.load(SubTaskCtrl.class, "client", "scenes", "SubTask.fxml");
            var currSubtask = new AnchorPane(pair.getValue());
            SubTaskCtrl subTaskCtrl = pair.getKey();
            subTaskCtrl.initialize(mainCtrl, server, subtask, taskEntity);

            subtasksList.getChildren().add(currSubtask);
            subtasks.add(currSubtask);
        }

        for(var tag : taskEntity.parentFelloList.parentBoard.tags){
            var pair = FXML.load(TagCtrl.class, "client", "scenes", "Tag.fxml");
            var currTag = new AnchorPane(pair.getValue());
            TagCtrl tagCtrl = pair.getKey();
            tagCtrl.initialize(mainCtrl, server, tag, taskEntity);

            tagsList.getChildren().add(currTag);
            tags.add(currTag);
        }

        backc.setValue(Color.web(taskEntity.background));
        fontc.setValue(Color.web(taskEntity.font));
        borderc.setValue(Color.web(taskEntity.border));
        exitWindow(window);
    }

    public void cancel() {
        clearFields();
        mainCtrl.showBoard(taskEntity.parentFelloList.parentBoard);
    }

    public void exitWindow(AnchorPane window){
        window.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                cancel();
                event.consume();
            }
            event.consume();
        });
    }


    /**
     * Updates the task with the new information entered by the user.
     *
     * @throws WebApplicationException if an error occurs while communicating with the server.
     */
    public void submit() {
        try {
            updateTask();
            server.editTask(taskEntity, taskEntity.id);

            // Clear the input fields and show the task overview
            clearFields();
            mainCtrl.showBoard(taskEntity.parentFelloList.parentBoard);

        } catch (WebApplicationException e) {
            // Handle any errors from the server
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
    }

    private void clearFields() {
        title.clear();
        description.clear();
    }

    private void updateTask() {
        taskEntity.title = title.getText();
        taskEntity.description = description.getText();
    }


    public void addSubTask() {
        try {
            SubTask emptyTask = new SubTask("", taskEntity, false);
            taskEntity.subTasks.add(emptyTask);
            taskEntity = server.editTask(taskEntity, taskEntity.id);

        }   catch (WebApplicationException e) {

            System.out.println("Error somewhere");

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        mainCtrl.showEditTask(taskEntity);
    }


    public void toggleDone () {
        return;
    }

    public void deleteSubTask() {
        return;
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Color pickers//

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void backgroundTask() {
        Color color = backc.getValue();
        this.taskEntity.background = color.toString();
        this.taskEntity.edited = true;
    }

    public void fontTask() {
        Color color = fontc.getValue();
        this.taskEntity.font = color.toString();
        this.taskEntity.edited = true;
    }

    public void borderTask() {
        Color color = borderc.getValue();
        this.taskEntity.border = color.toString();
        this.taskEntity.edited = true;
    }

    public void resetBackground(){
        for(int i = 0; i<taskEntity.parentFelloList.parentBoard.palettes.size(); i++) {
            if(taskEntity.parentFelloList.parentBoard.palettes.get(i).isDefault)
                taskEntity.background = taskEntity.parentFelloList.parentBoard.palettes.get(i).background;
        }
        backc.setValue(Color.web(taskEntity.background));
    }

    public void resetFont(){
        for(int i = 0; i<taskEntity.parentFelloList.parentBoard.palettes.size(); i++) {
            if(taskEntity.parentFelloList.parentBoard.palettes.get(i).isDefault)
                taskEntity.font = taskEntity.parentFelloList.parentBoard.palettes.get(i).font;
        }
        fontc.setValue(Color.web(taskEntity.font));
    }

    public void resetBorder(){
        for(int i = 0; i<taskEntity.parentFelloList.parentBoard.palettes.size(); i++) {
            if(taskEntity.parentFelloList.parentBoard.palettes.get(i).isDefault)
                taskEntity.border = taskEntity.parentFelloList.parentBoard.palettes.get(i).border;
        }
        borderc.setValue(Color.web(taskEntity.border));
    }
}
