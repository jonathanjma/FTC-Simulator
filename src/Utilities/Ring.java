package Utilities;

public class Ring {
    private double absX, absY;

    public Ring(double absX, double absY) {
        this.absX = absX;
        this.absY = absY;
    }

    public double[] driveToRing(double robotX, double robotY) {
        return new double[] {absX, absY, Math.atan2(absY - robotY, absX - robotX)};
    }

    public double getX() {
        return absX;
    }
    public double getY() {
        return absY;
    }
}