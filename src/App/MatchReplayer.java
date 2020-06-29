package App;

import Threads.FollowPositionData;
import Utilities.RobotDataUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import static Utilities.ConversionUtil.*;

@SuppressWarnings("FieldCanBeLocal")
public class MatchReplayer {

    // **************************************************************************************************
    private final static String logName = "RobotData5";
    // **************************************************************************************************
    
    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private VBox simInfoHousing = new VBox(2.5);
    private HBox simInfo1 = new HBox(5);
    private HBox simInfo2 = new HBox(5);

    private Group pathPointGroup = new Group();
    private Circle pathPoint;

    private Slider nodeSlider;
    private boolean manual = false;
    private boolean startBtnPressed = false;

    // ui labels
    private Label corLb = new Label("Position: (");
    private Label xInchLb = new Label("n/a");
    private Label commaLb1 = new Label(",");
    private Label yInchLb = new Label("n/a");
    private Label thetaLb1 = new Label(")  Theta:");
    private Label thetaLb = new Label("n/a");
    private Label velocityLb = new Label("  Velocity:");
    private Label velocityXLb = new Label("n/a");
    private Label commaLb2 = new Label(",");
    private Label velocityYLb = new Label("n/a");
    private Label commaLb3 = new Label(",");
    private Label velocityThetaLb = new Label("n/a");
    private Button startStopBtn = new Button("Start");
    private Button restartBtn = new Button("Restart");

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

    private Button backBtn = new Button("Back");

    private RobotDataUtil dataUtil = new RobotDataUtil(logName);
    private SimpleBooleanProperty startStopVisible = new SimpleBooleanProperty(true);

    private Rectangle robotRect;

    // update robot robotThread
    private FollowPositionData followPositionData;
    private Thread robotThread;

    public void launch(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        simInfo1.setPadding(new Insets(0, 5, 0, 5));
        simInfo1.setAlignment(Pos.CENTER);
        simInfo2.setPadding(new Insets(0, 5, 2.5, 5));
        simInfo2.setAlignment(Pos.CENTER);
        simInfoHousing.getChildren().addAll(simInfo1, simInfo2);

        corLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); commaLb1.setFont(Font.font(14)); xInchLb.setFont(Font.font(14)); yInchLb.setFont(Font.font(14));
        thetaLb1.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); thetaLb.setFont(Font.font(14));
        velocityLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); velocityXLb.setFont(Font.font(14)); commaLb2.setFont(Font.font(14)); velocityYLb.setFont(Font.font(14)); commaLb3.setFont(Font.font(14)); velocityThetaLb.setFont(Font.font(14));
        restartBtn.setVisible(false);

        startStopBtn.visibleProperty().bind(startStopVisible);

        accelLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); accelXLb.setFont(Font.font(14)); commaLb4.setFont(Font.font(14)); accelYLb.setFont(Font.font(14)); commaLb5.setFont(Font.font(14)); accelThetaLb.setFont(Font.font(14));
        armLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); armState.setFont(Font.font(14)); commaLb6.setFont(Font.font(14)); stoneClamped.setFont(Font.font(14)); commaLb7.setFont(Font.font(14)); tryingToDeposit.setFont(Font.font(14));

        nodeLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); nodeNum.setFont(Font.font(14));
        timeLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); curTime.setFont(Font.font(14));

        simInfo1.getChildren().addAll(corLb, xInchLb, commaLb1, yInchLb, thetaLb1, thetaLb,
                velocityLb, velocityXLb, commaLb2, velocityYLb, commaLb3, velocityThetaLb,
                startStopBtn, restartBtn);
        simInfo2.getChildren().addAll(accelLb, accelXLb, commaLb4, accelYLb, commaLb5, accelThetaLb,
                armLb, armState, commaLb6, stoneClamped, commaLb7, tryingToDeposit);
        simPane.getChildren().addAll(nodeLb, nodeNum, timeLb, curTime, pathPointGroup);

        backBtn.setLayoutX(10); backBtn.setLayoutY(10);
        simPane.getChildren().addAll(backBtn);

        dataUtil.parseLogFile();
        followPositionData = new FollowPositionData(dataUtil, startStopVisible, this);
        robotThread = new Thread(followPositionData, "UpdateRobotThread");

        nodeSlider = new Slider(1, dataUtil.getNumOfPoints(), 0);
        nodeSlider.setLayoutX(435); nodeSlider.setLayoutY(575); nodeSlider.setPrefWidth(150);

        nodeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (manual) {
                nodeSlider.setValue(newVal.intValue());
                followPositionData.setCounter((int) nodeSlider.getValue()-1);
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
                    startStopBtn.setText("Pause"); restartBtn.setVisible(true);
                    startBtnPressed = true;
                    break;
                case "Pause":
                    followPositionData.setPause(true); startStopBtn.setText("Resume");
                    break;
                case "Resume":
                    followPositionData.setPause(false); startStopBtn.setText("Pause");
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            if (followPositionData.getCounter() == dataUtil.getNumOfPoints()) {
                robotThread = new Thread(followPositionData, "UpdateRobotThread");
                robotThread.start();
            }
            //System.out.println(followPositionData.getCounter() + " " + dataUtil.getNumOfPoints());
            startStopBtn.setText("Pause"); startStopVisible.set(true);
            followPositionData.setCounter(0); followPositionData.setPause(false);
        });

        backBtn.setOnMouseClicked(e -> {
            followPositionData.endThread();
            CombinedSim app = new CombinedSim();
            app.start(primaryStage);
        });
        backBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                followPositionData.endThread();
                CombinedSim app = new CombinedSim();
                app.start(primaryStage);
            }
        });
        primaryStage.setOnCloseRequest(e -> followPositionData.endThread());

        robotRect = new Rectangle(robotLength, robotLength);
        updateRobot(dataUtil.getData(0), false);
        simPane.getChildren().add(robotRect);

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simInfoHousing);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 55);
        primaryStage.setTitle("Match Replayer");
        primaryStage.setScene(scene);
    }

    public void updateRobot(Object[] data, boolean sliderMoved) {

        // update current node, time since start
        nodeNum.setText((followPositionData.getCounter()+1) +"");

        if (!sliderMoved) {
            manual = false;
            nodeSlider.setValue(followPositionData.getCounter()+1);
        }

        double time = (double) data[0];
        curTime.setText(String.format("%.2f", time / 1000));

        // update robot xy
        double xCor = getXPixel((double) data[1]);
        double yCor = getYPixel((double) data[2]);
        robotRect.setX(xCor - robotRadius);
        robotRect.setY(yCor - robotRadius);

        // update robot theta
        robotRect.setRotate(getFXTheta((double) data[3]));

        // color robot yellow if stone in robot
        Stop[] stops;
        if ((boolean) data[10]) {
            stops = new Stop[] {new Stop(0, Color.rgb(255, 225, 53, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))};
        } else {
            stops = new Stop[] {new Stop(0, Color.rgb(0, 0, 0, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))};
        }
        LinearGradient background = new LinearGradient(xCor, yCor, xCor+robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);

        // update xy and theta text
        xInchLb.setText(String.format("%.2f", (double) data[1]));
        yInchLb.setText(String.format("%.2f", (double) data[2]));
        thetaLb.setText(String.format("%.2f", (double) data[3]));

        // update velocity text
        double velocityX = (double) data[4], velocityY = (double) data[5], velocityTheta = (double) data[6];
        velocityXLb.setText(String.format("%.2f", velocityX));
        velocityYLb.setText(String.format("%.2f", velocityY));
        velocityThetaLb.setText(String.format("%.2f", velocityTheta));

        // draw robot path dots, color based on velocity (red = slow, green = fast)
        int velocityFactor = (int) Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        pathPoint = new Circle(xCor, yCor, 2, Color.hsb(velocityFactor * 1.5, 1, 1));
        pathPointGroup.getChildren().add(pathPoint);

        // update acceleration text
        double accelX = (double) data[7];
        double accelY = (double) data[8];
        double accelTheta = (double) data[9];
        accelXLb.setText(String.format("%.2f", accelX));
        accelYLb.setText(String.format("%.2f", accelY));
        accelThetaLb.setText(String.format("%.2f", accelTheta));

        // update stone clamped text
        if ((boolean) data[11]) {stoneClamped.setText("Clamped");}
        else {stoneClamped.setText("Not Clamped");}

        // update trying to deposit text
        if ((boolean) data[12]) {tryingToDeposit.setText("Depositing");}
        else {tryingToDeposit.setText("Not Depositing");}

        // update arm state text
        if ((boolean) data[13]) {armState.setText("Home");}
        else if ((boolean) data[14]) {armState.setText("Down");}
        else if ((boolean) data[15]) {armState.setText("Out");}
    }

    public void clearPathPoints() {
        pathPointGroup.getChildren().clear();
    }

    public void nodeSliderAction() {
        if (startBtnPressed) {
            manual = true;
            followPositionData.setPause(true); startStopBtn.setText("Resume");
        }
    }
}
