package main.App;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import static main.Utilities.ConversionUtil.getXPixel;
import static main.Utilities.ConversionUtil.getYPixel;

public class Obstacle extends Polygon {

    public Obstacle(Point2D pt1, Point2D pt2, Point2D pt3, Point2D pt4) {
        getPoints().addAll(getXPixel(pt1.getX()), getYPixel(pt1.getY()), getXPixel(pt2.getX()), getYPixel(pt2.getY()),
                getXPixel(pt3.getX()), getYPixel(pt3.getY()), getXPixel(pt4.getX()), getYPixel(pt4.getY()));
        setFill(Color.rgb(128,128,128, 0.5));
        setStroke(Color.BLACK);
        setStrokeWidth(3);
    }
}
