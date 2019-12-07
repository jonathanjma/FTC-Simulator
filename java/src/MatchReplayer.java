import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
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
public class MatchReplayer extends Application {
    
    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox simSettings = new HBox(5);
    private Rectangle robotRect;
    
    private double xCor, yCor, angle;
    
    private Label corLb = new Label("Position: (");
    private Label commaLb = new Label(",");
    private Label angleLb1 = new Label(")     Angle (rad):");
    private Label angleLb2 = new Label("*π     ");
    private Label xInchLb = new Label("n/a");
    private Label yInchLb = new Label("n/a");
    private Label angleLb = new Label("n/a");
    private Button startStopBtn = new Button("Start");
    private Button restartBtn = new Button("Restart");
    
    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double pixelToInch = 4.16;
    private final static double robotLength = 18 * pixelToInch;
    private final static double robotRadius = robotLength / 2;

    private PositionUtil posUtil = new PositionUtil();
    private boolean pause = false; private int counter = 0;
    private Thread followPosData = new Thread() {
        public void run() {
            for (; counter < posUtil.getNumOfPoints();) {
                if (!pause) {
                    Platform.runLater(() -> updateRobotPos(posUtil.getNextPos()));
                    try {sleep(100);} catch (InterruptedException ex) {ex.printStackTrace();}
                    counter++;
                }
                System.out.print("");
            }
        }
    };

    @Override
    public void start(Stage primaryStage) {

        // write test file
        /*posUtil.startLogging();
        int x = 9, y = 111; double theta = 0;
        for (int i = 0; i < 91; i++) {
            posUtil.writePos(x, y, Double.parseDouble(String.format("%.2f", theta)));
            x++; y--; theta -= 0.1;
        }
        posUtil.stopLogging();*/

        simSettings.setPadding(new Insets(5, 5, 5, 5));
        simSettings.setAlignment(Pos.CENTER);
        
        corLb.setFont(Font.font(20)); commaLb.setFont(Font.font(20));
        xInchLb.setFont(Font.font(20)); yInchLb.setFont(Font.font(20));
        angleLb1.setFont(Font.font(20)); angleLb2.setFont(Font.font(20)); angleLb.setFont(Font.font(20));
        restartBtn.setVisible(false);

        simSettings.getChildren().addAll(corLb, xInchLb, commaLb, yInchLb, angleLb1, angleLb, angleLb2,
                startStopBtn, restartBtn);

        posUtil.parsePosFile();

        startStopBtn.setOnAction(e -> {
            switch (startStopBtn.getText()) {
                case "Start":
                    followPosData.start();
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
            counter = 0; posUtil.setGetCounter(0); pause = false;
        });

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simSettings);
        Scene scene = new Scene(mainPane, 600, 635);
        primaryStage.setTitle("Match Replayer");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("field.jpg"));
        primaryStage.show();
    }

    private void updateRobotPos(double[] posData) {

        xCor = posData[0] * pixelToInch;
        yCor = (144 - posData[1]) * pixelToInch;

        if (xCor - robotRadius < 0) xCor = robotRadius; //left
        if (xCor + robotRadius > 600) xCor = 600 - robotRadius; //right
        if (yCor - robotRadius < 0) yCor = robotRadius; //up
        if (yCor + robotRadius > 600) yCor = 600 - robotRadius; //down

        simPane.getChildren().remove(robotRect);
        robotRect = new Rectangle(xCor - robotRadius, yCor - robotRadius, robotLength, robotLength);

        Stop[] stops = {new Stop(0, Color.rgb(0, 0, 0, 0.85)),
                new Stop(1, Color.rgb(192, 192, 192, 0.85))};
        LinearGradient background = new LinearGradient(xCor, yCor, xCor+robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);

        angle = -((posData[2] * 180) + 0.5);
        if (angle > 360) {angle %= 360;}
        robotRect.setRotate(angle);
        simPane.getChildren().add(robotRect);

        xInchLb.setText(posData[0] + ""); yInchLb.setText(posData[1] + ""); angleLb.setText(posData[2] + "");
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
