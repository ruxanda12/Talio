package client.scenes.Boards;

import client.MyFXML;
import client.MyModule;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.Board;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

import static com.google.inject.Guice.createInjector;

public class HomeScreenCtrl {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    private ServerUtils server;

    private MainCtrl mainCtrl;

    @FXML
    private VBox boardList;

    @FXML
    private TextField boardKey;

    @FXML
    private Button connect;

    private List<Board> serverBoards;

    private List<Board> boards;

    @Inject
    public HomeScreenCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.server = serverUtils;
    }

    public void initialize() {
        Preferences pref = Preferences.userRoot().node("board");
        serverBoards = this.server.getBoards();
        if(serverBoards.size() == 0){
            pref.putInt("size", 0);
        }
        boards = new ArrayList<>();

        if(pref.getBoolean("admin", false)){
            boards = serverBoards;
        }

        else{
            int size = pref.getInt("size", -1);
            if(size == -1){
                size = 0;
            }

            for(int i = 0; i < size; i++){
                String key = "board" + i;
                boards.add(server.getBoard(pref.getLong(key, 0)));
            }
        }

        boardList.getChildren().removeAll(boardList.getChildren());

        for (var board : boards) {
            render(board);
        }

        server.registerForUpdates(b ->{
            Platform.runLater(() -> {
                serverBoards.add(b);
                if(pref.getBoolean("admin", false)){
                    render(b);
                }
            });
        });
    }

    public void render(Board board){
        var pair = FXML.load(HomeScreenBoardPartCtrl.class, "client", "scenes", "HomeScreenBoardPart.fxml");

        HomeScreenBoardPartCtrl homeScreenBoardPartCtrl = pair.getKey();
        homeScreenBoardPartCtrl.initialize(this.mainCtrl, board, server, boards);
        boardList.getChildren().add(pair.getValue());
    }

    public void joinBoard() {
        for(Board board : serverBoards){
            if(board.key.equals(boardKey.getText())){
                if(!boards.contains(board)){
                    boards.add(board);
                    savePreferences();
                }
                mainCtrl.showBoard(board);
                return;
            }
        }

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText("Key not found");
        alert.showAndWait();
    }

    private void savePreferences() {
        Preferences pref = Preferences.userRoot().node("board");
        pref.putInt("size", boards.size());
        for(int i = 0; i < boards.size(); i++){
            String key = "board" + i;
            pref.putLong(key, boards.get(i).id);
        }
    }


    public void newBoard() {
        this.mainCtrl.showCreateBoard();
    }

    public void exit() {
        this.mainCtrl.showClientConnect();
    }

    public void stop(){
        server.stop();
    }

}
