package App;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

@SuppressWarnings("FieldMayBeFinal")
public class CombinedSim extends Application {

    public static final int sceneWidth = 600;
    public static final String imgPath = "field.jpg";

    private BorderPane mainPane = new BorderPane();
    private Pane startPane = new Pane();
    private VBox homeBox = new VBox(10);

    private Rectangle optionsRect = new Rectangle(175, 225, 250, 230);

    private Text title = new Text("Choose One");
    private Button autoPlannerBtn = new Button("Simple Auto Planner");
    private Button pathGenBtn = new Button("Path Generator");
    private Button autoPlayerBtn = new Button("Auto Player");
    private Button matchReplayerBtn = new Button("Match Replayer");

    @Override
    public void start(Stage primaryStage) {

        //startPane.setOnMouseClicked(e -> System.out.println(e.getX()+", "+e.getY()));

        optionsRect.setFill(Color.rgb(255,255,255,0.85));

        homeBox.setPadding(new Insets(5, 5, 5, 5));
        homeBox.setAlignment(Pos.CENTER);
        homeBox.setLayoutX(220); homeBox.setLayoutY(230);

        title.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 25));
        autoPlannerBtn.setFont(Font.font(Font.getDefault()+"", 15));
        pathGenBtn.setFont(Font.font(Font.getDefault()+"", 15));
        autoPlayerBtn.setFont(Font.font(Font.getDefault()+"", 15));
        matchReplayerBtn.setFont(Font.font(Font.getDefault()+"", 15));

        homeBox.getChildren().addAll(title, autoPlannerBtn, pathGenBtn,
                autoPlayerBtn, matchReplayerBtn);
        startPane.getChildren().addAll(optionsRect, homeBox);

        autoPlannerBtn.setOnMouseClicked(e -> {
            new AutoPlanner().launch(primaryStage);
        });
        autoPlannerBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                new AutoPlanner().launch(primaryStage);
            }
        });

        pathGenBtn.setOnMouseClicked(e -> {
            new PathGenerator().launch(primaryStage);
        });
        pathGenBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                new PathGenerator().launch(primaryStage);
            }
        });

        autoPlayerBtn.setOnMouseClicked(e -> {
            new AutoPlayer().launch(primaryStage);
        });
        autoPlayerBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                new AutoPlayer().launch(primaryStage);
            }
        });

        matchReplayerBtn.setOnMouseClicked(e -> {
            new MatchReplayer().launch(primaryStage);
        });
        matchReplayerBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                new MatchReplayer().launch(primaryStage);
            }
        });

        startPane.setBackground(new Background(new BackgroundImage(
                new Image(CombinedSim.imgPath), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(startPane);
        Scene scene = new Scene(mainPane, sceneWidth, sceneWidth);
        primaryStage.setTitle("FTC Tools");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(CombinedSim.imgPath));
        primaryStage.show();
    }
}
