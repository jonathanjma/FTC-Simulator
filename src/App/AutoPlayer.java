package App;

import PathingFiles.Path;
import PathingFiles.Pose;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static java.lang.Thread.sleep;

@SuppressWarnings("FieldCanBeLocal")
public class AutoPlayer {

    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox simInfo = new HBox(5);

    private Rectangle robotRect;

    // ui labels
    private Label corLb = new Label("Position: (");
    private Label xInchLb = new Label("n/a");
    private Label commaLb1 = new Label(",");
    private Label yInchLb = new Label("n/a");
    private Label thetaLb1 = new Label(")  Theta:");
    private Label thetaLb = new Label("n/a");
    private Button startStopBtn = new Button("Start");
    private Label timeLb = new Label("  Time:");
    private Label curTimeLb = new Label("n/a");
    private Button restartBtn = new Button("Restart");

    private Button backBtn = new Button("Back");

    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double inchToPixel = 4.16;
    private final static double robotLength = 18 * inchToPixel;
    private final static double robotRadius = robotLength / 2; //37.44

    private int colorValue = 255;
    private final static double colorInterval = 20;
    private AutoPathsUtil pathsUtil = new AutoPathsUtil(simPane, colorValue, colorInterval);

    private double xCor, yCor, theta, timeForText;

    private boolean pause = false;

    // update robot thread
    private FollowPosData runnable = new FollowPosData();
    private Thread thread = new Thread(runnable);
    public class FollowPosData implements Runnable {
        private int pathNum = 0;
        private double currentTime = 0;
        private Path curPath;
        private Pose curPose;
        private double time;
        private boolean active = true;

        public void run() {
            for (; pathNum < pathsUtil.getPathList().size(); pathNum++) {
                curPath = pathsUtil.getPathList().get(pathNum);
                time = pathsUtil.getTimeList().get(pathNum);
                //System.out.println(paths + " "+ time);
                //double startTime = System.currentTimeMillis();

                for (; currentTime < time;) {
                    if (active) {
                        if (!pause) {
                            curPose = curPath.getRobotPose(currentTime);
                            //System.out.println(curPose.getX() +" "+ curPose.getY() +" "+ curPose.getTheta());
                            Platform.runLater(() -> updateRobot(curPose.getX(), curPose.getY(), curPose.getTheta()));
                            timeForText += 0.01;
                            currentTime += 0.01;

                            try {sleep(10);}
                            catch (InterruptedException ex) {ex.printStackTrace();}
                        }
                        System.out.print("");
                    }
                    else {thread.interrupt();}
                }
                currentTime = 0;
                //System.out.println(System.currentTimeMillis()-startTime);
            }
        }
        public void endThread() {active = false;}
        public int getPathNum() {return pathNum;}
        public void resetPathNum() {
            pathNum = 0; currentTime = 0;
            curPath = pathsUtil.getPathList().get(pathNum);
            time = pathsUtil.getTimeList().get(pathNum);
        }
    }

    public void launch(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        simInfo.setPadding(new Insets(0, 5, 5, 5));
        simInfo.setAlignment(Pos.CENTER);

        corLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); commaLb1.setFont(Font.font(14));
        xInchLb.setFont(Font.font(14)); yInchLb.setFont(Font.font(14));
        thetaLb1.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); thetaLb.setFont(Font.font(14));
        timeLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); curTimeLb.setFont(Font.font(14));
        restartBtn.setVisible(false);

        simInfo.getChildren().addAll(corLb, xInchLb, commaLb1, yInchLb, thetaLb1, thetaLb, timeLb, curTimeLb,
                startStopBtn, restartBtn);

        backBtn.setLayoutX(10); backBtn.setLayoutY(10);
        simPane.getChildren().addAll(backBtn);

        updateRobot(9,111,0);
        pathsUtil.drawAutoPaths();

        startStopBtn.setOnAction(e -> {
            switch (startStopBtn.getText()) {
                case "Start":
                    thread.start(); thread.setName("UpdateRobotThread");
                    startStopBtn.setText("Pause"); restartBtn.setVisible(true);
                    break;
                case "Pause":
                    pause = true; startStopBtn.setText("Resume");
                    break;
                case "Resume":
                    pause = false; startStopBtn.setText("Pause");
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            if (runnable.getPathNum() == pathsUtil.getPathList().size()) {
                thread = new Thread(runnable);
                thread.start(); thread.setName("UpdateRobotThread");
            }
            startStopBtn.setText("Pause");
            runnable.resetPathNum(); timeForText = 0;
            pause = false;
        });

        backBtn.setOnMouseClicked(e-> {
            runnable.endThread();
            CombinedApp app = new CombinedApp();
            app.start(primaryStage);
        });
        backBtn.setOnKeyPressed(e-> {
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
        mainPane.setBottom(simInfo);
        Scene scene = new Scene(mainPane, 600, 635);
        primaryStage.setTitle("Auto Player");
        primaryStage.setScene(scene);
    }

    private void updateRobot(double x, double y, double th) {

        // remove old rectangle
        simPane.getChildren().removeAll(robotRect);

        curTimeLb.setText(String.format("%.2f", timeForText));

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
}
