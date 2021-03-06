package main.Threads;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import main.App.MatchReplayer;
import main.App.PlayerBase;
import main.Utilities.RobotDataUtil;

import static java.lang.Thread.sleep;

@SuppressWarnings("FieldMayBeFinal")
public class FollowLogData implements Runnable {

    private MatchReplayer app;
    private RobotDataUtil dataUtil;
    private SimpleBooleanProperty startStopDisabled;
    private int counter = 0;
    private boolean active = true;
    private boolean pause = false;

    public FollowLogData(RobotDataUtil dataUtil, SimpleBooleanProperty startStopDisabled, MatchReplayer app) {
        this.dataUtil = dataUtil;
        this.startStopDisabled = startStopDisabled;
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

        Platform.runLater(() -> {
            startStopDisabled.set(true);
            app.setState(PlayerBase.State.Paused);
        });
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
