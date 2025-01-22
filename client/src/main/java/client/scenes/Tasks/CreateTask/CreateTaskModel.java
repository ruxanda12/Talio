package client.scenes.Tasks.CreateTask;

import commons.FelloList;
import commons.SubTask;
import commons.Task;

import java.util.ArrayList;

public class CreateTaskModel {
    private String title;
    private String description;
    private FelloList parentList;

    public CreateTaskModel(FelloList parentList) {
        this.parentList = parentList;
    }

    public Task createTask() {
        return new Task(title, description, new ArrayList<SubTask>(), parentList,
                "#f5f5f5", "black", "black", false);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParentList(FelloList parentList) {
        this.parentList = parentList;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public FelloList getParentList() {
        return parentList;
    }
}
