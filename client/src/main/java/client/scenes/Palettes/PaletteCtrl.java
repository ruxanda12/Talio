package client.scenes.Palettes;

import client.MyFXML;
import client.MyModule;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.Palette;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import static com.google.inject.Guice.createInjector;

public class PaletteCtrl {
    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private ColorPicker background;

    @FXML
    private ColorPicker font;

    @FXML
    private ColorPicker border;

    @FXML
    private Button def;

    @FXML
    private TextField title;

    private PaletteModel paletteModel;

    private MainCtrl mainCtrl;
    private ServerUtils server;

    @Inject
    public PaletteCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.paletteModel = new PaletteModel(new Palette());
    }

    public void initialize(Palette paletteEntity, MainCtrl mainCtrl, ServerUtils server) {
        this.paletteModel.palette = paletteEntity;
        this.mainCtrl = mainCtrl;
        this.server = server;

        title.setText(paletteModel.palette.title);

        background.setValue(Color.web(paletteModel.palette.background));
        font.setValue(Color.web(paletteModel.palette.font));
        border.setValue(Color.web(paletteModel.palette.border));
        def.setVisible(!paletteModel.palette.isDefault);
    }

    public void editPalette(){
        try{
            paletteModel.palette.title = title.getText();
            paletteModel.palette.background = background.getValue().toString();
            paletteModel.palette.font = font.getValue().toString();
            paletteModel.palette.border = border.getValue().toString();
            server.editPalette(paletteModel.palette);

        }   catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        mainCtrl.showCustomization(paletteModel.palette.parentBoard);
    }

    public void deletePalette() {
        //can't delete default -> pop-up
        if (paletteModel.palette.isDefault) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText("Not possible to delete the default palette!");
            alert.showAndWait();
        } else {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete");
                alert.setHeaderText("Are you sure you want to delete this?");
                alert.setContentText("(This action is irreversible!)");

                if (alert.showAndWait().get() == ButtonType.OK) {
                    paletteModel.palette.parentBoard.palettes.remove(paletteModel.palette);
                    server.deletePalette(paletteModel.palette.id);
                    mainCtrl.showCustomization(paletteModel.palette.parentBoard);
                }
            } catch (WebApplicationException e) {
                // Handle any errors from the server
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
        mainCtrl.showCustomization(paletteModel.palette.parentBoard);
    }

    public void isDefault() {
        for(var palette : paletteModel.palette.parentBoard.palettes) {
            if(palette.isDefault) {
                palette.isDefault = false;
            }
        }

        paletteModel.palette.isDefault = true;
        def.setVisible(false);
//        paletteModel.palette.title = paletteModel.palette.title.concat("(Default)");
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Color pickers//

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void setBackground() {
        Color color = background.getValue();
        paletteModel.palette.background = color.toString();
    }

    public void setFont() {
        Color color = font.getValue();
        paletteModel.palette.font = color.toString();
    }

    public void setBorder() {
        Color color = border.getValue();
        paletteModel.palette.border = color.toString();
    }

}
