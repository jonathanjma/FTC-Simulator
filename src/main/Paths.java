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
        double detectBarcodeTime = 0.75;
        double deliverPreloadedFreightTime = 1.0;
        double spinCarouselTime = 1.5;
        double deliverDuckTime = 1.5;
        double goToWarehouseTime = 1.5;
        double cycleTime = 1.5;
        double parkTime = 1.5;

//        process=false;
        int barcodeCase = 0;

        Waypoint[] deliverPreloadedFreightWaypoints = new Waypoint[] {
                new Waypoint(135, 38, PI, 20, 20, 0, 0),
                new Waypoint(120, 58, PI,20,20,0, deliverPreloadedFreightTime),
        };
        Path deliverPreloadedFreightPath = new Path(new ArrayList<>(Arrays.asList(deliverPreloadedFreightWaypoints)),true);
        drawPath(deliverPreloadedFreightPath);

        waitAtCurPose(0.5);

        process = true;

        Waypoint[] spinCarouselWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 40, 30, 0, 0),
                new Waypoint(130, 15, 7*PI/4, 30, -10, 0, spinCarouselTime),
        };
        Path spinCarouselPath = new Path(new ArrayList<>(Arrays.asList(spinCarouselWaypoints)));
        drawPath(spinCarouselPath);

        waitAtCurPose(1);

        process = true;

        Waypoint[] deliverDuckWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh+PI, 20, 10, 0, 0),
                new Waypoint(127, 20, 3*PI/5, 20,10,0,0),
                new Waypoint(124, 57, PI, 20, 5,0, deliverDuckTime),
        };
        Path deliverDuckPath = new Path(new ArrayList<>(Arrays.asList(deliverDuckWaypoints)),true);
        drawPath(deliverDuckPath);

        waitAtCurPose(1.25);

        process = true;

        Waypoint[] goToWarehouseWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh, 35, 30, 0, 0),
                new Waypoint(135, 78, PI/2, 20, 20, 0, 1),
                new Waypoint(135, 115, PI/2,10,-5,0,goToWarehouseTime),
        };
        Path goToWarehousePath = new Path(new ArrayList<>(Arrays.asList(goToWarehouseWaypoints)));
        drawPath(goToWarehousePath);

        waitAtCurPose(0.5);

        process = true;

        Waypoint[] cycleWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh,-10,-20,0,0),
                new Waypoint(135, 82, PI/2,-20,-5,0,1),
                new Waypoint(118, 63, PI/12,-20,-10,0,cycleTime),
        };
        Path cyclePath = new Path(new ArrayList<>(Arrays.asList(cycleWaypoints)),true);

        Waypoint[] goToWarehouseWaypoints2 = new Waypoint[] {
                new Waypoint(118, 63, PI/12, 10, 10, 0, 0),
                new Waypoint(135, 82, PI/2, 10, 10, 0, 1),
                new Waypoint(135, 115, PI/2,10,10,0,goToWarehouseTime),
        };
        Path goToWarehousePath2 = new Path(new ArrayList<>(Arrays.asList(goToWarehouseWaypoints2)));

        for (int i = 0; i < 4; i++) {
            drawPath(cyclePath);
            waitAtCurPose(1);
            System.out.println("right before gotowarehouse2");
            System.out.println("lX = " + lX + ", lY = " + lY + ", lTh = " + lTh);
            drawPath(goToWarehousePath2);
            waitAtCurPose(1);
        }

        Waypoint[] parkWaypoints = new Waypoint[] {
                new Waypoint(lX, lY, lTh,-20,-10,0,0),
                new Waypoint(114, 111, PI/2,-20,-10,0,0.5),
        };

        process = true;
    }
}
