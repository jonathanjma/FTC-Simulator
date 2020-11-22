package App;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import static Utilities.ConversionUtil.*;

public class PlannerBase extends Base {

    public HBox simSettings = new HBox(5);

    public Label corLb = new Label("Position: (");
    public Label commaLb = new Label(",");
    public Label angleLb1 = new Label(")  Angle (rad):");
    public Label angleLb2 = new Label("π/");
    public Label angleLb3 = new Label("or");
    public TextField xInchTf = new TextField("120");
    public TextField yInchTf = new TextField("25");
    public TextField angleTf1 = new TextField("1");
    public TextField angleTf2 = new TextField("2");
    public TextField angleTf3 = new TextField("0");
    public RadioButton advanced = new RadioButton();
    public RadioButton simple = new RadioButton();
    public ToggleGroup thetaChoices = new ToggleGroup();

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        xInchTf.setPrefWidth(50); yInchTf.setPrefWidth(50);
        angleTf1.setPrefWidth(35); angleTf2.setPrefWidth(35); angleTf3.setPrefWidth(50);

        advanced.setToggleGroup(thetaChoices);
        simple.setToggleGroup(thetaChoices);
        advanced.fire();
        angleTf1.setOnMouseClicked(e -> advanced.fire());
        angleTf2.setOnMouseClicked(e -> advanced.fire());
        angleTf3.setOnMouseClicked(e -> simple.fire());

        mainPane.setOnMouseClicked(e -> {if(e.getY()<=600) updateRobotPos(1, e);});
        mainPane.setOnMouseDragged(e -> {if(e.getY()<=600) updateRobotPos(1, e);});

        mainPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                updateRobotPos(2, null);
            }
            if (e.getCode() == KeyCode.ALT) {
                double next = Double.parseDouble(angleTf3.getText()) + 0.1;
                angleTf3.setText(String.format("%.3f", next));
                simple.fire();
                updateRobotPos(3, null);
            }
            if (e.getCode() == KeyCode.ALT_GRAPH) {
                double next = Double.parseDouble(angleTf3.getText()) - 0.1;
                angleTf3.setText(String.format("%.3f", next));
                simple.fire();
                updateRobotPos(3, null);
            }
        });

        simSettings.setAlignment(Pos.CENTER);
        simSettings.getChildren().addAll(corLb, xInchTf, commaLb, yInchTf, angleLb1, advanced,
                angleTf1, angleLb2, angleTf2, angleLb3, simple, angleTf3);
    }

    // 1 = mouse, 2 = pos input, 3 = angle input
    public void updateRobotPos(int inputMethod, MouseEvent e) {

        if (inputMethod == 1 || inputMethod == 2) {
            double xCor, yCor;

            if (inputMethod == 1) {
                xCor = getXInch(Double.parseDouble(String.format("%.2f", e.getSceneX())));
                yCor = getYInch(Double.parseDouble(String.format("%.2f", e.getSceneY())));
            } else {
                xCor = Double.parseDouble(xInchTf.getText());
                yCor = Double.parseDouble(yInchTf.getText());
            }

            robot.setPosition(xCor, yCor);
            xInchTf.setText(Double.parseDouble(String.format("%.2f", robot.xInch)) + "");
            yInchTf.setText(Double.parseDouble(String.format("%.2f", robot.yInch)) + "");
        }

        robot.setTheta(getInputTheta());
        robot.updateColor();
    }

    public double getInputTheta() {
        double thetaRad;
        if (simple.isSelected()) {
            if (angleTf3.getText().equals("")) {
                angleTf3.setText("0");
            }
            thetaRad = Double.parseDouble(angleTf3.getText());
        } else {
            if (angleTf1.getText().equals("")) {
                angleTf1.setText("0");
            }
            if (angleTf2.getText().equals("0") || angleTf2.getText().equals("")) {
                angleTf2.setText("1");
            }

            thetaRad = Double.parseDouble(angleTf1.getText()) * Math.PI /
                    Double.parseDouble(angleTf2.getText());
            angleTf3.setText(String.format("%.3f", thetaRad));
        }

        return thetaRad;
    }
}
