package Utilities;

public class DataPoint {
    public final double sinceStart, x, y, theta, velocityX, velocityY, velocityTheta,
        accelX, accelY, accelTheta;
    public final boolean stoneInRobot, stoneClamped, tryingToDeposit, armIsHome, armIsDown, armIsOut;

    public DataPoint(double sinceStart, double x, double y, double theta,
                     double velocityX, double velocityY, double velocityTheta,
                     double accelX, double accelY, double accelTheta,
                     boolean stoneInRobot, boolean stoneClamped, boolean tryingToDeposit,
                     boolean armIsHome, boolean armIsDown, boolean armIsOut) {
        this.sinceStart = sinceStart;
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityTheta = velocityTheta;
        this.accelX = accelX;
        this.accelY = accelY;
        this.accelTheta = accelTheta;
        this.stoneInRobot = stoneInRobot;
        this.stoneClamped = stoneClamped;
        this.tryingToDeposit = tryingToDeposit;
        this.armIsHome = armIsHome;
        this.armIsDown = armIsDown;
        this.armIsOut = armIsOut;
    }
}
