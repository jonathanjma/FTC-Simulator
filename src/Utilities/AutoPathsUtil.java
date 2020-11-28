package Utilities;

import PathingFiles.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Arrays;

import static Utilities.ConversionUtil.getXPixel;
import static Utilities.ConversionUtil.getYPixel;
import static java.lang.Math.PI;

public class AutoPathsUtil {

    private Group pathsGroup;
    private SplineGenerator splineGenerator = new SplineGenerator();

    private ArrayList<Path> pathList;
    private ArrayList<Double> timeList;

    private int colorValue;
    private final double colorInterval;

    public AutoPathsUtil(Group pathsGroup) {
        this.pathsGroup = pathsGroup;
        colorValue = 255;
        this.colorInterval = 0;
    }

    public AutoPathsUtil(Group pathsGroup, int startingColorValue, double colorInterval) {
        this.pathsGroup = pathsGroup;
        colorValue = startingColorValue;
        this.colorInterval = colorInterval;
    }

    enum RingCase {One, Four, Zero};
    double lX, lY, lTh;

    public void drawAutoPaths() {

        pathList = new ArrayList<>(); timeList = new ArrayList<>();

        double startLineTime = 2;
        double shootPowerShotsTime = 2;
        double deliverWobbleTime = 2;
        double intakeWobble2Time = 3;
        double intakeStackTime = 3;
        double shootHighGoalTime = 2;
        double deliverWobble2Time = 3;
        double parkTime = 1;

        RingCase ringCase = RingCase.One;
        Point2D[] wobbleDelivery = {
                new Point2D(123, 80), new Point2D(97, 100), new Point2D(123, 120)
        };

        Point2D wobbleCor;
        if (ringCase == RingCase.Four) {
            wobbleCor = wobbleDelivery[2];
            /*deliverWobbleTime =
            wobbleTwoTime = */
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
            /*deliverWobbleTime =
            wobbleTwoTime =*/
        } else {
            wobbleCor = wobbleDelivery[0];
            /*deliverWobbleTime =
            wobbleTwoTime =*/
        }

        Waypoint[] startLineWaypoints = new Waypoint[] {
                new Waypoint(90, 9, PI/2, 20.0, 50.0, 0.0, 0.0),
                new Waypoint(90, 74, PI/2, 10.0, -20.0, 0.0, startLineTime),
        };
        Path startLinePath = new Path(new ArrayList<>(Arrays.asList(startLineWaypoints)));
        drawPath(startLinePath, startLineTime);

        Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 10.0, 30.0, 0.0, 0.0),
                new Waypoint(wobbleCor.getX(), wobbleCor.getY(), 5*PI/6, 10.0, -30.0, 0.0, deliverWobbleTime),
        };
        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)));
        drawPath(deliverWobblePath, deliverWobbleTime);

        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, -10.0, -50.0, 0.0, 0.0),
                new Waypoint(125, 60, PI/3, -20.0, 10.0, 0.0, 1.5),
                new Waypoint(123, 37, PI/2, -10.0, 20.0, 0.0, intakeWobble2Time),
        };
        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)));
        drawPath(intakeWobble2Path, intakeWobble2Time);
        
        Waypoint[] intakeStackWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 10.0, 20.0, 0.0, 0.0),
                new Waypoint(112, 41, PI/2, 10.0, -40.0, 0.0, intakeStackTime),
        };
        Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)));
        drawPath(intakeStackPath, intakeStackTime);
        
        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 10.0, 30.0, 0.0, 0.0),
                new Waypoint(wobbleCor.getX(), wobbleCor.getY(), PI, 10.0, -30.0, 0.0, deliverWobble2Time),
        };
        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)));
        drawPath(deliverWobble2Path, deliverWobble2Time);
        
        Waypoint[] parkWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, -10.0, -30.0, 0.0, 0.0),
                new Waypoint(98, 80, PI/2, 10.0, 20.0, 0.0, parkTime),
        };
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)));
        drawPath(parkPath, parkTime);
        
    }

    public void drawPath(Path path, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            Pose curPose = path.getRobotPose(currentTime);
            double x = getXPixel(curPose.getX());
            double y = getYPixel(curPose.getY());

            Circle pathSegmentRed = new Circle(x, y, 1.5, Color.rgb(colorValue, 0, 0));
            Circle pathSegmentBlue = new Circle(getXPixel(144)-x, y, 1.5, Color.rgb(0, 0, colorValue));
            pathsGroup.getChildren().addAll(pathSegmentRed, pathSegmentBlue);
        }
        colorValue -= colorInterval;

        if (pathList != null) {
            pathList.add(path); timeList.add(time);
            Pose pose = path.getRobotPose(time);
            lX = pose.getX(); lY = pose.getY(); lTh = pose.getTheta();
        }
    }

    public void drawSpline(Spline[] splines, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            double x = getXPixel(splines[0].position(currentTime));
            double y = getYPixel(splines[1].position(currentTime));
            Circle pathSegmentRed = new Circle(x, y, 1.5, Color.rgb(colorValue, 0, 0));
            Circle pathSegmentBlue = new Circle(getXPixel(144)-x, y, 1.5, Color.rgb(0, 0, colorValue));
            pathsGroup.getChildren().addAll(pathSegmentRed, pathSegmentBlue);
        }
        colorValue -= colorInterval;
    }

    public void drawToPoint(double x1, double y1, double x2, double y2) {

        x1 = getXPixel(x1); x2 = getXPixel(x2);
        y1 = getYPixel(y1);  y2 = getYPixel(y2);
        Line toPointLineRed = new Line(x1, y1, x2, y2);
        toPointLineRed.setStroke(Color.rgb(colorValue, 0, 0));
        toPointLineRed.setStrokeWidth(1.5);
        Line toPointLineBlue = new Line(getXPixel(144)-x1, y1, getXPixel(144)-x2, y2);
        toPointLineBlue.setStroke(Color.rgb(0, 0, colorValue));
        toPointLineBlue.setStrokeWidth(1.5);
        pathsGroup.getChildren().addAll(toPointLineRed, toPointLineBlue);
        colorValue -= colorInterval;
    }

    public ArrayList<Path> getPathList() {
        return pathList;
    }
    public ArrayList<Double> getTimeList() {
        return timeList;
    }
}
