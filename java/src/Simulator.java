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

public class Simulator extends Application {

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
    private double time;

    private TextField tfX = new TextField();
    private TextField tfY = new TextField();
    private TextField tfAngle = new TextField();
    private TextField tfVelocity = new TextField();
    private TextField tfAngleVelocity = new TextField();
    private TextField tfTime = new TextField();

    @Override
    public void start(Stage primaryStage) {
        tfX.setPrefWidth(50); tfY.setPrefWidth(50); tfAngle.setPrefWidth(50);
        tfVelocity.setPrefWidth(50); tfAngleVelocity.setPrefWidth(50); tfTime.setPrefWidth(50);

        simSettings.setPadding(new Insets(5, 5, 5, 5));
        Button submit = new Button("Submit");
        simSettings.getChildren().addAll(new Label("X:"), tfX, new Label("Y:"), tfY,
                new Label("Angle:"), tfAngle, new Label("Velocity:"), tfVelocity,
                new Label("Angular Velocity:"), tfAngleVelocity, submit);

        AnimationTimer moveRobot = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double oldX = robotRect.getX(); double oldY = robotRect.getY();

                Double[] updatedInfo = robot.calcNextPoint();
                robotRect.setX(updatedInfo[0]);
                robotRect.setY(updatedInfo[1]);
                robotRect.setRotate(updatedInfo[2]);

                simPane.getChildren().add(new Line(oldX, oldY, robotRect.getX(), robotRect.getY()));
            }
        };

        submit.setOnAction(e -> {
            x = Double.parseDouble(tfX.getText());
            y = Double.parseDouble(tfY.getText());
            angle = Double.parseDouble(tfAngle.getText());
            velocity = Double.parseDouble(tfVelocity.getText());
            angleVelocity = Double.parseDouble(tfAngleVelocity.getText());

            /*x = 400;
            y = 300;
            angle = 0;
            velocity = 100;
            angleVelocity = 0;*/

            simPane.getChildren().clear();

            robot = new Robot(x, y, angle, velocity, angleVelocity);
            robotRect = new Rectangle(x, y, 50, 50);
            robotRect.setRotate(angle);

            simPane.getChildren().add(robotRect);
            moveRobot.start();
        });

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simSettings);
        Scene scene = new Scene(mainPane, 600, 635);
        primaryStage.setTitle("robot sim");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) { 
       Application.launch(args);
    }
}
