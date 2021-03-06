package main.Utilities;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import main.PathingFiles.Path;
import main.PathingFiles.Pose;
import main.PathingFiles.Spline;
import main.PathingFiles.Waypoint;
import main.Paths;

import java.util.ArrayList;
import java.util.Arrays;

import static main.Utilities.ConversionUtil.getXPixel;
import static main.Utilities.ConversionUtil.getYPixel;

public class AutoPathsUtil extends BasePathsUtil {

    private static Group pathsGroup;

    private static ArrayList<Path> pathList;

    private static int colorValue;
    private final int initColorValue;
    private static double colorInterval;

    public static double lX, lY, lTh;
    private static final boolean enableWaits = true;
    public static boolean process = false;

    public static ArrayList<Ring> rings;

    public AutoPathsUtil(Group pathsGroup) {
        this(pathsGroup, 255, 0);
    }

    public AutoPathsUtil(Group pathsGroup, int startingColorValue, double colorInterval) {
        this.pathsGroup = pathsGroup;
        colorValue = startingColorValue;
        initColorValue = colorValue;
        this.colorInterval = colorInterval;
    }

    public void drawAutoPaths() {
        drawAutoPaths(new Paths());
    }

    public void drawAutoPaths(BasePaths paths) {

        pathsGroup.getChildren().clear();
        colorValue = initColorValue;

        pathList = new ArrayList<>();

        if (rings == null) {
            rings = new ArrayList<>();
            rings.add(new Ring(70, 139));
            rings.add(new Ring(86, 115));
            rings.add(new Ring(102, 139));
        }

        process = true;

        paths.drawPaths();

        /*RingCase ringCase = RingCase.Four; // <------------------------------

        double goToStackTime = 0.75;
        double shootHighGoalTime = 1.5;
        double intakeStackTime = 1.5;
        double shoot1RingTime = 0.75;
        double intakeStackTime2 = 2.5;
        double goToPowerShootTime = 1.0;
        double shootPowerShotsTime = 3.0;
        double deliverWobbleTime = 1.0;
        double stopForWgDeliverTime = 0.5;
        double intakeWobble2Time = 3.5;
        double stopForWgPickupTime = 1.0;
        double goToHighShootTime = 1.0;
        double deliverWobble2Time = 2.0;
        double parkTime = 1.5;

        double[][] wobbleDelivery = {{120, 85}, {93, 106}, {120, 133}};
        double[][] wobble2Delivery = {{117, 75}, {92, 99}, {116, 127}};
        double[] wobbleCor;
        double[] wobble2Cor;
        if (ringCase == RingCase.Zero) {
            wobbleCor = wobbleDelivery[0];
            wobble2Cor = wobble2Delivery[0];
            goToPowerShootTime = 2.0;
            deliverWobbleTime = 2.0;
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
            wobble2Cor = wobble2Delivery[1];
            deliverWobbleTime = 1.5;
        } else {
            wobbleCor = wobbleDelivery[2];
            wobble2Cor = wobble2Delivery[2];
        }

        if (ringCase != RingCase.Zero) {
            Waypoint[] goToStackWaypoints = new Waypoint[] {
                    new Waypoint(114, 9, PI/2, 40, 50, 0, 0),
                    new Waypoint(109, 33, PI/2, 40, 30, 0, goToStackTime),
            };
            Path goToStackPath = new Path(new ArrayList<>(Arrays.asList(goToStackWaypoints)));
            drawPath(goToStackPath);

            waitAtCurPose(shootHighGoalTime);

            Waypoint[] intakeStackWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 2.5, 2.5, 0, 0),
                    new Waypoint(109, 43, PI/2, 10, 10, 0, intakeStackTime),
            };
            Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)));
            drawPath(intakeStackPath);
        }

        if (ringCase == RingCase.Four) {
            waitAtCurPose(shoot1RingTime);

            Waypoint[] intakeStackWaypoints2 = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                    new Waypoint(109, 60, PI/2, 20, 20, 0, intakeStackTime2),
            };
            Path intakeStackPath2 = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints2)));
            drawPath(intakeStackPath2);
        }

        Waypoint[] goToPowerShootWaypoints;
        if (ringCase != RingCase.Zero) {
            goToPowerShootWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 30, 20, 0, 0),
                    new Waypoint(87, 63, PI/2, 30, 30, 0, goToPowerShootTime),
            };
        } else {
            goToPowerShootWaypoints = new Waypoint[] {
                    new Waypoint(114, 9, PI/2, 30, 30, 0, 0),
                    new Waypoint(114, 29, PI/2, 30, 30, 0, 1),
                    new Waypoint(87, 63, PI/2, 30, 30, 0, goToPowerShootTime),
            };
        }
        Path goToPowerShootPath = new Path(new ArrayList<>(Arrays.asList(goToPowerShootWaypoints)));
        drawPath(goToPowerShootPath);

        waitAtCurPose(shootPowerShotsTime);

        process = true;

        double ringTime;
        rings = Ring.getRingCoords(rings, lX, lY);

        boolean sweep = true;
        for (Ring ring : rings) {
            sweep &= ring.getY() >= 132;
        }

        if (rings.size() == 0) {
            rings.add(new Ring(62, 130));
            rings.add(new Ring(109, 130));
        }

        Path ringPath;
        if (!sweep) {
            ArrayList<Waypoint> ringWaypoints = new ArrayList<>();
            ringWaypoints.add(new Waypoint(lX, lY, lTh, 50, 60, 0, 0));

            double[] ringPos;
            ringTime = 0;
            if (rings.size() >= 1) {
                ringPos = rings.get(0).driveToRing(lX, lY);
                if (ringPos[1] > 135) {
                    ringPos[2] = PI/2;
                }
                ringWaypoints.add(new Waypoint(ringPos[0], ringPos[1], ringPos[2], 20, 30, 0, ringTime + 1.5));
                ringTime += 1.5;

                if (rings.size() == 2) {
                    ringPos = rings.get(1).driveToRing(ringPos[0], ringPos[1]);
                    if (ringPos[1] > 135) {
                        ringPos[2] = PI/2;
                    }
                    ringWaypoints.add(new Waypoint(ringPos[0], ringPos[1], ringPos[2], 20, 30, 0, ringTime + 1.5));
                    ringTime += 1.5;
                }
            }
            ringWaypoints.add(new Waypoint(123, 134, PI, 30, 20, 0, ringTime + 1.5));
            ringTime += 1.5;
            ringPath = new Path(ringWaypoints);
        } else {
            ringTime = 4.5;
            Waypoint[] ringWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 50, 50, 0, 0),
                    new Waypoint(60, 132, 0, 30, 30, 0, 1.5),
                    new Waypoint(109, 132, 0, 30, 20, 0, 3.75),
                    new Waypoint(123, 134, 0, 30, 5, 0, ringTime),
            };
            Waypoint[] ringThWaypoints = new Waypoint[] {
                    new Waypoint(lTh, 0, 0, 0, 0, 0, 0),
                    new Waypoint(PI/4, 0, 0, 0, 0, 0, 1.5),
                    new Waypoint(PI/4, 0, 0, 0, 0, 0, 3.75),
                    new Waypoint(PI/2, 0, 0, 0, 0, 0, ringTime),
            };
            Path ringThPath = new Path(new ArrayList<>(Arrays.asList(ringThWaypoints)));
            ringPath = new Path(new ArrayList<>(Arrays.asList(ringWaypoints)), ringThPath, new Interval(1.5, ringTime));
        }
        drawPath(ringPath);

        if (!(ringCase == RingCase.Four && sweep)) {
            Waypoint[] deliverWobbleWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, -40, 50, 0, 0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], PI / 2, -30, -30, 0, deliverWobbleTime),
            };
            Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)), true);
            drawPath(deliverWobblePath);
        }

        waitAtCurPose(stopForWgDeliverTime);

//        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
//                new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
//                new Waypoint(lX - 12, lY, 3*PI/4, 5, -10, 0, 0.5),
//        };
//        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)));
//        drawPath(intakeWobble2Path);
        Waypoint[] intakeWobble2Waypoints2 = new Waypoint[] {
                new Waypoint(lX, lY, lTh, -5, -50, 0, 0),
                new Waypoint(83, 25, PI/2, -0.1, 0, 0, 2.5),
                new Waypoint(87, 25, PI/2, -0.1, 0, 0, intakeWobble2Time),
        };
        Waypoint[] intakeWobble2ThWaypoints2 = new Waypoint[] {
                new Waypoint(lTh, 0, 0, 0, 0, 0, 0),
                new Waypoint(lTh, 0, 0, 0, 0, 0, 2.5),
                new Waypoint(lTh, 0, 0, 0, 0, 0, intakeWobble2Time),
        };
        Path intakeWobble2ThPath2 = new Path(new ArrayList<>(Arrays.asList(intakeWobble2ThWaypoints2)));
        Path intakeWobble2Path2 = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints2)), intakeWobble2ThPath2,
                new Interval(2.5, intakeWobble2Time, true));
        drawPath(intakeWobble2Path2);

        waitAtCurPose(stopForWgPickupTime);

        Waypoint[] goToHighShootWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 40, 0, 0),
                new Waypoint(106, 63, PI/2, 30, 30, 0, goToHighShootTime),
        };
        Path goToHighShootPath = new Path(new ArrayList<>(Arrays.asList(goToHighShootWaypoints)));
        drawPath(goToHighShootPath);

        waitAtCurPose(shootHighGoalTime);

        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 40, 0, 0),
                new Waypoint(wobble2Cor[0], wobble2Cor[1], PI/2, 30, 30, 0, deliverWobble2Time),
        };
        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)));
        drawPath(deliverWobble2Path);

        waitAtCurPose(stopForWgDeliverTime);

        Waypoint[] parkWaypoints;
        if (ringCase == RingCase.Zero) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                    new Waypoint(89, 84, PI/2, 20, 10, 0, parkTime),
            };
        } else {
//            Waypoint[] parkWaypoints1 = new Waypoint[] {
//                    new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
//                    new Waypoint(lX - 10, lY, 3*PI/4, 5, -10, 0, 0.5),
//            };

            if (ringCase == RingCase.One) {
                parkWaypoints = new Waypoint[] {
                        new Waypoint(lX, lY, lTh, -30, -30, 0, 0),
                        new Waypoint(92, 84, PI/2, -30, -30, 0, parkTime),
                };
            } else {
                parkWaypoints = new Waypoint[] {
                        new Waypoint(lX, lY, lTh, -40, -40, 0, 0),
                        new Waypoint(104, 86, PI/2, -30, -40, 0, parkTime),
                };
            }
//            Path parkPath1 = new Path(new ArrayList<>(Arrays.asList(parkWaypoints1)));
//            drawPath(parkPath1);
        }
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), ringCase != RingCase.Zero);
        drawPath(parkPath);*/
    }

    public static void drawPath(Path path) {

        double time = path.totalTime();
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            Pose curPose = path.getRobotPose(currentTime);
            double x = getXPixel(curPose.getX());
            double y = getYPixel(curPose.getY());

            if (colorValue < 0) {
                colorValue = 255;
            }

            Circle pathSegmentRed = new Circle(x, y, 1.5, Color.rgb(colorValue, 0, 0));
            Circle pathSegmentBlue = new Circle(getXPixel(144)-x, y, 1.5, Color.rgb(0, 0, colorValue));
            if (process || pathList == null) {
                pathsGroup.getChildren().addAll(pathSegmentRed/*, pathSegmentBlue*/);
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

    public static void drawSpline(Spline[] splines, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            double x = getXPixel(splines[0].position(currentTime));
            double y = getYPixel(splines[1].position(currentTime));
            Circle pathSegmentRed = new Circle(x, y, 1.5, Color.rgb(colorValue, 0, 0));
            Circle pathSegmentBlue = new Circle(getXPixel(144)-x, y, 1.5, Color.rgb(0, 0, colorValue));
            pathsGroup.getChildren().addAll(pathSegmentRed, pathSegmentBlue);
        }
        colorValue -= colorInterval;
    }

    public static void drawToPoint(double x1, double y1, double x2, double y2) {

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

    public static void waitAtCurPose(double seconds) {
        if (enableWaits) {
            Waypoint[] waitWaypoints = new Waypoint[] {
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
