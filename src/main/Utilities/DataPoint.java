package main.Utilities;

public class DataPoint {

    public final double timeSinceSt, x, y, theta, vx, vy, w, ax, ay, alpha,
            depositSlidesPos, avgCycleTime;
    public final int intakeSlidesPos, intakeState, depositState, numCycles;
    public final String depositTarget;

    public DataPoint(double timeSinceSt, double x, double y, double theta, double vx, double vy, double w, double ax, double ay, double alpha,
                     double depositSlidesPos, String depositTarget, int intakeSlidesPos, int intakeState, int depositState,
                     int numCycles, double avgCycleTime) {
        this.timeSinceSt = timeSinceSt;
        this.x = x; this.y = y; this.theta = theta;
        this.vx = vx; this.vy = vy; this.w = w;
        this.ax = ax; this.ay = ay; this.alpha = alpha;
        this.depositSlidesPos = depositSlidesPos;
        this.depositTarget = depositTarget;
        this.intakeSlidesPos = intakeSlidesPos;
        this.intakeState = intakeState; this.depositState = depositState;
        this.numCycles = numCycles;
        this.avgCycleTime = avgCycleTime;
    }
}
