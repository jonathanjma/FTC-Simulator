import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class AutoPlanner extends Application {

    private BorderPane mainPane = new BorderPane();
    private Pane simPane = new Pane();
    private HBox simSettings = new HBox(5);
    
    private Label cor = new Label("(0, 0)");
    private Label corInch = new Label("(0, 0) inches");
    
    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    public final static double pixelToInch = 4.16;

    @Override
    public void start(Stage primaryStage) {

        simSettings.setPadding(new Insets(5, 5, 5, 5));
        simSettings.setAlignment(Pos.CENTER);
        
        cor.setFont(Font.font(20)); corInch.setFont(Font.font(20));
        //simSettings.getChildren().addAll(cor, corInch);
        simSettings.getChildren().addAll(corInch);

        mainPane.setOnMouseClicked(e -> {
            double xCor = Double.parseDouble(String.format("%.2f", e.getSceneX()));
            double yCor = Double.parseDouble(String.format("%.2f", e.getSceneY()));
            double xInch = Double.parseDouble(String.format("%.2f", xCor / pixelToInch));
            double yInch = Double.parseDouble(String.format("%.2f", yCor / pixelToInch));
            
            cor.setText("(" + xCor + ", " + yCor + ")");
            corInch.setText("(" + xInch + ", " + yInch + ") inches");
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

    public static void main(String[] args) { 
       Application.launch(args);
    }
}
