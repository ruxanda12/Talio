package client.scenes.Palettes;

import client.MyFXML;
import client.MyModule;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import com.google.inject.Injector;
import commons.Board;
import commons.Palette;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;

import static com.google.inject.Guice.createInjector;

public class CustomizationCtrl {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private MainCtrl mainCtrl;
    private ServerUtils server;
    private Board board;

    @FXML
    private ColorPicker boardBack;
    @FXML
    private ColorPicker boardFont;
    @FXML
    private ColorPicker boardBorder;
    @FXML
    private ColorPicker listBack;
    @FXML
    private ColorPicker listFont;
    @FXML
    private ColorPicker listBorder;
    @FXML
    private VBox vbox;
    @FXML
    private AnchorPane window;


    @Inject
    public CustomizationCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize(Board board) {
        this.board = board;

        boardBack.setValue(Color.web(board.background));
        boardFont.setValue(Color.web(board.font));
        boardBorder.setValue(Color.web(board.border));

        listBack.setValue(Color.web(board.listBackground));
        listFont.setValue(Color.web(board.listFont));
        listBorder.setValue(Color.web(board.listBorder));

        this.vbox.getChildren().clear();
        for (var palette : board.palettes) {
            var pair = FXML.load(PaletteCtrl.class, "client", "scenes", "Palette.fxml");
            var currPalette = new AnchorPane(pair.getValue());
            PaletteCtrl paletteCtrl = pair.getKey();
            paletteCtrl.initialize(palette, mainCtrl, server);
            this.vbox.getChildren().add(currPalette);
        }
        exitWindow(window);
    }

    public void resetBoard() {
        board.background = "#99cccc";
        board.font = "black";
        board.border = "#808080";

        boardBack.setValue(Color.web(board.background));
        boardFont.setValue(Color.web(board.font));
        boardBorder.setValue(Color.web(board.border));
    }

    public void resetList() {
        board.listBackground = "#008080";
        board.listFont = "black";
        board.listBorder = "#808080";

        listBack.setValue(Color.web(board.listBackground));
        listFont.setValue(Color.web(board.listFont));
        listBorder.setValue(Color.web(board.listBorder));
    }

    public void addColor() {
        try {
            Palette emptyPalette = new Palette(board, "", false,
                    "#f5f5f5", "black", "black");
            board.palettes.add(emptyPalette);
            board = server.editBoard(board);

        }   catch (WebApplicationException e) {

            System.out.println("Error somewhere");

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        mainCtrl.showCustomization(board);
    }

    public void goBack() {
        board = this.server.addBoard(board);
        this.mainCtrl.showBoard(board);
    }

    public void exitWindow(AnchorPane window){
        window.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ESCAPE){
                goBack();
                event.consume();
            }
            event.consume();
        });
    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // Color pickers//

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public void backgroundBoard() {
        Color color = boardBack.getValue();
        board.background = color.toString();
    }

    public void fontBoard() {
        Color color = boardFont.getValue();
        board.font = color.toString();
    }

    public void borderBoard() {
        Color color = boardBorder.getValue();
        board.border = color.toString();
        board.border = board.border.replace("0x", "#");
    }

    public void backgroundList() {
        Color color = listBack.getValue();
        board.listBackground = color.toString();
    }

    public void fontList() {
        Color color = listFont.getValue();
        board.listFont = color.toString();
    }

    public void borderList() {
        Color color = listBorder.getValue();
        board.listBorder = color.toString();
        board.listBorder = board.listBorder.replace("0x", "#");
    }
}
