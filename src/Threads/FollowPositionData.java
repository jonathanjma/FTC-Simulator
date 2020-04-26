package Threads;

import App.MatchReplayer;
import Utilities.RobotDataUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;

import static java.lang.Thread.sleep;

public class FollowPositionData implements Runnable {

    private MatchReplayer app;
    private RobotDataUtil dataUtil;
    private SimpleBooleanProperty startStopVisible;
    private int counter = 0;
    private boolean active = true;
    private boolean pause = false;

    public FollowPositionData(RobotDataUtil dataUtil, SimpleBooleanProperty startStopVisible,
                              MatchReplayer app) {
        this.dataUtil = dataUtil;
        this.startStopVisible = startStopVisible;
        this.app = app;
    }

    public void run() {
        while (counter < dataUtil.getNumOfPoints()) {
            if (active) {
                if (counter == 1) {
                    Platform.runLater(() -> app.clearPathPoints());
                }
                if (!pause) {
                    Platform.runLater(() -> app.updateRobot(dataUtil.getData(counter), false));
                    double timeDiff = dataUtil.getTimeDiff(counter);

                    try {
                        sleep((long) timeDiff);
                    } catch (InterruptedException ex) {ex.printStackTrace();}
                    counter++;
                }
            } else {
                break;
            }
            System.out.print("");
        }
        startStopVisible.set(false);
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public void endThread() {
        active = false;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public int getCounter() {
        return counter;
    }
}
