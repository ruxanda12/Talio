package client.scenes;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.concurrent.TimeoutException;

@ExtendWith(ApplicationExtension.class)
public abstract class TestFXBase extends FxRobot {

    protected Stage stage;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Start
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Parent root = FXMLLoader.load(getClass().getResource(getFxmlPath()));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @AfterEach
    public void tearDown() {
        try {
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            throw new IllegalStateException("Failed to hide stage", e);
        }
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    /**
     * Providing the path to the FXML file.
     *
     * @return The path to the FXML file as a String.
     */
    protected abstract String getFxmlPath();

    public <T extends Node> T find(final String query) {
        return (T) lookup(query).queryAll().iterator().next();
    }
}
