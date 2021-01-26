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

    public static double[][] ringPos = {};

    public void drawAutoPaths() {

        pathList = new ArrayList<>();

        // Path Time Variables

        RingCase ringCase = RingCase.Four; // <------------------------------

        double[][] wobbleDelivery = {{121, 82}, {96, 103}, {125, 130}};
        double[][] wobble2Delivery = {{119, 74}, {96, 92}, {124, 122}};
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

        double startLineTime = 1.5;
        Waypoint[] startLineWaypoints = new Waypoint[] {
                new Waypoint(90, 9, PI/2, 40, 50, 0, 0),
                new Waypoint(90, 58, PI/2, 10, -30, 0, startLineTime),
        };
        Path startLinePath = new Path(new ArrayList<>(Arrays.asList(startLineWaypoints)));
        drawPath(startLinePath);

        waitAtCurPose(2.5); // ps shoot

        double goToStackTime = 1.5;
        Waypoint[] goToStackWaypoints = new Waypoint[] {
                new Waypoint(85, lY, lTh, 30, 30, 0, 0),
                new Waypoint(96, 37, PI/2, -20, -20, 0, 1),
                new Waypoint(109, 37, PI/2, 20, 30, 0, goToStackTime),
        };
        Spline goToStackThetaSpline = new Spline(lTh, -3, 0, PI/2, 0, 0, goToStackTime);
        Path goToStackPath = new Path(new ArrayList<>(Arrays.asList(goToStackWaypoints)), goToStackThetaSpline);
        drawPath(goToStackPath);

        double intakeStackTime = 2;
        Waypoint[] sinxWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 30, 30, 0, 0),
                new Waypoint(109, 70, PI/2, 10, 20, 0, intakeStackTime),
        };
        Path sinxPath = new Path(new ArrayList<>(Arrays.asList(sinxWaypoints)));
        drawPath(sinxPath);

        waitAtCurPose(1.5); // hg shoot1

        lX = 109; lY = 70; lTh = PI/2;

        ringPos = new double[][] {
                {86, 130}, {94, 120}, {110, 130}
        };

        double bounceBackTime = 4;
        Waypoint[] bounceBackWaypoints = new Waypoint[]{
                new Waypoint(lX, lY, lTh, 50, 60, 0, 0),

                new Waypoint(ringPos[2][0], ringPos[2][1] - 10, PI / 2, 20, 30, 0, 1),
                new Waypoint(ringPos[2][0] - 2, ringPos[2][1] - 10, PI / 2, -30, -30, 0, 1.75),

                new Waypoint(ringPos[1][0], ringPos[1][1] - 10, PI / 2, 20, 30, 0, 2.5),
                new Waypoint(ringPos[1][0] - 2, ringPos[1][1] - 10, PI / 2, -30, -30, 0, 3.25),

                new Waypoint(ringPos[0][0], ringPos[0][1] - 2, PI / 2, 20, 30, 0, bounceBackTime),
        };
        Spline bounceBackThetaSpline = new Spline(PI/2, 0, 0, PI/2, 0, 0, bounceBackTime);
        Path bounceBackPath = new Path(new ArrayList<>(Arrays.asList(bounceBackWaypoints)), bounceBackThetaSpline);
        drawPath(bounceBackPath);

        double deliverWobbleTime = 1.5;
        Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -30, -30, 0, 0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], PI, -30, -30, 0, deliverWobbleTime),
        };
        Spline deliverWobbleThetaSpline = new Spline(lTh, 3, 0, PI, 0, 0, deliverWobbleTime);
        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)), deliverWobbleThetaSpline);
        drawPath(deliverWobblePath);

        waitAtCurPose(1.5); // wg1 delivery

        lX = wobbleCor[0]; lY = wobbleCor[1]; lTh = PI;

        double intakeWobble2Time = 4.5;
        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 30, 50, 0, 0),
                    new Waypoint(wobbleCor[0]-5, wobbleCor[1]-9, lTh, -20, -40, 0, 0.5),
                    new Waypoint(128, 66, PI/2, -30, -10, 0, 2),
                    new Waypoint(124, 36.5, 5*PI/12, 0, 60, 0, intakeWobble2Time),
        };
        Spline intakeWobble2ThetaSpline = new Spline(lTh, -6, 0, 5*PI/12, 0, 0, intakeWobble2Time);
        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)), intakeWobble2ThetaSpline);
        drawPath(intakeWobble2Path);

        waitAtCurPose(1.5); // wg2 pickup

        double goToHighShootTime = 0.5;
        Waypoint[] goToHighShootWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 30, 20, 0, 0),
                new Waypoint(124.5, 64, PI/2, 30, 10, 0, goToHighShootTime),
        };
        Path goToHighShootPath = new Path(new ArrayList<>(Arrays.asList(goToHighShootWaypoints)));
        drawPath(goToHighShootPath);

        waitAtCurPose(1.5); // hg shoot2

        double deliverWobble2Time = 2;
        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 50, 40, 0, 0),
                    new Waypoint(wobble2Cor[0], wobble2Cor[1], 5*PI/4, 30, -20, 0, deliverWobble2Time),
        };
        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)));
        drawPath(deliverWobble2Path);

        waitAtCurPose(1.5); // wg2 delivery

        double parkTime = 1.5;
        Waypoint[] parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 40, 40, 0, 0),
                    new Waypoint(98, 85, PI/2, 20, 20, 0, parkTime),
        };
        Spline parkThetaSpline = new Spline(5*PI/4, 0, 0, PI/2, 0, 0, parkTime);
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), parkThetaSpline);
        drawPath(parkPath);

        /* old red auto */
        /*
//        double startLineTime = 1.75;
//        double shootPowerShotsTime = 3.0; //4.0;
//        double deliverWobbleTime = 1.5;
//        double intakeWobble2Time = 4.0;
//        double intakeStackTime = 3.0;
//        double shootHighGoalTime = 3.0;
//        double deliverWobble2Time = 2.0;
//        double shootHighGoal2Time = 2.0;
//        double parkTime = 1.5;
//        //4 extra seconds for wobble related tasks
//
//        RingCase ringCase = RingCase.Four; // <------------------------------
//
//        double[][] wobbleDelivery = {{121, 82}, {96, 103}, {125, 130}};
//        double[][] wobble2Delivery = {{119, 74}, {96, 92}, {124, 122}};
//        double[] wobbleCor;
//        double[] wobble2Cor;
//        if (ringCase == RingCase.Zero) {
//            wobbleCor = wobbleDelivery[0];
//            wobble2Cor = wobble2Delivery[0];
//            intakeWobble2Time = 3.0;
//        } else if (ringCase == RingCase.One) {
//            wobbleCor = wobbleDelivery[1];
//            wobble2Cor = wobble2Delivery[1];
//            shootHighGoalTime = 2.0;
//        } else {
//            wobbleCor = wobbleDelivery[2];
//            wobble2Cor = wobble2Delivery[2];
//            intakeStackTime = 1.25;
//            deliverWobbleTime = 2.0;
//        }
//
//        Waypoint[] startLineWaypoints = new Waypoint[] {
//                new Waypoint(90, 9, PI/2, 40, 50, 0, 0),
//                new Waypoint(90, 68, PI/2, 10, -30, 0, startLineTime),
//        };
//        Path startLinePath = new Path(new ArrayList<>(Arrays.asList(startLineWaypoints)));
//        drawPath(startLinePath);
//
//        waitAtCurPose(shootPowerShotsTime);
//
//        Waypoint[] deliverWobbleWaypoints;
//        if (ringCase == RingCase.Zero) {
//            deliverWobbleWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, 20, 30, 0, 0),
//                    new Waypoint(wobbleCor[0], wobbleCor[1], 13*PI/12, 10, -20, 0, deliverWobbleTime),
//            };
//        } else {
//            deliverWobbleWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, 30, 30, 0, 0),
//                    new Waypoint(wobbleCor[0], wobbleCor[1], 13*PI/12, 30, -20, 0, deliverWobbleTime),
//            };
//        }
////        Spline deliverWobbleThetaSpline = new Spline(lTh, 0.3, 0, 13*PI/12, 0, 0, deliverWobbleTime);
//        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)));
//        drawPath(deliverWobblePath);
//
//        waitAtCurPose(1.5);
//
//        lX = wobbleCor[0]; lY = wobbleCor[1]; lTh = 13*PI/12;
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
//            intakeWobble2ThetaSpline = new Spline(lTh, 0.7, 0, 5*PI/12, 0, 0, intakeWobble2Time);
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
//                    new Waypoint(wobbleCor[0]-3, wobbleCor[1]-2, lTh, -10, -50, 0, 0.5),
//                    new Waypoint(128, 66, PI/2, -40, -5, 0, 2),
//                    new Waypoint(124.5, 37, 5*PI/12, 0, 30, 0, intakeWobble2Time),
//            };
//            intakeWobble2ThetaSpline = new Spline(lTh, -9, 0, 5*PI/12, 0, 0, intakeWobble2Time);
//        }
//        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)), intakeWobble2ThetaSpline);
//        drawPath(intakeWobble2Path);
//
//        waitAtCurPose(1.5);
//
//        lX = 124.5; lY = 37; lTh = 5*PI/12;
//
//        if (ringCase != RingCase.Zero) {
//            Waypoint[] intakeStackWaypoints;
//            Spline intakeStackThetaSpline;
//            if (ringCase == RingCase.One) {
//                intakeStackWaypoints = new Waypoint[] {
//                        new Waypoint(lX, lY, lTh, -10, -20, 0, 0),
//                        new Waypoint(107, 53, 7*PI/12, 30, 20, 0, intakeStackTime),
//                };
//                intakeStackThetaSpline = new Spline(lTh, 3, 0, 7*PI/12, 0, 0, intakeStackTime);
//            } else {
//                intakeStackWaypoints = new Waypoint[] {
//                        new Waypoint(lX, lY, lTh, -10, -20, 0, 0),
//                        new Waypoint(lX - 13, lY - 6, 7*PI/12, 10, 10, 0, 0.5),
//                        new Waypoint(108, 39, PI/2, 20, 10, 0, intakeStackTime),
//                };
//                intakeStackThetaSpline = new Spline(lTh, 1, 0, PI/2, 0, 0, intakeStackTime);
//            }
//            Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)), intakeStackThetaSpline);
//            drawPath(intakeStackPath);
//
//            if (ringCase == RingCase.Four) {
//                Waypoint[] sinx_xWaypoints = new Waypoint[] {
//                        new Waypoint(lX, lY, lTh, 30, 20, 0, 0),
//                        new Waypoint(110, 64, PI/2, 30, 10, 0, 2),
//                };
//                Path sinx_xPath = new Path(new ArrayList<>(Arrays.asList(sinx_xWaypoints)));
//                drawPath(sinx_xPath);
//            }
//
//            waitAtCurPose(shootHighGoalTime);
//        }
//
//        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, 15, 30, 0, 0),
//                    new Waypoint(wobble2Cor[0], wobble2Cor[1], 5*PI/4, 20, -20, 0, deliverWobble2Time),
//        };
//        Spline deliverWobble2ThetaSpline = new Spline(lTh, 0.3, 0, 5*PI/4, 0, 0, deliverWobble2Time);
//        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)), deliverWobble2ThetaSpline);
//        drawPath(deliverWobble2Path);
//
//        waitAtCurPose(1);
//
//        lX = wobble2Cor[0]; lY = wobble2Cor[1]; lTh = 5*PI/4;
//
//        if (ringCase == RingCase.Four) {
//            Waypoint[] shootHighGoal2Waypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, -10, -20, 0, 0),
//                    new Waypoint(lX - 10, lY - 10, lTh, 20, -20, 0, 0.4),
//                    new Waypoint(98, 60, PI/2, 20, 50, 0, shootHighGoal2Time),
//            };
//            Spline shootHighGoal2ThetaSpline = new Spline(lTh, -10, 0, PI/2, 0, 0, shootHighGoal2Time);
//            Path shootHighGoal2Path = new Path(new ArrayList<>(Arrays.asList(shootHighGoal2Waypoints)), shootHighGoal2ThetaSpline);
//            drawPath(shootHighGoal2Path);
//        }
//
//        Waypoint[] parkWaypoints;
//        if (ringCase == RingCase.Zero) {
//            parkWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh + PI, -10, -20, 0, 0),
//                    new Waypoint(112, 65, lTh + PI, -10, -20, 0, 1),
//                    new Waypoint(96, 80, -PI/2, 10, 20, 0, parkTime),
//            };
//        } else if (ringCase == RingCase.One) {
//            parkWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh + PI, -10, -20, 0, 0),
//                    new Waypoint(76, 80, -PI/2, 10, 20, 0, parkTime),
//            };
//        } else {
//            parkWaypoints = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, -10, -20, 0, 0),
//                    new Waypoint(98, 85, PI/2, 20, 50, 0, parkTime),
//            };
//        }
//        Spline parkThetaSpline;
//        Path parkPath;
//        if (ringCase != RingCase.Four) {
//            parkThetaSpline = new Spline(PI/4, 0, 0, -PI/2, 0, 0, parkTime);
//            parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), parkThetaSpline, true);
//        } else {
//            parkThetaSpline = new Spline(lTh, 0, 0, PI/2, 0, 0, parkTime);
//            parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), parkThetaSpline);
//        }
//        drawPath(parkPath);*/
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

    public void waitAtCurPose(double seconds) {
        if (enableWaits) {
            Waypoint[] waitWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 0.001, 0, 0, 0),
                    new Waypoint(lX, lY, lTh, 0.001, 0, 0, seconds),
            };
            Spline waitThetaSpline = new Spline(lTh, 0, 0, lTh, 0, 0, seconds);
            Path waitPath = new Path(new ArrayList<>(Arrays.asList(waitWaypoints)), waitThetaSpline);
            pathList.add(waitPath);
        }
    }

    public ArrayList<Path> getPathList() {
        return pathList;
    }
}
