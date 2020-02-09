package Old;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

@Deprecated
public class RobotSimulator extends Application {

    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox simSettings = new HBox(5);
    private Robot robot;
    private Rectangle robotRect;

    private double x;
    private double y;
    private double angle;
    private double velocity;
    private double angleVelocity;

    private TextField tfX = new TextField();
    private TextField tfY = new TextField();
    private TextField tfAngle = new TextField();
    private TextField tfVelocity = new TextField();
    private TextField tfAngleVelocity = new TextField();
    private Label lbTime = new Label("0 s");

    public final static double inchToPixel = 4.16;

    @Override
    public void start(Stage primaryStage) {
        tfX.setPrefWidth(50); tfY.setPrefWidth(50); tfAngle.setPrefWidth(50);
        tfVelocity.setPrefWidth(50); tfAngleVelocity.setPrefWidth(50);

        simSettings.setPadding(new Insets(5, 5, 5, 5));
        Button submit = new Button("Submit");
        simSettings.getChildren().addAll(new Label("X:"), tfX, new Label("Y:"), tfY,
                new Label("Angle:"), tfAngle, new Label("Velocity:"), tfVelocity,
                new Label("Angular Velocity:"), tfAngleVelocity, submit, lbTime);

        AnimationTimer moveRobot = new AnimationTimer() {
            private long startTime;
            private long timeDiff = 0;

            @Override
            public void start() {
                startTime = System.currentTimeMillis();
                super.start();
            }

            @Override
            public void stop() {
                super.stop();
                timeDiff = 0;
            }

            @Override
            public void handle(long now) {
                double oldX = robotRect.getX(); double oldY = robotRect.getY();

                Double[] updatedInfo = robot.calcNextPoint();
                robotRect.setX(updatedInfo[0]);
                robotRect.setY(updatedInfo[1]);
                robotRect.setRotate(updatedInfo[2]);

                simPane.getChildren().add(new Line(oldX, oldY, robotRect.getX(), robotRect.getY()));

                long newTime = System.currentTimeMillis();
                timeDiff = (newTime - startTime) / 1000;
                lbTime.setText(timeDiff + " s");

                if (robotRect.getX() < 0 || robotRect.getX() + robotRect.getWidth() > 600
                        || robotRect.getY() < 0 || robotRect.getY() + robotRect.getHeight() > 600) {
                    this.stop();
                }
            }
        };

        // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
        // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
        // if 36 in/sec, it should take 4 sec to traverse length

        tfX.setText("300");
        tfY.setText("300");
        tfAngle.setText("45");
        tfVelocity.setText("10");
        tfAngleVelocity.setText("0");

        submit.setOnAction(e -> {
            x = Double.parseDouble(tfX.getText());
            y = Double.parseDouble(tfY.getText());
            angle = Double.parseDouble(tfAngle.getText());
            velocity = Double.parseDouble(tfVelocity.getText());
            angleVelocity = Double.parseDouble(tfAngleVelocity.getText());

            simPane.getChildren().clear();

            robot = new Robot(x, y, angle, velocity, angleVelocity);
            robotRect = new Rectangle(x, y, 18 * inchToPixel, 18 * inchToPixel);
            robotRect.setRotate(angle);

            simPane.getChildren().add(robotRect);
            moveRobot.start();
        });

        mainPane.setOnMouseClicked(e -> {
            tfX.setText(String.format("%.2f", e.getSceneX()));
            tfY.setText(String.format("%.2f", e.getSceneY()));
        });

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simSettings);
        Scene scene = new Scene(mainPane, 600, 635);
        primaryStage.setTitle("Robot Simulator");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("field.jpg"));
        primaryStage.show();
    }

    public static void main(String[] args) { 
       Application.launch(args);
    }
}
