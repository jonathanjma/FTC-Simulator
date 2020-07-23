package App;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class PlayerBase extends Base {

    public HBox simInfo = new HBox(5);

    public Label corLb = new Label("Position: (");
    public Label xInchLb = new Label("n/a");
    public Label commaLb1 = new Label(",");
    public Label yInchLb = new Label("n/a");
    public Label thetaLb1 = new Label(")  Theta:");
    public Label thetaLb = new Label("n/a");
    public Button startStopBtn = new Button("Start");
    public Button restartBtn = new Button("Restart");

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        corLb.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14));
        commaLb1.setFont(Font.font(14)); xInchLb.setFont(Font.font(14));
        yInchLb.setFont(Font.font(14));
        thetaLb1.setFont(Font.font(Font.getDefault()+"", FontWeight.BOLD, 14));
        thetaLb.setFont(Font.font(14));

        restartBtn.setVisible(false);

        simInfo.setAlignment(Pos.CENTER);
        simInfo.getChildren().addAll(corLb, xInchLb, commaLb1, yInchLb, thetaLb1, thetaLb);
    }
}
