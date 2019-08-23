public class Robot {

    private double newX, newY, angle, velocity, angleVelocity, distance;
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
        distance = 0;
    }

    /**
     * Based on the robot's initialized values, this function calculates its next position
     * @return the robot's next x/y coordinate and angle
     */
    public Double[] calcNextPoint() {
        double time = (double) System.currentTimeMillis() / 1000.0;
        double timeDiff = time - lastTime;

        distance = velocity * timeDiff * Simulator.pixelToInch;
        angle += angleVelocity * timeDiff;

        if (angle >= 360) angle -= 360;

        newX += Math.cos(Math.toRadians(angle)) * distance;
        newY += Math.sin(Math.toRadians(angle)) * distance;

        /* for error checking
        System.out.println(Double.parseDouble(String.format("%.1f", distance)) + " " + angle + " "
                + Double.parseDouble(String.format("%.3f", newX)) + ", " + Double.parseDouble(String.format("%.3f", newY)));
        double pythagoreanLeft = Math.pow(newX - stX, 2) + Math.pow(newY - stY, 2);
        if (!(Math.abs(pythagoreanLeft - Math.pow(distance, 2)) < 0.1)) {
            System.out.println("error");
            System.exit(-1);
        }*/

        lastTime = time;

        return new Double[]{newX, newY, angle};
    }
}