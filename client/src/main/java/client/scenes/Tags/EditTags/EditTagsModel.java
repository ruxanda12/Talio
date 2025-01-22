package client.scenes.Tags.EditTags;

import commons.Board;
import commons.Tags;

import java.util.List;

public class EditTagsModel {
    public Board boardEntity;

    public EditTagsModel(Board boardEntity) {
        this.boardEntity = boardEntity;
    }

    public List<Tags> getTags(){
        return boardEntity.tags;
    }

    public void removeTag(Tags tag){
        boardEntity.tags.remove(tag);
    }
}
