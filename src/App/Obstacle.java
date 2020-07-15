package App;

import Utilities.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Obstacle extends Polygon {

    public Obstacle(Point pt1, Point pt2, Point pt3, Point pt4) {
        getPoints().addAll(pt1.x, pt1.y, pt2.x, pt2.y, pt3.x, pt3.y, pt4.x, pt4.y);
        setFill(Color.rgb(128,128,128, 0.5));
        setStroke(Color.BLACK);
        setStrokeWidth(3);
    }
}
