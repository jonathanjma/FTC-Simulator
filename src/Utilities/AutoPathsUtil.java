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

    private enum RingCase {One, Four, Zero}
    private double lX, lY, lTh;

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

    public void drawAutoPaths() {

        pathList = new ArrayList<>(); timeList = new ArrayList<>();

        // Path Time Variables
        double startLineTime = 1.75;
        double shootPowerShotsTime = 4.0;
        double deliverWobbleTime = 2;
        double intakeWobble2Time = 4;
        double intakeStackTime = 4.5;
        double shootHighGoalTime = 4.0;
        double deliverWobble2Time = 3.5;
        double parkTime = 2.0;

        RingCase ringCase = RingCase.Zero; // <------------------------------

        double[][] wobbleDelivery = {{123, 76}, {96, 100}, {126, 124}};
        double[][] wobble2Delivery = {{119, 71}, {96, 92}, {121, 118}};
        double[] wobbleCor;
        double[] wobble2Cor;
        if (ringCase == RingCase.Four) {
            wobbleCor = wobbleDelivery[2];
            wobble2Cor = wobble2Delivery[2];
            deliverWobbleTime += 1;
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
            wobble2Cor = wobble2Delivery[1];
        } else {
            wobbleCor = wobbleDelivery[0];
            wobble2Cor = wobble2Delivery[0];
            deliverWobbleTime += 1;
        }

        // Path Variables
        Waypoint[] startLineWaypoints = new Waypoint[] {
                new Waypoint(90, 9, PI/2, 40.0, 50.0, 0.0, 0.0),
                new Waypoint(90, 68, PI/2, 10.0, -30.0, 0.0, startLineTime),
        };
        Path startLinePath = new Path(new ArrayList<>(Arrays.asList(startLineWaypoints)));
        drawPath(startLinePath);

        Waypoint[] deliverWobbleWaypoints;
        if (ringCase == RingCase.Zero) {
            deliverWobbleWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 40.0, 50.0, 0.0, 0.0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], PI/6, 50.0, -20.0, 0.0, deliverWobbleTime),
            };
        } else if (ringCase == RingCase.One) {
            deliverWobbleWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 40.0, 50.0, 0.0, 0.0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], PI/4, 50.0, -20.0, 0.0, deliverWobbleTime),
            };
        } else {
            deliverWobbleWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 40.0, 50.0, 0.0, 0.0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], PI/4, 60.0, -10.0, 0.0, deliverWobbleTime),
            };
        }
        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)));
        drawPath(deliverWobblePath);

        Waypoint[] intakeWobble2Waypoints;
        Spline intakeWobble2ThetaSpline;
        if (ringCase == RingCase.Zero) {
            intakeWobble2Waypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -10.0, -50.0, 0.0, 0.0),
                    new Waypoint(125, 66, PI/2, -20.0, 10.0, 0.0, 2.0),
                    new Waypoint(125, 37, 5*PI/12, 0.0, -50.0, 0.0, intakeWobble2Time),
            };
            intakeWobble2ThetaSpline = new Spline(5*PI/12, 0.4, 0.0, 5*PI/12, 0.0, 0.0, intakeWobble2Time);
        } else if (ringCase == RingCase.One) {
            intakeWobble2Waypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -10.0, -50.0, 0.0, 0.0),
                    new Waypoint(127, 66, PI/2, -15.0, 10.0, 0.0, 2.0),
                    new Waypoint(124.5, 37, 5*PI/12, 0.0, -30.0, 0.0, intakeWobble2Time),
            };
            intakeWobble2ThetaSpline = new Spline(5*PI/12, 0.2, 0.0, 5*PI/12, 0.0, 0.0, intakeWobble2Time);
        } else {
            intakeWobble2Waypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -20.0, -50.0, 0.0, 0.0),
                    new Waypoint(127, 66, PI/2, -20.0, -20.0, 0.0, 1.75),
                    new Waypoint(124.5, 37, 5*PI/12, 2.0, -20.0, 0.0, intakeWobble2Time),
            };
            intakeWobble2ThetaSpline = new Spline(PI/3, 0.2, 0.0, 5*PI/12, 0.0, 0.0, intakeWobble2Time);
        }
        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)), intakeWobble2ThetaSpline);
        drawPath(intakeWobble2Path);

        Waypoint[] intakeStackWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 10.0, 20.0, 0.0, 0.0),
                new Waypoint(111, 44, 3*PI/4, 10.0, 0.0, 0.0, 1.5),
                new Waypoint(109, 61, PI/2, 5.0, -20.0, 0.0, intakeStackTime),
        };
        Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)));
        drawPath(intakeStackPath);

        Waypoint[] deliverWobble2Waypoints;
        Spline deliverWobble2ThetaSpline;
        if (ringCase == RingCase.Four) {
            deliverWobble2Waypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 10.0, 30.0, 0.0, 0.0),
                    new Waypoint(wobble2Cor[0], wobble2Cor[1], 3*PI/2, 10.0, 20.0, 0.0, deliverWobble2Time),
            };
            deliverWobble2ThetaSpline = new Spline(PI/2, 0.2, 0.0, 5*PI/4, 0.0, 0.0, deliverWobble2Time);

        } else {
            deliverWobble2Waypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 10.0, 30.0, 0.0, 0.0),
                    new Waypoint(wobble2Cor[0], wobble2Cor[1], 5*PI/4, 10.0, -30.0, 0.0, deliverWobble2Time),
            };
            deliverWobble2ThetaSpline = new Spline(PI/2, 0.2, 0.0, 5*PI/4, 0.0, 0.0, deliverWobble2Time);
        }
        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)), deliverWobble2ThetaSpline);
        drawPath(deliverWobble2Path);

        Waypoint[] parkWaypoints;
        Spline parkThetaSpline;
        if (ringCase == RingCase.Zero) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh + PI, -10.0, -20.0, 0.0, 0.0),
                    new Waypoint(121, 59, PI/3, 10.0, 20.0, 0.0, 1.0),
                    new Waypoint(98, 80, 3*PI/2, 10.0, 20.0, 0.0, parkTime),
            };
            parkThetaSpline = new Spline(PI/4, 0.0, 0.0, 3*PI/2, 0.0, 0.0, parkTime);
        } else if (ringCase == RingCase.One) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh + PI, -10.0, -20.0, 0.0, 0.0),
                    new Waypoint(99, 84, PI/3, 10.0, 20.0, 0.0, 1.0),
                    new Waypoint(76, 81, 3*PI/2, 10.0, 20.0, 0.0, parkTime),
            };
            parkThetaSpline = new Spline(PI/4, 0.0, 0.0, 3*PI/2, 0.0, 0.0, parkTime);
        } else {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh + PI, -10.0, -20.0, 0.0, 0.0),
                    new Waypoint(98, 80, 3*PI/2, 10.0, 30.0, 0.0, parkTime),
            };
            parkThetaSpline = new Spline(PI/4, 0.0, 0.0, 3*PI/2, 0.0, 0.0, parkTime);
        }
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), parkThetaSpline, true);
        drawPath(parkPath);
    }

    public void drawPath(Path path) {

        double time = path.totalTime();
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
