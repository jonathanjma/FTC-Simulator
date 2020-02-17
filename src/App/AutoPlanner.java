package App;

import Utilities.AutoPathsUtil;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import static Utilities.ConversionUtil.*;

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
    
    private double xCor, yCor;
    
    private Label corLb = new Label("Position: (");
    private Label commaLb = new Label(",");
    private Label angleLb1 = new Label(")          Angle (rad):");
    private Label angleLb2 = new Label("*π");
    private TextField xInchTf = new TextField("9");
    private TextField yInchTf = new TextField("111");
    private TextField angleTf = new TextField("0");
    private Button backBtn = new Button("Back");

    private int colorValue = 255;
    private final static double colorInterval = 40;
    private AutoPathsUtil pathsUtil = new AutoPathsUtil(simPane, colorValue, colorInterval);

    public void launch(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        simSettings.setPadding(new Insets(5, 5, 5, 5));
        simSettings.setAlignment(Pos.CENTER);
        
        corLb.setFont(Font.font(20)); commaLb.setFont(Font.font(20)); xInchTf.setPrefWidth(50); yInchTf.setPrefWidth(50);
        angleLb1.setFont(Font.font(20)); angleLb2.setFont(Font.font(20)); angleTf.setPrefWidth(50);

        simSettings.getChildren().addAll(corLb, xInchTf, commaLb, yInchTf, angleLb1, angleTf, angleLb2);

        backBtn.setLayoutX(10); backBtn.setLayoutY(10);
        simPane.getChildren().addAll(backBtn);

        pathsUtil.drawAutoPaths();
        updateRobotPos(2, null);
        
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
        Scene scene = new Scene(mainPane, CombinedApp.sceneWidth, CombinedApp.sceneWidth + 35);
        primaryStage.setTitle("Auto Planner");
        primaryStage.setScene(scene);
    }
    
    private void updateRobotPos(int code, MouseEvent e) { // 1 = mouse, 2 = pos input, 3 = angle input

        if (code == 1 || code == 2) {
            if (code == 1) {
                xCor = Double.parseDouble(String.format("%.2f", e.getSceneX()));
                yCor = Double.parseDouble(String.format("%.2f", e.getSceneY()));
            } else {
                xCor = getXPixel(Double.parseDouble(xInchTf.getText()));
                yCor = getYPixel(Double.parseDouble(yInchTf.getText()));
            }

            if (xCor - robotRadius < 0) {xCor = robotRadius;} //left
            if (xCor + robotRadius > CombinedApp.sceneWidth) {xCor = CombinedApp.sceneWidth - robotRadius;} //right
            if (yCor - robotRadius < 0) {yCor = robotRadius;} //up
            if (yCor + robotRadius > CombinedApp.sceneWidth) {yCor = CombinedApp.sceneWidth - robotRadius;} //down

            if (code == 1) {
                double xInch = Double.parseDouble(String.format("%.2f", getXInch(xCor)));
                double yInch = Double.parseDouble(String.format("%.2f", getYInch(yCor)));

                xInchTf.setText(xInch + "");
                yInchTf.setText(yInch + "");
            }
            //System.out.println(xCor + " " + yCor);
        }

        simPane.getChildren().remove(robotRect);

        robotRect = new Rectangle(xCor - robotRadius, yCor - robotRadius, robotLength, robotLength);

        robotRect.setRotate(getFXTheta_NoPi(Double.parseDouble(angleTf.getText())));

        Stop[] stops = {new Stop(0, Color.rgb(0, 0, 0, 0.75)),
                new Stop(1, Color.rgb(192, 192, 192, 0.75))};
        LinearGradient background = new LinearGradient(xCor, yCor, xCor+robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);

        simPane.getChildren().add(robotRect);
    }
}
