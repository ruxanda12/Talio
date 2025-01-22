package client.scenes.Boards.BoardCtrl;

import client.MyFXML;
import client.MyModule;
import client.scenes.Lists.ListCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import static com.google.inject.Guice.createInjector;

public class BoardCtrl {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    @FXML
    private HBox board;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button add;

    @FXML
    private Label title;

    @FXML
    private Label key;

    private ServerUtils server;

    private MainCtrl mainCtrl;
    private BoardModel boardModel;

    //TEST
    @Inject
    public BoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.boardModel = new BoardModel(new Board());
    }

    public void initialize(Board boardEntity) {

        this.board.getChildren().clear();
        this.board.setSpacing(3);
        this.boardModel.setBoardEntity(boardEntity);
        this.key.setText("Key: " + boardEntity.key);
        for (var list : boardEntity.felloLists) {
            var pair = FXML.load(ListCtrl.class, "client", "scenes", "List.fxml");
            var currList = new AnchorPane(pair.getValue());
            ListCtrl listCtrl = pair.getKey();
            listCtrl.initialize(boardEntity, list, currList, board, mainCtrl, server);
            this.board.getChildren().add(currList);
        }

        this.title.setText(boardEntity.title);
        this.title.setTextFill(Color.web(boardEntity.font));
        this.board.setBackground(new Background(new BackgroundFill(
                Color.web(boardEntity.background), null, null)));
        this.scrollPane.setStyle(" -fx-border-color: " + boardEntity.border + "; -fx-border-width: 2;");
        key.setTextFill(Color.BLACK);
        boardShortcuts(board);
    }

    public void addFelloList() {
        this.mainCtrl.showCreateFelloList(boardModel.getBoardEntity());
    }

    public void disconnectClient() {
        mainCtrl.showClientConnect();  //  Enough ?
    }

    public void goBackHomeScreen() {
        mainCtrl.showHomeScreen();
    }

    public void goCustomization() {
        mainCtrl.showCustomization(boardModel.getBoardEntity());
    }

    public void showTags(){
        mainCtrl.showEditTags(boardModel.getBoardEntity());
    }

    //TEST
    public void boardUpdate() {
        String topic = "/topic/boards/" + boardModel.getBoardEntity().id;
        this.server.registerForMessages(topic, Board.class, board -> {
            Platform.runLater(() -> {
                initialize(board);
            });
        });
    }

    public void copyKey(){
        StringSelection selection = new StringSelection(boardModel.getBoardEntity().key);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        key.setTextFill(Color.GREEN);
    }

    //TEST
    public void removeFelloList(int index) {
        if (index >= 0 && index < boardModel.getBoardEntity().felloLists.size()) {
            boardModel.getBoardEntity().felloLists.remove(index);
            // Update the view by calling the initialize method
            initialize(boardModel.getBoardEntity());
        }
    }

    public void displayHelp(){
        Stage helpStage = new Stage();
        helpStage.initModality(Modality.WINDOW_MODAL);
        helpStage.setTitle("Shortcut Help");

        VBox textBox = new VBox(20);
        TextArea text = new TextArea();
        text.setText("Hello!\n" +
                "With your mouse, hover over any task to highlight it and activate shortcuts.\n" +
                "The available shortcuts are:\n" +
                "ENTER to open EditTask screen, ESC to close.\n" +
                "DELETE to delete highlighted task.\n" +
                "E to directly change task's title.\n" +
                "T to enter Tags screen, ESC to escape.\n" +
                "C to enter customization screen, esc to escape.");

        textBox.getChildren().addAll(text);
        Scene helpScene = new Scene(textBox, 420, 170);
        helpStage.setScene(helpScene);
        helpStage.show();
    }
    public void boardShortcuts(HBox board){
        board.requestFocus();
        board.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.SLASH){
                displayHelp();
                event.consume();
            }
            if(event.getCode() == KeyCode.T){
                showTags();
                event.consume();
            }
            if(event.getCode() == KeyCode.C){
                goCustomization();
                event.consume();
            }
            event.consume();
        });
    }

    public HBox getBoard() {
        return board;
    }

    public Button getAdd() {
        return add;
    }

    public Label getTitle() {
        return title;
    }

    public ServerUtils getServer() {
        return server;
    }

    //test
    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public Board getBoardEntity() {
        return boardModel.getBoardEntity();
    }
}
