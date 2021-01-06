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

public class AutoPathsUtil extends BasePathsUtil {

    private Group pathsGroup;
    private SplineGenerator splineGenerator = new SplineGenerator();

    private ArrayList<Path> pathList;

    private int colorValue;
    private final double colorInterval;

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

        pathList = new ArrayList<>();

        // Path Time Variables
        double startLineTime = 1.75;
        double shootPowerShotsTime = 4.0;
        double deliverWobbleTime = 1.5;
        double intakeWobble2Time = 4.0;
        double intakeStackTime = 4.0;
        double shootHighGoalTime = 3.0;
        double deliverWobble2Time = 2.5;
        double parkTime = 2.0;

        RingCase ringCase = RingCase.One; // <------------------------------

        double[][] wobbleDelivery = {{121, 82}, {96, 100}, {126, 124}};
        double[][] wobble2Delivery = {{119, 71}, {96, 92}, {121, 118}};
        double[] wobbleCor;
        double[] wobble2Cor;
        if (ringCase == RingCase.Zero) {
            wobbleCor = wobbleDelivery[0];
            wobble2Cor = wobble2Delivery[0];
            intakeWobble2Time -= 1;
            deliverWobble2Time -= 0.5;
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
            wobble2Cor = wobble2Delivery[1];
        } else {
            wobbleCor = wobbleDelivery[2];
            wobble2Cor = wobble2Delivery[2];
            deliverWobbleTime += 1;
            deliverWobble2Time += 0.5;
        }

        // Path Variables
//        Waypoint[] startLineWaypoints = new Waypoint[] {
//                new Waypoint(90, 9, PI/2, 40, 50, 0, 0),
//                new Waypoint(90, 68, PI/2, 10, -30, 0, startLineTime),
//        };
//        Path startLinePath = new Path(new ArrayList<>(Arrays.asList(startLineWaypoints)));
//        drawPath(startLinePath);
//
//        Waypoint[] deliverWobbleWaypoints;
//        Spline deliverWobbleThetaSpline;
//        if (ringCase == RingCase.Zero) {
//            deliverWobbleWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, 20, 30, 0, 0),
//                    new Waypoint(wobbleCor[0], wobbleCor[1], 13*PI/12, 10, -20, 0, deliverWobbleTime),
//            };
//            deliverWobbleThetaSpline = new Spline(lTh, 0.4, 0, 13*PI/12, 0, 0, deliverWobbleTime);
//        } else {
//            deliverWobbleWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, 30, 30, 0, 0),
//                    new Waypoint(wobbleCor[0], wobbleCor[1], 13*PI/12, 20, -20, 0, deliverWobbleTime),
//            };
//            deliverWobbleThetaSpline = new Spline(lTh, 0.3, 0, 13*PI/12, 0, 0, deliverWobbleTime);
//        }
//        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)), deliverWobbleThetaSpline);
//        drawPath(deliverWobblePath);
//
//        Waypoint[] intakeWobble2Waypoints;
//        Spline intakeWobble2ThetaSpline;
//        if (ringCase == RingCase.Zero) {
//            intakeWobble2Waypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, -10, -50, 0, 0),
//                    new Waypoint(wobbleCor[0]-4, wobbleCor[1]-5, lTh, -10, -50, 0, 0.25),
//                    new Waypoint(127, 63, PI/2, -20, -5, 0, 1),
//                    new Waypoint(124.5, 37, 5*PI/12, 0, 30, 0, intakeWobble2Time),
//            };
//            intakeWobble2ThetaSpline = new Spline(lTh, 0.5, 0, 5*PI/12, 0, 0, intakeWobble2Time);
//        } else if (ringCase == RingCase.One) {
//            intakeWobble2Waypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, -10, -50, 0, 0),
//                    new Waypoint(wobbleCor[0]-4, wobbleCor[1]-5, lTh, -10, -50, 0, 0.25),
//                    new Waypoint(127, 66, PI/2, -30, -5, 0, 1.5),
//                    new Waypoint(124.5, 37, 5*PI/12, 0, 30, 0, intakeWobble2Time),
//            };
//            intakeWobble2ThetaSpline = new Spline(lTh, 0.5, 0, 5*PI/12, 0, 0, intakeWobble2Time);
//        } else {
//            intakeWobble2Waypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, -30, -50, 0, 0),
//                    new Waypoint(wobbleCor[0]-4, wobbleCor[1]-5, lTh, -10, -50, 0, 0.25),
//                    new Waypoint(127, 66, PI/2, -40, -5, 0, 2),
//                    new Waypoint(124.5, 37, 5*PI/12, 0, 30, 0, intakeWobble2Time),
//            };
//            intakeWobble2ThetaSpline = new Spline(lTh, 0.5, 0, 5*PI/12, 0, 0, intakeWobble2Time);
//        }
//        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)), intakeWobble2ThetaSpline);
//        drawPath(intakeWobble2Path);

        lX = 124.5; lY = 37; lTh = 5*PI/12;

        Waypoint[] intakeStackWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, -10, -30, 0, 0),
                new Waypoint(lX-4, lY-4, 2*PI/3, 20, 20, 0, 0.25),
                new Waypoint(118, 38, 2*PI/3, 10, 10, 0, 1.5),
                new Waypoint(106, 52, 2*PI/3, 10, -20, 0, intakeStackTime),
        };
        Spline intakeStackThetaSpline = new Spline(lTh, 4, 0, 2*PI/3, 0, 0, intakeStackTime);
        Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)), intakeStackThetaSpline);
        drawPath(intakeStackPath);

//        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, 10, 30, 0, 0),
//                    new Waypoint(wobble2Cor[0], wobble2Cor[1], 5*PI/4, 15, -20, 0, deliverWobble2Time),
//            };
//        Spline deliverWobble2ThetaSpline = new Spline(PI/2, 0.3, 0, 5*PI/4, 0, 0, deliverWobble2Time);
//        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)), deliverWobble2ThetaSpline);
//        drawPath(deliverWobble2Path);
//
//        Waypoint[] parkWaypoints;
//        if (ringCase == RingCase.Zero) {
//            parkWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh + PI, -10, -20, 0, 0),
//                    new Waypoint(112, 65, lTh + PI, -10, -20, 0, 1),
//                    new Waypoint(98, 80, -PI/2, 10, 20, 0, parkTime),
//            };
//        } else if (ringCase == RingCase.One) {
//            parkWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh + PI, -10, -20, 0, 0),
//                    new Waypoint(76, 80, -PI/2, 10, 20, 0, parkTime),
//            };
//        } else {
//            parkWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh + PI, -10, -20, 0, 0),
//                    new Waypoint(98, 80, -PI/2, 10, 30, 0, parkTime),
//            };
//        }
//        Spline parkThetaSpline = new Spline(PI/4, 0, 0, -PI/2, 0, 0, parkTime);
//        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), parkThetaSpline, true);
//        drawPath(parkPath);
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
            pathList.add(path);
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
}
