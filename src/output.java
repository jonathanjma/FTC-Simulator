import iLQR.Optimizer;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.ejml.simple.SimpleMatrix;
import sun.java2d.pipe.SpanShapeRenderer;

import static java.lang.Thread.sleep;

@SuppressWarnings("FieldCanBeLocal")
public class output extends Application {
    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private VBox simInfoHousing = new VBox(2.5);
    private HBox simInfo1 = new HBox(5);
    private HBox simInfo2 = new HBox(5);

    private Rectangle robotRect;

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
    
    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double inchToPixel = 4.16;
    private final static double robotLength = 18 * inchToPixel;
    private final static double robotRadius = robotLength / 2; //37.44

    private double xCor, yCor, theta;
    private double startx = 9, startY = 111;

    private boolean pause = false; private int counter = 0;

    private boolean firstUpdate = true;

    private final double timestep = 20;
    private SimpleMatrix[] Klis;
    private SimpleMatrix[] dlis;
    private SimpleMatrix[] xlis;
    private double [][] x0 = {{9},{111}};
    private double [][] xf = {{20},{30}};
    private double[][] uarr = {{0},{0}};
    private SimpleMatrix u = new SimpleMatrix(uarr);


    // update robot thread
    private FollowPosData runnable = new FollowPosData();
    private Thread thread = new Thread(runnable);
    public class FollowPosData implements Runnable {
        public void run() {
            for (; counter < 250;) {
                if (!pause) {
                    Platform.runLater(() -> {
                        double [][] xarr = {{xCor*(1/inchToPixel)},{(600-yCor)*(1/inchToPixel)}};
                        SimpleMatrix x = new SimpleMatrix(xarr);
                        u = (Klis[counter].mult(xlis[counter].minus(x)).negative()).plus(dlis[counter]);
                        updateRobot(u.get(0,0),u.get(1,0));
                    });
                    try {sleep((long) timestep);} catch (InterruptedException ex) {ex.printStackTrace();}
                    counter++;
                }
                System.out.print("");
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        //ilqr stuff
        double timestep = 0.02;

        SimpleMatrix[] Klis;
        SimpleMatrix [] dlis;
        SimpleMatrix [] lastxlis;
        SimpleMatrix A = SimpleMatrix.identity(2);
        SimpleMatrix B = SimpleMatrix.identity(2).scale(timestep);


        Optimizer optim = new Optimizer(A,B,timestep);


        SimpleMatrix startx = new SimpleMatrix(x0);
        SimpleMatrix finalx = new SimpleMatrix(xf);

        Object[] a = optim.optimizePath(startx,finalx,5);
        Klis = (SimpleMatrix[])a[0];
        dlis = (SimpleMatrix[])a[1];
        xlis = (SimpleMatrix[])a[2];

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

        simInfo1.getChildren().addAll(corLb, xInchLb, commaLb1, yInchLb, thetaLb1, thetaLb,
                velocityLb, velocityXLb, commaLb2, velocityYLb, commaLb3, velocityThetaLb,
                startStopBtn, restartBtn);

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
                    break;
            }
        });

        restartBtn.setOnAction(e -> {
            thread = new Thread(runnable);
            thread.start(); thread.setName("UpdateRobotThread");
            startStopBtn.setVisible(true);
        });

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simInfoHousing);
        Scene scene = new Scene(mainPane, 600, 655);
        primaryStage.setTitle("");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("field.jpg"));
        primaryStage.show();
    }

    private void updateRobot(double xvel, double yvel) {

        if (firstUpdate) {
            yCor = (144-startY) * inchToPixel; firstUpdate=false;
            xCor = startx*inchToPixel;
        }

        double time = timestep/1000;
        xCor += xvel*inchToPixel*time;
        yCor -= yvel*inchToPixel*time;

        //System.out.println(xCor + " " + yCor + ", " + xvel + " " + yvel);

        simPane.getChildren().removeAll(robotRect);

        robotRect = new Rectangle(xCor - robotRadius, yCor - robotRadius, robotLength, robotLength);

        theta = -(((Math.PI/2) * 180/Math.PI) + 0.5);
        if (theta > 360) {theta %= 360;}
        robotRect.setRotate(theta);

        Stop[] stops = new Stop[] {new Stop(0, Color.rgb(0, 0, 0, 0.85)),
                new Stop(1, Color.rgb(192, 192, 192, 0.85))};
        LinearGradient background = new LinearGradient(xCor, yCor, xCor+robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);

        simPane.getChildren().add(robotRect);

        xInchLb.setText(String.format("%.2f", xCor));
        yInchLb.setText(String.format("%.2f", yCor));

        velocityXLb.setText(String.format("%.2f", xvel));
        velocityYLb.setText(String.format("%.2f", yvel));
    }
}
