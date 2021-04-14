package main.Utilities;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import main.App.Obstacle;
import main.App.Robot;

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

        Obstacle blueAlliance = new Obstacle(
                new Point2D(0,0),
                new Point2D(0,144),
                new Point2D(48,144),
                new Point2D(48,0)
        );

        Obstacle backWall = new Obstacle(
                new Point2D(0,144),
                new Point2D(144,144),
                new Point2D(144,147),
                new Point2D(0,147)
        );

        obList = new ArrayList<>(Arrays.asList(blueAlliance, backWall));
        obstacleGroup.getChildren().addAll(blueAlliance, backWall);

        int wx = 435, wy = 505;
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

            ArrayList<Point2D> points = parsePolygon(polygon);
            for (int i1 = 0; i1 < points.size(); i1++) {
                int i2 = (i1 + 1) % points.size();
                Point2D p1 = points.get(i1);
                Point2D p2 = points.get(i2);

                Point2D normal = new Point2D(p2.getY() - p1.getY(), p1.getX() - p2.getX());

                double minA = Double.POSITIVE_INFINITY;
                double maxA = Double.NEGATIVE_INFINITY;

                ArrayList<Point2D> aPoints = parsePolygon(a);
                for (Point2D p : aPoints) {
                    double projected = normal.getX() * p.getX() + normal.getY() * p.getY();

                    if (projected < minA) {
                        minA = projected;
                    }
                    if (projected > maxA) {
                        maxA = projected;
                    }
                }

                double minB = Double.POSITIVE_INFINITY;
                double maxB = Double.NEGATIVE_INFINITY;

                ArrayList<Point2D> bPoints = parsePolygon(b);
                for (Point2D p : bPoints) {
                    double projected = normal.getX() * p.getX() + normal.getY() * p.getY();

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

    public ArrayList<Point2D> parsePolygon(Polygon py) {
        ArrayList<Point2D> list = new ArrayList<>();

        for (int i = 0; i < py.getPoints().size(); i+=2) {
            Point2D pt = new Point2D(py.getPoints().get(i), py.getPoints().get(i+1));
            list.add(pt);
        }
        return list;
    }
}
