package App;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Obstacle extends Polygon {

    public Obstacle(Point2D pt1, Point2D pt2, Point2D pt3, Point2D pt4) {
        getPoints().addAll(pt1.getX(), pt1.getY(), pt2.getX(), pt2.getY(), pt3.getX(), pt3.getY(), pt4.getX(), pt4.getY());
        setFill(Color.rgb(128,128,128, 0.5));
        setStroke(Color.BLACK);
        setStrokeWidth(3);
    }
}
