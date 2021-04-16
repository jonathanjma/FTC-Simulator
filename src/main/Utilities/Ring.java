package main.Utilities;

import java.util.ArrayList;
import java.util.Comparator;

public class Ring {
    private double absX, absY;

    public Ring(double absX, double absY) {
        this.absX = absX;
        this.absY = absY;
    }

    /////////////////////
    public int id;
    public Ring(double absX, double absY, int id) {
        this.absX = absX;
        this.absY = absY;
        this.id = id;
    }
    public void setPos(double x, double y) {
        absX = x; absY = y;
    }
    /////////////////////

    public static ArrayList<Ring> getRingCoords(ArrayList<Ring> rings, double robotX, double robotY) {
        // Sort rings based on y coordinate
        rings.sort(Comparator.comparingDouble(r -> r.getY()));

        // Return up to three rings
        if (rings.size() > 3) {
            rings = new ArrayList<>(rings.subList(0, 3));
        }

        // Determine left or right sweep
        if (rings.size() > 0) {
            if (rings.get(rings.size() - 1).getY() - rings.get(0).getY() > 8) {
                Ring closest = rings.remove(0);
                if (closest.getX() <= robotX + 9) {
                    rings.sort(Comparator.comparingDouble(r -> r.getX()));
                } else {
                    rings.sort(Comparator.comparingDouble(r -> -r.getX()));
                }
                rings.add(0, closest);
            } else {
                rings.sort(Comparator.comparingDouble(r -> r.getX()));
            }
        }

        return rings;
    }

    public static boolean isLowestX(ArrayList<Ring> rings, Ring ring) {
        double low = rings.get(0).getX();
        for (Ring r : rings) {
            if (r.getX() < low) {
                low = r.getX();
            }
        }
        return low == ring.getX();
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

    @Override
    public String toString() {
        return "Ring[" + absX + ", " + absY + ']';
    }
}