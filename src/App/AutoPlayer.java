package App;

import PathingFiles.Pose;
import Threads.FollowPathData;
import Utilities.*;
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
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

import static App.Robot.robotLength;
import static Utilities.ConversionUtil.*;

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

    private BasePathsUtil pathsUtil = new AutoPathsUtil(pathsGroup, 255, 20);
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

        ImageView nextImg = new ImageView(new Image("imgs/skip.png"));
        nextImg.setFitWidth(20); nextImg.setFitHeight(20);
        nextBtn = new Button("", nextImg);

        ImageView prevImg = new ImageView(new Image("imgs/skipback.png"));
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

        robot = new Robot(robotLength, robotLength);
        Pose start = pathsUtil.getPathList().get(0).getRobotPose(0);
        updateRobot(start.getX(), start.getY(), start.getTheta());

        slowBox.setOnAction(e -> {
            slowMode = !slowMode;
            followPathData.setSlow(slowMode);
        });

        Tooltip positionTooltip = new Tooltip("");
        simPane.setOnMouseMoved(e -> {
            if (showTooltip) {
                positionTooltip.setText("(x: " + String.format("%.2f", getXInch(e.getX()))
                        + ", y: " + String.format("%.2f", getYInch(e.getY())) + ")");
                positionTooltip.show((Node) e.getSource(), e.getScreenX() + 15, e.getScreenY());
            }
        });

//        simPane.setOnMouseExited(e -> {
//            if (showTooltip) {
//                positionTooltip.hide();
//            }
//        });
//
//        simPane.setOnMouseEntered(e -> {
//            if (showTooltip) {
//                positionTooltip.show((Node) e.getSource(), e.getScreenX() + 15, e.getScreenY());
//            }
//        });

        simPane.setOnMouseClicked(e -> {
            if (showTooltip) {
                positionTooltip.hide();
            }
            showTooltip = !showTooltip;
        });

        for (Ring ringPos : AutoPathsUtil.rings) {
            Shape ring = Shape.subtract(new Circle(10), new Circle(5.7));
            ring.setFill(Color.YELLOW);
            ring.setLayoutX(getXPixel(ringPos.getX()));
            ring.setLayoutY(getYPixel(ringPos.getY()));
            simPane.getChildren().add(ring);
        }

        simPane.getChildren().addAll(robot, pathsGroup, obstacleGroup, warningGroup, reloadBtn);
        robot.toFront();

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
            if (followPathData.getPathNum() == pathsUtil.getPathList().size()) {
                robotThread = new Thread(followPathData, "UpdateRobotThread");
                robotThread.start();
                startStopDisabled.set(false);
            }
            followPathData.resetPathNum();
            curTime.set("0.0");
            followPathData.setPause(false);
            setState(State.Playing);
        });

        nextBtn.setOnAction(e -> {
            followPathData.setPathNum(followPathData.getPathNum()+1);
        });

        prevBtn.setOnAction(e -> {
            if (followPathData.getPathNum() == pathsUtil.getPathList().size()) {
                robotThread = new Thread(followPathData, "UpdateRobotThread");
                robotThread.start();
                startStopDisabled.set(false);
                followPathData.setPathNum(followPathData.getPathNum()-1);
                curTime.set("0.0");
                followPathData.setPause(false);
                setState(State.Playing);
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

        robot.setPosition(x, y);
        robot.setTheta(theta);
        robot.updateColor();

        obUtil.checkCollisions(robot);

        xInchLb.setText(String.format("%.2f", x));
        yInchLb.setText(String.format("%.2f", y));
        thetaLb.setText(String.format("%.2f", theta));
    }

    public void reloadPaths() {
        try {
            pathsGroup.getChildren().clear();
            pathsUtil = CompileUtil.reloadPathsUtil(pathsGroup, 255, 20);
            pathsUtil.drawAutoPaths();
            Pose start = this.pathsUtil.getPathList().get(0).getRobotPose(0);
            updateRobot(start.getX(), start.getY(), start.getTheta());
            followPathData.setPathList(pathsUtil.getPathList());
            curTime.set("0.0");

            if (startStopDisabled.get()) {
                robotThread = new Thread(followPathData, "UpdateRobotThread");
                robotThread.start();
                startStopDisabled.set(false);
                setState(State.Playing);
            }
            System.out.println("Paths reloaded");
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            System.out.println("Failed to reload paths");
        }
    }

    @Override public void endTasks() {
        followPathData.endThread();
    }
}
