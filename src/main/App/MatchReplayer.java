package main.App;

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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.Threads.FollowLogData;
import main.Utilities.DataPoint;
import main.Utilities.RobotDataUtil;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static main.Utilities.ConversionUtil.getXPixel;
import static main.Utilities.ConversionUtil.getYPixel;

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
    private Label velocityLb = new Label("  V, w:");
    private Label velocityMagLb = new Label("n/a");
    private Label commaLb3 = new Label(",");
    private Label velocityThLb = new Label("n/a");
    private Label targetHubLb = new Label("Target:");
    private Label targetHubLb_V = new Label("n/a");
    private Label stateLb = new Label("  n/a");
    private Label cyclesLb = new Label("  # Cycles:");
    private Label numCyclesLb = new Label("n/a");
    private Label avgCyclesLb = new Label("  Avg Cycle Time:");
    private Label avgCycleTimeLb = new Label("n/a");

    private Text nodeLb = new Text(10, 565, "Node:");
    private Text nodeNumLb = new Text(53, 565, "n/a");
    private Text timeLb = new Text(10, 585, "Time:");
    private Text curTimeLb = new Text(48, 585, "n/a");

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

        setFontBold(targetHubLb, 14); setFont(targetHubLb_V, 14);
        setFontBold(stateLb, 14);
        setFontBold(cyclesLb, 14); setFont(numCyclesLb, 14);
        setFontBold(avgCyclesLb, 14); setFont(avgCycleTimeLb, 14);

        setFontBold(nodeLb, 14); setFont(nodeNumLb, 14);
        setFontBold(timeLb, 14); setFont(curTimeLb, 14);

        simInfo.getChildren().addAll(velocityLb, velocityMagLb, commaLb3, velocityThLb,
                startStopBtn, restartBtn);
        simInfo2.getChildren().addAll(targetHubLb, targetHubLb_V, stateLb, cyclesLb, numCyclesLb, avgCyclesLb, avgCycleTimeLb);
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

        robot = new Robot();
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
        curTimeLb.setText(String.format("%.2f", data.timeSinceSt / 1000));

        // update robot position
        robot.setPose(data.x, data.y, data.theta);

        // update slides
        turretGroup.getChildren().clear();

//        double extendedPos = 11.5 + data.depositSlidesDist;
//        double theta = data.turretTheta;
//        double TURRET_Y_OFFSET = 2.06066;
//        double turretCenterX = data.x + TURRET_Y_OFFSET * cos(data.theta);
//        double turretCenterY = data.y + TURRET_Y_OFFSET * sin(data.theta);
//
//        double[] leftSlidesX = {-2 * cos(data.turretTheta) - -4.5 * sin(data.turretTheta) + turretCenterX, -2 * cos(data.turretTheta) - extendedPos * sin(data.turretTheta) + turretCenterX, -3.5 * cos(data.turretTheta) - extendedPos * sin(data.turretTheta) + turretCenterX, -3.5 * cos(data.turretTheta) - -4.5 * sin(data.turretTheta) + turretCenterX};
//        double[] leftSlidesY = {-2 * sin(data.turretTheta) + -4.5 * cos(data.turretTheta) + turretCenterY, -2 * sin(data.turretTheta) + extendedPos * cos(data.turretTheta) + turretCenterY, -3.5 * sin(data.turretTheta) + extendedPos * cos(data.turretTheta) + turretCenterY, -3.5 * sin(data.turretTheta) + -4.5 * cos(data.turretTheta) + turretCenterY};
//        double[] rightSlidesX = {2 * cos(data.turretTheta) - -4.5 * sin(data.turretTheta) + turretCenterX, 2 * cos(data.turretTheta) - extendedPos * sin(data.turretTheta) + turretCenterX, 3.5 * cos(data.turretTheta) - extendedPos * sin(data.turretTheta) + turretCenterX, 3.5 * cos(data.turretTheta) - -4.5 * sin(data.turretTheta) + turretCenterX};
//        double[] rightSlidesY = {2 * sin(data.turretTheta) + -4.5 * cos(data.turretTheta) + turretCenterY, 2 * sin(data.turretTheta) + extendedPos * cos(data.turretTheta) + turretCenterY, 3.5 * sin(data.turretTheta) + extendedPos * cos(data.turretTheta) + turretCenterY, 3.5 * sin(data.turretTheta) + -4.5 * cos(theta) + turretCenterY};
//        Polygon leftSlidesRect = new Polygon();
//        Polygon rightSlidesRect = new Polygon();
//        leftSlidesRect.getPoints().addAll(
//                getXPixel(leftSlidesX[0]), getYPixel(leftSlidesY[0]),
//                getXPixel(leftSlidesX[1]), getYPixel(leftSlidesY[1]),
//                getXPixel(leftSlidesX[2]), getYPixel(leftSlidesY[2]),
//                getXPixel(leftSlidesX[3]), getYPixel(leftSlidesY[3])
//        );
//        rightSlidesRect.getPoints().addAll(
//                getXPixel(rightSlidesX[0]), getYPixel(rightSlidesY[0]),
//                getXPixel(rightSlidesX[1]), getYPixel(rightSlidesY[1]),
//                getXPixel(rightSlidesX[2]), getYPixel(rightSlidesY[2]),
//                getXPixel(rightSlidesX[3]), getYPixel(rightSlidesY[3])
//        );
//        leftSlidesRect.setFill(Color.RED);
//        rightSlidesRect.setFill(Color.RED);

        // update intake slides
        /*double intakeExtendedPos = 13.5;//data.intakeSlidesExtend ? 13.5 : 0;
        // @pi/2: cos=0, sin = 1
        //
        double[] leftIntakeX = {-2.5 * sin(data.theta) - 9 * cos(data.theta) + data.x, -2.5 * sin(data.theta) - (9 + intakeExtendedPos) * cos(data.theta) + data.x, -3 * sin(data.theta) - (9 + intakeExtendedPos) * sin(data.theta) + data.x, -3 * sin(data.theta) - 9 * cos(data.theta) + data.x};
        double[] leftIntakeY = {-2.5 * cos(data.theta) + 9 * sin(data.theta) + data.y, -2.5 * cos(data.theta) + (9 + intakeExtendedPos) * sin(data.theta) + data.y, -3 * cos(data.theta) + (9 + intakeExtendedPos) * cos(data.theta) + data.y, -3 * cos(data.theta) + 9 * sin(data.theta) + data.y};
        double[] rightIntakeX = {2.5 * sin(data.theta) - 9 * cos(data.theta) + data.x, 2.5 * sin(data.theta) - (9 + intakeExtendedPos) * cos(data.theta) + data.x, 3 * sin(data.theta) - (9 + intakeExtendedPos) * sin(data.theta) + data.x, 3 * sin(data.theta) - 9 * cos(data.theta) + data.x};
        double[] rightIntakeY = {2.5 * cos(data.theta) + 9 * sin(data.theta) + data.y, 2.5 * cos(data.theta) + (9 + intakeExtendedPos) * sin(data.theta) + data.y, 3 * cos(data.theta) + (9 + intakeExtendedPos) * cos(data.theta) + data.y, 3 * cos(data.theta) + 9 * sin(data.theta) + data.y};
        Polygon intakeLeftRect = new Polygon();
        Polygon intakeRightRect = new Polygon();
        intakeLeftRect.getPoints().addAll(
                getXPixel(leftIntakeX[0]), getYPixel(leftIntakeY[0]),
                getXPixel(leftIntakeX[1]), getYPixel(leftIntakeY[1]),
                getXPixel(leftIntakeX[2]), getYPixel(leftIntakeY[2]),
                getXPixel(leftIntakeX[3]), getYPixel(leftIntakeY[3])
        );
        intakeRightRect.getPoints().addAll(
                getXPixel(rightIntakeX[0]), getYPixel(rightIntakeY[0]),
                getXPixel(rightIntakeX[1]), getYPixel(rightIntakeY[1]),
                getXPixel(rightIntakeX[2]), getYPixel(rightIntakeY[2]),
                getXPixel(rightIntakeX[3]), getYPixel(rightIntakeY[3])
        );
        intakeLeftRect.setFill(Color.ORANGE);
        intakeRightRect.setFill(Color.ORANGE);*/

//        double cx = data.x + 0.5 * sin(data.theta) - 4 * cos(data.theta);
//        double cy = data.y - 0.5 * cos(data.theta) - 4 * sin(data.theta);
//        double r = 8.5 / 2;
//        Circle turretCircle = new Circle(getXPixel(cx), getYPixel(cy), r * inchToPixel);
//        turretCircle.setFill(Color.GREY);

        // color depending on rings in robot
//        if (data.numRings == 3) {
//            robot.updateColor(new Stop[] {
//                    new Stop(0, Color.rgb(50, 205, 50, 0.85)),
//                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
//        } else if (data.numRings == 2) {
//            robot.updateColor(new Stop[] {
//                    new Stop(0, Color.rgb(255, 225, 53, 0.85)),
//                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
//        } else if (data.numRings == 1) {
//            robot.updateColor(new Stop[] {
//                    new Stop(0, Color.rgb(255, 0, 56, 0.85)),
//                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
//        } else {
//            robot.updateColor();
//        }

        // update xy and theta text
        xInchLb.setText(String.format("%.2f", data.x));
        yInchLb.setText(String.format("%.2f", data.y));
        thetaLb.setText(String.format("%.2f", data.theta));

        // update velocity text
        double velocityMagnitude = Math.hypot(data.vx, data.vy);
        velocityMagLb.setText(String.format("%.2f", velocityMagnitude));
        velocityThLb.setText(String.format("%.2f", data.w));

        // draw robot path dots, color based on velocity (red = slow, green = fast)
        Circle pathPoint = new Circle(getXPixel(data.x), getYPixel(data.y), 3, Color.hsb(velocityMagnitude * 2, 1, 1));
        pathPointGroup.getChildren().add(pathPoint);

        // remove excess points
//        if (pathPointGroup.getChildren().size() > 700) {
//            pathPointGroup.getChildren().remove(0);
//        }

        // update target hub
        targetHubLb_V.setText(data.depositTarget);

        // update robot state
//        if (data.intakeTransfer) {
//            stateLb.setText("  Intaking/Transfer");
//        } else if (data.depositing) {
//            stateLb.setText("  Depositing");
//        }

        // update cycle data
        numCyclesLb.setText(data.numCycles + "");
        avgCycleTimeLb.setText(String.format("%.2f", data.avgCycleTime));
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
