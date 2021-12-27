package com.example.assignment2;

import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


public class HomeView extends VBox implements ModelSubscriber {
    protected Tab addTab;
    protected Model model;
    protected Controller controller;
    protected AddNewTaskView addNewTaskView;
    private final TabPane taskPane;
    private StackPane stp;


    public HomeView() {
        //set up task pane
        taskPane = new TabPane();
        taskPane.setTabMinHeight(100);
        taskPane.setTabMaxHeight(100);
        taskPane.setSide(Side.LEFT);
        taskPane.setRotateGraphic(true);

        // set up the tab for adding new task
        addNewTaskView = new AddNewTaskView();
        addTab = new Tab("Add new task", addNewTaskView);
        addTab.setClosable(false);
        taskPane.getTabs().add(addTab);

        // rotate the task Pane
        Label l;
        stp = null;
        for (Tab t : taskPane.getTabs()) {
            l = new Label(t.getText());
            if (t == addTab) {
                l.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            }
            l.setRotate(90);
            stp = new StackPane(new Group(l));
            stp.setRotate(90);
            t.setGraphic(stp);
            t.setText("");
        }

        this.getChildren().add(taskPane);
        setVgrow(taskPane, Priority.ALWAYS);
    }

    // Add new task tab
    public void addNewTask(Task task) {
        // create new task view
        TaskView taskView = new TaskView(task);
        taskView.setDetail(task.getDetail());
        taskView.setController(controller);

        // create new tab
        Tab newTab = new Tab(task.getTaskName(), taskView);
        newTab.setClosable(false);
        taskPane.getTabs().add(newTab);
        Label l = null;
        l = new Label(newTab.getText());
        l.setRotate(90);
        stp = new StackPane(new Group(l));
        newTab.setGraphic(stp);
        newTab.setText("");

        // direct user to the new tab
        taskPane.getSelectionModel().select(newTab);
    }

    // update the timer
    public void updateTimer() {
        for (Tab tab : taskPane.getTabs()) {
            if (tab != addTab) {
                ((TaskView) tab.getContent()).updateTimer();
            }
        }
    }

    public void setModel(Model newModel) {
        model = newModel;
    }

    public void setController(Controller newController) {
        controller = newController;
        addNewTaskView.setController(newController);
    }

    // update the history table in every tab
    public void updateHistory() {
        for (Tab tab : taskPane.getTabs()) {
            if (tab != addTab) {
                ((TaskView) tab.getContent()).updateTable();
            }
        }
    }

    @Override
    public void modelTaskAdded() {
        addNewTask(model.getLastTask());
    }

    @Override
    public void modelTimerChanged() {
        updateTimer();
    }

    @Override
    public void modelTaskHistoryChanged() {
        updateHistory();
    }
}