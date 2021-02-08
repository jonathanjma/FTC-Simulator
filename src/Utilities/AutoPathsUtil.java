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

        RingCase ringCase = RingCase.Four; // <------------------------------

        double[][] wobbleDelivery = {{121, 82}, {96, 103}, {125, 133}};
        double[][] wobble2Delivery = {{119, 74}, {96, 92}, {123, 127}};
        double[] wobbleCor;
        double[] wobble2Cor;
        if (ringCase == RingCase.Zero) {
            wobbleCor = wobbleDelivery[0];
            wobble2Cor = wobble2Delivery[0];
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
            wobble2Cor = wobble2Delivery[1];
        } else {
            wobbleCor = wobbleDelivery[2];
            wobble2Cor = wobble2Delivery[2];
        }

        double goToStackTime = 0.75;
        Waypoint[] goToStackWaypoints = new Waypoint[] {
                new Waypoint(114, 9, PI/2, 40, 50, 0, 0),
                new Waypoint(108, 40, PI/2, 30, 30, 0, goToStackTime),
        };
        Path goToStackPath = new Path(new ArrayList<>(Arrays.asList(goToStackWaypoints)));
        drawPath(goToStackPath);

        waitAtCurPose(1.5); // hg shoot1

        double intakeStackTime = 2.0;
        Waypoint[] intakeStackWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                new Waypoint(108, 55, PI/2, 20, 20, 0, intakeStackTime),
        };
        Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)));
        drawPath(intakeStackPath);

        waitAtCurPose(1); // shoot 3rd ring so we can do ps

        double intakeStackTime2 = 0.75;
        Waypoint[] intakeStackWaypoints2 = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 2.5, 0, 0, 0),
                new Waypoint(108, 58, PI/2, 2.5, 0, 0, intakeStackTime2),
        };
        Path intakeStackPath2 = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints2)));
        drawPath(intakeStackPath2);

        double goToPowerShootTime = 1.0;
        Waypoint[] goToPowerShootWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 30, 20, 0, 0),
                new Waypoint(87, 63, PI/2, 30, 30, 0, goToPowerShootTime),
        };
        Path goToPowerShootPath = new Path(new ArrayList<>(Arrays.asList(goToPowerShootWaypoints)));
        drawPath(goToPowerShootPath);

        waitAtCurPose(1.5); // ps shoot

        process = true;

        double bounceBackTime = 6.75, ringTime = 0.75;
        ArrayList<Waypoint> ringWaypoints = new ArrayList<>(3);
        ringWaypoints.add(new Waypoint(lX, lY, lTh, 50, 60, 0, 0));
        if (rings.size() >= 1) {
            ringTime += 2.5;
            double[] ringPos = rings.get(0).driveToRing(90, 33);
            ringWaypoints.add(new Waypoint(ringPos[0] - 4, ringPos[1] - 4, PI/4, 30, 40, 0, 0.75));
            ringWaypoints.add(new Waypoint(ringPos[0] + 4, ringPos[1] + 4, PI/4, 30, 40, 0, 1.25));
            if (rings.size() >= 2) {
                double ringY = rings.get(1).driveToRing(90, 33)[1];
                ringWaypoints.add(new Waypoint(ringPos[0], ringY - 15, PI/2, -30, -10, 0, 2.00));
            } else {
                ringWaypoints.add(new Waypoint(ringPos[0] + 4, ringPos[1] - 10, PI/2, -30, -10, 0, 2.00));
            }
        }
        if (rings.size() >= 2) {
            ringTime += 2.5;
            double[] ringPos = rings.get(1).driveToRing(90, 33);
            ringWaypoints.add(new Waypoint(ringPos[0] - 4, ringPos[1] - 4, PI/4, 30, 40, 0, 2.75));
            ringWaypoints.add(new Waypoint(ringPos[0] + 4, ringPos[1] + 4, PI/4, 30, 40, 0, 3.25));
            if (rings.size() == 3) {
                double ringY = rings.get(2).driveToRing(90, 33)[1];
                ringWaypoints.add(new Waypoint(ringPos[0], ringY - 15, PI/2, -30, -10, 0, 4.00));
            } else {
                ringWaypoints.add(new Waypoint(ringPos[0] + 4, ringPos[1] - 10, PI/2, -30, -10, 0, 4.00));
            }
        }
        if (rings.size() == 3) {
            ringTime += 2.5;
            double[] ringPos = rings.get(2).driveToRing(90, 33);
            ringWaypoints.add(new Waypoint(ringPos[0] - 4, ringPos[1] - 4, PI/4, 30, 40, 0, 4.75));
            ringWaypoints.add(new Waypoint(ringPos[0] + 4, ringPos[1] + 4, PI/4, 30, 40, 0, 5.25));
            ringWaypoints.add(new Waypoint(ringPos[0], ringPos[1] - 10, PI/2, -30, -10, 0, 6.00));
        }
        ringWaypoints.add(new Waypoint(90, 60, PI/2, 30, 40, 0, ringTime));

        Waypoint[] ringThWaypoints = {
                new Waypoint(PI/2, 0, 0, 0, 0, 0, 0),

                new Waypoint(PI/4, 0, 0, 0, 0, 0, 0.75),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 1.25),
                new Waypoint(PI/2, 0, 0, 0, 0, 0, 2.00),

                new Waypoint(PI/4, 0, 0, 0, 0, 0, 2.75),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 3.25),
                new Waypoint(PI/2, 0, 0, 0, 0, 0, 4.00),

                new Waypoint(PI/4, 0, 0, 0, 0, 0, 4.75),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 5.25),
                new Waypoint(PI/2, 0, 0, 0, 0, 0, 6.00),

                new Waypoint(PI/4, 0, 0, 0, 0, 0, ringTime),
        };

        Path ringThPath = new Path(new ArrayList<>(Arrays.asList(ringThWaypoints)));
        Path ringPath = new Path(ringWaypoints, ringThPath);
        drawPath(ringPath);

        process = false;

        double deliverWobbleTime = 1.0;
        Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, -30, -30, 0, 0),
                new Waypoint(wobbleCor[0], wobbleCor[1], PI, -30, -30, 0, deliverWobbleTime),
        };
        Spline deliverWobbleThetaSpline = new Spline(lTh, 3, 0, PI, 0, 0, deliverWobbleTime);
        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)), deliverWobbleThetaSpline);
        drawPath(deliverWobblePath);

        waitAtCurPose(1.5); // wg1 delivery

        double intakeWobble2Time = 3.5;
        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 40, 0, 0),
                new Waypoint(100, 36.5, 5*PI/12, 0, 40, 0, intakeWobble2Time),
        };
        Waypoint[] intakeWobble2ThWaypoints = new Waypoint[] {
                new Waypoint(lTh, 0, 0, -3, 0, 0, 0),
                new Waypoint(5*PI/12, 0, 0, 0, 0, 0, 1.75),
                new Waypoint(5*PI/12, 0, 0, 0, 0, 0, intakeWobble2Time),
        };
        Path intakeWobble2Theta = new Path(new ArrayList<>(Arrays.asList(intakeWobble2ThWaypoints)));
        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)), intakeWobble2Theta);
        drawPath(intakeWobble2Path);

        waitAtCurPose(1.5); // wg2 pickup

        double goToHighShootTime = 0.75;
        Waypoint[] goToHighShootWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 50, 0, 0),
                new Waypoint(106, 68, PI/2, 30, 30, 0, goToHighShootTime),
        };
        Path goToHighShootPath = new Path(new ArrayList<>(Arrays.asList(goToHighShootWaypoints)));
        drawPath(goToHighShootPath);

        waitAtCurPose(1.5); // hg shoot2

        double deliverWobble2Time = 1.75;
        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 40, 0, 0),
                new Waypoint(wobble2Cor[0], wobble2Cor[1], PI, 30, 30, 0, deliverWobble2Time),
        };
        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)));
        drawPath(deliverWobble2Path);

        waitAtCurPose(1.5); // wg2 delivery

        double parkTime = 1.5;
        Waypoint[] parkWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 50, 0, 0),
                new Waypoint(98, 86, PI/2, 30, -30, 0, parkTime),
        };
        Spline parkThetaSpline = new Spline(lTh, 0, 0, PI/2, 0, 0, parkTime);
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), parkThetaSpline);
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
