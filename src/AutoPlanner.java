import Splines.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("FieldCanBeLocal")
public class AutoPlanner {
    
    // Instructions to make jar file
    /*
    1. delete files in "out" folder
    2. Run Game, close once loaded (done to refresh class files generated from build)
    3. Copy files to jarfiles (C:\Users\jonat\AndroidStudioProjects\FTC_Simulator\jarfiles)
    4. In cmd, run
       jar cvfm Planner.jar manifest.txt AutoPlanner.class field.jpg
    5. Run jar by clicking on it or running
       java -jar Planner.jar
     */
    
    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox simSettings = new HBox(5);
    private Rectangle robotRect;
    private SplineGenerator splineGenerator = new SplineGenerator();
    
    private double xCor, yCor, xInch, yInch, angle;
    
    private Label corLb = new Label("Position: (");
    private Label commaLb = new Label(",");
    private Label angleLb1 = new Label(")          Angle (rad):");
    private Label angleLb2 = new Label("*π");
    private TextField xInchTf = new TextField("9");
    private TextField yInchTf = new TextField("111");
    private TextField angleTf = new TextField("0");

    private Button backBtn = new Button("Back");

    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double inchToPixel = 4.16;
    private final static double robotLength = 18 * inchToPixel;
    private final static double robotRadius = robotLength / 2;

    private int colorValue = 255;
    private final static double colorInterval = 20;

    public void launch(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        simSettings.setPadding(new Insets(5, 5, 5, 5));
        simSettings.setAlignment(Pos.CENTER);
        
        corLb.setFont(Font.font(20)); commaLb.setFont(Font.font(20)); xInchTf.setPrefWidth(50); yInchTf.setPrefWidth(50);
        angleLb1.setFont(Font.font(20)); angleLb2.setFont(Font.font(20)); angleTf.setPrefWidth(50);

        simSettings.getChildren().addAll(corLb, xInchTf, commaLb, yInchTf, angleLb1, angleTf, angleLb2);

        backBtn.setLayoutX(10); backBtn.setLayoutY(10);
        simPane.getChildren().addAll(backBtn);

        updateRobotPos(2, null);
        drawAutoPath();
        
        mainPane.setOnMouseClicked(e -> updateRobotPos(1, e));
        mainPane.setOnMouseDragged(e -> updateRobotPos(1, e));
        
        mainPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) updateRobotPos(2, null);
            if (e.getCode() == KeyCode.ALT) {
                angleTf.setText(String.format("%.2f", Double.parseDouble(angleTf.getText()) + 0.1));
                updateRobotPos(3, null);
            }
            if (e.getCode() == KeyCode.ALT_GRAPH) {
                angleTf.setText(String.format("%.2f", Double.parseDouble(angleTf.getText()) - 0.1));
                updateRobotPos(3, null);
            }
        });

        backBtn.setOnMouseClicked(e-> {
            CombinedApp app = new CombinedApp();
            app.start(primaryStage);
        });
        backBtn.setOnKeyPressed(e-> {
            if (e.getCode() == KeyCode.ENTER) {
                CombinedApp app = new CombinedApp();
                app.start(primaryStage);
            }
        });
        
        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simSettings);
        Scene scene = new Scene(mainPane, 600, 635);
        primaryStage.setTitle("Auto Planner");
        primaryStage.setScene(scene);
    }
    
    private void updateRobotPos(int code, MouseEvent e) { // 1 = mouse, 2 = pos input, 3 = angle input
        if (code == 1 || code == 2) {
            if (code == 1) {
                xCor = Double.parseDouble(String.format("%.2f", e.getSceneX()));
                yCor = Double.parseDouble(String.format("%.2f", e.getSceneY()));
            } else {
                xCor = Double.parseDouble(xInchTf.getText()) * inchToPixel;
                yCor = (144 - Double.parseDouble(yInchTf.getText())) * inchToPixel;
            }

            if (xCor - robotRadius < 0) xCor = robotRadius; //left
            if (xCor + robotRadius > 600) xCor = 600 - robotRadius; //right
            if (yCor - robotRadius < 0) yCor = robotRadius; //up
            if (yCor + robotRadius > 600) yCor = 600 - robotRadius; //down

            if (code == 1) {
                xInch = Double.parseDouble(String.format("%.2f", xCor / inchToPixel));
                double yCorAdjusted = 600 - yCor;
                yInch = Double.parseDouble(String.format("%.2f", yCorAdjusted / inchToPixel));

                xInchTf.setText(xInch + "");
                yInchTf.setText(yInch + "");
            }
            //System.out.println(xCor + " " + yCor);
        }

        simPane.getChildren().remove(robotRect);
        robotRect = new Rectangle(xCor - robotRadius, yCor - robotRadius, robotLength, robotLength);

        Stop[] stops = {new Stop(0, Color.rgb(0, 0, 0, 0.75)),
                new Stop(1, Color.rgb(192, 192, 192, 0.75))};
        LinearGradient background = new LinearGradient(xCor, yCor, xCor+robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);
        angle = -((Double.parseDouble(angleTf.getText()) * 180) + 0.5);
        if (angle > 360) {angle %= 360;}
        robotRect.setRotate(angle);
        simPane.getChildren().add(robotRect);
    }
    
    public void drawAutoPath() {

        double skystone1Time = 1.5;
        double toFoundation1Time = 3;
        double approachFoundationTime = 1;
        double skystone2Time = 2;
        double toFoundation2Time = 3;
        
        double skystoneY = -1, skystonePos = 3;
        if (skystonePos == 1) skystoneY = 128;
        else if (skystonePos == 2) skystoneY = 120;
        else if (skystonePos == 3) skystoneY = 112;

        Waypoint[] skystone1PathWaypoints = {
                new Waypoint(9,111,0,15,100,0),
                new Waypoint(33, skystoneY-5, Math.PI / 6, 10,  60, 1),
                new Waypoint(45,skystoneY,Math.PI / 4 + 0.2,10,-100, skystone1Time)
        };
        Path skystone1Path = new Path(new ArrayList<>(Arrays.asList(skystone1PathWaypoints)));
        drawPath(skystone1Path,skystone1Time);

        Waypoint[] toFoundation1PathWaypoints = new Waypoint[] {
                new Waypoint(45, skystoneY, Math.PI / 4 + 0.2, -30, -100, 0),
                new Waypoint(34, skystoneY - 25, Math.PI / 2, -50, -10, 1),
                new Waypoint(36, 30, Math.PI / 2, -30, 10, toFoundation1Time)
        };
        Path toFoundation1Path = new Path(new ArrayList<>(Arrays.asList(toFoundation1PathWaypoints)));
        drawPath(toFoundation1Path,toFoundation1Time);

        Waypoint[] approachFoundationPathWaypoints = new Waypoint[] {
                new Waypoint(36, 30, Math.PI / 2, -30, 50, 0),
                new Waypoint(47, 18, Math.PI, -30, -10, approachFoundationTime)
        };
        Path approachFoundationPath = new Path(new ArrayList<>(Arrays.asList(approachFoundationPathWaypoints)));
        drawPath(approachFoundationPath,approachFoundationTime);

        drawToPoint(47,18,30,45);

        Waypoint[] skystone2PathWaypoints = new Waypoint[] {
                new Waypoint(30, 45, Math.PI / 2, 30, 100, 0),
                new Waypoint(33, skystoneY - 34, Math.PI / 3, 30, 10, 1.25),
                new Waypoint(49, skystoneY - 20, Math.PI / 4 + 0.2, 30, -10, skystone2Time)
        };
        Path skystone2Path = new Path(new ArrayList<>(Arrays.asList(skystone2PathWaypoints)));
        drawPath(skystone2Path,skystone2Time);

        Waypoint[] toFoundation2PathWaypoints = new Waypoint[] {
                new Waypoint(49, skystoneY - 20, Math.PI / 4 + 0.2, -30, -100, 0),
                new Waypoint(34, skystoneY - 40, Math.PI / 2, -50, -10, 1),
                new Waypoint(30, 45, Math.PI / 2, -30, 50, toFoundation2Time)
        };
        Path toFoundation2Path = new Path(new ArrayList<>(Arrays.asList(toFoundation2PathWaypoints)));
        drawPath(toFoundation2Path,toFoundation2Time);
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
