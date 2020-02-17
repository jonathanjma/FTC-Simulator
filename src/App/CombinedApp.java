package App;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class CombinedApp extends Application {

    public static final int sceneWidth = 600;

    private BorderPane mainPane = new BorderPane();
    private Pane startPane = new Pane();

    private Rectangle optionsRect = new Rectangle(175, 225, 250, 200);

    private Text title = new Text(230, 260, "Choose One");
    private Button autoPlannerBtn = new Button("Auto Planner");
    private Button autoPlayerBtn = new Button("Auto Player");
    private Button matchReplayerBtn = new Button("Match Replayer");

    @Override
    public void start(Stage primaryStage) {

        //startPane.setOnMouseClicked(event -> System.out.println(event.getX()+", "+event.getY()));

        optionsRect.setFill(Color.rgb(255,255,255,0.85));

        title.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 25));

        autoPlannerBtn.setLayoutX(250); autoPlannerBtn.setLayoutY(280);
        autoPlannerBtn.setFont(Font.font(Font.getDefault()+"", 15));

        autoPlayerBtn.setLayoutX(255); autoPlayerBtn.setLayoutY(320);
        autoPlayerBtn.setFont(Font.font(Font.getDefault()+"", 15));

        matchReplayerBtn.setLayoutX(240); matchReplayerBtn.setLayoutY(360);
        matchReplayerBtn.setFont(Font.font(Font.getDefault()+"", 15));

        startPane.getChildren().addAll(optionsRect, title, autoPlannerBtn, autoPlayerBtn, matchReplayerBtn);

        autoPlannerBtn.setOnMouseClicked(e -> {
            AutoPlanner planner = new AutoPlanner();
            planner.launch(primaryStage);
        });
        autoPlannerBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                AutoPlanner planner = new AutoPlanner();
                planner.launch(primaryStage);
            }
        });

        autoPlayerBtn.setOnMouseClicked(e -> {
            AutoPlayer player = new AutoPlayer();
            player.launch(primaryStage);
        });
        autoPlayerBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                AutoPlayer player = new AutoPlayer();
                player.launch(primaryStage);
            }
        });

        matchReplayerBtn.setOnMouseClicked(e -> {
            MatchReplayer replayer = new MatchReplayer();
            replayer.launch(primaryStage);
        });
        matchReplayerBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                MatchReplayer replayer = new MatchReplayer();
                replayer.launch(primaryStage);
            }
        });

        startPane.setBackground(new Background(
                new BackgroundImage(new Image("field.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                        null, null)));
        mainPane.setCenter(startPane);
        Scene scene = new Scene(mainPane, sceneWidth, sceneWidth);
        primaryStage.setTitle("FTC Tools");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("field.jpg"));
        primaryStage.show();
    }
}
