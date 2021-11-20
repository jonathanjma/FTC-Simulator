package main.App;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.PathingFiles.Path;
import main.PathingFiles.Waypoint;
import main.Utilities.AutoPathsUtil;

import java.util.ArrayList;

@SuppressWarnings("FieldMayBeFinal")
public class PathPlanner extends PlannerBase {

    private HBox simSettings2 = new HBox(5);
    private VBox simSettingsMain = new VBox(2.5);
    private Group pathsGroup = new Group();

    private Label velocityLb = new Label("Vel: ");
    private Label accelerationLb = new Label("Acc: ");
    private Label angVelocityLb = new Label("AngVel: ");
    private Label timeLb = new Label("Time: ");
    private TextField velocityTf = new TextField("0");
    private TextField accelerationTf = new TextField("0");
    private TextField angVelocityTf = new TextField("0");
    private TextField timeTf = new TextField("0");
    private Button addPoint = new Button("Add Point");
    private Button finish = new Button("Finish");
    private Button reset = new Button("Reset");

    private ArrayList<Waypoint> currentWaypoints;
    private AutoPathsUtil pathsUtil = new AutoPathsUtil(pathsGroup);

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        simSettings.setPadding(new Insets(2.5, 5, 0, 5));
        simSettings2.setPadding(new Insets(0, 5, 2.5, 5));
        simSettings2.setAlignment(Pos.CENTER);
        simSettingsMain.getChildren().addAll(simSettings, simSettings2);

        setFont(corLb, 15); setFont(commaLb, 15);
        setFont(angleLb1, 15); setFont(angleLb2, 15);
        setFont(angleLb3, 15);
        setFont(velocityLb, 15); setFont(accelerationLb, 15);
        setFont(angVelocityLb, 15); setFont(timeLb, 15);

        velocityTf.setPrefWidth(50); accelerationTf.setPrefWidth(50);
        angVelocityTf.setPrefWidth(50); timeTf.setPrefWidth(50);

        finish.setVisible(false); reset.setVisible(false);

        simSettings2.getChildren().addAll(velocityLb, velocityTf, accelerationLb, accelerationTf,
                angVelocityLb, angVelocityTf, timeLb, timeTf, addPoint, finish, reset);

        addPoint.setOnAction(e -> {
            if (currentWaypoints == null) {
                currentWaypoints = new ArrayList<>();
                finish.setVisible(true);
                reset.setVisible(true);
            }
            Waypoint waypoint = new Waypoint(Double.parseDouble(xInchTf.getText()),
                    Double.parseDouble(yInchTf.getText()),
                    getInputTheta(), Double.parseDouble(velocityTf.getText()),
                    Double.parseDouble(accelerationTf.getText()),
                    Double.parseDouble(angVelocityTf.getText()), Double.parseDouble(timeTf.getText()));
            currentWaypoints.add(waypoint);
            System.out.println(currentWaypoints);
            Path path = new Path(currentWaypoints);
            AutoPathsUtil.drawPath(path);
            robot.toFront();
        });

        finish.setOnAction(e -> {
            copyPathWindow(currentWaypoints);
            Path path = new Path(currentWaypoints);
            AutoPathsUtil.drawPath(path);
            currentWaypoints = null;
            robot.toFront();
            finish.setVisible(false);
            reset.setVisible(false);
            timeTf.setText("0");
        });

        reset.setOnAction(e -> {
            pathsGroup.getChildren().clear();
            currentWaypoints = null;
            xInchTf.setText("114");
            yInchTf.setText("9");
            angleTf1.setText("1");
            angleTf2.setText("2");
            timeTf.setText("0");
            finish.setVisible(false);
            reset.setVisible(false);
            advanced.fire();
            updateRobotPos(2, null);
        });

        robot = new Robot();
        updateRobotPos(2, null);
        simPane.getChildren().addAll(robot, pathsGroup);

        mainPane.setBottom(simSettingsMain);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 55);
        primaryStage.setTitle("Path Planner");
        primaryStage.setScene(scene);
    }

    public void copyPathWindow(ArrayList<Waypoint> waypoints) {
        Stage newStage = new Stage();
        VBox box = new VBox();
        box.setPadding(new Insets(5, 5, 5, 5));

        Label info = new Label("Code below has been copied to the clipboard:\n ");

        box.getChildren().add(info);

        ArrayList<String> codeTxt = new ArrayList<>();
        codeTxt.add("Waypoint[] Waypoints = new Waypoint[] {");
        for (Waypoint point : waypoints) {
            codeTxt.add("\tnew Waypoint(" + point.x + ", " + point.y + ", " +
                    String.format("%.4f", point.theta) + ", " + point.getVelocity() + ", " +
                    point.getAcceleration() + ", " + point.getAngVelocity() + ", " + point.time + "),");
        }
        codeTxt.add("};");
        codeTxt.add("Path Path = new Path(new ArrayList<>(Arrays.asList(Waypoints)));");

        String codeToCopy = "";
        for (String line : codeTxt) {
            Label codeLine = new Label(line);
            box.getChildren().addAll(codeLine);
            //noinspection StringConcatenationInLoop
            codeToCopy += line + "\n";
        }

        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(codeToCopy);
        clipboard.setContent(content);

        Scene stageScene = new Scene(box, 400, 200);
        newStage.setScene(stageScene);
        newStage.setTitle("Path Code");
        newStage.getIcons().add(new Image(CombinedSim.imgPath));
        newStage.show();
    }
}
