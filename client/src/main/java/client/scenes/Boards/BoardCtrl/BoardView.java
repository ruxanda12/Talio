package client.scenes.Boards.BoardCtrl;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class BoardView {
    @FXML
    private HBox board;

    @FXML
    private Button add;

    @FXML
    private Label title;

    public HBox getBoard() {
        return board;
    }

    public Button getAdd() {
        return add;
    }

    public Label getTitle() {
        return title;
    }
}
