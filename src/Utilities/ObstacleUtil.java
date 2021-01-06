package Utilities;

import App.Obstacle;
import App.Robot;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

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
            boolean result = isPolygonsIntersecting(robot.getPolygon(), ob);
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

    public boolean isPolygonsIntersecting(Polygon a, Polygon b) {
        for (int x = 0; x < 2; x++) {
            Polygon polygon = (x == 0) ? a : b;

            ArrayList<Point> points = parsePolygon(polygon);
            for (int i1 = 0; i1 < points.size(); i1++) {
                int i2 = (i1 + 1) % points.size();
                Point p1 = points.get(i1);
                Point p2 = points.get(i2);

                Point normal = new Point(p2.y - p1.y, p1.x - p2.x);

                double minA = Double.POSITIVE_INFINITY;
                double maxA = Double.NEGATIVE_INFINITY;

                ArrayList<Point> aPoints = parsePolygon(a);
                for (Point p : aPoints) {
                    double projected = normal.x * p.x + normal.y * p.y;

                    if (projected < minA) {
                        minA = projected;
                    }
                    if (projected > maxA) {
                        maxA = projected;
                    }
                }

                double minB = Double.POSITIVE_INFINITY;
                double maxB = Double.NEGATIVE_INFINITY;

                ArrayList<Point> bPoints = parsePolygon(b);
                for (Point p : bPoints) {
                    double projected = normal.x * p.x + normal.y * p.y;

                    if (projected < minB) {
                        minB = projected;
                    }
                    if (projected > maxB) {
                        maxB = projected;
                    }
                }

                if (maxA < minB || maxB < minA) {
                    return false;
                }
            }
        }

        return true;
    }

    public ArrayList<Point> parsePolygon(Polygon py) {
        ArrayList<Point> list = new ArrayList<>();

        for (int i = 0; i < py.getPoints().size(); i+=2) {
            Point pt = new Point(py.getPoints().get(i), py.getPoints().get(i+1));
            list.add(pt);
        }
        return list;
    }
}
