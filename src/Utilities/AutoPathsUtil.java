package Utilities;

import PathingFiles.*;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Arrays;

import static Utilities.ConversionUtil.getXPixel;
import static Utilities.ConversionUtil.getYPixel;

public class AutoPathsUtil {

    private Pane drawingPane;
    private SplineGenerator splineGenerator = new SplineGenerator();

    private ArrayList<Path> pathList;
    private ArrayList<Double> timeList;

    private int colorValue;
    private double colorInterval;

    public AutoPathsUtil(Pane paneToDrawOn, int startingColorValue, double colorInterval) {
        drawingPane = paneToDrawOn;
        colorValue = startingColorValue;
        this.colorInterval = colorInterval;
    }

    public void drawAutoPaths() {

        // Skystone Position Variables
        double skystoneY = -1;
        int skystonePos = 2; // <-----------------------------
        if (skystonePos == 1) {skystoneY = 129;}
        else if (skystonePos == 2) {skystoneY = 121;}
        else if (skystonePos == 3) {skystoneY = 113;}

        // Path Time Variables
        // skystone 1 times
        double skystone1Time = 1.25;
        double toFoundation1Time = 3;
        double skystone2Time = 2;
        double toFoundation2Time = 2;
        double stone3Time = 2;
        double toFoundation3Time = 2;
        double stone4Time = 2.5;
        double toFoundation4Time = 2.5;
        double stone5Time = 2.5;
        double toFoundation5Time = 2.5;

        if (skystonePos == 2) {
            skystone1Time = 1.5;
            toFoundation1Time = 2.75;
            skystone2Time = 2;
            toFoundation2Time = 2;
            stone3Time = 2;
            toFoundation3Time = 2;
            stone4Time = 2.5;
            toFoundation4Time = 2.5;
            stone5Time = 2.5;
            toFoundation5Time = 2.5;

        } else if(skystonePos == 3) {
            skystone1Time = 1.25;
            toFoundation1Time = 2.5;
            skystone2Time = 2;
            toFoundation2Time = 2;
            stone3Time = 2.25;
            toFoundation3Time = 2;
            stone4Time = 2.5;
            toFoundation4Time = 2.5;
            stone5Time = 2.5;
            toFoundation5Time = 2.5;
        }

        // stone 3-5 locations
        Point2D[][] stonelocations = {
                {new Point2D(54, 100), new Point2D(74, 115), new Point2D(60, 125)},
                {new Point2D(55,114), new Point2D(55,93), new Point2D(47,130)},
                {new Point2D(55,108), new Point2D(47,126), new Point2D(47,130)}
        };

        Waypoint[] skystone1PathWaypoints = {
                new Waypoint(9,111,0,20,100,0,0),
                new Waypoint(45,skystoneY,Math.PI / 4 + 0.2, 20, -100, 0, skystone1Time)
        };
        Path skystone1Path = new Path(new ArrayList<>(Arrays.asList(skystone1PathWaypoints)));
        drawPath(skystone1Path,skystone1Time);

        Waypoint[] toFoundation1PathWaypoints = new Waypoint[] {
                new Waypoint(45, skystoneY, Math.PI / 4 + 0.2, -30, -100,0, 0),
                new Waypoint(36, skystoneY - 25, Math.PI / 3, -30, -40,0, 1),
                new Waypoint(31, 35, Math.PI / 2, -30, -30,0, 2),
                new Waypoint(47, 24, Math.PI, -30, 100,0, toFoundation1Time)
        };
        Path toFoundation1Path = new Path(new ArrayList<>(Arrays.asList(toFoundation1PathWaypoints)));
        //drawPath(toFoundation1Path,toFoundation1Time);

        //drawToPoint(47,24,30,45);

        Waypoint[] skystone2PathWaypoints = new Waypoint[] {
                new Waypoint(30, 45, Math.PI / 2,  10, 75,0, 0),
                new Waypoint(31, skystoneY - 28, Math.PI / 3, 30, 10,-3, 1.5),
                new Waypoint(47, skystoneY - 21, Math.PI / 4, 10, -100,0, skystone2Time)
        };
        Path skystone2Path = new Path(new ArrayList<>(Arrays.asList(skystone2PathWaypoints)));
        drawPath(skystone2Path,skystone2Time);

        Waypoint[] toFoundation2PathWaypoints = new Waypoint[] {
                new Waypoint(47, skystoneY - 21, Math.PI / 4, -30, -100,0, 0),
                new Waypoint(31, skystoneY - 32, Math.PI / 2, -50, -10,0, 1),
                new Waypoint(30, 28, Math.PI / 2, -30, 50,0, toFoundation2Time)
        };
        Path toFoundation2Path = new Path(new ArrayList<>(Arrays.asList(toFoundation2PathWaypoints)));
        //drawPath(toFoundation2Path,toFoundation2Time);

        Waypoint[] stone3PathWaypoints = new Waypoint[] {
                new Waypoint(30, 28, Math.PI / 2, 10, 100,0, 0),
                new Waypoint(31, stonelocations[skystonePos-1][0].getY() - 26, Math.PI / 3, 30, 10,-3, 1.5),
                new Waypoint(stonelocations[skystonePos-1][0].getX(), stonelocations[skystonePos-1][0].getY(), Math.PI / 4, 10, -100,0, stone3Time)
        };
        Path stone3Path = new Path(new ArrayList<>(Arrays.asList(stone3PathWaypoints)));
        drawPath(stone3Path,stone3Time);

        Waypoint[] toFoundation3PathWaypoints = new Waypoint[] {
                new Waypoint(stonelocations[skystonePos-1][0].getX(), stonelocations[skystonePos-1][0].getY(), Math.PI / 4, -30, -100,0, 0),
                new Waypoint(31, stonelocations[skystonePos-1][0].getY() - 32, Math.PI / 2, -50, -10,0, 1),
                new Waypoint(30, 25, Math.PI / 2, -30, 50,0, toFoundation3Time)
        };
        Path toFoundation3Path = new Path(new ArrayList<>(Arrays.asList(toFoundation3PathWaypoints)));
        //drawPath(toFoundation3Path,toFoundation3Time);

        Waypoint[] stone4PathWaypoints;
        if (skystonePos == 2) {
            stone4PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 25, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stonelocations[skystonePos - 1][1].getY() - 16, Math.PI / 3, 30, 10, -3, 1.5),
                    new Waypoint(stonelocations[skystonePos - 1][1].getX(), stonelocations[skystonePos - 1][1].getY(), -Math.PI / 8, 10, -100, 0, stone4Time)
            };
        } else if (skystonePos == 3) {
            stone4PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 25, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stonelocations[skystonePos - 1][1].getY() - 21, Math.PI / 3, 30, 10, -3, 1.5),
                    new Waypoint(stonelocations[skystonePos - 1][1].getX(), stonelocations[skystonePos - 1][1].getY(), Math.PI / 2, 10, -100, 0, stone4Time)
            };
        } else { //pos 1
            stone4PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 25, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stonelocations[skystonePos - 1][1].getY() - 26, Math.PI / 3, 30, 10, -3, 1.5),
                    new Waypoint(stonelocations[skystonePos - 1][1].getX(), stonelocations[skystonePos - 1][1].getY(), Math.PI / 4, 10, -100, 0, stone4Time)
            };
        }
        Path stone4Path = new Path(new ArrayList<>(Arrays.asList(stone4PathWaypoints)));
        drawPath(stone4Path,stone4Time);

        Waypoint[] toFoundation4PathWaypoints = new Waypoint[] {
                new Waypoint(stonelocations[skystonePos - 1][1].getX(), stonelocations[skystonePos - 1][1].getY(), /*add last x/y/theta function*/Math.PI / 4, -30, -100,0, 0),
                new Waypoint(31, stonelocations[skystonePos-1][1].getY() - 32, Math.PI / 2, -50, -10,0, 1),
                new Waypoint(30, 25, Math.PI / 2, -30, 50,0, toFoundation4Time)
        };
        Path toFoundation4Path = new Path(new ArrayList<>(Arrays.asList(toFoundation4PathWaypoints)));
        //drawPath(toFoundation4Path,toFoundation4Time);

        Waypoint[] stone5PathWaypoints;
        if (skystonePos == 2 || skystonePos == 3) {
            stone5PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 25, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stonelocations[skystonePos - 1][2].getY() - 26, Math.PI / 3, 30, 10, -3, 1.5),
                    new Waypoint(stonelocations[skystonePos - 1][2].getX(), stonelocations[skystonePos - 1][2].getY(), Math.PI / 2, 10, -100, 0, stone5Time)
            };
        } else { //pos 1
            stone5PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 25, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stonelocations[skystonePos - 1][2].getY() - 26, Math.PI / 3, 30, 10, -3, 1.5),
                    new Waypoint(stonelocations[skystonePos - 1][2].getX(), stonelocations[skystonePos - 1][2].getY(), Math.PI / 4, 10, -100, 0, stone5Time)
            };
        }
        Path stone5Path = new Path(new ArrayList<>(Arrays.asList(stone5PathWaypoints)));
        drawPath(stone5Path,stone5Time);

        Waypoint[] toFoundation5PathWaypoints = new Waypoint[] {
                new Waypoint(stonelocations[skystonePos - 1][2].getX(), stonelocations[skystonePos - 1][2].getY(), /*add last x/y/theta function*/Math.PI / 4, -30, -100,0, 0),
                new Waypoint(31, stonelocations[skystonePos-1][2].getY() - 32, Math.PI / 2, -50, -10,0, 1),
                new Waypoint(30, 25, Math.PI / 2, -30, 50,0, toFoundation5Time)
        };
        Path toFoundation5Path = new Path(new ArrayList<>(Arrays.asList(toFoundation5PathWaypoints)));
        //drawPath(toFoundation5Path,toFoundation5Time);

        //drawToPoint(30,45,33,72);

        pathList = new ArrayList<>(Arrays.asList(skystone1Path,toFoundation1Path,skystone2Path,toFoundation2Path,stone3Path,toFoundation3Path,stone4Path,toFoundation4Path,stone5Path,toFoundation5Path));
        timeList = new ArrayList<>(Arrays.asList(skystone1Time,toFoundation1Time,skystone2Time,toFoundation2Time,stone3Time,toFoundation3Time,stone4Time,toFoundation4Time,stone5Time,toFoundation5Time));
    }

    public void drawPath(Path path, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            Pose curPose = path.getRobotPose(currentTime);
            double x = getXPixel(curPose.getX());
            double y = getYPixel(curPose.getY());

            Line pathSegmentLineRed = new Line(x, y, x, y);
            pathSegmentLineRed.setStroke(Color.rgb(colorValue, 0, 0));
            Line pathSegmentLineBlue = new Line(getXPixel(144)-x, y, getXPixel(144)-x, y);
            pathSegmentLineBlue.setStroke(Color.rgb(0, 0, colorValue));
            drawingPane.getChildren().addAll(pathSegmentLineRed, pathSegmentLineBlue);
        }
        colorValue -= colorInterval;
    }

    public void drawSpline(Spline[] splines, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            double x = getXPixel(splines[0].position(currentTime));
            double y = getYPixel(splines[1].position(currentTime));
            Line splineSegmentLineRed = new Line(x, y, x, y);
            splineSegmentLineRed.setStroke(Color.rgb(colorValue, 0, 0));
            Line splineSegmentLineBlue = new Line(getXPixel(144)-x, y, getXPixel(144)-x, y);
            splineSegmentLineBlue.setStroke(Color.rgb(0, 0, colorValue));
            drawingPane.getChildren().addAll(splineSegmentLineRed, splineSegmentLineBlue);
        }
        colorValue -= colorInterval;
    }

    public void drawToPoint(double x1, double y1, double x2, double y2) {

        x1 = getXPixel(x1); x2 = getXPixel(x2);
        y1 = getYPixel(y1);  y2 = getYPixel(y2);
        Line toPointLineRed = new Line(x1, y1, x2, y2);
        toPointLineRed.setStroke(Color.rgb(colorValue, 0, 0));
        Line toPointLineBlue = new Line(getXPixel(144)-x1, y1, getXPixel(144)-x2, y2);
        toPointLineBlue.setStroke(Color.rgb(0, 0, colorValue));
        drawingPane.getChildren().addAll(toPointLineRed, toPointLineBlue);
        colorValue -= colorInterval;
    }

    public ArrayList<Path> getPathList() {
        return pathList;
    }
    public ArrayList<Double> getTimeList() {
        return timeList;
    }
}
