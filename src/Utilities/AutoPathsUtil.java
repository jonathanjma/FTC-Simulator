package Utilities;

import PathingFiles.*;
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

        // Path Time Variables
        double startLineTime = 2.0;
        double shootPowerShotsTime = 4;
        double deliverWobbleTime = 2.0;
        double intakeWobble2Time = 3.25;
        double intakeStackTime = 4.0;
        double shootHighGoalTime = 4;
        double deliverWobble2Time = 3.5;
        double parkTime = 2;

        RingCase ringCase = RingCase.One;
        double[][] wobbleDelivery = {{123, 73}, {97, 96}, {123, 121}};
        double[][] wobble2Delivery = {{123, 69}, {97, 93}, {113, 126}};

        double[] wobbleCor;
        double[] wobble2Cor;
        if (ringCase == RingCase.Four) {
            wobbleCor = wobbleDelivery[2];
            wobble2Cor = wobble2Delivery[2];
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
            wobble2Cor = wobble2Delivery[1];
        } else {
            wobbleCor = wobbleDelivery[0];
            wobble2Cor = wobble2Delivery[0];
            intakeWobble2Time -= 0.5;
        }

        // Path Variables
        Waypoint[] startLineWaypoints = new Waypoint[] {
                new Waypoint(90, 9, PI/2, 20.0, 50.0, 0.0, 0.0),
                new Waypoint(90, 68, PI/2, 10.0, -10.0, 0.0, startLineTime),
        };
        Path startLinePath = new Path(new ArrayList<>(Arrays.asList(startLineWaypoints)));
        drawPath(startLinePath, startLineTime);

        Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40.0, 30.0, 0.0, 0.0),
                new Waypoint(97, 96, PI/4, 50.0, -30.0, 0.0, deliverWobbleTime),
        };
        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)));
        drawPath(deliverWobblePath, deliverWobbleTime);

        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, -10.0, -50.0, 0.0, 0.0),
                new Waypoint(125, 66, PI/2, -25.0, 10.0, 0.0, 2.0),
                new Waypoint(124, 38.5, 5*PI/12, -5.0, -10.0, 0.0, intakeWobble2Time),
        };
        Spline intakeWobble2ThetaSpline = new Spline(PI/4, 0.2, 0.0, 5*PI/12, 0.0, 0.0, intakeWobble2Time);
        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)), intakeWobble2ThetaSpline);
        drawPath(intakeWobble2Path, intakeWobble2Time);

        Waypoint[] intakeStackWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 10.0, 20.0, 0.0, 0.0),
                new Waypoint(111, 44, PI/2, 10.0, 0.0, 0.0, 1.5),
                new Waypoint(109, 61, PI/2, 5.0, -20.0, 0.0, intakeStackTime),
        };
        Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)));
        drawPath(intakeStackPath, intakeStackTime);

        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 10.0, 30.0, 0.0, 0.0),
                new Waypoint(97, 93, 5*PI/4, 10.0, -30.0, 0.0, deliverWobble2Time),
        };
        Spline deliverWobble2ThetaSpline = new Spline(PI/2, 0.2, 0.0, 5*PI/4, 0.0, 0.0, deliverWobble2Time);
        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)), deliverWobble2ThetaSpline);
        drawPath(deliverWobble2Path, deliverWobble2Time);

        Waypoint[] parkWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, PI/4, -10.0, -20.0, 0.0, 0.0),
                new Waypoint(76, 81, PI/2, 10.0, 20.0, 0.0, parkTime),
        };
        Spline parkThetaSpline = new Spline(5*PI/4, 0.0, 0.0, PI/2, 0.0, 0.0, parkTime);
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), parkThetaSpline);
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
