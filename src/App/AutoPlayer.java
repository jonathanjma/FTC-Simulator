package App;

import Threads.FollowPathData;
import Utilities.AutoPathsUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import static Utilities.ConversionUtil.*;

@SuppressWarnings("FieldCanBeLocal")
public class AutoPlayer {

    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox simInfo = new HBox(5);

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

    private int colorValue = 255;
    private final static double colorInterval = 20;
    private AutoPathsUtil pathsUtil = new AutoPathsUtil(simPane, colorValue, colorInterval);

    private SimpleBooleanProperty startStopVisible = new SimpleBooleanProperty(true);
    private SimpleStringProperty curTime = new SimpleStringProperty("0.00");

    private Rectangle robotRect;

    // update robot thread
    private FollowPathData followPathData;
    private Thread robotThread;

    public void launch(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        simInfo.setPadding(new Insets(0, 5, 5, 5));
        simInfo.setAlignment(Pos.CENTER);

        corLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); commaLb1.setFont(Font.font(14));
        xInchLb.setFont(Font.font(14)); yInchLb.setFont(Font.font(14));
        thetaLb1.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); thetaLb.setFont(Font.font(14));
        timeLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14)); curTimeLb.setFont(Font.font(14));
        restartBtn.setVisible(false);

        curTimeLb.textProperty().bind(curTime);
        startStopBtn.visibleProperty().bind(startStopVisible);

        simInfo.getChildren().addAll(corLb, xInchLb, commaLb1, yInchLb, thetaLb1, thetaLb, timeLb, curTimeLb,
                startStopBtn, restartBtn);

        backBtn.setLayoutX(10); backBtn.setLayoutY(10);
        simPane.getChildren().addAll(backBtn);

        pathsUtil.drawAutoPaths();
        robotRect = new Rectangle(robotLength, robotLength);
        updateRobot(9,111,0);
        simPane.getChildren().add(robotRect);

        followPathData = new FollowPathData(pathsUtil.getPathList(), pathsUtil.getTimeList(),
                curTime, startStopVisible, this);
        robotThread = new Thread(followPathData, "UpdateRobotThread");

        startStopBtn.setOnAction(e -> {
            switch (startStopBtn.getText()) {
                case "Start":
                    robotThread.start();
                    startStopBtn.setText("Pause"); restartBtn.setVisible(true);
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
            followPathData.resetPathNum(); curTime.set(0 + "");
            followPathData.setPause(false);
        });

        backBtn.setOnMouseClicked(e-> {
            followPathData.endThread();
            CombinedApp app = new CombinedApp();
            app.start(primaryStage);
        });
        backBtn.setOnKeyPressed(e-> {
            if (e.getCode() == KeyCode.ENTER) {
                followPathData.endThread();
                CombinedApp app = new CombinedApp();
                app.start(primaryStage);
            }
        });
        primaryStage.setOnCloseRequest(e -> followPathData.endThread());

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simInfo);
        Scene scene = new Scene(mainPane, CombinedApp.sceneWidth, CombinedApp.sceneWidth + 35);
        primaryStage.setTitle("Auto Player");
        primaryStage.setScene(scene);
    }

    public void updateRobot(double x, double y, double theta) {

        // update robot xy
        double xCor = getXPixel(x);
        double yCor = getYPixel(y);
        robotRect.setX(xCor - robotRadius);
        robotRect.setY(yCor - robotRadius);

        // update robot theta
        robotRect.setRotate(getFXTheta(theta));

        // color robot yellow if stone in robot
        Stop[] stops = new Stop[] {new Stop(0, Color.rgb(0, 0, 0, 0.85)),
                    new Stop(1, Color.rgb(192, 192, 192, 0.85))};
        LinearGradient background = new LinearGradient(xCor, yCor, xCor + robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);

        // update xy and theta text
        xInchLb.setText(String.format("%.2f", x));
        yInchLb.setText(String.format("%.2f", y));
        thetaLb.setText(String.format("%.2f", theta));
    }
}
