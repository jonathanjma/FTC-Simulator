import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
public class MatchReplayer extends Application {
    
    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private VBox simInfoHousing = new VBox(2.5);
    private HBox simInfo1 = new HBox(5);
    private HBox simInfo2 = new HBox(5);
    private Rectangle robotRect;

    private Group pathPoints = new Group();
    private Circle pathPoint;
    
    private double xCor, yCor, theta;
    private final static boolean logAccel = true; //<----------------------------------
    
    private Label corLb = new Label("Position: (");
    private Label xInchLb = new Label("n/a");
    private Label commaLb1 = new Label(",");
    private Label yInchLb = new Label("n/a");
    private Label thetaLb1 = new Label(") Theta:");
    private Label thetaLb = new Label("n/a");
    private Label velocityLb = new Label("Velocity:");
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
    private Label armLb = new Label("Arm: ");
    private Label armState = new Label("n/a");
    private Label commaLb6 = new Label(",");
    private Label stoneClamped = new Label("n/a");

    private Text nodeLb = new Text(507, 25, "Node:");
    private Text nodeNum = new Text(550, 25, "n/a");
    private Text timeLb = new Text(507, 45, "Time:");
    private Text curTime = new Text(545, 45, "n/a");
    
    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double inchToPixel = 4.16;
    private final static double robotLength = 18 * inchToPixel;
    private final static double robotRadius = robotLength / 2; //37.44

    private RobotDataUtil dataUtil = new RobotDataUtil(logAccel);
    private boolean pause = false; private int counter = 0;

    private FollowPosData runnable = new FollowPosData();
    private Thread thread = new Thread(runnable);
    public class FollowPosData implements Runnable {
        public void run() {
            for (; counter < dataUtil.getNumOfPoints();) {
                if (counter == 1) {Platform.runLater(() -> pathPoints.getChildren().clear());}
                if (!pause) {
                    Platform.runLater(() -> updateRobot(dataUtil.getNextPos()));
                    try {sleep(55);} catch (InterruptedException ex) {ex.printStackTrace();} // sleep
                    counter++;
                }
                System.out.print("");
            }
            Platform.runLater(() -> startStopBtn.setVisible(false));
        }
    }

    @Override
    public void start(Stage primaryStage) {

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
        armLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); armState.setFont(Font.font(14)); commaLb6.setFont(Font.font(14)); stoneClamped.setFont(Font.font(14));

        nodeLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); nodeNum.setFont(Font.font(14));
        timeLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); curTime.setFont(Font.font(14));

        simInfo1.getChildren().addAll(corLb, xInchLb, commaLb1, yInchLb, thetaLb1, thetaLb,
                velocityLb, velocityXLb, commaLb2, velocityYLb, commaLb3, velocityThetaLb,
                startStopBtn, restartBtn);
        simInfo2.getChildren().addAll(accelLb, accelXLb, commaLb4, accelYLb, commaLb5, accelThetaLb,
                armLb, armState, commaLb6, stoneClamped);
        simPane.getChildren().addAll(nodeLb, nodeNum, timeLb, curTime, pathPoints);

        dataUtil.parseLogFile();

        startStopBtn.setOnAction(e -> {
            switch (startStopBtn.getText()) {
                case "Start":
                    thread.start();
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
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            if (counter == dataUtil.getNumOfPoints()) {
                thread = new Thread(runnable);
                thread.start();
                startStopBtn.setVisible(true);
            }
            startStopBtn.setText("Pause");
            counter = 0; dataUtil.setGetCounter(0); pause = false;
        });

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simInfoHousing);
        Scene scene = new Scene(mainPane, 600, 655);
        primaryStage.setTitle("Match Replayer");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("field.jpg"));
        primaryStage.show();
    }

    private void updateRobot(Object[] data) {

        nodeNum.setText(counter +"");
        double time = (double) data[0];
        curTime.setText(String.format("%.2f", time / 1000));

        xCor = (double) data[1] * inchToPixel;
        yCor = (144 - (double) data[2]) * inchToPixel;
        if (xCor - robotRadius < 0) xCor = robotRadius; //left
        if (xCor + robotRadius > 600) xCor = 600 - robotRadius; //right
        if (yCor - robotRadius < 0) yCor = robotRadius; //up
        if (yCor + robotRadius > 600) yCor = 600 - robotRadius; //down

        simPane.getChildren().removeAll(robotRect);

        robotRect = new Rectangle(xCor - robotRadius, yCor - robotRadius, robotLength, robotLength);
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

        theta = -(((double) data[3] * 180/Math.PI) + 0.5);
        if (theta > 360) {theta %= 360;}
        robotRect.setRotate(theta);
        simPane.getChildren().add(robotRect);

        xInchLb.setText(String.format("%.2f", (double) data[1]));
        yInchLb.setText(String.format("%.2f", (double) data[2]));
        thetaLb.setText(String.format("%.2f", (double) data[3]));

        double velocityX = (double) data[4], velocityY = (double) data[5], velocityTheta = (double) data[6];
        velocityXLb.setText(String.format("%.2f", velocityX));
        velocityYLb.setText(String.format("%.2f", velocityY));
        velocityThetaLb.setText(String.format("%.2f", velocityTheta));

        int velocityFactor = (int) Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
        pathPoint = new Circle(xCor, yCor, 2, Color.hsb(velocityFactor * 1.5, 1, 1));
        pathPoints.getChildren().add(pathPoint);

        double accelX = (double) data[7];
        double accelY = (double) data[8];
        double accelTheta = (double) data[9];
        accelXLb.setText(String.format("%.2f", accelX));
        accelYLb.setText(String.format("%.2f", accelY));
        accelThetaLb.setText(String.format("%.2f", accelTheta));

        if ((boolean) data[11]) stoneClamped.setText("Clamped");
        else stoneClamped.setText("Not Clamped");

        if ((boolean) data[12]) armState.setText("Home");
        else if ((boolean) data[13]) armState.setText("Down");
        else if ((boolean) data[14]) armState.setText("Out");
    }
    
    public static void main(String[] args) {Application.launch(args);}
}
