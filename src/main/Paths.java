package main;

import main.PathingFiles.Path;
import main.PathingFiles.Waypoint;
import main.Utilities.BasePaths;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.PI;
import static main.Utilities.AutoPathsUtil.*;

public class Paths extends BasePaths {

    private final boolean warehouse_auto = false;
    public enum Case {None, Left, Middle, Right}

    public void drawPaths() {
        drawPathsFull();
    }

    public void drawPathsFull() {

        double depositTime = 1;
        double intakeTime = 0.5;

        double[] preloadScoreCoords;
        Case barcodeCase = Case.Left;

        if (warehouse_auto) {

            Waypoint[] preload = new Waypoint[] {
                    new Waypoint(138, 86, PI/2, 5, 1, 0, 0),
                    new Waypoint(138,111,PI/2,10,1,0, 1)
            };
            Path preloadP = new Path(new ArrayList<>(Arrays.asList(preload)));
            drawPath(preloadP);

            waitAtCurPose(intakeTime);

            // cycle
            for (int i = 0; i < 4; i++) {

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
        else {
            double goToPreloadTime = 1.5;
            double timeToCarousel = 2;

            if (barcodeCase == Case.Left) {
                preloadScoreCoords = new double[]{118, 48, 11 * PI/6};
            } else if (barcodeCase == Case.Middle) {
                preloadScoreCoords = new double[]{120, 48, 11 * PI/6};
            } else {
                preloadScoreCoords = new double[]{128, 44, 11 * PI/6};
            }
            double[] spinPose = new double[]{129, 16, 7.2 * PI / 4};
            double[] parkCoords = new double[]{109, 10, 0};

            Waypoint[] preloadScoreWaypoints = new Waypoint[]{
                    new Waypoint(135, 41, PI, 10, 10, 0, 0),
                    new Waypoint(preloadScoreCoords[0], preloadScoreCoords[1], preloadScoreCoords[2] + PI, 2, -10, 0.01, goToPreloadTime),
            };
            Path preloadPath = new Path(new ArrayList<>(Arrays.asList(preloadScoreWaypoints)), true);
            drawPath(preloadPath);

            waitAtCurPose(intakeTime);

            Waypoint[] pathToCarousel = new Waypoint[]{
                    new Waypoint(lX, lY, lTh, 10, 10, 0, 0),
                    new Waypoint(spinPose[0], spinPose[1] - 4, spinPose[2], 1, -5, 0, timeToCarousel),
            };
            Path spinPath = new Path(new ArrayList<>(Arrays.asList(pathToCarousel)));
            drawPath(spinPath);

            drawToPoint(lX, lY, parkCoords[0], 6);
        }
    }
}
