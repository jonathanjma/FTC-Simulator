package App;

import Utilities.AutoPathsUtil;
import Utilities.CompileUtil;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static App.Robot.robotLength;

public class AutoPlanner extends PlannerBase {

    private Group pathsGroup = new Group();
    private AutoPathsUtil pathsUtil = new AutoPathsUtil(pathsGroup, 255, 20);

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        simSettings.setPadding(new Insets(0, 5, 5, 5));

        setFont(corLb, 20); setFont(commaLb, 20);
        setFont(angleLb1, 20); setFont(angleLb2, 20);
        setFont(angleLb3, 20);

        Button reloadBtn = new Button("Reload Paths");
        reloadBtn.setLayoutX(513); reloadBtn.setLayoutY(570);
        reloadBtn.setOnAction(e -> reloadPaths());

        robot = new Robot(robotLength, robotLength);

        pathsUtil.drawAutoPaths();

        updateRobotPos(2, null);
        simPane.getChildren().addAll(robot, pathsGroup, reloadBtn);
        robot.toFront();

        mainPane.setBottom(simSettings);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 35);
        primaryStage.setTitle("Auto Planner");
        primaryStage.setScene(scene);
    }

    public void reloadPaths() {
        try {
            pathsGroup.getChildren().clear();
            CompileUtil.reloadPathsUtil(pathsGroup, 255, 20).drawAutoPaths();
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }
}
