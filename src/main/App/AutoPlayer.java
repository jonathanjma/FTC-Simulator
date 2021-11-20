package main.App;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import main.PathingFiles.Pose;
import main.Threads.FollowPathData;
import main.Utilities.AutoPathsUtil;
import main.Utilities.CompileUtil;
import main.Utilities.ObstacleUtil;

import java.util.ArrayList;
import java.util.Arrays;

import static main.Utilities.ConversionUtil.getXInch;
import static main.Utilities.ConversionUtil.getYInch;

@SuppressWarnings("FieldMayBeFinal")
public class AutoPlayer extends PlayerBase {

    private Group pathsGroup = new Group();
    private Group obstacleGroup = new Group();
    private Group warningGroup = new Group();

    // ui labels
    private Label timeLb = new Label("  Time:");
    private Label curTimeLb = new Label("n/a");
    private Label slowLb = new Label("Slow: ");
    private CheckBox slowBox = new CheckBox();
    private Button nextBtn;
    private Button prevBtn;
    private Tooltip positionTooltip = new Tooltip("");

    private AutoPathsUtil pathsUtil = new AutoPathsUtil(pathsGroup, 255, 20);
    private ObstacleUtil obUtil = new ObstacleUtil(obstacleGroup, warningGroup);

    private SimpleBooleanProperty startStopDisabled = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty nextDisabled = new SimpleBooleanProperty(false);
    private SimpleBooleanProperty prevDisabled = new SimpleBooleanProperty(true);
    private SimpleStringProperty curTime = new SimpleStringProperty("0.00");
    private boolean slowMode = false;

    private boolean showTooltip = false;

    // update robot thread
    private FollowPathData followPathData;
    private Thread robotThread;

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        simInfo.setPadding(new Insets(0, 5, 5, 5));

        setFontBold(timeLb, 14); setFont(curTimeLb, 14);
        setFont(slowLb, 14);

        ImageView nextImg = new ImageView(new Image("main/imgs/skip.png"));
        nextImg.setFitWidth(20); nextImg.setFitHeight(20);
        nextBtn = new Button("", nextImg);

        ImageView prevImg = new ImageView(new Image("main/imgs/skipback.png"));
        prevImg.setFitWidth(20); prevImg.setFitHeight(20);
        prevBtn = new Button("", prevImg);
        prevBtn.setDisable(true);

        curTimeLb.textProperty().bind(curTime);
        startStopBtn.disableProperty().bind(startStopDisabled);
        nextBtn.disableProperty().bind(nextDisabled);
        prevBtn.disableProperty().bind(prevDisabled);

        Button reloadBtn = new Button("Reload Paths");
        reloadBtn.setLayoutX(513); reloadBtn.setLayoutY(570);
        reloadBtn.setOnAction(e -> reloadPaths());

        simInfo.getChildren().addAll(timeLb, curTimeLb, startStopBtn, restartBtn, prevBtn, nextBtn, slowLb, slowBox);

        pathsUtil.drawAutoPaths();
        obUtil.initializeObstacles();

        robot = new Robot();
        Pose start = pathsUtil.getPathList().get(0).getRobotPose(0);
        updateRobot(start.getX(), start.getY(), start.getTheta());

        slowBox.setOnAction(e -> {
            slowMode = !slowMode;
            followPathData.setSlow(slowMode);
        });

        simPane.setOnMouseMoved(e -> {
            if (showTooltip) {
                positionTooltip.setText("(x: " + String.format("%.2f", getXInch(e.getX()))
                        + ", y: " + String.format("%.2f", getYInch(e.getY())) + ")");
                positionTooltip.show((Node) e.getSource(), e.getScreenX() + 15, e.getScreenY());
            }
        });

        /*simPane.setOnMouseExited(e -> {
            if (showTooltip) {
                positionTooltip.hide();
            }
        });

        simPane.setOnMouseEntered(e -> {
            if (showTooltip) {
                positionTooltip.show((Node) e.getSource(), e.getScreenX() + 15, e.getScreenY());
            }
        });*/

        simPane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                if (showTooltip) {
                    positionTooltip.hide();
                }
                showTooltip = !showTooltip;
            }
        });

        simPane.getChildren().addAll(robot, pathsGroup, obstacleGroup, warningGroup, reloadBtn);
        robot.toFront();
        backBtn.toFront();

        followPathData = new FollowPathData(pathsUtil.getPathList(), curTime,
                new ArrayList<>(Arrays.asList(startStopDisabled, nextDisabled, prevDisabled)), this);
        robotThread = new Thread(followPathData, "UpdateRobotThread");

        startStopBtn.setOnAction(e -> {
            switch (state) {
                case NotStarted:
                    robotThread.start();
                    setState(State.Playing);
                    restartBtn.setDisable(false);
                    break;
                case Playing:
                    followPathData.setPause(true);
                    setState(State.Paused);
                    break;
                case Paused:
                    followPathData.setPause(false);
                    setState(State.Playing);
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            restart();
        });

        nextBtn.setOnAction(e -> {
            followPathData.setPathNum(followPathData.getPathNum()+1);
        });

        prevBtn.setOnAction(e -> {
            if (followPathData.getPathNum() == pathsUtil.getPathList().size()) {
                restart(1);
            } else {
                followPathData.setPathNum(followPathData.getPathNum()-1);
            }
        });

        mainPane.setBottom(simInfo);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 35);
        primaryStage.setTitle("Auto Player");
        primaryStage.setScene(scene);
    }

    public void updateRobot(double x, double y, double theta) {

        robot.setPose(x, y, theta);
        robot.updateColor();

        obUtil.checkCollisions(robot);

        xInchLb.setText(String.format("%.2f", x));
        yInchLb.setText(String.format("%.2f", y));
        thetaLb.setText(String.format("%.2f", theta));
    }

    public void reloadPaths() {
        try {
            pathsGroup.getChildren().clear();
            if (state == State.NotStarted) restartBtn.setDisable(false);
            pathsUtil.setPaths(CompileUtil.reloadPaths());
            pathsUtil.drawAutoPaths();
            restart();
            System.out.println("Paths reloaded");
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            System.out.println("Failed to reload paths");
        }
    }

    public void restart() {
        restart(0);
    }

    // option 0 uses the pathList from the path utility, option 1 sets the decrements the pathNum by 1
    public void restart(int option) {
        if (!robotThread.isAlive()) {
            robotThread = new Thread(followPathData, "UpdateRobotThread");
            robotThread.start();
            startStopDisabled.set(false);
            if (option == 1) {
                followPathData.setPathNum(followPathData.getPathNum()-1);
            }
        }

        if (option == 0) {
            followPathData.setPathList(pathsUtil.getPathList());
        }

        curTime.set("0.0");
        followPathData.setPause(false);
        setState(State.Playing);
    }

    @Override
    public void endTasks() {
        positionTooltip.hide();
        followPathData.endThread();
    }
}
