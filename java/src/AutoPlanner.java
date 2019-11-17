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
       jar cfe Planner.jar AutoPlanner *
    5. Run jar by clicking on it or running
       java -jar Planner.jar
     */
    
    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox simSettings = new HBox(5);
    private Rectangle robotRect;
    
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
    private final static double pixelToInch = 4.16;
    private final static double robotLength = 18 * pixelToInch;
    private final static double robotRadius = robotLength / 2;
    
    @Override
    public void start(Stage primaryStage) {
        
        simSettings.setPadding(new Insets(5, 5, 5, 5));
        simSettings.setAlignment(Pos.CENTER);
        
        corLb.setFont(Font.font(20)); commaLb.setFont(Font.font(20)); xInchTf.setPrefWidth(50); yInchTf.setPrefWidth(50);
        angleLb1.setFont(Font.font(20)); angleLb2.setFont(Font.font(20)); angleTf.setPrefWidth(50);
        
        simSettings.getChildren().addAll(corLb, xInchTf, commaLb, yInchTf, angleLb1, angleTf, angleLb2);
        
        updateRobotPos(2, null);
        
        mainPane.setOnMouseClicked(e -> updateRobotPos(1, e));
        mainPane.setOnMouseDragged(e -> updateRobotPos(1, e));
        
        mainPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) updateRobotPos(2, null);
            if (e.getCode() == KeyCode.ALT_GRAPH) {
                angleTf.setText(String.format("%.2f", Double.parseDouble(angleTf.getText()) + 0.1));
                updateRobotPos(3, null);
            }
            if (e.getCode() == KeyCode.CONTROL) {
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
                xCor = Double.parseDouble(xInchTf.getText()) * pixelToInch;
                yCor = (144 - Double.parseDouble(yInchTf.getText())) * pixelToInch;
            }
    
            if (xCor - robotRadius < 0) xCor = robotRadius; //left
            if (xCor + robotRadius > 600) xCor = 600 - robotRadius; //right
            if (yCor - robotRadius < 0) yCor = robotRadius; //up
            if (yCor + robotRadius > 600) yCor = 600 - robotRadius; //down
    
            if (code == 1) {
                xInch = Double.parseDouble(String.format("%.2f", xCor / pixelToInch));
                double yCorAdjusted = 600 - yCor;
                yInch = Double.parseDouble(String.format("%.2f", yCorAdjusted / pixelToInch));
        
                xInchTf.setText(xInch + "");
                yInchTf.setText(yInch + "");
            }
            //System.out.println(xCor + " " + yCor);
        }
        
        simPane.getChildren().remove(robotRect);
        robotRect = new Rectangle(xCor - robotRadius, yCor - robotRadius, robotLength, robotLength);
    
        Stop[] stops = {new Stop(0, Color.rgb(0, 0, 0, 0.85)),
                new Stop(1, Color.rgb(192, 192, 192, 0.85))};
        LinearGradient background = new LinearGradient(xCor, yCor, xCor+robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);
        angle = -((Double.parseDouble(angleTf.getText()) * 180) + 0.5);
        if (angle > 360) {angle %= 360;}
        robotRect.setRotate(angle);
        simPane.getChildren().add(robotRect);
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
