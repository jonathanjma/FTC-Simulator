package App;

import PathingFiles.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Arrays;

public class AutoPathsUtil {

    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    private final static double inchToPixel = 4.16;

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

        double skystone1Time = 1.5;
        double toFoundation1Time = 3;
        double skystone2Time = 2;
        double toFoundation2Time = 2.5;

        double skystoneY = -1, skystonePos = 1; // <-------------
        if (skystonePos == 1) skystoneY = 128;
        else if (skystonePos == 2) skystoneY = 120;
        else if (skystonePos == 3) skystoneY = 112;

        Waypoint[] skystone1PathWaypoints = {
                new Waypoint(9,111,0,20,100,0,0),
                new Waypoint(45,skystoneY,Math.PI / 4 + 0.2, 20, -100, 0, skystone1Time)
        };
        Path skystone1Path = new Path(new ArrayList<>(Arrays.asList(skystone1PathWaypoints)));
        drawPath(skystone1Path,skystone1Time);

        Waypoint[] toFoundation1PathWaypoints = new Waypoint[] {
                new Waypoint(45, skystoneY, Math.PI / 4 + 0.2, -30, -100,0, 0),
                new Waypoint(36, skystoneY - 25, Math.PI / 3, -30, -40,0, 1),
                new Waypoint(36, 35, Math.PI / 2, -30, -30,0, 2),
                new Waypoint(47, 20, Math.PI, -30, 100,0, toFoundation1Time)
        };
        Path toFoundation1Path = new Path(new ArrayList<>(Arrays.asList(toFoundation1PathWaypoints)));
        drawPath(toFoundation1Path,toFoundation1Time);

        drawToPoint(47,20,30,45);

        Waypoint[] skystone2PathWaypoints = new Waypoint[] {
                new Waypoint(30,45, Math.PI/2, 50, 100,0, 0),
                new Waypoint(35, skystoneY - 32, Math.PI / 3, 60, 10,-1.5, 1),
                new Waypoint(52, skystoneY - 18, Math.PI / 4, 1, -100,0, skystone2Time)
        };
        Path skystone2Path = new Path(new ArrayList<>(Arrays.asList(skystone2PathWaypoints)));
        drawPath(skystone2Path,skystone2Time);

        Waypoint[] toFoundation2PathWaypoints = new Waypoint[] {
                new Waypoint(52, skystoneY - 18, Math.PI / 4, -30, -100,0, 0),
                new Waypoint(36, skystoneY - 32, Math.PI / 2, -50, -10,0, 1),
                new Waypoint(30, 25, Math.PI / 2, -30, 50,0, toFoundation2Time)
        };
        Path toFoundation2Path = new Path(new ArrayList<>(Arrays.asList(toFoundation2PathWaypoints)));
        drawPath(toFoundation2Path,toFoundation2Time);

        drawToPoint(30,45,30,72);

        pathList = new ArrayList<>(Arrays.asList(skystone1Path,toFoundation1Path,skystone2Path,toFoundation2Path));
        timeList = new ArrayList<>(Arrays.asList(skystone1Time,toFoundation1Time,skystone2Time,toFoundation2Time));
    }

    public void drawPath(Path path, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            Pose curPose = path.getRobotPose(currentTime);
            double x = curPose.getX() * inchToPixel;
            double y = (144-curPose.getY()) * inchToPixel;

            Line splineSegmentLineRed = new Line(x, y, x, y);
            splineSegmentLineRed.setStroke(Color.rgb(colorValue, 0, 0));
            Line splineSegmentLineBlue = new Line((144*inchToPixel)-x, y, (144*inchToPixel)-x, y);
            splineSegmentLineBlue.setStroke(Color.rgb(0, 0, colorValue));
            drawingPane.getChildren().addAll(splineSegmentLineRed, splineSegmentLineBlue);
        }
        colorValue -= colorInterval;
    }

    public void drawSpline(Spline[] splines, double time) {
        for (double currentTime = 0; currentTime < time; currentTime += 0.01) {

            double x = splines[0].position(currentTime) * inchToPixel;
            double y = (144-splines[1].position(currentTime)) * inchToPixel;
            Line splineSegmentLineRed = new Line(x, y, x, y);
            splineSegmentLineRed.setStroke(Color.rgb(colorValue, 0, 0));
            Line splineSegmentLineBlue = new Line((144*inchToPixel)-x, y, (144*inchToPixel)-x, y);
            splineSegmentLineBlue.setStroke(Color.rgb(0, 0, colorValue));
            drawingPane.getChildren().addAll(splineSegmentLineRed, splineSegmentLineBlue);
        }
        colorValue -= colorInterval;
    }

    public void drawToPoint(double x1, double y1, double x2, double y2) {

        x1 *= inchToPixel; x2 *= inchToPixel;
        y1 = (144-y1) * inchToPixel;  y2 = (144-y2) * inchToPixel;
        Line toPointLineRed = new Line(x1, y1, x2, y2);
        toPointLineRed.setStroke(Color.rgb(colorValue, 0, 0));
        Line toPointLineBlue = new Line((144*inchToPixel)-x1, y1, (144*inchToPixel)-x2, y2);
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
