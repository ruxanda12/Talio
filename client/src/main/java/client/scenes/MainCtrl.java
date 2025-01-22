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
package client.scenes;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import client.scenes.Boards.*;
import client.scenes.Boards.BoardCtrl.BoardCtrl;
import client.scenes.Boards.HomeScreenCtrl;
import client.scenes.Lists.CreateFelloListCtrl;
import client.scenes.Lists.EditListCtrl;
import client.scenes.Lists.ListCtrl;
import client.scenes.Palettes.CustomizationCtrl;
import client.scenes.Palettes.PaletteCtrl;
import client.scenes.Subtask.SubTaskCtrl;
import client.scenes.Tags.EditTags.EditTagsCtrl;
import client.scenes.Tasks.CreateTask.CreateTaskCtrl;
import client.scenes.Tasks.EditTaskCtrl;
import client.scenes.Tasks.TaskCtrl;
import client.utils.ServerUtils;
import client.utils.ShrekCtrl;
import com.github.kiprobinson.bigfraction.BigFraction;
import commons.Board;
import commons.FelloList;
import commons.Task;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;
    private CreateTaskCtrl createTaskCtrl;
    private Scene createTask;
    private CreateFelloListCtrl createFelloListCtrl;
    private Scene createFelloList;
    private CreateBoardCtrl createBoardCtrl;
    private Scene createBoard;
    private TaskCtrl taskCtrl;
    private Scene task;
    private ListCtrl listCtrl;
    private Scene list;
    private BoardCtrl boardCtrl;
    private Scene board;
    private ClientConnectCtrl clientConnectCtrl;
    private Scene clientConnect;
    private EditTaskCtrl editTaskCtrl;
    private Scene editTask;
    private ShrekCtrl shrekCtrl;
    private Scene shrek;
    private SubTaskCtrl subTaskCtrl;
    private Scene subtask;

    private PaletteCtrl paletteCtrl;

    private Scene palette;

    private CustomizationCtrl customizationCtrl;

    private Scene customization;

    private Scene homeScreen;

    private HomeScreenCtrl homeScreenCtrl;

    private Scene editList;

    private EditListCtrl editListCtrl;

    private EditTagsCtrl editTagsCtrl;

    private Scene editTags;


    private  EditBoardCtrl editBoardCtrl;

    private Scene editBoard;


    private Scene homeScreenBoardPart;

    private ServerUtils serverUtils;

    private HomeScreenBoardPartCtrl homeScreenBoardPartCtrl;
    //create an instance of the control class and a scene for each


    public void initialize(Stage primaryStage, LinkedList<Pair> pairs) {
        this.primaryStage = primaryStage;
        this.createTaskCtrl = (CreateTaskCtrl) pairs.get(0).getKey();
        this.createTask = new Scene((Parent) pairs.get(0).getValue());
        this.createFelloListCtrl = (CreateFelloListCtrl) pairs.get(1).getKey();
        this.createFelloList = new Scene((Parent) pairs.get(1).getValue());
        this.createBoardCtrl = (CreateBoardCtrl) pairs.get(2).getKey();
        this.createBoard = new Scene((Parent) pairs.get(2).getValue());
        this.taskCtrl = (TaskCtrl) pairs.get(3).getKey();
        this.task = new Scene((Parent) pairs.get(3).getValue());
        this.listCtrl = (ListCtrl) pairs.get(4).getKey();
        this.list = new Scene((Parent) pairs.get(4).getValue());
        this.boardCtrl = (BoardCtrl) pairs.get(5).getKey();
        this.board = new Scene((Parent) pairs.get(5).getValue());
        this.clientConnectCtrl = (ClientConnectCtrl) pairs.get(6).getKey();
        this.clientConnect = new Scene((Parent) pairs.get(6).getValue());
        this.editTaskCtrl = (EditTaskCtrl) pairs.get(7).getKey();
        this.editTask = new Scene((Parent) pairs.get(7).getValue());
        this.shrekCtrl = (ShrekCtrl) pairs.get(8).getKey();
        this.shrek = new Scene((Parent) pairs.get(8).getValue());
        this.subTaskCtrl = (SubTaskCtrl) pairs.get(9).getKey();
        this.subtask = new Scene((Parent) pairs.get(9).getValue());
        this.homeScreenCtrl = (HomeScreenCtrl) pairs.get(10).getKey();
        this.homeScreen = new Scene((Parent) pairs.get(10).getValue());
        this.editListCtrl = (EditListCtrl) pairs.get(11).getKey();
        this.editList = new Scene((Parent) pairs.get(11).getValue());
        this.editTagsCtrl = (EditTagsCtrl) pairs.get(12).getKey();
        this.editTags = new Scene((Parent) pairs.get(12).getValue());
        this.customizationCtrl = (CustomizationCtrl) pairs.get(13).getKey();
        this.customization = new Scene((Parent) pairs.get(13).getValue());
        this.paletteCtrl = (PaletteCtrl) pairs.get(14).getKey();
        this.palette = new Scene((Parent) pairs.get(14).getValue());
        this.editBoardCtrl=(EditBoardCtrl) pairs.get(15).getKey();
        this.editBoard= new Scene((Parent) pairs.get(15).getValue());


        primaryStage.show();
        showClientConnect();
    }


    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
    }

    public void showCreateTask(FelloList listParent) {
        primaryStage.setTitle("Task: Create New Task");
        primaryStage.setScene(createTask);
        centerMainStage();
        createTaskCtrl.initialize(listParent);
    }

    public void showCreateFelloList(Board boardEntity) {
        primaryStage.setTitle("Fello List: Create New Fello List");
        primaryStage.setScene(createFelloList);
        centerMainStage();
        createFelloListCtrl.initialize(boardEntity);
    }

    public void showEditFelloList(FelloList felloList){
        primaryStage.setTitle("Fello List: Edit Fello List");
        primaryStage.setScene(editList);
        centerMainStage();
        editListCtrl.initialize(felloList, this.listCtrl);
    }

    public void showCreateBoard() {
        primaryStage.setTitle("Board: Create New Board");
        primaryStage.setScene(createBoard);
        centerMainStage();
        createBoardCtrl.initialize();
    }

    public void showHomeScreen() {
        primaryStage.setTitle("HomeScreen");
        primaryStage.setScene(homeScreen);
        centerMainStage();
        homeScreenCtrl.initialize();
    }

    public void showBoard(Board boardEntity) {
        primaryStage.setTitle("Board");
        primaryStage.setScene(board);
        centerMainStage();
        boardCtrl.initialize(boardEntity);
        boardCtrl.boardUpdate();
    }

    public void showEditBoard(Board boardEntity) {
        primaryStage.setTitle("Edit Board");
        primaryStage.setScene(editBoard);
        centerMainStage();
        editBoardCtrl.initialize(boardEntity);
    }

    public void showClientConnect() {
        primaryStage.setTitle("ClientConnect");
        primaryStage.setScene(clientConnect);
        centerMainStage();
        clientConnectCtrl.initialize();
    }

    public void showHomeScreenBoardPart(Board board) {
        primaryStage.setTitle("HomeScreenBoardPart");
        primaryStage.setScene(homeScreenBoardPart);
        centerMainStage();
        homeScreenBoardPartCtrl.initialize(this, board, serverUtils, null);
    }

    public void showEditTask(Task taskEntity) {
        primaryStage.setTitle("Edit Task");
        primaryStage.setScene(editTask);
        centerMainStage();
        editTaskCtrl.initialize(taskEntity, taskCtrl);
    }

    public void showEditTags(Board boardEntity){
        primaryStage.setTitle("Edit Tags");
        primaryStage.setScene(editTags);
        centerMainStage();
        editTagsCtrl.initialize(boardEntity);
    }

    public void showShrek(){
        primaryStage.setTitle("Shrek is the coolest!");
        primaryStage.setScene(shrek);
        centerMainStage();
        shrekCtrl.initialize();
    }

//    public void showSubtask() {
//        primaryStage.setTitle("subtasks are the coolest!");
//        primaryStage.setScene(subtask);
//        subTaskCtrl.initialize();
//    }

    public void showCustomization(Board boardEntity) {
        primaryStage.setTitle("Customize your boards!");
        primaryStage.setScene(customization);
        centerMainStage();
        customizationCtrl.initialize(boardEntity);
    }

    public void centerMainStage(){
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((bounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((bounds.getHeight() - primaryStage.getHeight()) / 2);
    }

    public void addEnd(Task task, FelloList parentList) {
        if(parentList.tasks.size() == 0){
            BigFraction position = BigFraction.valueOf(0.5);
            task.position = position.toString();
            parentList.tasks.add(task);
            return;
        }

        Task last = parentList.tasks.get(parentList.tasks.size() - 1);
        BigFraction prevLast = BigFraction.valueOf(last.position);
        BigFraction position = prevLast.add(BigFraction.valueOf(1));
        position = position.divide(BigFraction.valueOf(2));
        task.position = position.toString();
        parentList.tasks.add(task);

        arrange(parentList.tasks);
    }

    public void addFirst(Task task, FelloList parentList) {
        if(parentList.tasks.size() == 0){
            BigFraction position = BigFraction.valueOf(0.5);
            task.position = position.toString();
            parentList.tasks.add(task);
            return;
        }

        Task first = parentList.tasks.get(0);
        BigFraction prevFirst = BigFraction.valueOf(first.position);
        BigFraction position = prevFirst.divide(BigFraction.valueOf(2));
        task.position = position.toString();
        parentList.tasks.add(0, task);

        arrange(parentList.tasks);
    }

    public void addAt(int pos, Task task, FelloList parentList) {
        if(parentList.tasks.size() == 0){
            BigFraction position = BigFraction.valueOf(0.5);
            task.position = position.toString();
            parentList.tasks.add(task);
            return;
        }

        Task prev = parentList.tasks.get(pos - 1);
        Task next = parentList.tasks.get(pos);
        BigFraction prevPosition = BigFraction.valueOf(prev.position);
        BigFraction nextPosition = BigFraction.valueOf(next.position);
        BigFraction position = prevPosition.divide(nextPosition);
        task.position = position.toString();
        parentList.tasks.add(pos, task);

        arrange(parentList.tasks);
    }

    public void arrange(List<Task> tasks) {
        Collections.sort(tasks,
                (o1, o2) -> BigFraction.valueOf(o1.position).compareTo(BigFraction.valueOf(o2.position)));
    }
}