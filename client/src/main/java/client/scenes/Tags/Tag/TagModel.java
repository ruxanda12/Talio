package client.scenes.Tags.Tag;

import commons.Tags;
import commons.Task;

public class TagModel {

    private Tags tag;
    private Task parenTask;

    public TagModel(Tags tag, Task parenTask) {
        this.tag = tag;
        this.parenTask = parenTask;
    }

    public Tags getTag() {
        return tag;
    }

    public void setTag(Tags tag) {
        this.tag = tag;
    }

    public Task getParenTask() {
        return parenTask;
    }

    public void setParenTask(Task parenTask) {
        this.parenTask = parenTask;
    }
}
