package NewSim;

import App.CombinedSim;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

@SuppressWarnings("FieldMayBeFinal")
public class SimulatorWindow extends Application {

    /**
     * Do not run this program!!!
     * Run Simulator.java
     */

    private BorderPane mainPane = new BorderPane();
    private static Pane simPane = new Pane();
    private HBox simInfo = new HBox(5);
    private Button startBtn = new Button("Start");

    public static boolean started = false;

    @Override
    public void start(Stage primaryStage) {

        simInfo.setPadding(new Insets(0, 5, 5, 5));
        simInfo.setAlignment(Pos.CENTER);
        simInfo.getChildren().addAll(startBtn);

        startBtn.setOnAction(e -> {
            startBtn.setVisible(false);
            started = true;
        });
        primaryStage.setOnCloseRequest(e -> System.exit(0));

        simPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(simPane);
        mainPane.setBottom(simInfo);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 35);
        primaryStage.setTitle("New Sim Test");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("field.jpg"));
        primaryStage.show();
    }

    public static void drawShape(Shape shape) {
        simPane.getChildren().clear();
        simPane.getChildren().add(shape);
    }
}
