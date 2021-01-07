package App;

import Threads.FollowPositionData;
import Utilities.DataPoint;
import Utilities.RobotDataUtil;
import javafx.animation.PathTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import static App.Robot.robotLength;
import static Utilities.ConversionUtil.*;

@SuppressWarnings("FieldMayBeFinal")
public class MatchReplayer extends PlayerBase {

    // **************************************************************************************************
    private final static String logName = "RobotData168";
    // **************************************************************************************************

    private VBox simInfoHousing = new VBox(2.5);
    private HBox simInfo2 = new HBox(5);

    private Group pathPointGroup = new Group();

    private Slider nodeSlider;
    private boolean manual = false;
    private boolean startBtnPressed = false;

    // ui labels
    private Label velocityLb = new Label("  Velocity:");
    private Label velocityXLb = new Label("n/a");
    private Label commaLb2 = new Label(",");
    private Label velocityYLb = new Label("n/a");
    private Label commaLb3 = new Label(",");
    private Label velocityThLb = new Label("n/a");
    private Label accelLb = new Label("Accel:");
    private Label accelXLb = new Label("n/a");
    private Label commaLb4 = new Label(",");
    private Label accelYLb = new Label("n/a");
    private Label commaLb5 = new Label(",");
    private Label accelThLb = new Label("n/a");
    private Label ringsLb = new Label("  # Rings: ");
    private Label numRingsLb = new Label("n/a");

    private Text nodeLb = new Text(515, 25, "Node:");
    private Text nodeNumLb = new Text(558, 25, "n/a");
    private Text timeLb = new Text(515, 45, "Time:");
    private Text curTimeLb = new Text(553, 45, "n/a");

    private boolean prevFeedHome = true;

    private RobotDataUtil dataUtil = new RobotDataUtil(logName);
    private SimpleBooleanProperty startStopVisible = new SimpleBooleanProperty(true);

    // update robot thread
    private FollowPositionData followPositionData;
    private Thread robotThread;

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        simInfo.setPadding(new Insets(0, 5, 0, 5));
        simInfo2.setPadding(new Insets(0, 5, 2.5, 5));
        simInfo2.setAlignment(Pos.CENTER);
        simInfoHousing.getChildren().addAll(simInfo, simInfo2);

        setFontBold(velocityLb, 14);
        setFont(velocityXLb, 14); setFont(commaLb2, 14);
        setFont(velocityYLb, 14); setFont(commaLb3, 14);
        setFont(velocityThLb, 14);

        startStopBtn.visibleProperty().bind(startStopVisible);

        setFontBold(accelLb, 14);
        setFont(accelXLb, 14); setFont(commaLb4, 14);
        setFont(accelYLb, 14); setFont(commaLb5, 14);
        setFont(accelThLb, 14);

        setFontBold(ringsLb, 14); setFont(numRingsLb, 14);

        setFontBold(nodeLb, 14); setFont(nodeNumLb, 14);
        setFontBold(timeLb, 14); setFont(curTimeLb, 14);

        simInfo.getChildren().addAll(velocityLb, velocityXLb, commaLb2, velocityYLb, commaLb3,
                velocityThLb, startStopBtn, restartBtn);
        simInfo2.getChildren().addAll(accelLb, accelXLb, commaLb4, accelYLb, commaLb5, accelThLb,
                ringsLb, numRingsLb);
        simPane.getChildren().addAll(nodeLb, nodeNumLb, timeLb, curTimeLb, pathPointGroup);

        dataUtil.parseLogFile();
        followPositionData = new FollowPositionData(dataUtil, startStopVisible, this);
        robotThread = new Thread(followPositionData, "UpdateRobotThread");

        nodeSlider = new Slider(1, dataUtil.getNumOfPoints(), 0);
        nodeSlider.setLayoutX(435); nodeSlider.setLayoutY(575); nodeSlider.setPrefWidth(150);

        nodeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (manual) {
                nodeSlider.setValue(newVal.intValue());
                followPositionData.setCounter((int) nodeSlider.getValue() - 1);
                pathPointGroup.getChildren().clear();
                updateRobot(dataUtil.getData(followPositionData.getCounter()), true);
                startStopVisible.set(nodeSlider.getValue() != dataUtil.getNumOfPoints());
                //System.out.println(followPositionData.getCounter() + " " + nodeSlider.getValue());
            }
        });

        nodeSlider.setOnMouseClicked(e -> nodeSliderAction());
        nodeSlider.setOnMouseDragged(e -> nodeSliderAction());
        nodeSlider.setOnKeyPressed(e -> nodeSliderAction());

        simPane.getChildren().add(nodeSlider);

        startStopBtn.setOnAction(e -> {
            switch (startStopBtn.getText()) {
                case "Start":
                    robotThread.start();
                    startStopBtn.setText("Pause");
                    restartBtn.setVisible(true);
                    startBtnPressed = true;
                    break;
                case "Pause":
                    followPositionData.setPause(true);
                    startStopBtn.setText("Resume");
                    break;
                case "Resume":
                    followPositionData.setPause(false);
                    startStopBtn.setText("Pause");
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            if (followPositionData.getCounter() == dataUtil.getNumOfPoints()) {
                robotThread = new Thread(followPositionData, "UpdateRobotThread");
                robotThread.start();
            }
            //System.out.println(followPositionData.getCounter() + " " + dataUtil.getNumOfPoints());
            startStopBtn.setText("Pause");
            startStopVisible.set(true);
            followPositionData.setCounter(0);
            followPositionData.setPause(false);
        });

        robot = new Robot(robotLength, robotLength);
        updateRobot(dataUtil.getData(0), false);
        simPane.getChildren().add(robot);

        mainPane.setBottom(simInfoHousing);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 55);
        primaryStage.setTitle("Match Replayer");
        primaryStage.setScene(scene);
    }

    public void updateRobot(DataPoint data, boolean sliderMoved) {

        // update current node, time since start
        nodeNumLb.setText((followPositionData.getCounter() + 1) + "");

        if (!sliderMoved) {
            manual = false;
            nodeSlider.setValue(followPositionData.getCounter() + 1);
        }

        curTimeLb.setText(String.format("%.2f", data.sinceStart / 1000));

        // update robot xy
        robot.setPosition(data.x, data.y);
        double xCor = getXPixel(data.x);
        double yCor = getYPixel(data.y);

        // update robot theta
        robot.setTheta(data.theta);

        // color depending on rings in robot
        if (data.numRings == 3) {
            robot.updateColor(new Stop[]{
                    new Stop(0, Color.rgb(50, 205, 50, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
        } else if (data.numRings == 2) {
            robot.updateColor(new Stop[]{
                    new Stop(0, Color.rgb(255, 225, 53, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
        } else if (data.numRings == 1) {
            robot.updateColor(new Stop[]{
                    new Stop(0, Color.rgb(255, 0, 56, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
        } else {
            robot.updateColor();
        }

        // update xy and theta text
        xInchLb.setText(String.format("%.2f", data.x));
        yInchLb.setText(String.format("%.2f", data.y));
        thetaLb.setText(String.format("%.2f", data.theta));

        // update velocity text
        velocityXLb.setText(String.format("%.2f", data.velocityX));
        velocityYLb.setText(String.format("%.2f", data.velocityY));
        velocityThLb.setText(String.format("%.2f", data.velocityTheta));

        // draw robot path dots, color based on velocity (red = slow, green = fast)
        int velocityFactor = (int) Math.sqrt(Math.pow(data.velocityX, 2) + Math.pow(data.velocityY, 2));
        Circle pathPoint = new Circle(xCor, yCor, 3, Color.hsb(velocityFactor * 2.25, 1, 1));
        pathPointGroup.getChildren().add(pathPoint);

        // update acceleration text
        accelXLb.setText(String.format("%.2f", data.accelX));
        accelYLb.setText(String.format("%.2f", data.accelY));
        accelThLb.setText(String.format("%.2f", data.accelTheta));

        numRingsLb.setText(data.numRings + "");

        if (prevFeedHome && !data.feedHome) {

            Shape ring = Shape.subtract(new Circle(15), new Circle(8.5));
            ring.setFill(Color.YELLOW);
            simPane.getChildren().add(ring);

            double shooterX = data.x + 6.5 * Math.sin(data.theta);
            double shooterY = data.y - 6.5 * Math.cos(data.theta);
            Line path = new Line(getXPixel(shooterX), getYPixel(shooterY), getXPixel(108), getYPixel(150));

            PathTransition ringLaunch = new PathTransition(Duration.millis(1000), path, ring);
            ringLaunch.setOnFinished(e -> simPane.getChildren().remove(ring));
            ringLaunch.play();
        }
        prevFeedHome = data.feedHome;
    }

    public void clearPathPoints() {
        pathPointGroup.getChildren().clear();
    }

    public void nodeSliderAction() {
        if (startBtnPressed) {
            manual = true;
            followPositionData.setPause(true);
            startStopBtn.setText("Resume");
        }
    }

    @Override public void endTasks() {
        followPositionData.endThread();
    }
}
