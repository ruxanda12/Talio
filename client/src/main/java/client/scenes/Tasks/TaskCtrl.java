package client.scenes.Tasks;

import client.scenes.MainCtrl;
import client.scenes.Tasks.EditTask.EditTaskModel;
import client.utils.ServerUtils;
import commons.FelloList;
import commons.SubTask;
import commons.Tags;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TaskCtrl {

    public MainCtrl mainCtrl;

    public ServerUtils server;

    private Task taskEntity;

    private FelloList parentFelloList;

    private Node taskNode;

    private VBox listNode;

    private HBox boardNode;

    private EditTaskModel editTaskModel;

    @FXML
    private Label description;
    @FXML
    private Label title;
    @FXML
    private Label progress;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button deleteTask;
    @FXML
    private Pane taskPane;
    @FXML
    private Button details;
    private String style;
    @FXML
    private HBox tagsBox;

    public void initialize(FelloList parentFelloList, Task taskEntity, Node taskNode, VBox listNode, HBox boardNode,
                           MainCtrl mainCtrl, ServerUtils server) {
        this.parentFelloList = parentFelloList;
        this.taskEntity = taskEntity;
        this.taskNode = taskNode;
        this.listNode = listNode;
        this.boardNode = boardNode;
        this.mainCtrl = mainCtrl;
        this.server = server;
        editTaskModel = new EditTaskModel(server, mainCtrl);

        if(taskEntity.description.equals("")){
            description.setText("");
        }

        title.setText(taskEntity.title);
        customizeTask();

        updateProgressBar();

        tagsBox.setAlignment(Pos.TOP_LEFT);
        tagsBox.setSpacing(10);

        for(Tags tag : taskEntity.tags){
            Circle circle = new Circle();
            circle.setRadius(10);
            circle.setFill(Paint.valueOf(tag.color));
            tagsBox.getChildren().add(circle);
        }

        makeDraggableTask(taskNode, listNode);
        makeDraggableList(taskNode, listNode);
        taskShortcuts(taskNode);
    }

    public void customizeTask() {
        if(!taskEntity.edited) {
            for (var palette : taskEntity.parentFelloList.parentBoard.palettes) {
                if (palette.isDefault) {
                    taskEntity.background = palette.background;
                    taskEntity.font = palette.font;
                    taskEntity.border = palette.border;
                }
            }
        }

        this.title.setTextFill(Color.web(taskEntity.font));
        this.taskPane.setBackground(new Background(new BackgroundFill(
                Color.web(taskEntity.background), null, null)));
        Platform.runLater(() -> {       //taskPane was not rendered properly
            this.taskPane.setBorder(new Border(new BorderStroke(Color.web(taskEntity.border),
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1))));
        });

        if(taskEntity.edited)
            taskEntity.edited = false;
    }

    public void updateProgressBar() {
        int completed = 0;
        for(SubTask st : taskEntity.subTasks){
            if(st.done){
                completed++;
            }
        }

        String label = completed + "/" + taskEntity.subTasks.size();
        progress.setText(label);
        progressBar.setProgress((double) completed / (double) taskEntity.subTasks.size());
    }

    public void deleteTask() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you sure you want to delete ?");
            alert.setContentText("(This action is irreversible!)");

            if (alert.showAndWait().get() == ButtonType.OK) {
                var deletedTask = server.deleteTask(taskEntity.id);
                mainCtrl.showBoard(deletedTask.parentFelloList.parentBoard);
            }
        } catch (WebApplicationException e) {
            // Handle any errors from the server
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public void editTask() {
        this.mainCtrl.showEditTask(taskEntity);
    }

    public void highlight(){

        Color color = Color.web(taskEntity.background);
        if(color.getBrightness() < 0.5)
            taskPane.setBackground(new Background(new BackgroundFill(color.deriveColor(
                0, 1, 1.4, 1), CornerRadii.EMPTY, Insets.EMPTY)));
        else
            taskPane.setBackground(new Background(new BackgroundFill(color.deriveColor(
                    0, 1, 0.9, 1), CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void unhighlight(){
        Color color = Color.web(taskEntity.background);
        taskPane.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        taskPane.setBorder(new Border(new BorderStroke(Color.web(taskEntity.border), BorderStrokeStyle.SOLID ,
                CornerRadii.EMPTY, new BorderWidths(1))));
    }

    @FXML
    private void deleteEntered(){
        style = deleteTask.getStyle();
        deleteTask.setStyle("-fx-background-color: red");
    }

    @FXML
    private void deleteExited(){
        deleteTask.setStyle(style);
    }

    @FXML
    private void editEntered(){
        style = details.getStyle();
        details.setStyle("-fx-background-color: lightblue");
    }

    @FXML
    private void editExited(){
        details.setStyle(style);
    }

    public void makeDraggableTask(Node taskNode, VBox listNode){
        taskNode.setOnDragDetected(event -> {
            Dragboard db = taskNode.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent content = new ClipboardContent();
            content.putString(String.valueOf(taskEntity.id));
            db.setContent(content);
            event.consume();
        });

        taskNode.setOnDragOver(event -> {
            if(event.getGestureSource() != taskNode && event.getDragboard().hasString()){
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        taskNode.setOnDragEntered(event -> {
            if(event.getGestureSource() != taskNode && event.getDragboard().hasString()){
                taskNode.getParent().setStyle("-fx-background-color: lightpink");
            }
            event.consume();
        });

        taskNode.setOnDragExited(event -> {
            taskNode.getParent().setStyle("");
            taskNode.setEffect(null);
            event.consume();
        });
    }

    public void makeDraggableList(Node taskNode, VBox listNode){
        listNode.setOnDragOver(event -> {
            if(event.getGestureSource() != listNode && event.getDragboard().hasString()){
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        listNode.setOnDragEntered(event -> {
            if(event.getGestureSource() != listNode && event.getDragboard().hasString()){
                listNode.setStyle("-fx-background-color: lightpink");
            }
            event.consume();
        });

        listNode.setOnDragExited(event -> {
            listNode.setStyle("");
            listNode.setEffect(null);
            event.consume();
        });
    }

    public void editDirect(Node node){
        Stage editTitleStage = new Stage();
        editTitleStage.initModality(Modality.APPLICATION_MODAL);
        editTitleStage.setTitle("Edit Title");

        VBox dialogBox = new VBox(20);
        TextField title = new TextField(taskEntity.title);
        Button saveButton = new Button("save");

        saveButton.setOnAction(event -> {
            try {
                this.editTaskModel.setTaskEntity(taskEntity);
                editTaskModel.updateTask(title.getText(), taskEntity.description);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            editTitleStage.close();
        });

        dialogBox.getChildren().addAll(new Label("Enter new title:"), title, saveButton);
        Scene dialogScene = new Scene(dialogBox, 300, 200);
        editTitleStage.setScene(dialogScene);
        editTitleStage.show();
    }

    public void taskShortcuts(Node node){
        node.setOnMouseEntered(event -> {
            node.requestFocus();
            style = node.getStyle();
            node.setStyle("-fx-background-color: lightblue");
            node.setOnKeyPressed(keyEvent ->{
                if(keyEvent.getCode() == KeyCode.ENTER){
                    editTask();
                    event.consume();
                }
                if(keyEvent.getCode() == KeyCode.DELETE){
                    deleteTask();
                    event.consume();
                }
                if(keyEvent.getCode() == KeyCode.E){
                    editDirect(node);
                    event.consume();
                }
            });
            event.consume();
        });

        node.setOnMouseExited(event -> {
            node.setStyle(style);
            event.consume();
        });

    }
}
