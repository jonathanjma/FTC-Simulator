package App;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static App.Robot.robotLength;
import static Utilities.ConversionUtil.*;

@SuppressWarnings("FieldCanBeLocal")
public class AutoPlanner {
    
    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox simSettings = new HBox(5);

    private Robot robot;
//    private Rectangle dumbRect;
    
    private Label corLb = new Label("Position: (");
    private Label commaLb = new Label(",");
    private Label angleLb1 = new Label(")  Angle (rad):");
    private Label angleLb2 = new Label("π/");
    private Label angleLb3 = new Label("or");
    private TextField xInchTf = new TextField("9");
    private TextField yInchTf = new TextField("111");
    private TextField angleTf1 = new TextField("0");
    private TextField angleTf2 = new TextField("1");
    private TextField angleTf3 = new TextField("0");
    private RadioButton advanced = new RadioButton();
    private RadioButton simple = new RadioButton();
    private ToggleGroup thetaChoices = new ToggleGroup();
    private Button backBtn = new Button("Back");

//    private Group bounds = new Group();

    public void launch(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        simSettings.setPadding(new Insets(5, 5, 5, 5));
        simSettings.setAlignment(Pos.CENTER);
        
        corLb.setFont(Font.font(20)); commaLb.setFont(Font.font(20)); xInchTf.setPrefWidth(50); yInchTf.setPrefWidth(50);
        angleLb1.setFont(Font.font(20)); angleLb2.setFont(Font.font(20)); angleLb3.setFont(Font.font(20)); angleTf1.setPrefWidth(35); angleTf2.setPrefWidth(35); angleTf3.setPrefWidth(50);

        simSettings.getChildren().addAll(corLb, xInchTf, commaLb, yInchTf, angleLb1, advanced, angleTf1,
                angleLb2, angleTf2, angleLb3, simple, angleTf3);

        backBtn.setLayoutX(10); backBtn.setLayoutY(10);
        simPane.getChildren().addAll(backBtn);

        advanced.setToggleGroup(thetaChoices);
        simple.setToggleGroup(thetaChoices);
        advanced.fire();
        angleTf1.setOnMouseClicked(e -> advanced.fire());
        angleTf2.setOnMouseClicked(e -> advanced.fire());
        angleTf3.setOnMouseClicked(e -> simple.fire());

        mainPane.setOnMouseClicked(e -> updateRobotPos(1, e));
        mainPane.setOnMouseDragged(e -> updateRobotPos(1, e));
        
        mainPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                updateRobotPos(2, null);
            }
            if (e.getCode() == KeyCode.ALT) {
                double next = Double.parseDouble(angleTf3.getText()) + 0.1;
                angleTf3.setText(String.format("%.3f", next));
                simple.fire();
                updateRobotPos(3, null);
            }
            if (e.getCode() == KeyCode.ALT_GRAPH) {
                double next = Double.parseDouble(angleTf3.getText()) - 0.1;
                angleTf3.setText(String.format("%.3f", next));
                simple.fire();
                updateRobotPos(3, null);
            }
        });

        robot = new Robot(robotLength, robotLength);

//        dumbRect = new Rectangle(200,300,300,100);
//        dumbRect.setRotate(45);

        updateRobotPos(2, null);
        simPane.getChildren().addAll(robot/*,dumbRect,bounds*/);

        backBtn.setOnMouseClicked(e-> {
            CombinedSim app = new CombinedSim();
            app.start(primaryStage);
        });
        backBtn.setOnKeyPressed(e-> {
            if (e.getCode() == KeyCode.ENTER) {
                CombinedSim app = new CombinedSim();
                app.start(primaryStage);
            }
        });

        simPane.setBackground(new Background(new BackgroundImage(
                new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simSettings);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 35);
        primaryStage.setTitle("Auto Planner");
        primaryStage.setScene(scene);
    }

    private void updateRobotPos(int inputMethod, MouseEvent e) { // 1 = mouse, 2 = pos input, 3 = angle input

        if (inputMethod == 1 || inputMethod == 2) {
            double xCor, yCor;

            if (inputMethod == 1) {
                xCor = getXInch(Double.parseDouble(String.format("%.2f", e.getSceneX())));
                yCor = getYInch(Double.parseDouble(String.format("%.2f", e.getSceneY())));
            } else {
                xCor = Double.parseDouble(xInchTf.getText());
                yCor = Double.parseDouble(yInchTf.getText());
            }

            robot.setPosition(xCor, yCor);
            xInchTf.setText(Double.parseDouble(String.format("%.2f", robot.xInch)) + "");
            yInchTf.setText(Double.parseDouble(String.format("%.2f", robot.yInch)) + "");
        }

        robot.setTheta(getInputTheta());

//        bounds.getChildren().clear();
//        Circle[] points = robot.getCorners();
//        bounds.getChildren().addAll(points[0],points[1],points[2],points[3]);
        //bounds.getChildren().add(robot.createBoundsRectangle());

        robot.updateColor();
    }

    public double getInputTheta() {
        double thetaRad;
        if (simple.isSelected()) {
            if (angleTf3.getText().equals("")) {
                angleTf3.setText("0");
            }
            thetaRad = Double.parseDouble(angleTf3.getText());
        } else {
            if (angleTf1.getText().equals("")) {
                angleTf1.setText("0");
            }
            if (angleTf2.getText().equals("0") || angleTf2.getText().equals("")) {
                angleTf2.setText("1");
            }

            thetaRad = Double.parseDouble(angleTf1.getText()) * Math.PI /
                    Double.parseDouble(angleTf2.getText());
            angleTf3.setText(String.format("%.3f", thetaRad));
        }

        return thetaRad;
    }
}
