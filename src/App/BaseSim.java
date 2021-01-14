package App;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BaseSim {

    public BorderPane mainPane = new BorderPane();
    public Pane simPane = new Pane();

    public Robot robot;

    public Button backBtn = new Button("Back");

    public void launch(Stage primaryStage) {

        //simPane.setOnMouseClicked(event -> System.out.println(event.getX()+","+event.getY()));

        backBtn.setLayoutX(10); backBtn.setLayoutY(10);
        simPane.getChildren().add(backBtn);

        backBtn.setOnAction(e -> {
            new CombinedSim().start(primaryStage);
            endTasks();
        });
        backBtn.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                new CombinedSim().start(primaryStage);
                endTasks();
            }
        });

        primaryStage.setOnCloseRequest(e -> endTasks());

        simPane.setBackground(new Background(new BackgroundImage(
                new Image(CombinedSim.imgPath), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                null, null)));
        mainPane.setCenter(simPane);
    }

    public void endTasks() {}

    public void setFont(Label label, int fontSize) {
        label.setFont(Font.font(fontSize));
    }
    public void setFontBold(Label label, int fontSize) {
        label.setFont(Font.font(Font.getDefault() + "", FontWeight.BOLD, fontSize));
    }
    public void setFont(Text text, int fontSize) {
        text.setFont(Font.font(fontSize));
    }
    public void setFontBold(Text text, int fontSize) {
        text.setFont(Font.font(Font.getDefault() + "", FontWeight.BOLD, fontSize));
    }
}
