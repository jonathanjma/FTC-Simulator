package Old;

import static Utilities.ConversionUtil.getXPixel;

@Deprecated
public class Robot {

    private double newX, newY, angle, velocity, angleVelocity, distanceFactor;
    private double lastTime = (double) System.currentTimeMillis() / 1000.0;

    /**
     * Creates a new robot
     * @param stX starting x coordinate
     * @param stY starting y coordinate
     * @param a starting angle in degrees
     * @param v velocity in inches/second
     * @param av angular velocity in degrees/second
     */
    public Robot(double stX, double stY, double a, double v, double av) {
        angle = a; velocity = v; angleVelocity = av;
        newX = stX; newY = stY;
        distanceFactor = 0;
    }

    /**
     * Based on the robot's initialized values, this function calculates its next position
     * @return the robot's next x/y coordinate and angle
     */
    public Double[] calcNextPoint() {
        double time = (double) System.currentTimeMillis() / 1000.0;
        double timeDiff = time - lastTime; //without, movement too fast

        distanceFactor = getXPixel(velocity * timeDiff); //without, movement too slow
        angle += angleVelocity * timeDiff;

        if (angle >= 360) angle -= 360;

        newX += Math.cos(Math.toRadians(angle)) * distanceFactor;
        newY += Math.sin(Math.toRadians(angle)) * distanceFactor;

        lastTime = time;

        return new Double[]{newX, newY, angle};
    }
}