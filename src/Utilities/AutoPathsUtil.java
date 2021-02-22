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
    private final boolean enableWaits = true;
    private boolean process = false;

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

    public static ArrayList<Ring> rings;

    public void drawAutoPaths() {

        pathList = new ArrayList<>();
        rings = new ArrayList<>(3);
        rings.add(new Ring(86, 125));
        rings.add(new Ring(98, 120));
        rings.add(new Ring(110, 115));
//        process = true;

        RingCase ringCase = RingCase.One; // <------------------------------

        double goToStackTime = 0.75;
        double shootHighGoalTime = 1.5;
        double intakeStackTime = 1.25;
        double shoot1RingTime = 0.75;
        double intakeStackTime2 = 2.5;
        double goToPowerShootTime = 1.0;
        double shootPSTime = 3.0;
        double deliverWobbleTime = 1.0;
        double stopForWgDeliverTime = 0.5;
        double intakeWobble2Time = 3.5;
        double stopForWgPickupTime = 1.0;
        double goToHighShootTime = 0.75;
        double deliverWobble2Time = 2.0;
        double parkTime = 1.75;

        double[][] wobbleDelivery = {{121, 90}, {100, 109}, {122, 130}};
        double[][] wobble2Delivery = {{119, 80}, {96, 99}, {123, 127}};
        double[] wobbleCor;
        double[] wobble2Cor;
        if (ringCase == RingCase.Zero) {
            wobbleCor = wobbleDelivery[0];
            wobble2Cor = wobble2Delivery[0];
            goToPowerShootTime = 2.0;
            deliverWobbleTime = 2.0;
            parkTime = 1.0;
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
            wobble2Cor = wobble2Delivery[1];
            deliverWobbleTime = 1.5;
            parkTime = 1.0;
        } else {
            wobbleCor = wobbleDelivery[2];
            wobble2Cor = wobble2Delivery[2];
        }

        if (ringCase != RingCase.Zero) {
            Waypoint[] goToStackWaypoints = new Waypoint[]{
                    new Waypoint(114, 9, PI/2, 40, 50, 0, 0),
                    new Waypoint(109, 38, PI/2, 40, 30, 0, goToStackTime),
            };
            Path goToStackPath = new Path(new ArrayList<>(Arrays.asList(goToStackWaypoints)));
            drawPath(goToStackPath);

            waitAtCurPose(shootHighGoalTime);

            Waypoint[] intakeStackWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 2.5, 2.5, 0, 0),
                    new Waypoint(109, 43, PI/2, 10, 10, 0, intakeStackTime),
            };
            Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)));
            drawPath(intakeStackPath);
        }

        if (ringCase == RingCase.Four) {
            waitAtCurPose(shoot1RingTime);

            Waypoint[] intakeStackWaypoints2 = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                    new Waypoint(109, 60, PI/2, 20, 20, 0, intakeStackTime2),
            };
            Path intakeStackPath2 = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints2)));
            drawPath(intakeStackPath2);
        }

        Waypoint[] goToPowerShootWaypoints;
        if (ringCase != RingCase.Zero) {
            goToPowerShootWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 30, 20, 0, 0),
                    new Waypoint(87, 63, PI/2, 30, 30, 0, goToPowerShootTime),
            };
        } else {
            goToPowerShootWaypoints = new Waypoint[]{
                    new Waypoint(114, 9, PI/2, 30, 30, 0, 0),
                    new Waypoint(114, 29, PI/2, 30, 30, 0, 1),
                    new Waypoint(87, 63, PI/2, 30, 30, 0, goToPowerShootTime),
            };
        }
        Path goToPowerShootPath = new Path(new ArrayList<>(Arrays.asList(goToPowerShootWaypoints)));
        drawPath(goToPowerShootPath);

        waitAtCurPose(shootPSTime);

        process = true;

        double ringTime = 0;
        rings = Ring.getRingCoords(rings, lX, lY);

        Waypoint[] ringWaypoints = new Waypoint[]{
                new Waypoint(lX, lY, lTh, 50, 50, 0, 0),
                new Waypoint(65, 130, 0, 30, 30, 0, 1.5),
                new Waypoint(109, 130, 0, 30, 20, 0, 3.75),
                new Waypoint(123, 134, 0, 30, 5, 0, 4.5),
        };

        Waypoint[] ringThWaypoints = new Waypoint[]{
                new Waypoint(lTh, 0, 0, 0, 0, 0, 0),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 1.5),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 3.75),
                new Waypoint(PI/2, 0, 0, 0, 0, 0, 4.5),
        };

//        ArrayList<Waypoint> ringWaypoints = new ArrayList<>();
//        ringWaypoints.add(new Waypoint(lX, lY, lTh, 50, 60, 0, 0));
//
//        double[] ringPos = rings.get(0).driveToRing(lX, lY);
//        if (rings.size() >= 1) {
//            if (ringPos[1] > 135) ringPos[2] = PI/2;
//            ringWaypoints.add(new Waypoint(ringPos[0], ringPos[1], ringPos[2], 20, 30, 0, ringTime += 1.5));
//        }
//        if (rings.size() >= 2) {
//            ringPos = rings.get(1).driveToRing(ringPos[0], ringPos[1]);
//            if (ringPos[1] > 135) ringPos[2] = PI/2;
//            ringWaypoints.add(new Waypoint(ringPos[0], ringPos[1], ringPos[2], 20, 10, 0, ringTime += 1.0));
//        }
//        if (rings.size() == 3) {
//            ringPos = rings.get(2).driveToRing(ringPos[0], ringPos[1]);
//            if (ringPos[1] > 135) ringPos[2] = PI/2;
//            ringWaypoints.add(new Waypoint(ringPos[0], ringPos[1], ringPos[2], 20, 10, 0, ringTime += 1.0));
//        }
//
//        Path ringPath = new Path(ringWaypoints);
        Path ringThPath = new Path(new ArrayList<>(Arrays.asList(ringThWaypoints)));
        Path ringPath = new Path(new ArrayList<>(Arrays.asList(ringWaypoints)), ringThPath);
        drawPath(ringPath);

        Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh+PI, 40, 50, 0, 0),
                new Waypoint(wobbleCor[0], wobbleCor[1], PI, -30, -30, 0, deliverWobbleTime),
        };
        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)), true);
        drawPath(deliverWobblePath);

        process = false;

        waitAtCurPose(stopForWgDeliverTime);

        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                new Waypoint(lX - 12, lY, 3*PI/4, 5, -10, 0, 0.5),
        };
        Waypoint[] intakeWobble2Waypoints2 = new Waypoint[] {
                new Waypoint(lX - 12, lY, 3*PI/4, -5, -50, 0, 0),
                new Waypoint(103, 44, 5*PI/12, -20, 40, 0, 2.75),
                new Waypoint(99, 34, 5*PI/12, -0.1, 70, 0, intakeWobble2Time),
        };
        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)));
        drawPath(intakeWobble2Path);
        Path intakeWobble2Path2 = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints2)), true);
        drawPath(intakeWobble2Path2);

        waitAtCurPose(stopForWgPickupTime);

        Waypoint[] goToHighShootWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 50, 0, 0),
                new Waypoint(106, 68, PI/2, 30, 30, 0, goToHighShootTime),
        };
        Path goToHighShootPath = new Path(new ArrayList<>(Arrays.asList(goToHighShootWaypoints)));
        drawPath(goToHighShootPath);

        waitAtCurPose(shootHighGoalTime);

        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 40, 0, 0),
                new Waypoint(wobble2Cor[0], wobble2Cor[1], PI, 30, 30, 0, deliverWobble2Time),
        };
        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)));
        drawPath(deliverWobble2Path);

        waitAtCurPose(stopForWgDeliverTime);

        Waypoint[] parkWaypoints;
        if (ringCase == RingCase.Zero) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                    new Waypoint(98, 84, PI/2, 20, 10, 0, parkTime),
            };
        } else {
            Waypoint[] parkWaypoints1 = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                    new Waypoint(lX - 10, lY, PI/2, 5, -10, 0, 0.5),
            };
            Path parkPath1 = new Path(new ArrayList<>(Arrays.asList(parkWaypoints1)));
            drawPath(parkPath1);

            if (ringCase == RingCase.One) {
                parkWaypoints = new Waypoint[]{
                        new Waypoint(lX, lY, lTh, -30, -30, 0, 0),
                        new Waypoint(86, 84, PI/2, -30, -30, 0, parkTime),
                };
            } else {
                parkWaypoints = new Waypoint[]{
                        new Waypoint(lX, lY, lTh, -40, -50, 0, 0),
                        new Waypoint(98, 86, PI/2, -30, -30, 0, parkTime),
                };
            }
        }
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), ringCase != RingCase.Zero);
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
            if (process) {
                pathsGroup.getChildren().addAll(pathSegmentRed, pathSegmentBlue);
            }
        }
        colorValue -= colorInterval;

        if (pathList != null) {
            if (process) {
                pathList.add(path);
            }
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

    public void waitAtCurPose(double seconds) {
        if (enableWaits) {
            Waypoint[] waitWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 0.001, 0, 0, 0),
                    new Waypoint(lX, lY, lTh, 0.001, 0, 0, seconds),
            };
            Spline waitThetaSpline = new Spline(lTh, 0, 0, lTh, 0, 0, seconds);
            Path waitPath = new Path(new ArrayList<>(Arrays.asList(waitWaypoints)), waitThetaSpline);
            if (process) {
                pathList.add(waitPath);
            }
        }
    }

    public ArrayList<Path> getPathList() {
        return pathList;
    }
}
