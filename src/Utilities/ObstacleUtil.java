package Utilities;

import App.Obstacle;
import App.Robot;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

import static App.Robot.robotLength;

@SuppressWarnings("FieldMayBeFinal")
public class ObstacleUtil {

    private Group obstacleGroup;
    private Group warningGroup;
    private ArrayList<Obstacle> obList;
    private SimpleBooleanProperty collisionProp;

    public ObstacleUtil(Group obstacleGroup, Group warningGroup) {
        this.obstacleGroup = obstacleGroup;
        this.warningGroup = warningGroup;
        collisionProp = new SimpleBooleanProperty(false);
    }

    public void initializeObstacles() {

//        Obstacle island = new Obstacle(
//                new Point(200,265),
//                new Point(200,340),
//                new Point(400,340),
//                new Point(400,265)
//        );
//        Obstacle alliance = new Obstacle(
//                new Point(5,230),
//                new Point(robotLength+6,230),
//                new Point(robotLength+6,400),
//                new Point(5,400)
//        );

        obList = new ArrayList<>(Arrays.asList(/*island, alliance*/));
        obstacleGroup.getChildren().addAll(/*island, alliance*/);

        int wx = 430, wy = 530;
        Rectangle warning = new Rectangle(wx, wy, 160, 60);
        warning.setFill(Color.rgb(255,0,56));
        warning.setStroke(Color.BLACK);
        warning.setArcWidth(30.0);
        warning.setArcHeight(30.0);

        Text warnText = new Text(wx+8,wy+37,"Potential Collision!");
        warnText.setFont(Font.font(18));

        warning.visibleProperty().bind(collisionProp);
        warnText.visibleProperty().bind(collisionProp);
        warningGroup.getChildren().addAll(warning, warnText);
    }

    public void checkCollisions(Robot robot) {
        for (Obstacle ob : obList) {
            boolean result = Intersect.isPolygonsIntersecting(robot.getPolygon(), ob);
            //System.out.println(result);
            if (result) {
                if (!collisionProp.get()) {
                    collisionProp.set(true);
                    robot.setStroke(Color.RED);
                }
                if (ob.getStroke() == Color.BLACK) ob.setStroke(Color.YELLOW);
                break;
            } else {
                if (collisionProp.get()) {
                    collisionProp.set(false);
                    robot.setStroke(Color.TRANSPARENT);
                }
                if (ob.getStroke() == Color.YELLOW) ob.setStroke(Color.BLACK);
            }
        }
    }
}
