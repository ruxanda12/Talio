package client.scenes.Tags.EditTags;

import client.MyFXML;
import client.MyModule;
import client.scenes.MainCtrl;
import client.scenes.Tags.Tags.TagsCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.Board;
import commons.Tags;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;

import java.awt.*;

import static com.google.inject.Guice.createInjector;

public class EditTagsCtrl {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private EditTagsModel editTagsModel;
    @FXML
    private VBox tags;
    @FXML
    private Button addTag;
    @FXML
    private Label title;
    @FXML
    private AnchorPane window;

    @Inject
    public EditTagsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.editTagsModel = new EditTagsModel(new Board());
    }

    public void initialize(Board boardEntity) {
        this.tags.getChildren().clear();
        this.editTagsModel.boardEntity = boardEntity;
        for (var tag : boardEntity.tags) {
            var pair = FXML.load(TagsCtrl.class, "client", "scenes", "Tags.fxml");
            var currTag = new AnchorPane(pair.getValue());
            TagsCtrl tagsCtrl = pair.getKey();
            tagsCtrl.initialize(mainCtrl, server, tag);
            this.tags.getChildren().add(currTag);
        }
        exitWindow(window);
    }

    public void cancel(){
        mainCtrl.showBoard(editTagsModel.boardEntity);
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

    public void addTag(){
        try {
            Tags emptyTag = new Tags("WHITE", "", editTagsModel.boardEntity);
            editTagsModel.boardEntity.tags.add(emptyTag);
            editTagsModel.boardEntity = server.editBoard(editTagsModel.boardEntity);

        }   catch (WebApplicationException e) {

            System.out.println("Error somewhere");

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        mainCtrl.showEditTags(editTagsModel.boardEntity);
    }
}
