package PathingFiles;

public class Waypoint {

    public double x;
    public double y;
    public double theta;
    public double vx;
    public double vy;
    public double ax;
    public double ay;
    public double time;

    private double velocity, acceleration, angVelocity;
    public double getVelocity() { return velocity; }
    public double getAcceleration() { return acceleration; }
    public double getAngVelocity() { return angVelocity; }
    public String toString() { return "Waypoint(" + x + ", " + y + ", " + String.format("%.4f", theta) + ", "
            + getVelocity() + ", " + getAcceleration() + ", " + getAngVelocity() + ", " + time + ")";}

    /**
     * Generates Waypoint for differential drive constraints: velocity and acceleration are in
     * the same direction as heading
     */
    public Waypoint(double x, double y, double theta, double velocity, double acceleration, double angularVelocity, double time) {
        this.x = x;
        this.y = y;
        this.theta = theta;
        vx = velocity * Math.cos(theta);
        vy = velocity * Math.sin(theta);
        ax = acceleration * Math.cos(theta) - velocity * Math.sin(theta) * angularVelocity;
        ay = acceleration * Math.sin(theta) + velocity * Math.cos(theta) * angularVelocity;
        this.time = time;

        this.velocity = velocity;
        this.acceleration = acceleration;
        this.angVelocity = angularVelocity;
    }

    /**
     * Generates Waypoint for Mecanum drive constraints: velocity and acceleration vectors can
     * be in any direction.
     */
    public Waypoint(double x, double y, double theta, double vx, double vy, double ax, double ay, double time) {
        assert (Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2)) != 0) : "Waypoint Velocity is zero";
        this.x = x;
        this.y = y;
        this.theta = theta;
        this.vx = vx;
        this.vy = vy;
        this.ax = ax;
        this.ay = ay;
        this.time = time;
    }
}
