package Threads;

import App.AutoPlayer;
import PathingFiles.Path;
import PathingFiles.Pose;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

@SuppressWarnings("FieldMayBeFinal")
public class FollowPathData implements Runnable {

    private AutoPlayer app;
    private SimpleStringProperty curTime;
    private SimpleBooleanProperty btnVisible;
    private ArrayList<Path> pathList;
    private boolean pause = false;
    private int pathNum = 0;
    private double currentTime = 0;
    private Path curPath;
    private Pose curPose;
    private double time;
    private boolean active = true;
    private int sleepMilli = 10;

    public FollowPathData(ArrayList<Path> pathList, SimpleStringProperty curTime,
                          SimpleBooleanProperty btnVisible, AutoPlayer app) {
        this.pathList = pathList;
        this.curTime = curTime;
        this.btnVisible = btnVisible;
        this.app = app;
    }

    public void run() {
        for (; pathNum < pathList.size(); pathNum++) {
            curPath = pathList.get(pathNum);
            time = curPath.totalTime();

            while (currentTime < time) {
                if (active) {
                    if (!pause) {
                        curPose = curPath.getRobotPose(currentTime);
                        //System.out.println(curPose.getX() +" "+ curPose.getY() +" "+ curPose.getTheta());
                        Platform.runLater(() ->
                                app.updateRobot(curPose.getX(), curPose.getY(), curPose.getTheta()));
                        Platform.runLater(() ->
                                curTime.set(String.format("%.2f", Double.parseDouble(curTime.getValue()) + 0.01)));
                        currentTime += 0.01;

                        try {
                            sleep(sleepMilli);
                        } catch (InterruptedException ex) {ex.printStackTrace();}
                    }
                } else {
                    break;
                }
                System.out.print("");
            }

            if (!active) {
                break;
            }
            currentTime = 0;
        }
        btnVisible.set(false);
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setSlow(boolean slow) {
        sleepMilli = slow ? 30 : 10;
    }

    public int getPathNum() {
        return pathNum;
    }

    public void resetPathNum() {
        pathNum = 0; currentTime = 0;
        curPath = pathList.get(pathNum);
        time = curPath.totalTime();
    }

    public void setPathList(ArrayList<Path> newPathList) {
        pathList = newPathList;
        resetPathNum();
    }

    public void endThread() {
        active = false;
    }
}
