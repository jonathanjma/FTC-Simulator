package App;

import PathingFiles.Pose;
import Threads.FollowPathData;
import Utilities.AutoPathsUtil;
import Utilities.ObstacleUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static App.Robot.robotLength;

@SuppressWarnings("FieldMayBeFinal")
public class AutoPlayer extends PlayerBase {

    private Group pathsGroup = new Group();
    private Group obstacleGroup = new Group();
    private Group warningGroup = new Group();

    // ui labels
    private Label timeLb = new Label("  Time:");
    private Label curTimeLb = new Label("n/a");

    private AutoPathsUtil pathsUtil = new AutoPathsUtil(pathsGroup, 255, 20);
    private ObstacleUtil obUtil = new ObstacleUtil(obstacleGroup, warningGroup);

    private SimpleBooleanProperty startStopVisible = new SimpleBooleanProperty(true);
    private SimpleStringProperty curTime = new SimpleStringProperty("0.00");

    // update robot thread
    private FollowPathData followPathData;
    private Thread robotThread;

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        simInfo.setPadding(new Insets(0, 5, 5, 5));

        timeLb.setFont(Font.font(Font.getDefault() + "", FontWeight.BOLD, 14));
        curTimeLb.setFont(Font.font(14));

        curTimeLb.textProperty().bind(curTime);
        startStopBtn.visibleProperty().bind(startStopVisible);

        simInfo.getChildren().addAll(timeLb, curTimeLb, startStopBtn, restartBtn);

        pathsUtil.drawAutoPaths();
        obUtil.initializeObstacles();

        robot = new Robot(robotLength, robotLength);
        Pose start = pathsUtil.getPathList().get(0).getRobotPose(0);
        updateRobot(start.getX(), start.getY(), start.getTheta());

        simPane.getChildren().addAll(robot, pathsGroup, obstacleGroup, warningGroup);
        robot.toFront();

        followPathData = new FollowPathData(pathsUtil.getPathList(), curTime, startStopVisible, this);
        robotThread = new Thread(followPathData, "UpdateRobotThread");

        startStopBtn.setOnAction(e -> {
            switch (startStopBtn.getText()) {
                case "Start":
                    robotThread.start();
                    startStopBtn.setText("Pause");
                    restartBtn.setVisible(true);
                    break;
                case "Pause":
                    followPathData.setPause(true);
                    startStopBtn.setText("Resume");
                    break;
                case "Resume":
                    followPathData.setPause(false);
                    startStopBtn.setText("Pause");
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            if (followPathData.getPathNum() == pathsUtil.getPathList().size()) {
                robotThread = new Thread(followPathData, "UpdateRobotThread");
                robotThread.start();
                startStopVisible.set(true);
            }
            startStopBtn.setText("Pause");
            followPathData.resetPathNum();
            curTime.set(0 + "");
            followPathData.setPause(false);
        });

        primaryStage.setOnCloseRequest(e -> endTasks());

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

    @Override public void endTasks() {
        followPathData.endThread();
    }
}
