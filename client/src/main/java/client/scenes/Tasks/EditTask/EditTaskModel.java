package client.scenes.Tasks.EditTask;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Task;
import jakarta.ws.rs.WebApplicationException;

import java.io.IOException;

public class EditTaskModel {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private Task taskEntity;

    public EditTaskModel(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void setTaskEntity(Task taskEntity) {
        this.taskEntity = taskEntity;
    }

    public Task getTaskEntity() {
        return taskEntity;
    }

    public ServerUtils getServer() {
        return server;
    }

    public MainCtrl getMainCtrl() {
        return mainCtrl;
    }

    public void cancel() {
        mainCtrl.showBoard(taskEntity.parentFelloList.parentBoard);
    }

    public void updateTask(String title, String description) throws IOException {
        taskEntity.title = title;
        taskEntity.description = description;
        try {
            server.editTask(taskEntity, taskEntity.id);
        } catch (WebApplicationException e) {
            throw new IOException("Unable to update task: " + e.getMessage());
        }
    }

    public void updateTaskEntity(Task updatedTask) {
        this.taskEntity = updatedTask;
    }
}
