package main;

import main.PathingFiles.Interval;
import main.PathingFiles.Path;
import main.PathingFiles.Waypoint;
import main.Utilities.BasePaths;
import main.Utilities.Ring;
import main.Utilities.RingCase;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.PI;
import static main.Utilities.AutoPathsUtil.*;

public class Paths extends BasePaths {

    private final RingCase ringCase = RingCase.Zero; // <------------------------------

    public void drawPaths() {
        double goToStackTime = 0.75;
        double shootHighGoal1Time = 1.5;
        double intakeStackTime = 3.0;
        double shoot1RingTime = 0.75;
        double intakeStack2Time = 1.0;
        double shootPowerShotsTime = 3.0;
        double deliverWobbleTime = 1.75;
        double ringTime = 0;
        double stopForWgDeliverTime = 0.5;
        double intakeWobble2Time = 3.5;
        double goToHighShootTime = 1.25;
        double stopForWgPickupTime = 0.5;
        double shootHighGoal2Time = 1.5;
        double deliverWobble2Time = 2.0;
        double parkTime = 1.5;

        double[][] wobbleDelivery = {{118, 96}, {97, 118}, {126, 132}};
        double[][] wobble2Delivery = {{116, 79}, {92, 109}, {120, 132}};
        double[] wobbleCor;
        double[] wobble2Cor;

        if (ringCase == RingCase.Zero) {
            wobbleCor = wobbleDelivery[0];
            wobble2Cor = wobble2Delivery[0];
            intakeStack2Time = 1.75;
            parkTime = 2.5;
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
            wobble2Cor = wobble2Delivery[1];
        } else {
            wobbleCor = wobbleDelivery[2];
            wobble2Cor = wobble2Delivery[2];
            intakeStack2Time = 2.0;
        }

//        process = false;

        if (ringCase != RingCase.Zero) {
            Waypoint[] goToStackWaypoints = new Waypoint[] {
                    new Waypoint(114, 9, PI/2, 50, 50, 0, 0),
                    new Waypoint(109, 36, PI/2, 30, 30, 0, goToStackTime),
            };
            Path goToStackPath = new Path(new ArrayList<>(Arrays.asList(goToStackWaypoints)));
            drawPath(goToStackPath);

            waitAtCurPose(shootHighGoal1Time);

            if (ringCase == RingCase.Four) {
                Waypoint[] intakeStackWaypoints = new Waypoint[] {
                        new Waypoint(lX, lY, lTh, 0.5, 0, 0, 0),
                        new Waypoint(109, 37.5, PI/2, 0.5, 0, 0, intakeStackTime),
                };
                Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)));
                drawPath(intakeStackPath);

                waitAtCurPose(shoot1RingTime);
            }

            Waypoint[] intakeStack2Waypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                    new Waypoint(111, 63, PI/2, 20, 20, 0, intakeStack2Time),
            };
            Path intakeStack2Path = new Path(new ArrayList<>(Arrays.asList(intakeStack2Waypoints)));
            drawPath(intakeStack2Path);
        } else {
            Waypoint[] goToStackWaypoints = new Waypoint[] {
                    new Waypoint(114, 9, PI/2, 40, 50, 0, 0),
                    new Waypoint(111, 63, PI/2, 30, 20, 0, goToStackTime),
            };
            Path goToStackPath = new Path(new ArrayList<>(Arrays.asList(goToStackWaypoints)));
            drawPath(goToStackPath);
        }

        waitAtCurPose(shootPowerShotsTime);

        process = true;

        boolean sweep = bouncePath();

        if (!(ringCase == RingCase.Four && sweep)) {
            Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh + PI, 30, 30, 0, 0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], PI/2, -30, -30, 0, deliverWobbleTime),
            };
            Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)), true);
            drawPath(deliverWobblePath);
        }

        waitAtCurPose(stopForWgDeliverTime);

        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, -30, -50, 0, 0),
                new Waypoint(79, 23.5, PI/2, -0.1, 50, 0, 2.5),
                new Waypoint(88, 23.5, PI/2, -0.1, -0.1, 0, intakeWobble2Time),
        };
        Waypoint[] intakeWobble2ThWaypoints = new Waypoint[] {
                new Waypoint(lTh, 0, 0, 0, 0, 0, 0),
                new Waypoint(PI/2, 0, 0, 0, 0, 0, 2.5),
                new Waypoint(PI/2, 0, 0, 0, 0, 0, intakeWobble2Time),
        };
        Path intakeWobble2Path = new Path(new ArrayList<>(Arrays.asList(intakeWobble2Waypoints)), true);
        intakeWobble2Path.addInterval(new Interval(2.5, intakeWobble2Time,
                new Path(new ArrayList<>(Arrays.asList(intakeWobble2ThWaypoints)))));
        drawPath(intakeWobble2Path);

        waitAtCurPose(stopForWgPickupTime);

        Waypoint[] goToHighShootWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 40, 0, 0),
                new Waypoint(106, 61, PI/2, 30, 30, 0, goToHighShootTime),
        };
        Path goToHighShootPath = new Path(new ArrayList<>(Arrays.asList(goToHighShootWaypoints)));
        drawPath(goToHighShootPath);

        waitAtCurPose(shootHighGoal2Time);

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
                    new Waypoint(lX, lY, PI/2, -30, -30, 0, 0),
                    new Waypoint(lX, lY - 15, PI/2, -10, 0, 0, 0.75),
                    new Waypoint(69, 90, PI/3, 20, 20, 0, parkTime),
            };
        } else if (ringCase == RingCase.One) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, PI/2, -30, -30, 0, 0),
                    new Waypoint(90, 88, PI/2, -30, -30, 0, parkTime),
            };
        } else {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, PI/2, -50, -100, 0, 0),
                    new Waypoint(104, 88, PI/2, -30, -30, 0, parkTime),
            };
        }
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), ringCase != RingCase.Zero);
        drawPath(parkPath);
    }

    public boolean bouncePath() { // after reloading ring mode paths are not updated???
        boolean sweep = true;
        double ringTime;

        rings = Ring.getRingCoords(rings, lX, lY);

        int maxY = 130;
        for (Ring ring : rings) {
            sweep &= ring.getY() >= maxY;
        }

        Path ringPath;
        if (!sweep) {
            ArrayList<Waypoint> ringWaypoints = new ArrayList<>();
            ringWaypoints.add(new Waypoint(lX, lY, lTh, 50, 60, 0, 0));

            ArrayList<Waypoint> ringThWaypoints = new ArrayList<>();
            ringThWaypoints.add(new Waypoint(lTh, 0, 0, 0, 0, 0, 0));

            System.out.println(rings);

            double[] ringPos = new double[3];
            ringTime = 0;
            double ringIntakeTheta;
            if (rings.size() >= 1) {
                ringPos = rings.get(0).driveToRing(lX, lY);
                ringTime += 1.5;
                if (ringPos[1] > maxY) {
                    ringIntakeTheta = ringPos[0] - lX < 0 ? 3*PI/4 : PI/4;
                    ringPos[0] -= 2;
                } else if (Ring.isLowestX(rings, rings.get(0))) {
                    ringIntakeTheta = PI/2;
                } else {
                    ringIntakeTheta = ringPos[2];
                }

                System.out.println("1:" + ringIntakeTheta);
                ringWaypoints.add(new Waypoint(ringPos[0], Math.min(maxY, ringPos[1]), ringIntakeTheta, 30, 10, 0, ringTime));
                ringThWaypoints.add(new Waypoint(ringIntakeTheta, 0, 0, 0, 0, 0, ringTime));
            }

            if (rings.size() >= 2) {
                ringPos = rings.get(1).driveToRing(ringPos[0], ringPos[1]);
                ringTime += 1.5;
                if (ringPos[1] > maxY || Ring.isLowestX(rings, rings.get(1))) {
                    if (ringPos[1] > maxY && !Ring.isLowestX(rings, rings.get(1))) {
                        ringIntakeTheta = ringPos[0] - rings.get(0).getX() < 0 ? 3 * PI / 4 : PI / 4;
                        ringPos[0] -= 2;
                    } else {
                        ringIntakeTheta = PI/4;
                        ringWaypoints.add(new Waypoint(ringPos[0]-5, Math.min(maxY, ringPos[1])-5, ringIntakeTheta, 30, 10, 0, ringTime-0.6));
                    }
                } else {
                    ringIntakeTheta = ringPos[2];
                }
                System.out.println("2:" + ringIntakeTheta);
                ringWaypoints.add(new Waypoint(ringPos[0], Math.min(maxY, ringPos[1]), ringIntakeTheta, 30, 10, 0, ringTime));
                ringThWaypoints.add(new Waypoint(ringIntakeTheta, 0, 0, 0, 0, 0, ringTime));
            }

            if (rings.size() >= 3) {
                ringPos = rings.get(2).driveToRing(ringPos[0], ringPos[1]);
                ringTime += 1.5;
                if (ringPos[1] > maxY && rings.get(2).getY() > maxY) {
                    ringIntakeTheta = ringPos[0] - rings.get(1).getX() < 0 ? 3*PI/4 : PI/4;
                    ringPos[0] -= 2;
                } else {
                    ringIntakeTheta = ringPos[2];
                }
                System.out.println("3:"+ ringIntakeTheta);
                ringWaypoints.add(new Waypoint(ringPos[0], Math.min(maxY, ringPos[1]), ringIntakeTheta, 30, 10, 0, ringTime));
                ringThWaypoints.add(new Waypoint(ringIntakeTheta, 0, 0, 0, 0, 0, ringTime));
            }

            Path ringThPath = new Path(ringThWaypoints);
            ringPath = new Path(new ArrayList<>(ringWaypoints));
            ringPath.addInterval(new Interval(1.4, ringTime, ringThPath));

            System.out.println();
        } else {
            ringTime = 4.5;
            Waypoint[] ringWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 50, 50, 0, 0),
                    new Waypoint(65, 129, 0, 30, 30, 0, 2.0),
                    new Waypoint(109, 129, 0, 30, 20, 0, 3.75),
                    new Waypoint(126, 131, 0, 30, 5, 0, ringTime),
            };
            Waypoint[] ringThWaypoints = new Waypoint[] {
                    new Waypoint(lTh, 0, 0, 0, 0, 0, 0),
                    new Waypoint(PI/4, 0, 0, 0, 0, 0, 2.0),
                    new Waypoint(PI/4, 0, 0, 0, 0, 0, 3.75),
                    new Waypoint(PI/2, 0, 0, 0, 0, 0, ringTime),
            };
            Path ringThPath = new Path(new ArrayList<>(Arrays.asList(ringThWaypoints)));
            ringPath = new Path(new ArrayList<>(Arrays.asList(ringWaypoints)));
            ringPath.addInterval(new Interval(1.8, ringTime, ringThPath));
        }
        drawPath(ringPath);

        return sweep;
    }
}
