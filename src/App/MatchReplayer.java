package App;

import Threads.FollowPositionData;
import Utilities.DataPoint;
import Utilities.RobotDataUtil;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static App.Robot.robotLength;
import static Utilities.ConversionUtil.*;

@SuppressWarnings("FieldMayBeFinal")
public class MatchReplayer extends PlayerBase {

    // **************************************************************************************************
    private final static String logName = "RobotData5";
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
    private Label velocityThetaLb = new Label("n/a");
    private Label accelLb = new Label("Accel:");
    private Label accelXLb = new Label("n/a");
    private Label commaLb4 = new Label(",");
    private Label accelYLb = new Label("n/a");
    private Label commaLb5 = new Label(",");
    private Label accelThetaLb = new Label("n/a");
    private Label armLb = new Label("  Arm: ");
    private Label armState = new Label("n/a");
    private Label commaLb6 = new Label(",");
    private Label stoneClamped = new Label("n/a");
    private Label commaLb7 = new Label(",");
    private Label tryingToDeposit = new Label("n/a");

    private Text nodeLb = new Text(507, 25, "Node:");
    private Text nodeNum = new Text(550, 25, "n/a");
    private Text timeLb = new Text(507, 45, "Time:");
    private Text curTime = new Text(545, 45, "n/a");

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

        velocityLb.setFont(Font.font(Font.getDefault() + "", FontWeight.BOLD, 14));
        velocityXLb.setFont(Font.font(14)); commaLb2.setFont(Font.font(14));
        velocityYLb.setFont(Font.font(14)); commaLb3.setFont(Font.font(14));
        velocityThetaLb.setFont(Font.font(14));

        startStopBtn.visibleProperty().bind(startStopVisible);

        accelLb.setFont(Font.font(Font.getDefault() + "", FontWeight.BOLD, 14));
        accelXLb.setFont(Font.font(14)); commaLb4.setFont(Font.font(14));
        accelYLb.setFont(Font.font(14)); commaLb5.setFont(Font.font(14));
        accelThetaLb.setFont(Font.font(14));
        armLb.setFont(Font.font(Font.getDefault() + "", FontWeight.BOLD, 14));
        armState.setFont(Font.font(14)); commaLb6.setFont(Font.font(14));
        stoneClamped.setFont(Font.font(14)); commaLb7.setFont(Font.font(14));
        tryingToDeposit.setFont(Font.font(14));

        nodeLb.setFont(Font.font(Font.getDefault() + "", FontWeight.BOLD, 14));
        nodeNum.setFont(Font.font(14));
        timeLb.setFont(Font.font(Font.getDefault() + "", FontWeight.BOLD, 14));
        curTime.setFont(Font.font(14));

        simInfo.getChildren().addAll(velocityLb, velocityXLb, commaLb2, velocityYLb, commaLb3,
                velocityThetaLb, startStopBtn, restartBtn);
        simInfo2.getChildren().addAll(accelLb, accelXLb, commaLb4, accelYLb, commaLb5, accelThetaLb,
                armLb, armState, commaLb6, stoneClamped, commaLb7, tryingToDeposit);
        simPane.getChildren().addAll(nodeLb, nodeNum, timeLb, curTime, pathPointGroup);

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

        primaryStage.setOnCloseRequest(e -> followPositionData.endThread());

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
        nodeNum.setText((followPositionData.getCounter() + 1) + "");

        if (!sliderMoved) {
            manual = false;
            nodeSlider.setValue(followPositionData.getCounter() + 1);
        }

        curTime.setText(String.format("%.2f", data.sinceStart / 1000));

        // update robot xy
        robot.setPosition(data.x, data.y);
        double xCor = getXPixel(data.x);
        double yCor = getYPixel(data.y);

        // update robot theta
        robot.setTheta(data.theta);

        // color robot yellow if stone in robot
        if (data.stoneInRobot) {
            robot.updateColor(new Stop[]{
                    new Stop(0, Color.rgb(255, 225, 53, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))});
        } else {
            robot.updateColor();
        }

        // update xy and theta text
        xInchLb.setText(String.format("%.2f", data.x));
        yInchLb.setText(String.format("%.2f", data.y));
        thetaLb.setText(String.format("%.2f", data.theta));

        // update velocity text
        double velocityX = data.velocityX, velocityY = data.velocityY, velocityTheta = data.velocityTheta;
        velocityXLb.setText(String.format("%.2f", velocityX));
        velocityYLb.setText(String.format("%.2f", velocityY));
        velocityThetaLb.setText(String.format("%.2f", velocityTheta));

        // draw robot path dots, color based on velocity (red = slow, green = fast)
        int velocityFactor = (int) Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        Circle pathPoint = new Circle(xCor, yCor, 2, Color.hsb(velocityFactor * 1.5, 1, 1));
        pathPointGroup.getChildren().add(pathPoint);

        // update acceleration text
        double accelX = data.accelX; double accelY = data.accelY; double accelTheta = data.accelTheta;
        accelXLb.setText(String.format("%.2f", accelX));
        accelYLb.setText(String.format("%.2f", accelY));
        accelThetaLb.setText(String.format("%.2f", accelTheta));

        // update stone clamped text
        if (data.stoneClamped) {
            stoneClamped.setText("Clamped");
        } else {
            stoneClamped.setText("Not Clamped");
        }

        // update trying to deposit text
        if (data.tryingToDeposit) {
            tryingToDeposit.setText("Depositing");
        } else {
            tryingToDeposit.setText("Not Depositing");
        }

        // update arm state text
        if (data.armIsHome) {
            armState.setText("Home");
        } else if (data.armIsDown) {
            armState.setText("Down");
        } else if (data.armIsOut) {
            armState.setText("Out");
        }
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
}
