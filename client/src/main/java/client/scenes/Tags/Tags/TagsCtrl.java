package client.scenes.Tags.Tags;

import client.MyFXML;
import client.MyModule;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.Tags;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import static com.google.inject.Guice.createInjector;

public class TagsCtrl {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private ServerUtils server;
    private MainCtrl mainCtrl;
    private TagsModel tagsModel;

    @FXML
    private ColorPicker color;

    @FXML
    private TextField title;

    @Inject
    public TagsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.tagsModel = new TagsModel(new Tags());
    }

    public void initialize(MainCtrl mainCtrl, ServerUtils server, Tags tagEntity){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.tagsModel.tag = tagEntity;

        title.setText(tagsModel.tag.title);
        color.setValue(Color.valueOf(tagsModel.tag.color));
    }

    public void editTag(){
        try{
            tagsModel.tag.title = title.getText();
            tagsModel.tag.color = color.getValue().toString();
            server.editTag(tagsModel.tag);
        }   catch (
        WebApplicationException e) {

            System.out.println("Error somewhere");

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        mainCtrl.showEditTags(tagsModel.tag.parentBoard);
    }

    public void deleteTag() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you sure you want to delete ?");
            alert.setContentText("(This action is irreversible!)");

            if (alert.showAndWait().get() == ButtonType.OK) {
                tagsModel.tag.parentBoard.tags.remove(tagsModel.tag);
                server.deleteTag(tagsModel.tag.id);
                mainCtrl.showEditTags(tagsModel.tag.parentBoard);
            }
        } catch (WebApplicationException e) {
            // Handle any errors from the server
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }

        mainCtrl.showEditTags(tagsModel.tag.parentBoard);
    }
}
