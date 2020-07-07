package NewSim;

import App.Robot;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import static App.Robot.robotLength;
import static Utilities.ConversionUtil.*;

public class Frame {

    private Robot robotRect;
    private Polygon robotPoly;

    public Frame(double x, double y, double theta) {

        robotRect = new Robot(robotLength, robotLength);
        robotRect.setPosition(x, y);
        robotRect.setTheta(theta);
        robotRect.updateColor();
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
