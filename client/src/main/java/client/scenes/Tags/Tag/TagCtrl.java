package client.scenes.Tags.Tag;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tags;
import commons.Task;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TagCtrl {
    private MainCtrl mainCtrl;
    private ServerUtils server;
    private TagModel tagModel;
    private TagView tagView;
    @FXML
    private Label title;
    @FXML
    private CheckBox saved;
    @FXML
    private Circle circle;

    @Inject
    public TagCtrl(MainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void initialize(MainCtrl mainCtrl, ServerUtils server, Tags tagEntity, Task parentTask){
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.tagModel = new TagModel(tagEntity, parentTask);

        circle.setFill(Color.valueOf(tagModel.getTag().color));
        title.setText(tagModel.getTag().title);
        if(tagModel.getParenTask().tags.contains(tagModel.getTag())){
            saved.setSelected(true);
        }
    }

    public void toggleSaved(){
        if(saved.isSelected()){
            Task parent = tagModel.getParenTask();
            parent.tags.add(tagModel.getTag());
            tagModel.setParenTask(parent);
            server.editTask(parent, parent.id);
        }
        else{
            Task parent = tagModel.getParenTask();
            parent.tags.remove(tagModel.getTag());
            tagModel.setParenTask(parent);
            server.editTask(parent, parent.id);
        }
    }
}
