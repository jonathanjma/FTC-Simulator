package main.PathingFiles;

public class Interval {
    public final double start, end;
    public final boolean addPi;

    public Interval(double start, double end) {
        this(start, end, false);
    }

    public Interval(double start, double end, boolean addPi) {
        this.start = start;
        this.end = end;
        this.addPi = addPi;
    }
}
