package client.utils;

import client.scenes.MainCtrl;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ShrekCtrl {
    @FXML
    private ImageView imageView;

    private ServerUtils server;
    private MainCtrl mainCtrl;
    private boolean ok = false;

    private Image image2 = new Image("client/scenes/Photos/shrek2.jpg");
    private Image image1 = new Image("client/scenes/Photos/shrek1.jpg");

    @Inject
    public ShrekCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public  void initialize() {
    }

    public void displayImage() {
        if(ok == false) {
            imageView.setImage(image2);
            ok = true;
        }
        else {
            imageView.setImage(image1);
            ok = false;
        }

    }

    public void goBack() {
        mainCtrl.showClientConnect();
    }
}
