package main;

import main.PathingFiles.Path;
import main.PathingFiles.Waypoint;
import main.Utilities.BasePaths;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.PI;
import static main.Utilities.AutoPathsUtil.drawPath;
import static main.Utilities.AutoPathsUtil.waitAtCurPose;

public class Paths extends BasePaths {

    private final boolean carousel_side = false;

    public void drawPaths() {
        drawPathsFull();
    }

    public void drawPathsFull() {

        double depositTime = 1;
        double intakeTime = 0.5;

        if (!carousel_side) {

            double goToWarehouseTime = 1;
            double cycleScoreTime = 1;
            double parkTime = 0.5;

            Waypoint[] preload = new Waypoint[] {
                    new Waypoint(138, 86, PI/2, 5, 1, 0, 0),
                    new Waypoint(138,111,PI/2,10,1,0, 1)
            };
            Path preloadP = new Path(new ArrayList<>(Arrays.asList(preload)));
            drawPath(preloadP);

            waitAtCurPose(intakeTime);

            // cycle
            for (int i = 0; i < 5; i++) {

                Waypoint[] cycleScoreWaypoints = new Waypoint[] {
                        new Waypoint(138, 111, 3*PI/2, 10, 10, 0, 0),
                        new Waypoint(138, 84, 3*PI/2, 5, 1, 0, 1),
                        new Waypoint(130, 78.5, 6*PI/5, 5, 5, 0, 1.5),
                };
                Path cycleScorePath = new Path(new ArrayList<>(Arrays.asList(cycleScoreWaypoints)), true);
                drawPath(cycleScorePath);

                waitAtCurPose(depositTime);

                Waypoint[] goToWarehouseWaypoints = new Waypoint[] {
                        new Waypoint(130, 78.5, PI/5, 10, 10,0, 0),
                        new Waypoint(138, 84, PI/2, 5, 1, 0, 1),
                        new Waypoint(138,111,PI/2,10,1,0, 1.5)
                };
                Path goToWarehousePath = new Path(new ArrayList<>(Arrays.asList(goToWarehouseWaypoints)));
                drawPath(goToWarehousePath);

                waitAtCurPose(intakeTime);
            }
        }
        /*else {
            double detectBarcodeTime = 0.75;
            double deliverPreloadedFreightTime = 1.0;
            double spinCarouselTime = 1.5;
            double deliverDuckTime = 1.5;
            double goToWarehouseTime = 1.5;
            double cycleTime = 1.5;
            double parkTime = 1.5;

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
                drawPath(goToWarehousePath2);
                waitAtCurPose(1);
            }

            Waypoint[] parkWaypoints = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, -20, -10, 0, 0),
                    new Waypoint(114, 111, PI / 2, -20, -10, 0, 0.5),
            };

            process = true;
        }*/
    }
}
