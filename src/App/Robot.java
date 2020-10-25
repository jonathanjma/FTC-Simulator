package App;

import javafx.geometry.Bounds;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

import static Utilities.ConversionUtil.*;

public class Robot extends Rectangle {

    public double xInch;
    public double yInch;
    public double thetaRad;

    public final static double robotLength = 18 * inchToPixel;
    public final static double robotRadius = robotLength / 2;

    private final int robotRad = 9, fieldWidth = 144;

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

    public void updateColor() {
        Stop[] stops = {new Stop(0, Color.rgb(0, 0, 0, 0.75)),
                new Stop(1, Color.rgb(192, 192, 192, 0.75))};
        LinearGradient background = new LinearGradient(getXPixel(xInch), getYPixel(yInch),
                getXPixel(xInch) + robotLength, getYPixel(yInch),
                false, CycleMethod.NO_CYCLE, stops);
        setFill(background);
    }

    public void updateColor(Stop[] stops) {
        LinearGradient background = new LinearGradient(getXPixel(xInch), getYPixel(yInch),
                getXPixel(xInch) + robotLength, getYPixel(yInch),
                false, CycleMethod.NO_CYCLE, stops);
        setFill(background);
    }

    public Circle[] getCorners() {
        double tl_x = xInch + (robotRad * Math.cos(thetaRad)) - (robotRad * Math.sin(thetaRad));
        double tl_y = yInch + (robotRad * Math.sin(thetaRad)) + (robotRad * Math.cos(thetaRad));
        Circle tl = new Circle(getXPixel(tl_x),getYPixel(tl_y),5);
        tl.setFill(Color.BLACK);

        double tr_x = xInch + (robotRad * Math.cos(thetaRad)) + (robotRad * Math.sin(thetaRad));
        double tr_y = yInch + (robotRad * Math.sin(thetaRad)) - (robotRad * Math.cos(thetaRad));
        Circle tr = new Circle(getXPixel(tr_x),getYPixel(tr_y),5);
        tr.setFill(Color.BLUE);

        double bl_x = xInch - (robotRad * Math.cos(thetaRad)) - (robotRad * Math.sin(thetaRad));
        double bl_y = yInch - (robotRad * Math.sin(thetaRad)) + (robotRad * Math.cos(thetaRad));
        Circle bl = new Circle(getXPixel(bl_x),getYPixel(bl_y),5);
        bl.setFill(Color.WHITE);

        double br_x = xInch - (robotRad * Math.cos(thetaRad)) + (robotRad * Math.sin(thetaRad));
        double br_y = yInch - (robotRad * Math.sin(thetaRad)) - (robotRad * Math.cos(thetaRad));
        Circle br = new Circle(getXPixel(br_x),getYPixel(br_y),5);
        br.setFill(Color.RED);

        return new Circle[] {tl,tr,bl,br};
    }

    public Polygon getPolygon() {
        Polygon polygon = new Polygon();
        Circle[] points = getCorners();
        polygon.getPoints().addAll(
                points[0].getCenterX(),points[0].getCenterY(),
                points[1].getCenterX(),points[1].getCenterY(),
                points[3].getCenterX(),points[3].getCenterY(),
                points[2].getCenterX(),points[2].getCenterY()
        );
        polygon.setFill(Color.GREEN);
        return polygon;
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
