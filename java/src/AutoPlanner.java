import Splines.Spline;
import Splines.SplineGenerator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

@SuppressWarnings("FieldCanBeLocal")
public class AutoPlanner extends Application {
    
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
    
    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double inchToPixel = 4.16;
    private final static double robotLength = 18 * inchToPixel;
    private final static double robotRadius = robotLength / 2;

    private int colorValue = 255; private final static double colorInterval = 10;
    private boolean isRed = true;
    
    @Override
    public void start(Stage primaryStage) {
        
        simSettings.setPadding(new Insets(5, 5, 5, 5));
        simSettings.setAlignment(Pos.CENTER);
        
        corLb.setFont(Font.font(20)); commaLb.setFont(Font.font(20)); xInchTf.setPrefWidth(50); yInchTf.setPrefWidth(50);
        angleLb1.setFont(Font.font(20)); angleLb2.setFont(Font.font(20)); angleTf.setPrefWidth(50);
        
        simSettings.getChildren().addAll(corLb, xInchTf, commaLb, yInchTf, angleLb1, angleTf, angleLb2);
        
        updateRobotPos(2, null);
        drawAutoPaths();
        
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
        
        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simSettings);
        Scene scene = new Scene(mainPane, 600, 635);
        primaryStage.setTitle("Auto Planner");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("field.jpg"));
        primaryStage.show();
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
    
    public void drawAutoPaths() {

        double skystone1Time = 2.5;
        double backToCenterTime = 1;
        double toQuarryTime = 2;
        double skystone2Time = 2;
        double foundationPullTime = 4;
        
        double skystoneY = -1, skystonePos = 1;
        if (skystonePos == 1) {
            skystoneY = 132;
        } else if (skystonePos == 2) {
            skystoneY = 123;
        } else if (skystonePos == 3) {
            skystoneY = 114;
        }

        Spline[] foundationPullSpline = splineGenerator.SplineBetweenTwoPoints(44, 24,
                30, 55, Math.PI, Math.PI / 2, 10, 100,
                15, 100, 0, 0, foundationPullTime);
        drawSpline(foundationPullSpline, foundationPullTime);

        // red -----------------------------------------------------------------------------------------------------
        /*Spline[] skystone1Spline = splineGenerator.SplineBetweenTwoPoints(9, 111,
                45, skystoneY, 0, Math.PI / 4, 0, 0,
                20, 0, 0, 0, skystone1Time);
        drawSpline(skystone1Spline, skystone1Time);
    
        Spline[] backToCenterSpline = splineGenerator.SplineBetweenTwoPoints(45, skystoneY,
                33, skystoneY - 12, Math.PI / 4, Math.PI / 2, 0, -70,
                -20, -50, 0, 0, backToCenterTime);
        drawSpline(backToCenterSpline, backToCenterTime);

        drawToPoint(33, skystoneY - 12, 36, 55); // to foundation
        drawToPoint(36, 55, 38, 33); // foundation turn
        drawToPoint(38, 33, 44, 23); // approach foundation
        drawToPoint(44, 23, 26, 35); // pull foundation
        drawToPoint(26,  35, 35, 35); // turn foundation
        drawToPoint(35, 35, 35, 29); // push foundation

        Spline[] toQuarrySpline = splineGenerator.SplineBetweenTwoPoints(35, 29,
                24, skystoneY - 30, Math.PI / 2, Math.PI / 4, 0, 0,
                20, 0, 0, 0, toQuarryTime);
        drawSpline(toQuarrySpline, toQuarryTime);

        Spline[] skystone2Spline = splineGenerator.SplineBetweenTwoPoints(24, skystoneY - 30,
                45, skystoneY - 26, Math.PI / 2, Math.PI / 4, 30, 0,
                20, 0, 0, 0, skystone2Time);
        drawSpline(skystone2Spline, skystone2Time);
        
        drawToPoint(45, skystoneY - 26, 33, 91); // back to center
        drawToPoint(33, 91, 33, 33); // go to foundation

        toQuarrySpline = splineGenerator.SplineBetweenTwoPoints(33, 33,
                24, skystoneY - 30, Math.PI / 2, Math.PI / 4, 0, 0,
                20, 0, 0, 0, toQuarryTime);
        drawSpline(toQuarrySpline, toQuarryTime);

        Spline[] skystone3Spline = splineGenerator.SplineBetweenTwoPoints(24, skystoneY - 30,
                45, skystoneY - 15, Math.PI / 2, Math.PI / 4, 30, 0,
                20, 0, 0, 0, skystone2Time);
        drawSpline(skystone3Spline, skystone2Time);

        drawToPoint(45, skystoneY - 15, 33, 91); // back to center
        drawToPoint(33, 91, 33, 33); // go to foundation
        drawToPoint(33, 33, 30, 72); // go to tape

        /*colorValue = 255; isRed = false;

        // blue -----------------------------------------------------------------------------------------------------
        skystone1Spline = splineGenerator.SplineBetweenTwoPoints(135, 111,
                99, skystoneY, Math.PI, 3*Math.PI / 4, 0, 0,
                20, 0, 0, 0, skystone1Time);
        drawSpline(skystone1Spline, skystone1Time);

        backToCenterSpline = splineGenerator.SplineBetweenTwoPoints(99, skystoneY,
                111, skystoneY - 12, 3*Math.PI / 4, Math.PI / 2, 0, -70,
                -20, -50, 0, 0, backToCenterTime);
        drawSpline(backToCenterSpline, backToCenterTime);

        drawToPoint(111, skystoneY - 12, 108, 55); // to foundation

        foundationTurnSpline = splineGenerator.SplineBetweenTwoPoints(108, 55,
                113, 36, Math.PI / 2, 0, -70, -30,
                -50, -20, 0, 0, foundationTurnTime);
        drawSpline(foundationTurnSpline, foundationTurnTime);

        drawToPoint(113, 36, 100, 25); // approach foundation
        drawToPoint(100, 25, 118, 25); // pull foundation
        drawToPoint(118,  25, 109, 35); // turn foundation
        drawToPoint(109, 35, 109, 29); // push foundation

        toQuarrySpline = splineGenerator.SplineBetweenTwoPoints(109, 29,
                120, skystoneY - 30, Math.PI / 2, 3*Math.PI / 4, 0, 0,
                20, 0, 0, 0, toQuarryTime);
        drawSpline(toQuarrySpline, toQuarryTime);

        skystone2Spline = splineGenerator.SplineBetweenTwoPoints(120, skystoneY - 30,
                99, skystoneY - 26, Math.PI / 2, 3*Math.PI / 4, 30, 0,
                20, 0, 0, 0, skystone2Time);
        drawSpline(skystone2Spline, skystone2Time);

        drawToPoint(99, skystoneY - 26, 111, 85); // back to center
        drawToPoint(111, 85, 111, 33); // go to foundation
        drawToPoint(111, 33, 114, 62); // go to tape*/
    }
    
    public void drawSpline(Spline[] splines, double time) {
        for (double currentTime = 0; currentTime < time; currentTime+=0.01) {

            /*double x_inch = splines[0].position(currentTime);
            if (currentTime == 0) System.out.println("Spine: " + (144-x_inch));
            if (currentTime > time-0.01) System.out.println("\t" + (144-x_inch));*/

            double x = splines[0].position(currentTime) * inchToPixel;
            double y = (144-splines[1].position(currentTime)) * inchToPixel;
            Line splineSegmentLine = new Line(x, y, x, y);
            if (isRed) splineSegmentLine.setStroke(Color.rgb(colorValue, 0, 0));
            else splineSegmentLine.setStroke(Color.rgb(0, 0, colorValue));
            simPane.getChildren().add(splineSegmentLine);
        }
        colorValue -= colorInterval;
    }

    public void drawToPoint(double x1, double y1, double x2, double y2) {
        //System.out.println("To Point: " + (144-x1) + " " + (144-x2));

        x1 *= inchToPixel; x2 *= inchToPixel;
        y1 = (144-y1) * inchToPixel;  y2 = (144-y2) * inchToPixel;
        Line toPointLine = new Line(x1, y1, x2, y2);
        if (isRed) toPointLine.setStroke(Color.rgb(colorValue, 0, 0));
        else toPointLine.setStroke(Color.rgb(0, 0, colorValue));
        simPane.getChildren().add(toPointLine);
        colorValue -= colorInterval;
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
