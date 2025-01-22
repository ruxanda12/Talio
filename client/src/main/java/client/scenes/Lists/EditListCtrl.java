package client.scenes.Lists;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.FelloList;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Modality;



public class EditListCtrl {

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private FelloList felloListEntity;

    private ListCtrl listCtrl;

    @FXML
    private ColorPicker  backColor;

    @FXML
    private ColorPicker borderColor;

    @FXML
    private ColorPicker fontColor;

    @FXML
    private TextField title;



    /**
     * Creates a new instance of the EditListCtrl class.
     *
     * @param server   The ServerUtils instance used to communicate with the server.
     * @param mainCtrl The MainCtrl instance used to switch between scenes.
     */
    @Inject
    public EditListCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }


    public void initialize(FelloList felloListEntity, ListCtrl listCtrl) {
        this.felloListEntity = felloListEntity;
        title.setText(felloListEntity.title);
        this.listCtrl = listCtrl;

        backColor.setValue(Color.web(felloListEntity.background));
        fontColor.setValue(Color.web(felloListEntity.font));
        borderColor.setValue(Color.web(felloListEntity.border));
    }

    public void cancel() {
        title.clear();
        mainCtrl.showBoard(felloListEntity.parentBoard);
    }


    /**
     * Updates the list with the new information entered by the user.
     *
     * @throws WebApplicationException if an error occurs while communicating with the server.
     */
    public void editFelloList() {
        try {
            felloListEntity.title = title.getText();
            felloListEntity = server.editFelloList(felloListEntity, felloListEntity.id);
            title.clear();
            mainCtrl.showBoard(felloListEntity.parentBoard);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Color pickers//

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////



    public void backgroundList() {
        Color color = backColor.getValue();
        this.felloListEntity.background = color.toString();
        this.felloListEntity.edited = true;
    }

    public void fontList() {
        Color color = fontColor.getValue();
        this.felloListEntity.font = color.toString();
        this.felloListEntity.edited = true;
    }

    public void borderList() {
        Color color = borderColor.getValue();
        this.felloListEntity.border = color.toString();
        this.felloListEntity.edited = true;

    }

    public void resetBackground(){
        felloListEntity.background = felloListEntity.parentBoard.listBackground;
        backColor.setValue(Color.web(felloListEntity.background));
    }

    public void resetFont(){
        felloListEntity.font = felloListEntity.parentBoard.listFont;
        fontColor.setValue(Color.web(felloListEntity.font));
    }

    public void resetBorder(){
        felloListEntity.border = felloListEntity.parentBoard.listBorder;
        borderColor.setValue(Color.web(felloListEntity.border));
    }

}
