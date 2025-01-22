/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client;

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;

import client.scenes.Boards.*;
import client.scenes.Boards.BoardCtrl.BoardCtrl;
import client.scenes.Lists.CreateFelloListCtrl;
import client.scenes.Lists.EditListCtrl;
import client.scenes.Lists.ListCtrl;
import client.scenes.Palettes.CustomizationCtrl;
import client.scenes.Palettes.PaletteCtrl;
import client.scenes.Subtask.SubTaskCtrl;
import client.scenes.Tags.EditTags.EditTagsCtrl;
import client.scenes.Tasks.CreateTaskCtrl;
import client.scenes.MainCtrl;
import client.scenes.Tasks.EditTaskCtrl;
import client.scenes.Tasks.TaskCtrl;
import client.utils.ShrekCtrl;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Pair;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        LinkedList<Pair> pairs = new LinkedList<>();

        var createTask = FXML.load(CreateTaskCtrl.class, "client", "scenes", "CreateTask.fxml");
        pairs.addLast(createTask);

        var createList = FXML.load(CreateFelloListCtrl.class, "client", "scenes", "CreateFelloList.fxml");
        pairs.addLast(createList);

        var createBoard = FXML.load(CreateBoardCtrl.class, "client", "scenes", "CreateBoard.fxml");
        pairs.addLast(createBoard);

        var task = FXML.load(TaskCtrl.class, "client", "scenes", "Task.fxml");
        pairs.addLast(task);

        var list = FXML.load(ListCtrl.class, "client", "scenes", "List.fxml");
        pairs.addLast(list);

        var board = FXML.load(BoardCtrl.class, "client", "scenes", "Board.fxml");
        pairs.addLast(board);

        var clientConnect = FXML.load(ClientConnectCtrl.class, "client", "scenes", "ClientConnect.fxml");
        pairs.addLast(clientConnect);

        var editTask = FXML.load(EditTaskCtrl.class, "client", "scenes", "EditTask.fxml");
        pairs.addLast(editTask);

        var shrek = FXML.load(ShrekCtrl.class, "client", "scenes", "Shrek.fxml");
        pairs.addLast(shrek);

        var subtask = FXML.load(SubTaskCtrl.class, "client", "scenes", "subtask.fxml");
        pairs.addLast(subtask);

        var homeScreen = FXML.load(HomeScreenCtrl.class, "client", "scenes", "HomeScreen.fxml");
        pairs.addLast(homeScreen);

        var editList = FXML.load(EditListCtrl.class, "client", "scenes", "EditFelloList.fxml");
        pairs.addLast(editList);

        var editTags = FXML.load(EditTagsCtrl.class, "client", "scenes", "EditTags.fxml");
        pairs.addLast(editTags);

        var customization = FXML.load(CustomizationCtrl.class, "client", "scenes", "Customization.fxml");
        pairs.addLast(customization);

        var palette = FXML.load(PaletteCtrl.class, "client", "scenes", "Palette.fxml");
        pairs.addLast(palette);
        //var for each fxml file

        var editBoard = FXML.load(EditBoardCtrl.class, "client", "scenes", "EditBoard.fxml");
        pairs.addLast(editBoard);

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, pairs);

        primaryStage.setOnCloseRequest(e -> {
            homeScreen.getKey().stop();
        });
    }
}