package App;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

import static java.lang.Thread.sleep;

@SuppressWarnings("FieldCanBeLocal")
public class MatchReplayer {

    // **************************************************************************************************
    private final static String logName = "RobotData77";
    private final static boolean logAcceleration = false;
    // **************************************************************************************************
    
    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private VBox simInfoHousing = new VBox(2.5);
    private HBox simInfo1 = new HBox(5);
    private HBox simInfo2 = new HBox(5);

    private Rectangle robotRect;

    private Group pathPointGroup = new Group();
    private Circle pathPoint;

//    private Slider nodeSlider;
//    private boolean isFollow = true;
//    private boolean startBtnPressed = false;

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

    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double inchToPixel = 4.16;
    private final static double robotLength = 18 * inchToPixel;
    private final static double robotRadius = robotLength / 2; //37.44

    private double xCor, yCor, theta;

    private RobotDataUtil dataUtil = new RobotDataUtil(logName, logAcceleration);
    private boolean pause = false; private int counter = 0;

    private double timeDiff;
    private boolean firstUpdate = true;

    // update robot thread
    private FollowPosData runnable = new FollowPosData();
    private Thread thread = new Thread(runnable);
    public class FollowPosData implements Runnable {
        private boolean active = true;

        public void run() {
            for (; counter < dataUtil.getNumOfPoints();) {
                if (active) {
                    if (counter == 1) {Platform.runLater(() -> pathPointGroup.getChildren().clear());}
                    if (!pause) {
                        Platform.runLater(() -> updateRobot(dataUtil.getData(counter)));
                        if (firstUpdate) {timeDiff = 0; firstUpdate = false;}
                        else timeDiff = dataUtil.getTimeDiff(counter);

                        try {sleep((long) timeDiff);}
                        catch (InterruptedException ex) {ex.printStackTrace(); }
                        counter++;
                    }
                    System.out.print("");
                    //System.out.println(isFollow + " " + pause);
                } else {thread.interrupt();}
            }
            Platform.runLater(() -> startStopBtn.setVisible(false));
        }

        public void endThread() {active = false;}
    }

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

//        nodeSlider = new Slider(1, dataUtil.getNumOfPoints(), 0);
//        nodeSlider.setLayoutX(435); nodeSlider.setLayoutY(575); nodeSlider.setPrefWidth(150);
//        nodeSlider.valueProperty().addListener((obs, oldval, newVal) -> {
//            if (!isFollow) {
//                nodeSlider.setValue(newVal.intValue());
//                counter = (int) nodeSlider.getValue(); //- 1;
//                pathPointGroup.getChildren().clear();
//                updateRobot(dataUtil.getData(counter));
//            }
//        });
//
//        nodeSlider.setOnMouseDragged(e -> {
//            isFollow = false;
//            pause = true; startStopBtn.setText("Resume");
//        });
//        nodeSlider.setOnKeyPressed(e -> {
//            isFollow = false;
//            pause = true; startStopBtn.setText("Resume");
//        });
//
//        simPane.getChildren().add(nodeSlider);

        startStopBtn.setOnAction(e -> {
            switch (startStopBtn.getText()) {
                case "Start":
                    thread.start(); thread.setName("UpdateRobotThread");
                    startStopBtn.setText("Pause");
                    restartBtn.setVisible(true);
                    break;
                case "Pause":
                    pause = true;
                    startStopBtn.setText("Resume");
                    break;
                case "Resume":
                    pause = false;
                    startStopBtn.setText("Pause");
                    //isFollow = true;
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            if (counter == dataUtil.getNumOfPoints()) {
                thread = new Thread(runnable);
                thread.start(); thread.setName("UpdateRobotThread");
                startStopBtn.setVisible(true);
            }
            startStopBtn.setText("Pause");
            counter = 0; pause = false; firstUpdate = true;
        });

        backBtn.setOnMouseClicked(e -> {
            runnable.endThread();
            CombinedApp app = new CombinedApp();
            app.start(primaryStage);
        });
        backBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                runnable.endThread();
                CombinedApp app = new CombinedApp();
                app.start(primaryStage);
            }
        });

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simInfoHousing);
        Scene scene = new Scene(mainPane, 600, 655);
        primaryStage.setTitle("Match Replayer");
        primaryStage.setScene(scene);
    }

    private void updateRobot(Object[] data) {

        // remove old rectangle
        simPane.getChildren().removeAll(robotRect);

        // update current node, time since start
        nodeNum.setText(counter +"");

//        if (isFollow) {
//            nodeSlider.setValue(counter);
//            isFollow = true;
//        }

        double time = (double) data[0];
        curTime.setText(String.format("%.2f", time / 1000));

        // update robot xy
        xCor = (double) data[1] * inchToPixel;
        yCor = (144 - (double) data[2]) * inchToPixel;
        if (xCor - robotRadius < 0) xCor = robotRadius; //left
        if (xCor + robotRadius > 600) xCor = 600 - robotRadius; //right
        if (yCor - robotRadius < 0) yCor = robotRadius; //up
        if (yCor + robotRadius > 600) yCor = 600 - robotRadius; //down

        // define updated rectangle
        robotRect = new Rectangle(xCor - robotRadius, yCor - robotRadius, robotLength, robotLength);

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

        // update robot theta
        theta = -(((double) data[3] * 180/Math.PI) + 0.5);
        if (theta > 360) {theta %= 360;}
        robotRect.setRotate(theta);

        // draw updated robot on screen
        simPane.getChildren().add(robotRect);

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
        if ((boolean) data[11]) stoneClamped.setText("Clamped");
        else stoneClamped.setText("Not Clamped");

        // update trying to deposit text
        if ((boolean) data[12]) tryingToDeposit.setText("Depositing");
        else tryingToDeposit.setText("Not Depositing");

        // update arm state text
        if ((boolean) data[13]) armState.setText("Home");
        else if ((boolean) data[14]) armState.setText("Down");
        else if ((boolean) data[15]) armState.setText("Out");
    }
}