package Threads;

import App.AutoPlayer;
import App.PlayerBase;
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
    private ArrayList<SimpleBooleanProperty> buttonDisableProperties;
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
                          ArrayList<SimpleBooleanProperty> buttonDisableProperties, AutoPlayer app) {
        this.pathList = pathList;
        this.curTime = curTime;
        this.buttonDisableProperties = buttonDisableProperties;
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
                        Platform.runLater(() -> {
                            app.updateRobot(curPose.getX(), curPose.getY(), curPose.getTheta());
                            curTime.set(String.format("%.2f", Double.parseDouble(curTime.getValue()) + 0.01));

                            System.out.println(Math.hypot(curPose.getVx(), curPose.getVy()));

                            buttonDisableProperties.get(1).set(pathNum == pathList.size() - 1); // next
                            buttonDisableProperties.get(2).set(pathNum == 0); // prev
                        });
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

        Platform.runLater(() -> {
            buttonDisableProperties.get(0).set(true);
            app.setState(PlayerBase.State.Paused);
        });
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void setSlow(boolean slow) {
        sleepMilli = slow ? 30 : 10;
    }

    public void setPathNum(int pathNum) {
        if (pathNum >= 0 && pathNum <= pathList.size()-1) {
            this.pathNum = pathNum;
            currentTime = 0;
            curPath = pathList.get(pathNum);
            time = curPath.totalTime();

            Pose pose = pathList.get(pathNum).getRobotPose(0);
            Platform.runLater(() -> app.updateRobot(pose.getX(), pose.getY(), pose.getTheta()));
        }
    }

    public void resetPathNum() {
        setPathNum(0);
    }

    public int getPathNum() {
        return pathNum;
    }

    public void setPathList(ArrayList<Path> newPathList) {
        pathList = newPathList;
        resetPathNum();
    }

    public void endThread() {
        active = false;
    }
}
