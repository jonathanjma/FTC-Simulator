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

    public final static double robotHeight = 13 * inchToPixel;
    public final static double robotWidth = 18 * inchToPixel;

    private final static double robotRadX = 6.5, robotRadY = 9, fieldWidth = 144;

    public Robot() {
        this(robotWidth, robotHeight);
    }

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

        if (xInch - robotRadX < 0) { // left
            xInch = robotRadX;
        }
        if (xInch + robotRadX > fieldWidth) { // right
            xInch = fieldWidth - robotRadX;
        }
        if (yInch - robotRadY < 0) { // down
            yInch = robotRadY;
        }
        if (yInch + robotRadY > fieldWidth) { // up
            yInch = fieldWidth - robotRadY;
        }

        this.xInch = xInch;
        this.yInch = yInch;
        setX(getXPixel(xInch) - robotWidth/2);
        setY(getYPixel(yInch) - robotHeight/2);
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
                getXPixel(xInch) + robotHeight, getYPixel(yInch),
                false, CycleMethod.NO_CYCLE, stops);
        setFill(background);
    }

    public double[][] getCorners() {
        double tl_x = xInch + (robotRadX * Math.cos(thetaRad)) - (robotRadX * Math.sin(thetaRad));
        double tl_y = yInch + (robotRadY * Math.sin(thetaRad)) + (robotRadY * Math.cos(thetaRad));

        double tr_x = xInch + (robotRadX * Math.cos(thetaRad)) + (robotRadX * Math.sin(thetaRad));
        double tr_y = yInch + (robotRadY * Math.sin(thetaRad)) - (robotRadY * Math.cos(thetaRad));

        double bl_x = xInch - (robotRadX * Math.cos(thetaRad)) - (robotRadX * Math.sin(thetaRad));
        double bl_y = yInch - (robotRadY * Math.sin(thetaRad)) + (robotRadY * Math.cos(thetaRad));

        double br_x = xInch - (robotRadX * Math.cos(thetaRad)) + (robotRadX * Math.sin(thetaRad));
        double br_y = yInch - (robotRadY * Math.sin(thetaRad)) - (robotRadY * Math.cos(thetaRad));

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
