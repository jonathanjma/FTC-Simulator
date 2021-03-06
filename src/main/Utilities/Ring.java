package main.Utilities;

import java.util.ArrayList;
import java.util.Collections;

public class Ring {
    private double absX, absY;

    public Ring(double absX, double absY) {
        this.absX = absX;
        this.absY = absY;
    }

    public static ArrayList<Ring> getRingCoords(ArrayList<Ring> rings, double robotX, double robotY) {

        rings.sort((r1, r2) -> Double.compare(r1.getAbsDist(robotX, robotY), r2.getAbsDist(robotX, robotY)));
        if (rings.size() > 3) {
            rings = new ArrayList<>(rings.subList(0, 3));
        }

        if (rings.size() == 3) {
            Ring closest = rings.get(0);
            if (rings.get(1).getAbsDist(closest.absX, closest.absY) > rings.get(2).getAbsDist(closest.absX, closest.absY)) {
                Collections.swap(rings, 1, 2);
            }
        }

        return rings;
    }

    public double[] driveToRing(double robotX, double robotY) {
        return new double[] {absX, absY, Math.atan2(absY - robotY, absX - robotX)};
    }

    public double getAbsDist(double robotX, double robotY) {
        return Math.hypot(absX - robotX, absY - robotY);
    }

    public double getX() {
        return absX;
    }
    public double getY() {
        return absY;
    }
    public double[] getAbsCoords() {
        return new double[] {absX, absY};
    }
}