package App;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Base {

    public BorderPane mainPane = new BorderPane();
    public Pane simPane = new Pane();

    public Robot robot;

    public Button backBtn = new Button("Back");

    public void launch(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        backBtn.setLayoutX(10);
        backBtn.setLayoutY(10);
        simPane.getChildren().add(backBtn);

        backBtn.setOnMouseClicked(e -> {
            new CombinedSim().start(primaryStage);
            endTasks();
        });
        backBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                new CombinedSim().start(primaryStage);
                endTasks();
            }
        });

        simPane.setBackground(new Background(new BackgroundImage(
                new Image(CombinedSim.imgPath), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                null, null)));
        mainPane.setCenter(simPane);
    }

    public void endTasks() {}
}
