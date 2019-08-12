public class Robot {

    private double newX, newY, angle, velocity, angleVelocity, distance, t;
    private double lastTime = (double) System.currentTimeMillis() / 1000.0;

    // use inches velocity, coordinates
    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 0.35 pixels = 1 in

    /**
     * Creates a new robot
     * @param stX starting x coordinate
     * @param stY starting y coordinate
     * @param a starting angle
     * @param v velocity
     * @param av angular velocity
     */
    public Robot(double stX, double stY, double a, double v, double av) {
        angle = a; velocity = v; angleVelocity = av;
        newX = stX; newY = stY;
        distance = 0;
    }

    /**
     * Based on the robot's initialized values, this calculates its next position
     * @return the robot's next x/y coordinates and angle
     */
    public Double[] calcNextPoint() {
        double time = (double) System.currentTimeMillis() / 1000.0;
        double timeDiff = time - lastTime;

        distance = velocity * timeDiff;
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