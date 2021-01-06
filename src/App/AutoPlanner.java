package App;

import Utilities.AutoPathsUtil;
import Utilities.CompileUtil;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static App.Robot.robotLength;

public class AutoPlanner extends PlannerBase {

    private Group pathsGroup = new Group();
    private AutoPathsUtil pathsUtil = new AutoPathsUtil(pathsGroup, 255, 20);

    public void launch(Stage primaryStage) {
        super.launch(primaryStage);

        simSettings.setPadding(new Insets(5, 5, 5, 5));

        corLb.setFont(Font.font(20)); commaLb.setFont(Font.font(20));
        angleLb1.setFont(Font.font(20)); angleLb2.setFont(Font.font(20));
        angleLb3.setFont(Font.font(20));

        Button hi = new Button("pressss");
        hi.setOnAction(e -> reloadPaths());

        robot = new Robot(robotLength, robotLength);

        pathsUtil.drawAutoPaths();

        updateRobotPos(2, null);
        simPane.getChildren().addAll(robot, pathsGroup, hi);
        robot.toFront();

        mainPane.setBottom(simSettings);
        Scene scene = new Scene(mainPane, CombinedSim.sceneWidth, CombinedSim.sceneWidth + 35);
        primaryStage.setTitle("Auto Planner");
        primaryStage.setScene(scene);
    }

    public void reloadPaths() {
//        try {
//            pathsGroup.getChildren().clear();
//            pathsUtil = CompileUtil.reloadPathsUtil(pathsGroup);
//            pathsUtil.drawAutoPaths();
//        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException
//                | InstantiationException | InvocationTargetException ex) {
//            ex.printStackTrace();
//        }
    }
}
