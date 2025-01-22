package client.scenes.Lists;

import client.MyFXML;
import client.MyModule;
import client.scenes.MainCtrl;
import client.scenes.Tasks.TaskCtrl;
import client.utils.ServerUtils;
import com.google.inject.Injector;
import commons.Board;
import commons.FelloList;
import commons.SubTask;
import commons.Task;
import commons.Tags;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.util.ArrayList;
import java.util.List;

import static com.google.inject.Guice.createInjector;

public class ListCtrl {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    @FXML
    private Label title;

    private FelloList felloListEntity;

    @FXML
    private AnchorPane list;

    @FXML
    private Button delete;

    @FXML
    private Button edit;

    private AnchorPane felloListNode;
    @FXML
    private Button addTask;

    private MainCtrl mainCtrl;

    private ServerUtils server;
    @FXML
    private VBox taskList;

    private HBox boardNode;

    private Board parentBoard;

    private List<Node> tasks = new ArrayList<>();


    public void initialize(Board parentBoard, FelloList felloListEntity, AnchorPane felloListNode, HBox boardNode,
                           MainCtrl mainCtrl, ServerUtils server) {
        this.parentBoard = parentBoard;
        this.felloListNode = felloListNode;
        this.felloListEntity = felloListEntity;
        this.boardNode = boardNode;
        this.mainCtrl = mainCtrl;
        this.server = server;

        title.setText(felloListEntity.title);
        customizeList();
        taskList.setAlignment(Pos.TOP_CENTER);
        taskList.getChildren().clear();
        taskList.setSpacing(5);

        mainCtrl.arrange(felloListEntity.tasks);

        for (var task : felloListEntity.tasks) {
            var pair = FXML.load(TaskCtrl.class, "client", "scenes", "Task.fxml");
            var taskNode = new AnchorPane(pair.getValue());
            TaskCtrl taskCtrl = pair.getKey();
            taskCtrl.initialize(felloListEntity, task, taskNode, taskList, boardNode, mainCtrl, server);

            this.felloListNode.getChildren().add(taskNode);
            this.taskList.getChildren().add(taskNode);
            this.tasks.add(taskNode);
        }

        makeDroppable(taskList);
    }

    public void customizeList() {

        if(!felloListEntity.edited) {
            felloListEntity.background = parentBoard.listBackground;
            felloListEntity.font = parentBoard.listFont;
            felloListEntity.border = parentBoard.listBorder;
        }

        title.setTextFill(Color.web(felloListEntity.font));
        taskList.setBackground(new Background(new BackgroundFill(
                Color.web(felloListEntity.background), null, null)));
        list.setBorder(new Border(new BorderStroke(Color.web(felloListEntity.border),
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));

        if(felloListEntity.edited)
            felloListEntity.edited = false;

    }

    public void addTask() {
        this.mainCtrl.showCreateTask(felloListEntity);
    }


    public void deleteFelloList() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you sure you want to delete?");
            alert.setContentText("(This action is irreversible!)");

            if (alert.showAndWait().get() == ButtonType.OK) {
                var felloListRet = server.deleteFelloList(felloListEntity.id);
                mainCtrl.showBoard(felloListRet.parentBoard);
            }
        } catch (WebApplicationException e) {
            // Handle any errors from the server
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

    }
    public void makeDroppable(VBox taskList){
        taskList.setOnDragEntered(event -> {
            taskList.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, CornerRadii.EMPTY, Insets.EMPTY)));
            event.consume();
        });

        taskList.setOnDragExited(event -> {
            taskList.setBackground(new Background(new BackgroundFill(Color.web(felloListEntity.background), CornerRadii.EMPTY, Insets.EMPTY)));
            event.consume();
        });

        taskList.setOnDragOver(event -> {
            if(event.getGestureSource() != taskList && event.getDragboard().hasString()){
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        taskList.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if(db.hasString()){
                updateAfterDrop(db.getString(), event.getY());
                success  = true;
            }
            event.setDropCompleted(success);

            db.clear();
            event.consume();
        });

    }

    public void updateAfterDrop(String id, double yPos){
        Task oldTask = server.getTask(Long.parseLong(id));
        Task newTask = new Task(oldTask.title, oldTask.description, new ArrayList<SubTask>(),
                felloListEntity, oldTask.background, oldTask.font, oldTask.border, oldTask.edited);
        List<Tags> tags = new ArrayList<>();
        tags.addAll(oldTask.tags);
        oldTask.tags.clear();
        List<SubTask> subTasks = new ArrayList<>();
        subTasks.addAll(oldTask.subTasks);


        if(oldTask.parentFelloList.equals(newTask.parentFelloList)){
            felloListEntity.tasks.remove(oldTask);
        }

        server.deleteTask(oldTask.id);
        int position = (int) Math.round(yPos / 200);

        if(position == 0){
            mainCtrl.addFirst(newTask, felloListEntity);
        }

        else if(position >= felloListEntity.tasks.size()){
            mainCtrl.addEnd(newTask, felloListEntity);
        }

        else{
            mainCtrl.addAt(position, newTask, felloListEntity);
        }

        felloListEntity = server.editFelloList(felloListEntity, felloListEntity.id);

        if(position >= felloListEntity.tasks.size()){
            position = felloListEntity.tasks.size() - 1;
        }

        newTask = felloListEntity.tasks.get(position);
        for(SubTask st : subTasks){
            SubTask copy = new SubTask(st.title, newTask, st.done);
            newTask.subTasks.add(copy);
            newTask = server.editTask(newTask, newTask.id);
        }

        for(Tags tag : tags){
            newTask.tags.add(tag);
            newTask = server.editTask(newTask, newTask.id);
        }
    }

    public void editFelloList() {
        this.mainCtrl.showEditFelloList(felloListEntity);
    }
}
