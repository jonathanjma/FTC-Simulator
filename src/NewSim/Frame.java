package NewSim;

import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import static Utilities.ConversionUtil.*;

public class Frame {

    private Rectangle robotRect;
    private Polygon robotPoly;

    public Frame(double x, double y, double theta) {

        robotRect = new Rectangle(robotLength, robotLength);

        // update robot xy
        double xCor = getXPixel(x);
        double yCor = getYPixel(y);
        robotRect.setX(xCor - robotRadius);
        robotRect.setY(yCor - robotRadius);

        // update robot theta
        robotRect.setRotate(getFXTheta(theta));

        // update fill
        Stop[] stops = new Stop[] {new Stop(0, Color.rgb(0, 0, 0, 0.85)),
                new Stop(1, Color.rgb(192, 192, 192, 0.85))};
        LinearGradient background = new LinearGradient(xCor, yCor, xCor + robotLength, yCor,
                false, CycleMethod.NO_CYCLE, stops);
        robotRect.setFill(background);
    }

    public Frame(Polygon polygon) {
        robotPoly = polygon;
        for (int i = 0; i < robotPoly.getPoints().size(); i++) {
            if (i % 2 == 0) { // x coors
                robotPoly.getPoints().set(i, getXPixel(robotPoly.getPoints().get(i)));
            } else { // y coors
                robotPoly.getPoints().set(i, getYPixel(robotPoly.getPoints().get(i)));
            }
        }
        robotPoly.setFill(Color.rgb(0, 0, 0, 0.85));
    }

    public Shape getRobot() {
        if (robotRect == null) {
            return robotPoly;
        } else {
            return robotRect;
        }
    }
}
