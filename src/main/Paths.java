package main;

import main.PathingFiles.Interval;
import main.PathingFiles.Path;
import main.PathingFiles.Waypoint;
import main.Utilities.BasePaths;
import main.Utilities.RingCase;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.PI;
import static main.Utilities.AutoPathsUtil.*;

public class Paths extends BasePaths {

    private final RingCase ringCase = RingCase.Zero; // <------------------------------

    double[][] wobbleDelivery = {{115, 85, PI/2}, {90, 100, PI/2}, {125, 130, 2*PI/3}};
    double[][] wobble2Delivery = {{123, 65, 3*PI/4}, {104, 85, 5*PI/6}, {121, 118, 2*PI/3}};

    public void drawPaths() {
        drawPathsFull();
//        drawPathsPowerShots();
//        drawPathsStarterStack();
    }

    public void drawPathsPowerShots() {
        double goToPowerShotsTime = 1.25;
        double shootPowerShotsTime = 3.0;
        double deliverWobbleTime = 1.75;
        double stopForWgDeliverTime = 0.5;
        double goToBounceBackTime = 1.5;
        double bounceBackTime = 3.5;
        double goToShootTime = 1.5;
        double shootBounceBackTime = 1.5;
        double parkTime = 1.25;

        double[] wobbleCor;
        if (ringCase == RingCase.Zero) {
            wobbleCor = wobbleDelivery[0];
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobbleDelivery[1];
        } else {
            wobbleCor = wobbleDelivery[2];
        }

        Waypoint[] goToPowerShotsWaypoints = new Waypoint[] {
                new Waypoint(90, 9, PI/2, 30, 30, 0, 0),
                new Waypoint(87, 63, PI/2, 5, -30, 0, goToPowerShotsTime),
        };
        Path goToPowerShotsPath = new Path(new ArrayList<>(Arrays.asList(goToPowerShotsWaypoints)));
        drawPath(goToPowerShotsPath);

        waitAtCurPose(shootPowerShotsTime);

        Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 30, 0, 0),
                new Waypoint(wobbleCor[0], wobbleCor[1], wobbleCor[2], 5, -30, 0, deliverWobbleTime),
        };
        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)));
        drawPath(deliverWobblePath);

        waitAtCurPose(stopForWgDeliverTime);

        if (ringCase != RingCase.Four) {
            Waypoint[] goToBounceBackWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh,  10, 5, 0, 0),
                    new Waypoint(85, 129, PI/4, 5, 5, 0, goToBounceBackTime),
            };
            Path goToBounceBackPath = new Path(new ArrayList<>(Arrays.asList(goToBounceBackWaypoints)));
            drawPath(goToBounceBackPath);
        }

        Waypoint[] bounceBackWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, PI, 15, 5, 0, 0),
                new Waypoint(109, 129, PI, 10, 0, 0, 3.0),
                new Waypoint(114, 129, PI, 1, -10, 0, bounceBackTime),
        };

        Waypoint[] bounceBackThWaypoints = new Waypoint[] {
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 0),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 3.0),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, bounceBackTime),
        };
        Path bounceBackThPath = new Path(new ArrayList<>(Arrays.asList(bounceBackThWaypoints)));
        Path bounceBackPath = new Path(new ArrayList<>(Arrays.asList(bounceBackWaypoints)), bounceBackThPath);
        drawPath(bounceBackPath);

        Waypoint[] goToBounceShootWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, PI, 10, 5, 0, 0),
                new Waypoint(85, 127, PI, 5, 5, 0, 2.0),
                new Waypoint(85, 63, PI, 5, 5, 0, 4),
        };

        Waypoint[] goToShootThWaypoints = new Waypoint[] {
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 0),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 2.0),
                new Waypoint(PI/2, 0, 0, 0, 0, 0, 4),
        };
        Path goToShootThPath = new Path(new ArrayList<>(Arrays.asList(goToShootThWaypoints)));
        Path goToShootPath = new Path(new ArrayList<>(Arrays.asList(goToBounceShootWaypoints)), goToShootThPath);
        drawPath(goToShootPath);

        waitAtCurPose(shootBounceBackTime);

        Waypoint[] parkWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 30, 0, 0),
                new Waypoint(85, 85, PI/2, 5, -30, 0, parkTime),
        };
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)));
        drawPath(parkPath);
    }

    public void drawPathsStarterStack() {
        double goToStackTime = 1.0;
        double shootHighGoal1Time = 1.5;
        double intakeStackTime = 2.0;
        double shoot1RingTime = 0.75;
        double intakeStack2Time = 2.25;
        double shootHighGoal2Time = 1.5;
        double deliverWobbleTime = 1.75;
        double stopForWgDeliverTime = 0.5;
        double parkTime = 1.5;

        double[] wobbleCor;
        if (ringCase == RingCase.Zero) {
            wobbleCor = wobble2Delivery[0];
        } else if (ringCase == RingCase.One) {
            wobbleCor = wobble2Delivery[1];
        } else {
            wobbleCor = wobble2Delivery[2];
        }

        Waypoint[] goToStackWaypoints = new Waypoint[] {
                new Waypoint(114, 9, PI/2, 30, 30, 0, 0),
                new Waypoint(110, 32, PI/2, 5, -30, 0, goToStackTime),
        };
        Path goToStackPath = new Path(new ArrayList<>(Arrays.asList(goToStackWaypoints)));
        drawPath(goToStackPath);

        waitAtCurPose(shootHighGoal1Time);

        if (ringCase != RingCase.Zero) {

            Waypoint[] intakeStackWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 0.5, 0, 0, 0),
                    new Waypoint(110, 39, PI/2, 0.5, 0, 0, intakeStackTime),
            };
            Path intakeStackPath = new Path(new ArrayList<>(Arrays.asList(intakeStackWaypoints)));
            drawPath(intakeStackPath);

            waitAtCurPose(shoot1RingTime);

            if (ringCase == RingCase.Four) {
                Waypoint[] intakeStack2Waypoints = new Waypoint[] {
                        new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                        new Waypoint(110, 63, PI/2, 20, 20, 0, intakeStack2Time),
                };
                Path intakeStack2Path = new Path(new ArrayList<>(Arrays.asList(intakeStack2Waypoints)));
                drawPath(intakeStack2Path);

                waitAtCurPose(shootHighGoal2Time);
            }
        }

        Waypoint[] deliverWobbleWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 40, 30, 0, 0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], wobbleCor[2], 5, -30, 0, deliverWobbleTime),
        };
        Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)));
        drawPath(deliverWobblePath);

        waitAtCurPose(stopForWgDeliverTime);

        Waypoint[] parkWaypoints;
        if (ringCase == RingCase.Zero) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, 20, 20, 0, 0),
                    new Waypoint(lX - 15, lY + 4, PI, 5, 5, 0, 0.75),
                    new Waypoint(110, 85, PI/2, 20, 10, 0, parkTime),
            };
        } else if (ringCase == RingCase.One) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -10, -5, 0, 0),
                    new Waypoint(110, 80, PI/2, -10, -5, 0, parkTime),
            };
        } else {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -50, -40, 0, 0),
                    new Waypoint(113, 85, PI/2, -30, -10, 0, parkTime),
            };
        }
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), ringCase != RingCase.Zero);
        drawPath(parkPath);
    }

    public void drawPathsFull() {
        double goToStackTime = 0.75;
        double shootHighGoal1Time = 1.5;
        double intakeStackTime = 2.5;
        double shoot1RingTime = 0.75;
        double intakeStack2Time = 2.25;
        double shootPowerShotsTime = 3.0;
        double deliverWobbleTime = 1.75;
        double ringTime = 5.0;
        double stopForWgDeliverTime = 0.5;
        double intakeWobble2Time = 3.25;
        double goToHighShootTime = 0.75;
        double stopForWgPickupTime = 0.5;
        double shootHighGoal2Time = 1.5;
        double deliverWobble2Time = 2.25;
        double parkTime = 1.5;

        double[][] wobbleDelivery = {{121, 93}, {97, 116}, {124.5, 128}};
        double[][] wobble2Delivery = {{116, 79}, {92, 108}, {115.5, 127.5}};
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

       // process = false;

        if (ringCase != RingCase.Zero) {
            Waypoint[] goToStackWaypoints = new Waypoint[] {
                    new Waypoint(114, 9, PI/2, 30, 30, 0, 0),
                    new Waypoint(109, 32, PI/2, 5, -30, 0, goToStackTime),
            };
            Path goToStackPath = new Path(new ArrayList<>(Arrays.asList(goToStackWaypoints)));
            drawPath(goToStackPath);

            waitAtCurPose(shootHighGoal1Time);

            if (ringCase == RingCase.Four) {
                Waypoint[] intakeStackWaypoints = new Waypoint[] {
                        new Waypoint(lX, lY, lTh, 0.5, 0, 0, 0),
                        new Waypoint(110, 36, PI/2, 0.5, 0, 0, intakeStackTime),
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

        ArrayList<Waypoint> ringWaypoints = new ArrayList<>();
        ringWaypoints.add(new Waypoint(lX, lY, lTh, 60, 60, 0, 0));
        ringWaypoints.add(new Waypoint(62, 127, 0, 30, 30, 0, 2.0));
        ringWaypoints.add(new Waypoint(116, 127, 0, 30, 20, 0, 3.75));
        ringWaypoints.add(new Waypoint(124.5, 128, 0, 20, 5, 0, ringTime));

        Waypoint[] ringThWaypoints = new Waypoint[] {
                new Waypoint(lTh, 0, 0, 0, 0, 0, 0),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 2.0),
                new Waypoint(PI/4, 0, 0, 0, 0, 0, 3.75),
                new Waypoint(PI/2, 0, 0, 0, 0, 0, ringTime),
        };
        Path ringThPath = new Path(new ArrayList<>(Arrays.asList(ringThWaypoints)));
        Path ringPath = new Path(ringWaypoints);
        ringPath.addInterval(new Interval(1.8, ringTime, ringThPath));
        drawPath(ringPath);

        if (ringCase != RingCase.Four) {
            Waypoint[] deliverWobbleWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh + PI, 40, 30, 0, 0),
                    new Waypoint(wobbleCor[0], wobbleCor[1], PI/2, -30, -30, 0, deliverWobbleTime),
            };
            Path deliverWobblePath = new Path(new ArrayList<>(Arrays.asList(deliverWobbleWaypoints)), true);
            drawPath(deliverWobblePath);
        }

        waitAtCurPose(stopForWgDeliverTime);

        Waypoint[] intakeWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, -30, -50, 0, 0),
                new Waypoint(79, 24.5, PI/2, -0.1, 60, 0, 2.5),
                new Waypoint(87, 24.5, PI/2, -0.1, -0.1, 0, intakeWobble2Time),
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
                new Waypoint(88, 61, PI/2, 30, 30, 0, goToHighShootTime),
        };
        Path goToHighShootPath = new Path(new ArrayList<>(Arrays.asList(goToHighShootWaypoints)));
        drawPath(goToHighShootPath);

        waitAtCurPose(shootHighGoal2Time);

        Waypoint[] deliverWobble2Waypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 50, 40, 0, 0),
                new Waypoint(wobble2Cor[0], wobble2Cor[1], PI/2, 30, 30, 0, deliverWobble2Time),
        };
        Path deliverWobble2Path = new Path(new ArrayList<>(Arrays.asList(deliverWobble2Waypoints)));
        drawPath(deliverWobble2Path);

        waitAtCurPose(stopForWgDeliverTime);

        Waypoint[] parkWaypoints;
        if (ringCase == RingCase.Zero) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -30, -30, 0, 0),
                    new Waypoint(lX, lY - 15, PI/2, -10, 0, 0, 0.75),
                    new Waypoint(85, 90, PI/3, 20, 20, 0, parkTime),
            };
        } else if (ringCase == RingCase.One) {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -30, -30, 0, 0),
                    new Waypoint(90, 88, PI/2, -30, -30, 0, parkTime),
            };
        } else {
            parkWaypoints = new Waypoint[] {
                    new Waypoint(lX, lY, lTh, -50, -60, 0, 0),
                    new Waypoint(104, 88, PI/4, -30, -30, 0, parkTime),
            };
        }
        Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)), ringCase != RingCase.Zero);
        drawPath(parkPath);
    }
}
