package client.scenes.Boards;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;


import java.util.prefs.Preferences;

public class ClientConnectCtrl {
    private ServerUtils server;

    private MainCtrl mainCtrl;

    @FXML
    public TextField serverAddress;

    @FXML
    public TextField pwd;


    @FXML
    private Text greeting;

    public void initialize() {
        serverAddress.setText("http://localhost:8080");
    }


    @Inject
    public ClientConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }


    public void join(){
        try {
            this.server.setServer(serverAddress.getText());

            Preferences pref = Preferences.userRoot().node("board");

            pref.putBoolean("admin", false);

            if(pwd.getText().equals("10forthisproject")){
                pref.putBoolean("admin", true);
            }

            else if(!pwd.getText().equals("") && !pwd.getText().equals("10forthisproject")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setHeaderText("Incorrect password");
                alert.showAndWait();
                this.mainCtrl.showClientConnect();
                return;
            }

            this.mainCtrl.showHomeScreen();
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Server not found");
            alert.showAndWait();
            this.mainCtrl.showClientConnect();
        }

    }

    // ?
    //    public void goShrek(){
    //        mainCtrl.showShrek();
    //    }
}