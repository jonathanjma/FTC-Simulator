package main.Utilities;

public class DataPoint {

    public final double timeSinceSt, x, y, theta, vx, vy, w, ax, ay, alpha,
            turretTheta, depositSlidesDist, avgCycleTime;
    public final int numCycles;
    public final boolean intakeSlidesExtend, intakeTransfer, depositing;
    public final String depositTarget;

    public DataPoint(double timeSinceSt, double x, double y, double theta, double vx, double vy, double w, double ax, double ay, double alpha,
                     double turretTheta, double depositSlidesDist, String depositTarget, boolean intakeSlidesExtend, boolean intakeTransfer, boolean depositing,
                     int numCycles, double avgCycleTime) {
        this.timeSinceSt = timeSinceSt;
        this.x = x; this.y = y; this.theta = theta;
        this.vx = vx; this.vy = vy; this.w = w;
        this.ax = ax; this.ay = ay; this.alpha = alpha;
        this.turretTheta = turretTheta;
        this.depositSlidesDist = depositSlidesDist;
        this.depositTarget = depositTarget;
        this.intakeSlidesExtend = intakeSlidesExtend;
        this.intakeTransfer = intakeTransfer; this.depositing = depositing;
        this.numCycles = numCycles;
        this.avgCycleTime = avgCycleTime;
    }
}
