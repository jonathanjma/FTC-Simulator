package main.App;

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
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Threads.FollowLogData;
import main.Utilities.DataPoint;
import main.Utilities.RobotDataUtil;

import static java.lang.Math.*;
import static main.App.Robot.robotLength;
import static main.Utilities.ConversionUtil.*;

@SuppressWarnings("FieldMayBeFinal")
public class MatchReplayer extends PlayerBase {

    private VBox simInfoHousing = new VBox(2.5);
    private HBox simInfo2 = new HBox(5);

    private Group pathPointGroup = new Group();
    private Group turretGroup = new Group();

    private Slider nodeSlider;
    private boolean manual = false;
    private boolean startBtnPressed = false;

    // ui labels
    private Label velocityLb = new Label("  Vx, w:");
    private Label velocityMagLb = new Label("n/a");
    private Label commaLb3 = new Label(",");
    private Label velocityThLb = new Label("n/a");
    private Label accelLb = new Label("  Ax, a:");
    private Label accelMagLb = new Label("n/a");
    private Label commaLb4 = new Label(",");
    private Label accelThLb = new Label("n/a");
    private Label ringsLb = new Label("# Rings:");
    private Label numRingsLb = new Label("n/a");
    private Label shootingLb = new Label("  n/a");
    private Label cyclesLb = new Label("  # Cycles:");
    private Label numCyclesLb = new Label("n/a");
    private Label avgCyclesLb = new Label("  Avg Cycle Time:");
    private Label avgCycleTimeLb = new Label("n/a");

    private Text nodeLb = new Text(515, 25, "Node:");
    private Text nodeNumLb = new Text(558, 25, "n/a");
    private Text timeLb = new Text(515, 45, "Time:");
    private Text curTimeLb = new Text(553, 45, "n/a");

    private int prevRings = 0;

    private RobotDataUtil dataUtil = new RobotDataUtil(true);
    // private RobotDataUtil dataUtil = new RobotDataUtil("RobotData3");
    private SimpleBooleanProperty startStopDisabled = new SimpleBooleanProperty(false);

    // update robot thread
    private FollowLogData followLogData;
    private Thread robotThread;

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        simInfo.setPadding(new Insets(0, 5, 0, 5));
        simInfo2.setPadding(new Insets(0, 5, 2.5, 5));
        simInfo2.setAlignment(Pos.CENTER);
        simInfoHousing.getChildren().addAll(simInfo, simInfo2);

        setFontBold(velocityLb, 14);
        setFont(velocityMagLb, 14); velocityMagLb.setPrefWidth(35); setFont(commaLb3, 14);
        setFont(velocityThLb, 14); velocityThLb.setPrefWidth(35);

        startStopBtn.disableProperty().bind(startStopDisabled);

        setFontBold(accelLb, 14);
        setFont(accelMagLb, 14); accelMagLb.setPrefWidth(40); setFont(commaLb4, 14);
        setFont(accelThLb, 14); accelThLb.setPrefWidth(35);

        setFontBold(ringsLb, 14); setFont(numRingsLb, 14);
        setFontBold(shootingLb, 14);
        setFontBold(cyclesLb, 14); setFont(numCyclesLb, 14);
        setFontBold(avgCyclesLb, 14); setFont(avgCycleTimeLb, 14);

        setFontBold(nodeLb, 14); setFont(nodeNumLb, 14);
        setFontBold(timeLb, 14); setFont(curTimeLb, 14);

        simInfo.getChildren().addAll(velocityLb, velocityMagLb, commaLb3, velocityThLb, accelLb, accelMagLb, commaLb4, accelThLb,
                startStopBtn, restartBtn);
        simInfo2.getChildren().addAll(ringsLb, numRingsLb, shootingLb, cyclesLb, numCyclesLb, avgCyclesLb, avgCycleTimeLb);
        simPane.getChildren().addAll(nodeLb, nodeNumLb, timeLb, curTimeLb, pathPointGroup);

        dataUtil.parseLogFile();
        followLogData = new FollowLogData(dataUtil, startStopDisabled, this);
        robotThread = new Thread(followLogData, "UpdateRobotThread");

        nodeSlider = new Slider(1, dataUtil.getNumOfPoints(), 0);
        nodeSlider.setLayoutX(435); nodeSlider.setLayoutY(575); nodeSlider.setPrefWidth(150);

        nodeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (manual) {
                nodeSlider.setValue(newVal.intValue());
                followLogData.setCounter((int) nodeSlider.getValue() - 1);
                pathPointGroup.getChildren().clear();
                updateRobot(dataUtil.getData(followLogData.getCounter()), true);
                startStopDisabled.set(nodeSlider.getValue() == dataUtil.getNumOfPoints());
                //System.out.println(followPositionData.getCounter() + " " + nodeSlider.getValue());
            }
        });

        nodeSlider.setOnMouseClicked(e -> nodeSliderAction());
        nodeSlider.setOnMouseDragged(e -> nodeSliderAction());
        nodeSlider.setOnKeyPressed(e -> nodeSliderAction());

        simPane.getChildren().add(nodeSlider);

        startStopBtn.setOnAction(e -> {
            switch (state) {
                case NotStarted:
                    robotThread.start();
                    setState(State.Playing);
                    restartBtn.setDisable(false);
                    startBtnPressed = true;
                    break;
                case Playing:
                    followLogData.setPause(true);
                    setState(State.Paused);
                    break;
                case Paused:
                    if (robotThread.isAlive()) {
                        followLogData.setPause(false);
                        setState(State.Playing);
                    } else {
                        restart((int) nodeSlider.getValue() - 1);
                    }
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            restart();
        });

        robot = new Robot(robotLength, robotLength);
        updateRobot(dataUtil.getData(0), false);
        simPane.getChildren().addAll(robot, turretGroup);
        nodeSlider.toFront();

        mainPane.setBottom(simInfoHousing);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 55);
        primaryStage.setTitle(dataUtil.getInfo() + " (" + dataUtil.getLogName() + ") — Match Replayer");
        primaryStage.setScene(scene);
    }

    public void updateRobot(DataPoint data, boolean sliderMoved) {

        // update current node, time since start
        nodeNumLb.setText((followLogData.getCounter() + 1) + "");

        // update slider position as match progresses
        if (!sliderMoved) {
            manual = false;
            nodeSlider.setValue(followLogData.getCounter() + 1);
        }

        // update time since start
        curTimeLb.setText(String.format("%.2f", data.sinceStart / 1000));

        // update robot position
        robot.setPose(data.x, data.y, data.theta);

        // update turret
        turretGroup.getChildren().clear();
        if (data.turretGlobalTheta != Double.MAX_VALUE) {
            double cx = data.x + 0.5 * sin(data.theta) - 4 * cos(data.theta);
            double cy = data.y - 0.5 * cos(data.theta) - 4 * sin(data.theta);
            double r = 8.5 / 2;
            Circle turretCircle = new Circle(getXPixel(cx), getYPixel(cy), r * inchToPixel);
            turretCircle.setFill(Color.GREY);

            double[] xcoords = {cx - r * sin(data.turretGlobalTheta), cx + r*sqrt(2) * cos(data.turretGlobalTheta + PI/4), cx + r*sqrt(2) * cos(data.turretGlobalTheta - PI/4), cx + r * sin(data.turretGlobalTheta)};
            double[] ycoords = {cy + r * cos(data.turretGlobalTheta), cy + r*sqrt(2) * sin(data.turretGlobalTheta + PI/4), cy + r*sqrt(2) * sin(data.turretGlobalTheta - PI/4), cy - r * cos(data.turretGlobalTheta)};
            Polygon turretRect = new Polygon();
            turretRect.getPoints().addAll(
                    getXPixel(xcoords[0]), getYPixel(ycoords[0]),
                    getXPixel(xcoords[1]), getYPixel(ycoords[1]),
                    getXPixel(xcoords[2]), getYPixel(ycoords[2]),
                    getXPixel(xcoords[3]), getYPixel(ycoords[3])
            );
            turretRect.setFill(Color.GREY);

            turretGroup.getChildren().addAll(turretCircle, turretRect);
        }

        // color depending on rings in robot
        if (data.numRings == 3) {
            robot.updateColor(new Stop[] {
                    new Stop(0, Color.rgb(50, 205, 50, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
        } else if (data.numRings == 2) {
            robot.updateColor(new Stop[] {
                    new Stop(0, Color.rgb(255, 225, 53, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
        } else if (data.numRings == 1) {
            robot.updateColor(new Stop[] {
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
        double velocityMagnitude = Math.hypot(data.velocityX, data.velocityY);
        velocityMagLb.setText(String.format("%.2f", velocityMagnitude));
        velocityThLb.setText(String.format("%.2f", data.velocityTheta));

        // draw robot path dots, color based on velocity (red = slow, green = fast)
        Circle pathPoint = new Circle(getXPixel(data.x), getYPixel(data.y), 3, Color.hsb(velocityMagnitude * 2, 1, 1));
        pathPointGroup.getChildren().add(pathPoint);

        // remove excess points
//        if (pathPointGroup.getChildren().size() > 700) {
//            pathPointGroup.getChildren().remove(0);
//        }

        // update acceleration text
        double accelMagnitude = Math.hypot(data.accelX, data.accelY);
        accelMagLb.setText(String.format("%.1f", accelMagnitude));
        accelThLb.setText(String.format("%.1f", data.accelTheta));

        // update rings in robot
        numRingsLb.setText(data.numRings + "");

        // update shooting status
        if (!data.magHome) {
            shootingLb.setText("  Shooting");
        } else {
            shootingLb.setText("  Intaking Rings");
        }

        // update cycle data
        numCyclesLb.setText(data.numCycles + "");
        avgCycleTimeLb.setText(!Double.isNaN(data.avgCycleTime) ? String.format("%.2f", data.avgCycleTime) : 0 + "");

        // show shoot animation when rings are feeded
        if (data.numRings != prevRings && prevRings != 0 && prevRings != 4) {

            Shape ring = Shape.subtract(new Circle(15), new Circle(8.5));
            ring.setFill(Color.YELLOW);
            simPane.getChildren().add(ring);

            double shooterX = data.x + 0.5 * sin(data.theta) - 4 * cos(data.theta);
            double shooterY = data.y - 0.5 * cos(data.theta) - 4 * sin(data.theta);
            double targetX;
            if (data.lastTarget == 0) {
                targetX = 76.5;
            } else if (data.lastTarget == 1) {
                targetX = 84;
            } else if (data.lastTarget == 2) {
                targetX = 91.5;
            } else {
                targetX = 108;
            }

            Line ringPath = new Line(getXPixel(shooterX), getYPixel(shooterY), getXPixel(targetX), getYPixel(150));
            PathTransition ringLaunch = new PathTransition(Duration.millis(1000), ringPath, ring);
            ringLaunch.setOnFinished(e -> simPane.getChildren().remove(ring));
            ringLaunch.play();
        }
        prevRings = data.numRings;
    }

    public void restart() {
        restart(0);
    }

    // restart to different counter values
    public void restart(int counter) {
        if (!robotThread.isAlive()) {
            robotThread = new Thread(followLogData, "UpdateRobotThread");
            robotThread.start();
        }

        startStopDisabled.set(false);
        followLogData.setCounter(counter);
        followLogData.setPause(false);
        setState(State.Playing);
    }

    public void clearPathPoints() {
        pathPointGroup.getChildren().clear();
    }

    public void nodeSliderAction() {
        if (startBtnPressed) {
            manual = true;
            followLogData.setPause(true);
            setState(State.Paused);
        }
    }

    @Override
    public void endTasks() {
        followLogData.endThread();
    }
}
