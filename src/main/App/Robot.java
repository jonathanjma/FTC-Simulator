package main.App;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static main.Utilities.ConversionUtil.*;

public class Robot extends Rectangle {

    public double xInch;
    public double yInch;
    public double thetaRad;

    public final static double robotLength = 18 * inchToPixel;
    public final static double robotRadius = robotLength / 2;

    private final static int robotRad = 9, fieldWidth = 144;

    public Robot(double width, double height) {
        setWidth(width);
        setHeight(height);
        setStrokeWidth(1.5);
    }

    public Robot(double x, double y, double width, double height) {
        this(width, height);
        setPosition(x, y);
    }

    public void setPosition(double xInch, double yInch) {

        if (xInch - robotRad < 0) { // left
            xInch = robotRad;
        }
        if (xInch + robotRad > fieldWidth) { // right
            xInch = fieldWidth - 9;
        }
        if (yInch - robotRad < 0) { // down
            yInch = robotRad;
        }
        if (yInch + robotRad > fieldWidth) { // up
            yInch = fieldWidth - 9;
        }

        this.xInch = xInch;
        this.yInch = yInch;
        setX(getXPixel(xInch) - robotRadius);
        setY(getYPixel(yInch) - robotRadius);
    }

    public void setTheta(double thetaRad) {
        this.thetaRad = thetaRad;
        setRotate(getFXTheta(thetaRad));
    }

    public void setPose(double xInch, double yInch, double thetaRad) {
        setPosition(xInch, yInch);
        setTheta(thetaRad);
    }

    public void updateColor() {
        updateColor(new Stop[] {
                new Stop(0, Color.rgb(0, 0, 0, 0.75)),
                new Stop(1, Color.rgb(192, 192, 192, 0.75))});
    }

    public void updateColor(Stop[] stops) {
        LinearGradient background = new LinearGradient(getXPixel(xInch), getYPixel(yInch),
                getXPixel(xInch) + robotLength, getYPixel(yInch),
                false, CycleMethod.NO_CYCLE, stops);
        setFill(background);
    }

    public double[][] getCorners() {
        double tl_x = xInch + (robotRad * Math.cos(thetaRad)) - (robotRad * Math.sin(thetaRad));
        double tl_y = yInch + (robotRad * Math.sin(thetaRad)) + (robotRad * Math.cos(thetaRad));

        double tr_x = xInch + (robotRad * Math.cos(thetaRad)) + (robotRad * Math.sin(thetaRad));
        double tr_y = yInch + (robotRad * Math.sin(thetaRad)) - (robotRad * Math.cos(thetaRad));

        double bl_x = xInch - (robotRad * Math.cos(thetaRad)) - (robotRad * Math.sin(thetaRad));
        double bl_y = yInch - (robotRad * Math.sin(thetaRad)) + (robotRad * Math.cos(thetaRad));

        double br_x = xInch - (robotRad * Math.cos(thetaRad)) + (robotRad * Math.sin(thetaRad));
        double br_y = yInch - (robotRad * Math.sin(thetaRad)) - (robotRad * Math.cos(thetaRad));

        return new double[][] {{tl_x, tl_y}, {tr_x, tr_y}, {bl_x, bl_y}, {br_x, br_y}};
    }

    public Polygon getPolygon() {
        Polygon polygon = new Polygon();
        double[][] points = getCorners();
        polygon.getPoints().addAll(
                getXPixel(points[0][0]), getYPixel(points[0][1]),
                getXPixel(points[1][0]), getYPixel(points[1][1]),
                getXPixel(points[2][0]), getYPixel(points[2][1]),
                getXPixel(points[3][0]), getYPixel(points[3][1])
        );
        polygon.setFill(Color.GREEN);
        return polygon;
    }

    public Circle[] cornerCircles() {
        double[][] points = getCorners();
        Circle tl = new Circle(getXPixel(points[0][0]), getYPixel(points[0][1]),5); tl.setFill(Color.BLACK);
        Circle tr = new Circle(getXPixel(points[1][0]), getYPixel(points[1][1]),5); tr.setFill(Color.BLUE);
        Circle bl = new Circle(getXPixel(points[2][0]), getYPixel(points[2][1]),5); bl.setFill(Color.WHITE);
        Circle br = new Circle(getXPixel(points[3][0]), getYPixel(points[3][1]),5); br.setFill(Color.RED);
        return new Circle[] {tl,tr,bl,br};
    }

    public Rectangle createBoundsRectangle() {
        Bounds b = getBoundsInParent();
        Rectangle r = new Rectangle(b.getMinX(), b.getMinY(), b.getWidth(), b.getHeight());
        r.setFill(Color.TRANSPARENT);
        r.setStroke(Color.LIGHTGRAY.deriveColor(1, 1, 1, 0.5));
        r.setStrokeType(StrokeType.INSIDE);
        r.setStrokeWidth(3);
        return r;
    }
}
