package client.scenes.Boards.BoardCtrl;

import commons.Board;

public class BoardModel {
    private Board boardEntity;

    public BoardModel(Board boardEntity) {
        this.boardEntity = boardEntity;
    }

    public Board getBoardEntity() {
        return boardEntity;
    }

    public void setBoardEntity(Board boardEntity) {
        this.boardEntity = boardEntity;
    }

    public void removeFelloList(int index) {
        if (index >= 0 && index < boardEntity.felloLists.size()) {
            boardEntity.felloLists.remove(index);
        }
    }
}
