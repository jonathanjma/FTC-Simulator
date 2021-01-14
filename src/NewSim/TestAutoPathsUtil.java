package NewSim;

import PathingFiles.*;
import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Arrays;

public class TestAutoPathsUtil {

    private ArrayList<Path> pathList;
    private ArrayList<Double> timeList;

    public void parsePaths() {

        // Skystone Position Variables
        double skystoneY = -1;
        int skystonePos = 1; // <-----------------------------
        if (skystonePos == 1) {skystoneY = 128;}
        else if (skystonePos == 2) {skystoneY = 121;}
        else if (skystonePos == 3) {skystoneY = 113;}

        // Path Time Variables
        // stone 1 times
        double skystone1Time = 1.5;
        double toFoundation1Time = 3.5;
        double foundationPullTime = 2;
        double skystone2Time = 1.5;
        double toFoundation2Time = 1.75;
        double stone3Time = 1.75;
        double toFoundation3Time = 1.75;
        double stone4Time = 1.75;
        double toFoundation4Time = 2.25;
        double stone5Time = 1.75;
        double toFoundation5Time = 2.25;

        if (skystonePos == 2) {
            skystone1Time = 1.5;
            toFoundation1Time = 3;
            skystone2Time = 1.75;
            toFoundation2Time = 1.75;
            stone3Time = 1.65;
            toFoundation3Time = 1.75;
            stone4Time = 2;
            toFoundation4Time = 1.5;
            stone5Time = 2;
            toFoundation5Time = 2.25;

        } else if(skystonePos == 3) {
            skystone1Time = 1.25;
            toFoundation1Time = 3;
            skystone2Time = 1.5;
            toFoundation2Time = 1.5;
            stone3Time = 1.5;
            toFoundation3Time = 1.5;
            stone4Time = 1.75;
            toFoundation4Time = 2.25;
            stone5Time = 1.75;
            toFoundation5Time = 2.25;
        }

        // stone 3-5 locations
        Point2D[][] stoneLocations = {
                {new Point2D(54, 100), new Point2D(75, 115), new Point2D(57, 122)},
                {new Point2D(55,112), new Point2D(70,93), new Point2D(44,128)},
                {new Point2D(55,108), new Point2D(44,124), new Point2D(44,128)}
        };

        Waypoint[] skystone1PathWaypoints = {
                new Waypoint(9,111,0,20,100,0,0),
                new Waypoint(47,skystoneY,Math.PI / 4 + 0.2, 15, -100, 0, skystone1Time)
        };
        Path skystone1Path = new Path(new ArrayList<>(Arrays.asList(skystone1PathWaypoints)));

        Waypoint[] toFoundation1PathWaypoints = new Waypoint[] {
                new Waypoint(47, skystoneY, Math.PI / 4 + 0.2, -30, -100,0, 0),
                new Waypoint(36, skystoneY - 25, Math.PI / 3, -50, -40,0, 1),
                new Waypoint(31, 35, Math.PI / 2, -30, -30,0, 2),
                new Waypoint(48, 30, Math.PI, -25, 100,0, toFoundation1Time)
        };
        Path toFoundation1Path = new Path(new ArrayList<>(Arrays.asList(toFoundation1PathWaypoints)));

        Waypoint[] foundationPullWaypoints = new Waypoint[] {
                new Waypoint(48, 30, Math.PI, 5, 30,0, 0),
                new Waypoint(33, 30, Math.PI , 30, 30,0,0.75),
                new Waypoint(28, 65, Math.PI / 2, 60, 10,0, foundationPullTime)
        };
        Path foundationPullPath = new Path(new ArrayList<>(Arrays.asList(foundationPullWaypoints)));

        Waypoint[] skystone2PathWaypoints = new Waypoint[] {
                new Waypoint(28, 65, Math.PI / 2,  10, 75,0, 0),
                new Waypoint(31, skystoneY - 28, Math.PI / 3, 30, 10,-3, 1.25),
                new Waypoint(47, skystoneY - 24, Math.PI / 4, 10, -100,0, skystone2Time)
        };
        Path skystone2Path = new Path(new ArrayList<>(Arrays.asList(skystone2PathWaypoints)));

        Waypoint[] toFoundation2PathWaypoints = new Waypoint[] {
                new Waypoint(47, skystoneY - 24, Math.PI / 4, -30, -100,0, 0),
                new Waypoint(33, skystoneY - 35, Math.PI / 2, -50, -10,0, 0.75),
                new Waypoint(28, 57, Math.PI / 2, -5, 100,0, toFoundation2Time)
        };
        Path toFoundation2Path = new Path(new ArrayList<>(Arrays.asList(toFoundation2PathWaypoints)));

        Waypoint[] stone3PathWaypoints;
        if (skystonePos == 1) {
            stone3PathWaypoints = new Waypoint[] {
                    new Waypoint(28, 57, Math.PI / 2, 10, 100,0, 0),
                    new Waypoint(31, stoneLocations[skystonePos-1][0].getY() - 18, Math.PI / 3, 30, 10,-3, 1.25),
                    new Waypoint(stoneLocations[skystonePos-1][0].getX(), stoneLocations[skystonePos-1][0].getY(), Math.PI/4-0.2, 10, -100,0, stone3Time)
            };
        } else {
            stone3PathWaypoints = new Waypoint[] {
                    new Waypoint(28, 57, Math.PI / 2, 10, 100,0, 0),
                    new Waypoint(31, stoneLocations[skystonePos-1][0].getY() - 18, Math.PI / 3, 30, 10,-3, 1.25),
                    new Waypoint(stoneLocations[skystonePos-1][0].getX(), stoneLocations[skystonePos-1][0].getY(), Math.PI/4, 10, -100,0, stone3Time)
            };
        }
        Path stone3Path = new Path(new ArrayList<>(Arrays.asList(stone3PathWaypoints)));

        Waypoint[] toFoundation3PathWaypoints = new Waypoint[] {
                new Waypoint(stoneLocations[skystonePos-1][0].getX(), stoneLocations[skystonePos-1][0].getY(), Math.PI / 4, -30, -100,0, 0),
                new Waypoint(31, stoneLocations[skystonePos-1][0].getY() - 18, Math.PI / 2, -50, -10,0, 0.75),
                new Waypoint(30, 57, Math.PI / 2, -5, 50,0, toFoundation3Time)
        };
        Path toFoundation3Path = new Path(new ArrayList<>(Arrays.asList(toFoundation3PathWaypoints)));

        Waypoint[] stone4PathWaypoints;
        if (skystonePos == 2) {
            stone4PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 57, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stoneLocations[skystonePos - 1][1].getY() - 14, Math.PI / 3, 30, 10, -3, 1.25),
                    new Waypoint(stoneLocations[skystonePos - 1][1].getX(), stoneLocations[skystonePos - 1][1].getY(), Math.PI/8, 10, -100, 0, stone4Time)
            };
        } else if (skystonePos == 3) {
            stone4PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 57, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stoneLocations[skystonePos - 1][1].getY() - 25, Math.PI / 3, 30, 10, -3, 1.33),
                    new Waypoint(stoneLocations[skystonePos - 1][1].getX(), stoneLocations[skystonePos - 1][1].getY(), Math.PI/2, 10, -100, 0, stone4Time)
            };
        } else { //pos 1
            stone4PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 57, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stoneLocations[skystonePos - 1][1].getY() - 20, Math.PI / 3, 30, 10, -3, 1.33),
                    new Waypoint(stoneLocations[skystonePos - 1][1].getX(), stoneLocations[skystonePos - 1][1].getY(), Math.PI / 4, 10, -100, 0, stone4Time)
            };
        }
        Path stone4Path = new Path(new ArrayList<>(Arrays.asList(stone4PathWaypoints)));

        Waypoint[] toFoundation4PathWaypoints = new Waypoint[] {
                new Waypoint(stoneLocations[skystonePos - 1][1].getX(), stoneLocations[skystonePos - 1][1].getY(), Math.PI / 4, -30, -100,0, 0),
                new Waypoint(31, stoneLocations[skystonePos-1][1].getY() - 10, Math.PI / 2, -50, -10,0, 0.7),
                new Waypoint(30, 57, Math.PI / 2, -5, 50,0, toFoundation4Time)
        };
        Path toFoundation4Path = new Path(new ArrayList<>(Arrays.asList(toFoundation4PathWaypoints)));

        Waypoint[] stone5PathWaypoints;
        if (skystonePos == 2 || skystonePos == 3) {
            stone5PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 57, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stoneLocations[skystonePos - 1][2].getY() - 26, Math.PI / 3, 30, 10, -3, 1.33),
                    new Waypoint(stoneLocations[skystonePos - 1][2].getX(), stoneLocations[skystonePos - 1][2].getY(), Math.PI / 2, 10, -100, 0, stone5Time)
            };
        } else { //pos 1
            stone5PathWaypoints = new Waypoint[]{
                    new Waypoint(30, 57, Math.PI / 2, 10, 100, 0, 0),
                    new Waypoint(31, stoneLocations[skystonePos - 1][2].getY() - 20, Math.PI / 3, 30, 10, -3, 1.33),
                    new Waypoint(stoneLocations[skystonePos - 1][2].getX(), stoneLocations[skystonePos - 1][2].getY(), Math.PI / 4, 10, -100, 0, stone5Time)
            };
        }
        Path stone5Path = new Path(new ArrayList<>(Arrays.asList(stone5PathWaypoints)));

        Waypoint[] toFoundation5PathWaypoints = new Waypoint[] {
                new Waypoint(stoneLocations[skystonePos - 1][2].getX(), stoneLocations[skystonePos - 1][2].getY(), Math.PI / 4, -30, -100,0, 0),
                new Waypoint(31, stoneLocations[skystonePos-1][2].getY() - 32, Math.PI / 2, -50, -10,0, 1),
                new Waypoint(30, 25, Math.PI / 2, -30, 50,0, toFoundation5Time)
        };
        Path toFoundation5Path = new Path(new ArrayList<>(Arrays.asList(toFoundation5PathWaypoints)));

        pathList = new ArrayList<>(Arrays.asList(skystone1Path,toFoundation1Path,foundationPullPath,skystone2Path,toFoundation2Path,stone3Path,toFoundation3Path,stone4Path,toFoundation4Path,stone5Path,toFoundation5Path));
        timeList = new ArrayList<>(Arrays.asList(skystone1Time,toFoundation1Time,foundationPullTime,skystone2Time,toFoundation2Time,stone3Time,toFoundation3Time,stone4Time,toFoundation4Time,stone5Time,toFoundation5Time));
    }


    public ArrayList<Path> getPathList() {
        return pathList;
    }
    public ArrayList<Double> getTimeList() {
        return timeList;
    }
}
