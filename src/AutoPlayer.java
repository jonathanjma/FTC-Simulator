import Splines.Path;
import Splines.Pose;
import Splines.Spline;
import Splines.Waypoint;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Thread.sleep;

@SuppressWarnings("FieldCanBeLocal")
public class AutoPlayer extends Application {

    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private VBox simInfoHousing = new VBox(2.5);
    private HBox simInfo1 = new HBox(5);
    private HBox simInfo2 = new HBox(5);

    private Rectangle robotRect;

    private ArrayList<Path> pathList;
    private ArrayList<Double> timeList;

    // ui labels
    private Label corLb = new Label("Position: (");
    private Label xInchLb = new Label("n/a");
    private Label commaLb1 = new Label(",");
    private Label yInchLb = new Label("n/a");
    private Label thetaLb1 = new Label(")  Theta:");
    private Label thetaLb = new Label("n/a");
    private Button startStopBtn = new Button("Start");
    //private Button restartBtn = new Button("Restart");

    private Text timeLb = new Text(507, 25, "Time:");
    private Text curTime = new Text(545, 25, "n/a");

    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double inchToPixel = 4.16;
    private final static double robotLength = 18 * inchToPixel;
    private final static double robotRadius = robotLength / 2; //37.44

    private int colorValue = 255;
    private final static double colorInterval = 20;

    private double xCor, yCor, theta, timeForText;

    //private boolean pause = false;

    // update robot thread
    private FollowPosData runnable = new FollowPosData();
    private Thread thread = new Thread(runnable);
    public class FollowPosData implements Runnable {
        Path curPath;
        Pose curPose;
        double time;

        public void run() {
            for (int paths = 0; paths < pathList.size(); paths++) {
                curPath = pathList.get(paths);
                time = timeList.get(paths);
                System.out.println(paths + " "+ time);
                double startTime = System.currentTimeMillis();

                for (double currentTime = 0; currentTime < time; currentTime += 0.01) {
                    curPose = curPath.getRobotPose(currentTime);
                    //System.out.println(curPose.getX() +" "+ curPose.getY() +" "+ curPose.getTheta());
                    Platform.runLater(() -> updateRobot(curPose.getX(), curPose.getY(), curPose.getTheta()));
                    timeForText = currentTime;

                    try {sleep(10);}
                    catch (InterruptedException ex) {ex.printStackTrace();}
                }
                System.out.println(System.currentTimeMillis()-startTime);
            }
        }
    }

    public void start(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        simInfo1.setPadding(new Insets(0, 5, 0, 5));
        simInfo1.setAlignment(Pos.CENTER);
        simInfo2.setPadding(new Insets(0, 5, 2.5, 5));
        simInfo2.setAlignment(Pos.CENTER);
        simInfoHousing.getChildren().addAll(simInfo1, simInfo2);

        corLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); commaLb1.setFont(Font.font(14));
        xInchLb.setFont(Font.font(14)); yInchLb.setFont(Font.font(14));
        thetaLb1.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); thetaLb.setFont(Font.font(14));
        //restartBtn.setVisible(false);

        timeLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); curTime.setFont(Font.font(14));

        simInfo1.getChildren().addAll(corLb, xInchLb, commaLb1, yInchLb, thetaLb1, thetaLb, startStopBtn/*, restartBtn*/);
        simPane.getChildren().addAll(timeLb, curTime);

        startStopBtn.setOnAction(e -> {
            switch (startStopBtn.getText()) {
                case "Start":
                    thread.start(); thread.setName("UpdateRobotThread");
                    startStopBtn.setVisible(false);
                    //startStopBtn.setText("Pause");
                    //restartBtn.setVisible(true);
                    break;
//                case "Pause":
//                    pause = true;
//                    startStopBtn.setText("Resume");
//                    break;
//                case "Resume":
//                    pause = false;
//                    startStopBtn.setText("Pause");
//                    break;
            }
        });

//        restartBtn.setOnAction(e -> {
//            if (counter == dataUtil.getNumOfPoints()) {
//                thread = new Thread(runnable);
//                thread.start(); thread.setName("UpdateRobotThread");
//                startStopBtn.setVisible(true);
//            }
//            startStopBtn.setText("Pause");
//            counter = 0; pause = false; firstUpdate = true;
//        });

        updateRobot(9,111,0);
        drawAutoPath();

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simInfoHousing);
        Scene scene = new Scene(mainPane, 600, 655);
        primaryStage.setTitle("Auto Player");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("field.jpg"));
        primaryStage.show();
    }

    private void updateRobot(double x, double y, double th) {

        // remove old rectangle
        simPane.getChildren().removeAll(robotRect);

        curTime.setText(String.format("%.2f", timeForText));

        // update robot xy
        xCor = x * inchToPixel;
        yCor = (144 - y) * inchToPixel;

        // define updated rectangle
        robotRect = new Rectangle(xCor - robotRadius, yCor - robotRadius, robotLength, robotLength);

        // color robot yellow if stone in robot
        Stop[] stops = new Stop[] {new Stop(0, Color.rgb(0, 0, 0, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))};
        LinearGradient background = new LinearGradient(xCor, yCor, xCor+robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);

        // update robot theta
        theta = -((th * 180/Math.PI) + 0.5);
        if (th > 360) {th %= 360;}
        robotRect.setRotate(theta);

        // draw updated robot on screen
        simPane.getChildren().add(robotRect);

        // update xy and theta text
        xInchLb.setText(String.format("%.2f", x));
        yInchLb.setText(String.format("%.2f", y));
        thetaLb.setText(String.format("%.2f", th));
    }

    public void drawAutoPath() {

        double skystone1Time = 1.5;
        double toFoundation1Time = 3;
        double skystone2Time = 2.75;
        double toFoundation2Time = 3;

        double skystoneY = -1, skystonePos = 1;
        if (skystonePos == 1) skystoneY = 128;
        else if (skystonePos == 2) skystoneY = 120;
        else if (skystonePos == 3) skystoneY = 112;

        Waypoint[] skystone1PathWaypoints = {
                new Waypoint(9,111,0,15,100,0,0),
                new Waypoint(33, skystoneY-5, Math.PI / 6, 10,  60, 0,1),
                new Waypoint(45,skystoneY,Math.PI / 4 + 0.2,10,-100,0, skystone1Time)
        };
        Path skystone1Path = new Path(new ArrayList<>(Arrays.asList(skystone1PathWaypoints)));
        drawPath(skystone1Path,skystone1Time);

        Waypoint[] toFoundation1PathWaypoints = new Waypoint[] {
                new Waypoint(45, skystoneY, Math.PI / 4 + 0.2, -30, -100,0, 0),
                new Waypoint(36, skystoneY - 25, Math.PI / 3, -30, -40,0, 1),
                new Waypoint(36, 35, Math.PI / 2, -30, -30,0, 2),
                new Waypoint(47, 18, Math.PI, -30, 100,0, toFoundation1Time)
        };
        Path toFoundation1Path = new Path(new ArrayList<>(Arrays.asList(toFoundation1PathWaypoints)));
        drawPath(toFoundation1Path,toFoundation1Time);

        drawToPoint(47,18,30,45);

        Waypoint[] skystone2PathWaypoints = new Waypoint[] {
                new Waypoint(30,45, Math.PI/2, 50, 100,0, 0),
                new Waypoint(35, skystoneY - 34, Math.PI / 3, 50, 10,-1.5, 2),
                new Waypoint(49, skystoneY - 20, Math.PI / 4 + 0.2, 10, -100,0, skystone2Time)
        };
        Path skystone2Path = new Path(new ArrayList<>(Arrays.asList(skystone2PathWaypoints)));
        drawPath(skystone2Path,skystone2Time);

        Waypoint[] toFoundation2PathWaypoints = new Waypoint[] {
                new Waypoint(49, skystoneY - 20, Math.PI / 4 + 0.2, -30, -100,0, 0),
                new Waypoint(34, skystoneY - 40, Math.PI / 2, -50, -10,0, 1),
                new Waypoint(30, 45, Math.PI / 2, -30, 50,0, toFoundation2Time)
        };
        Path toFoundation2Path = new Path(new ArrayList<>(Arrays.asList(toFoundation2PathWaypoints)));
        drawPath(toFoundation2Path,toFoundation2Time);

        drawToPoint(30,45,30,72);

        pathList = new ArrayList<>(Arrays.asList(skystone1Path,toFoundation1Path,skystone2Path,toFoundation2Path));
        timeList = new ArrayList<>(Arrays.asList(skystone1Time,toFoundation1Time,skystone2Time,toFoundation2Time));
    }

    public void drawPath(Path path, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            Pose curPose = path.getRobotPose(currentTime);
            double x = curPose.getX() * inchToPixel;
            double y = (144-curPose.getY()) * inchToPixel;

            Line splineSegmentLineRed = new Line(x, y, x, y);
            splineSegmentLineRed.setStroke(Color.rgb(colorValue, 0, 0));
            Line splineSegmentLineBlue = new Line((144*inchToPixel)-x, y, (144*inchToPixel)-x, y);
            splineSegmentLineBlue.setStroke(Color.rgb(0, 0, colorValue));
            simPane.getChildren().addAll(splineSegmentLineRed, splineSegmentLineBlue);
        }
        colorValue -= colorInterval;
    }

    public void drawSpline(Spline[] splines, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            double x = splines[0].position(currentTime) * inchToPixel;
            double y = (144-splines[1].position(currentTime)) * inchToPixel;
            Line splineSegmentLineRed = new Line(x, y, x, y);
            splineSegmentLineRed.setStroke(Color.rgb(colorValue, 0, 0));
            Line splineSegmentLineBlue = new Line((144*inchToPixel)-x, y, (144*inchToPixel)-x, y);
            splineSegmentLineBlue.setStroke(Color.rgb(0, 0, colorValue));
            simPane.getChildren().addAll(splineSegmentLineRed, splineSegmentLineBlue);
        }
        colorValue -= colorInterval;
    }

    public void drawToPoint(double x1, double y1, double x2, double y2) {

        x1 *= inchToPixel; x2 *= inchToPixel;
        y1 = (144-y1) * inchToPixel;  y2 = (144-y2) * inchToPixel;
        Line toPointLineRed = new Line(x1, y1, x2, y2);
        toPointLineRed.setStroke(Color.rgb(colorValue, 0, 0));
        Line toPointLineBlue = new Line((144*inchToPixel)-x1, y1, (144*inchToPixel)-x2, y2);
        toPointLineBlue.setStroke(Color.rgb(0, 0, colorValue));
        simPane.getChildren().addAll(toPointLineRed, toPointLineBlue);
        colorValue -= colorInterval;
    }
}
