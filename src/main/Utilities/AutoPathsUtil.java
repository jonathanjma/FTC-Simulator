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

public class AutoPathsUtil {

    private static Group pathsGroup;

    private BasePaths paths;
    private static ArrayList<Path> pathList;

    private static int colorValue;
    private final int initColorValue;
    private static double colorInterval;

    public static double lX, lY, lTh;
    private static final boolean enableWaits = true;
    public static boolean process = true;

    public static ArrayList<Ring> initRings, rings;

    public AutoPathsUtil(Group pathsGroup) {
        this(pathsGroup, 255, 0);
    }

    public AutoPathsUtil(Group pathsGroup, int startingColorValue, double colorInterval) {
        AutoPathsUtil.pathsGroup = pathsGroup;
        paths = new Paths();
        colorValue = startingColorValue;
        initColorValue = colorValue;
        AutoPathsUtil.colorInterval = colorInterval;
    }

    public void drawAutoPaths() {
        pathsGroup.getChildren().clear();
        colorValue = initColorValue;

        pathList = new ArrayList<>();

        if (rings == null) {
            rings = new ArrayList<>();
            rings.add(new Ring(85, 139, 0));
            rings.add(new Ring(97, 139, 1));
            rings.add(new Ring(113, 139, 2));
            initRings = new ArrayList<>(rings);
        }

        process = true;

        paths.drawPaths();
    }

    public static void drawPath(Path path) {
        double time = path.totalTime();
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {
            Pose curPose = path.getRobotPose(currentTime);
            double x = getXPixel(curPose.getX());
            double y = getYPixel(curPose.getY());

            Circle pathSegmentRed = new Circle(x, y, 1.5, Color.rgb(colorValue, 0, 0));
            Circle pathSegmentBlue = new Circle(getXPixel(144) - x, y, 1.5, Color.rgb(0, 0, colorValue));
            if (process || pathList == null) {
                pathsGroup.getChildren().addAll(pathSegmentRed, pathSegmentBlue);
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

    public static void setColorValue(int colorValue) {
        AutoPathsUtil.colorValue = colorValue;
    }

    public void setPaths(BasePaths newPaths) {
        paths = newPaths;
    }

    public BasePaths getPaths() {
        return paths;
    }

    public ArrayList<Path> getPathList() {
        return pathList;
    }
}
