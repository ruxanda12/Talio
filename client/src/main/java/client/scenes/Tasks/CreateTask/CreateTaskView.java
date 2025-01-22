package client.scenes.Tasks.CreateTask;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateTaskView {
    private Stage stage;

    public CreateTaskView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateTaskView.fxml"));
        Parent root = loader.load();
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
    }

    public void show() {
        stage.showAndWait();
    }

    public void close() {
        stage.close();
    }

    public void showError(String message) {
        var alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
