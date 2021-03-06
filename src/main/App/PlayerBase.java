package main.App;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class PlayerBase extends BaseSim {

    public enum State {NotStarted, Playing, Paused}
    public State state = State.NotStarted;

    public HBox simInfo = new HBox(5);

    public Label corLb = new Label("Pose: (");
    public Label xInchLb = new Label("n/a");
    public Label commaLb1 = new Label(",");
    public Label yInchLb = new Label("n/a");
    public Label commaLb2 = new Label(",");
    public Label thetaLb = new Label("n/a");
    public Label corLb2 = new Label(")");
    public Button startStopBtn;
    public Button restartBtn;

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        setFontBold(corLb, 14);
        setFont(xInchLb, 14); setFont(commaLb1, 14); setFont(yInchLb, 14);
        setFont(commaLb2, 14); setFont(thetaLb, 14); setFontBold(corLb2, 14);

        ImageView startStopImg = new ImageView(new Image("main/imgs/play.png"));
        startStopImg.setFitWidth(20); startStopImg.setFitHeight(20);
        startStopBtn = new Button("", startStopImg);

        ImageView restartImg = new ImageView(new Image("main/imgs/restart.png"));
        restartImg.setFitWidth(20); restartImg.setFitHeight(20);
        restartBtn = new Button("", restartImg);
        restartBtn.setDisable(true);

        simInfo.setAlignment(Pos.CENTER);
        simInfo.getChildren().addAll(corLb, xInchLb, commaLb1, yInchLb, commaLb2, thetaLb, corLb2);
    }

    public void setState(State state) {
        this.state = state;
        ImageView startStopImg;
        if (state == State.Playing) {
            startStopImg = new ImageView(new Image("main/imgs/pause.png"));
        } else {
            startStopImg = new ImageView(new Image("main/imgs/play.png"));
        }
        startStopImg.setFitWidth(20); startStopImg.setFitHeight(20);
        startStopBtn.setGraphic(startStopImg);
    }
}
