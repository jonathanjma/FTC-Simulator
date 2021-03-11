package main;

import main.PathingFiles.Interval;
import main.PathingFiles.Path;
import main.PathingFiles.Spline;
import main.PathingFiles.Waypoint;
import main.Utilities.BasePaths;
import main.Utilities.Ring;
import main.Utilities.RingCase;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.PI;
import static main.Utilities.AutoPathsUtil.*;

public class Paths extends BasePaths {

    public void drawPaths() {

        RingCase ringCase = RingCase.Four; // <------------------------------

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

        double[][] wobbleDelivery = {{120, 92}, {97, 114}, {123, 134}};
        double[][] wobble2Delivery = {{117, 83}, {94, 106}, {116, 128}};
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

        process = false;

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

        boolean sweep = bouncePath();

        if (!(ringCase == RingCase.Four && sweep)) {
            Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -40, 50, 0, 0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], PI/2, -30, -30, 0, deliverWobbleTime),
            };
            Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)), true);
            drawPath(deliverWobblePath);
        }

        waitAtCurPose(stopForWgDeliverTime);

        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 0.1, 0.1, 0, 0),
                new Waypoint(lX - 4, lY, PI/2, 0.1, 0.1, 0, 0.5),
        };
        Waypoint[] intakeWobble2Waypoints2 = new Waypoint[] {
                new Waypoint(lX - 4, lY, PI/2, -5, -50, 0, 0),
                new Waypoint(83, 27, PI/2, -0.1, 0, 0, 2.5),
                new Waypoint(87, 27, PI/2, -0.1, 0, 0, intakeWobble2Time),
        };
        Waypoint[] intakeWobble2ThWaypoints2 = new Waypoint[] {
                new Waypoint(lTh, 0, 0, 0, 0, 0, 0),
                new Waypoint(lTh, 0, 0, 0, 0, 0, 2.5),
                new Waypoint(lTh, 0, 0, 0, 0, 0, intakeWobble2Time),
        };

        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)),
                new Spline(lTh, 0, 0, PI/2, 0, 0, 0.5));
        Path intakeWobble2ThPath2 = new Path(new ArrayList<>(Arrays.asList(intakeWobble2ThWaypoints2)));
        Path intakeWobble2Path2 = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints2)), intakeWobble2ThPath2,
                new Interval(2.5, intakeWobble2Time, true));
        drawPath(intakeWobble2Path);
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

        Waypoint[] parkWaypoints1 = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 0.1, 0.1, 0, 0),
                new Waypoint(lX - 4, lY, PI/2, 0.1, 0.1, 0, 0.5),
        };

        Waypoint[] parkWaypoints2;
        if (ringCase == RingCase.Zero) {
            parkWaypoints2 = new Waypoint[] {
                    new Waypoint(lX - 4, lY, PI/2, 20, 20, 0, 0),
                    new Waypoint(89, 84, PI/2, 20, 10, 0, parkTime),
            };
        } else if (ringCase == RingCase.One) {
            parkWaypoints2 = new Waypoint[] {
                    new Waypoint(lX - 4, lY, PI/2, -30, -30, 0, 0),
                    new Waypoint(90, 84, PI/2, -30, -30, 0, parkTime),
            };
        } else {
            parkWaypoints2 = new Waypoint[] {
                    new Waypoint(lX - 4, lY, PI/2, -40, -40, 0, 0),
                    new Waypoint(104, 86, PI/2, -30, -40, 0, parkTime),
            };
        }
        Path parkPath1 = new Path(new ArrayList<>(Arrays.asList(parkWaypoints1)),
                new Spline(lTh, 0, 0, PI/2, 0, 0, 0.5));
        Path parkPath2 = new Path(new ArrayList<>(Arrays.asList(parkWaypoints2)), ringCase != RingCase.Zero);
        drawPath(parkPath1);
        drawPath(parkPath2);
    }

    public boolean bouncePath() {
        double ringTime;
        rings = Ring.getRingCoords(rings, lX, lY);

        boolean sweep = true;
        for (Ring ring : rings) {
            sweep &= ring.getY() >= 132;
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

                if (rings.size() >= 2) {
                    ringPos = rings.get(1).driveToRing(ringPos[0], ringPos[1]);
                    if (ringPos[1] > 135) {
                        ringPos[2] = PI/2;
                    }
                    ringWaypoints.add(new Waypoint(ringPos[0], ringPos[1], ringPos[2], 20, 30, 0, ringTime + 1.5));
                    ringTime += 1.5;

                    if (rings.size() >= 3) {
                        ringPos = rings.get(2).driveToRing(ringPos[0], ringPos[1]);
                        if (ringPos[1] > 135) {
                            ringPos[2] = PI/2;
                        }
                        ringWaypoints.add(new Waypoint(ringPos[0], ringPos[1], ringPos[2], 20, 30, 0, ringTime + 1.5));
                        ringTime += 1.5;
                    }
                }
            }
            ringWaypoints.add(new Waypoint(120, 133, PI/2, 20, 20, 0, ringTime + 1.5));
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
            ringPath = new Path(new ArrayList<>(Arrays.asList(ringWaypoints)), ringThPath, new Interval(1.3, ringTime));
        }
        drawPath(ringPath);

        return sweep;
    }
}
