package main;

import main.PathingFiles.Interval;
import main.PathingFiles.Path;
import main.PathingFiles.Waypoint;
import main.Utilities.BasePaths;
import main.Utilities.RingCase;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.PI;
import static main.Utilities.AutoPathsUtil.*;

public class Paths extends BasePaths {

    private final boolean carousel_side = false;
    private final int marker_pos = 1;

    double[][] wobbleDelivery = {{115, 85, PI / 2}, {90, 100, PI / 2}, {125, 130, 2 * PI / 3}};
    double[][] wobble2Delivery = {{123, 65, 3 * PI / 4}, {104, 85, 5 * PI / 6}, {121, 118, 2 * PI / 3}};

    public void drawPaths() {
        drawPathsFull();
    }

    public void drawPathsFull() {
        double preloadScoreTime = 1.5;
        double goToWarehouseTime1 = 1.1;
        double goToWarehouseTime2 = 1.9;
        double cycleScoreTime1 = .6;
        double cycleScoreTime2 = 2;
        double parkTime1 = 1.2;
        double parkTime2 = 1.9;

        double depositTime = 2;
        double intakeTime = 2;

        double[][] preloadScore = {{117, 60}, {120, 60}, {130, 60}};

        if (!carousel_side) {

            Waypoint[] preloadScoreWaypoints;

            if (marker_pos == 1) {
                preloadScoreWaypoints = new Waypoint[]{
                        new Waypoint(135, 84, 0, -10, .1, 2, 0.0),
                        new Waypoint(preloadScore[0][0], preloadScore[0][1], PI, 20, .1, 3, preloadScoreTime)
                };
            } else if (marker_pos == 2) {
                preloadScoreWaypoints = new Waypoint[]{
                        new Waypoint(135, 84, 0, -10, .1, 10, 0.0),
                        new Waypoint(preloadScore[1][0], preloadScore[1][1], PI, 20, .1, 3, preloadScoreTime)
                };
            } else {
                preloadScoreWaypoints = new Waypoint[]{
                        new Waypoint(135, 84, 0, -10, .1, 10, 0.0),
                        new Waypoint(preloadScore[2][0], preloadScore[2][1], PI, 20, .1, 3, preloadScoreTime)
                };

            }

            Path preloadScorePath = new Path(new ArrayList<>(Arrays.asList(preloadScoreWaypoints)), true);
            drawPath(preloadScorePath);

            waitAtCurPose(depositTime);

//cycle
            for (int i = 0; i < 3; i++) {
                Waypoint[] goToWarehouseWaypoints = new Waypoint[]{
                        new Waypoint(lX, lY, lTh, 40, 40, .1, 0),
                        new Waypoint(lX + 20, lY + 40, PI / 2, 40, .1, 0, goToWarehouseTime1),
                        new Waypoint(137, 120.19, PI / 2, 20, 0, 0, goToWarehouseTime2)
                };

                Path goToWarehousePath = new Path(new ArrayList<>(Arrays.asList(goToWarehouseWaypoints)));
                drawPath(goToWarehousePath);

                waitAtCurPose(intakeTime);


                Waypoint[] cycleScoreWaypoints = new Waypoint[]{
                        new Waypoint(lX, lY, lTh, -20, .1, .1, 0),
                        new Waypoint(lX, lY - 20, -PI / 2, 20, .1, .5, cycleScoreTime1),
                        new Waypoint(117, 60, -PI, 40, 20, .1, cycleScoreTime2)
                };
                Path cycleScorePath = new Path(new ArrayList<>(Arrays.asList(cycleScoreWaypoints)), true);
                drawPath(cycleScorePath);


                waitAtCurPose(depositTime);

            }

            Waypoint[] parkWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 40, 40, .1, 0),
                    new Waypoint(lX + 20, lY + 30, -PI / 2, -30, .1, .1, parkTime1),
                    new Waypoint(135, 110, PI / 2, 10, 5, 0, parkTime2)
            };
            Path parkPath = new Path(new ArrayList<>(Arrays.asList(parkWaypoints)));
            drawPath(parkPath);


        } else {
            double detectBarcodeTime = 0.75;
            double deliverPreloadedFreightTime = 1.0;
            double spinCarouselTime = 1.5;
            double deliverDuckTime = 1.5;
            double goToWarehouseTime = 1.5;
            double cycleTime = 1.5;
            double parkTime = 1.5;

//        process=false;
            int barcodeCase = 0;

            Waypoint[] deliverPreloadedFreightWaypoints = new Waypoint[]{
                    new Waypoint(135, 38, PI, 20, 20, 0, 0),
                    new Waypoint(120, 58, PI, 20, 20, 0, deliverPreloadedFreightTime),
            };
            Path deliverPreloadedFreightPath = new Path(new ArrayList<>(Arrays.asList(deliverPreloadedFreightWaypoints)), true);
            drawPath(deliverPreloadedFreightPath);

            waitAtCurPose(0.5);

            process = true;

            Waypoint[] spinCarouselWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 40, 30, 0, 0),
                    new Waypoint(130, 15, 7 * PI / 4, 30, -10, 0, spinCarouselTime),
            };
            Path spinCarouselPath = new Path(new ArrayList<>(Arrays.asList(spinCarouselWaypoints)));
            drawPath(spinCarouselPath);

            waitAtCurPose(1);

            process = true;

            Waypoint[] deliverDuckWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh + PI, 20, 10, 0, 0),
                    new Waypoint(127, 20, 3 * PI / 5, 20, 10, 0, 0),
                    new Waypoint(124, 57, PI, 20, 5, 0, deliverDuckTime),
            };
            Path deliverDuckPath = new Path(new ArrayList<>(Arrays.asList(deliverDuckWaypoints)), true);
            drawPath(deliverDuckPath);

            waitAtCurPose(1.25);

            process = true;

            Waypoint[] goToWarehouseWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 35, 30, 0, 0),
                    new Waypoint(135, 78, PI / 2, 20, 20, 0, 1),
                    new Waypoint(135, 115, PI / 2, 10, -5, 0, goToWarehouseTime),
            };
            Path goToWarehousePath = new Path(new ArrayList<>(Arrays.asList(goToWarehouseWaypoints)));
            drawPath(goToWarehousePath);

            waitAtCurPose(0.5);

            process = true;

            Waypoint[] cycleWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, -10, -20, 0, 0),
                    new Waypoint(135, 82, PI / 2, -20, -5, 0, 1),
                    new Waypoint(118, 63, PI / 12, -20, -10, 0, cycleTime),
            };
            Path cyclePath = new Path(new ArrayList<>(Arrays.asList(cycleWaypoints)), true);

            Waypoint[] goToWarehouseWaypoints2 = new Waypoint[]{
                    new Waypoint(118, 63, PI / 12, 10, 10, 0, 0),
                    new Waypoint(135, 82, PI / 2, 10, 10, 0, 1),
                    new Waypoint(135, 115, PI / 2, 10, 10, 0, goToWarehouseTime),
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

            Waypoint[] parkWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, -20, -10, 0, 0),
                    new Waypoint(114, 111, PI / 2, -20, -10, 0, 0.5),
            };

            process = true;
        }
    }
}
