package NewSim;

import PathingFiles.Path;
import PathingFiles.Pose;
import Utilities.RobotDataUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class Simulator {

    public static void main(String[] args) {
        new Thread(() -> Application.launch(SimulatorWindow.class)).start();
        while (!SimulatorWindow.started) {doNothing();}
        //////////////////////////////////////////////////////////////////////////// put sim stuff below

        /*AutoPlayer test*/
        AutoPathsUtil_Test pathsUtil = new AutoPathsUtil_Test();
        pathsUtil.parsePaths();
        ArrayList<Path> pathList = pathsUtil.getPathList();
        ArrayList<Double> timeList = pathsUtil.getTimeList();

        double currentTime = 0;
        for (int pathNum = 0; pathNum < pathList.size(); pathNum++) {
            Path curPath = pathList.get(pathNum);
            double time = timeList.get(pathNum);

            while (currentTime < time) {
                Pose curPose = curPath.getRobotPose(currentTime);
                setFrame(new Frame(curPose.getX(), curPose.getY(), curPose.getTheta()));
                currentTime += 0.01;

                try {
                    sleep(10);
                } catch (InterruptedException ex) {ex.printStackTrace();}
            }
            currentTime = 0;
        }

        /*MatchReplayer test*/
        RobotDataUtil dataUtil = new RobotDataUtil("RobotData5");
        dataUtil.parseLogFile();
        int counter = 0;
        while (counter < dataUtil.getNumOfPoints()) {

            Object[] data = dataUtil.getData(counter);
            setFrame(new Frame((double)data[1], (double)data[2], (double)data[3]));
            double timeDiff = dataUtil.getTimeDiff(counter);

            try {
                sleep((long) timeDiff);
            } catch (InterruptedException ex) {ex.printStackTrace();}
            counter++;
        }

        /*Polygon test*/
        for (int i = 0; i < 45; i++) {
            Polygon poly = new Polygon();
            poly.getPoints().addAll(
                    48.0+i, 48.0+i,
                    96.0+i, 48.0+i,
                    96.0+i, 96.0+i,
                    48.0+i, 96.0+i);
            setFrame(new Frame(poly));
            try {
                sleep(15);
            } catch (InterruptedException ex) {ex.printStackTrace();}
        }
    }

    public static void setFrame(Frame frame) {
        Platform.runLater(() -> SimulatorWindow.drawShape(frame.getRobot()));
    }

    public static void doNothing() {
        System.out.print("");
    }
}
