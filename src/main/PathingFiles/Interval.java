package main.PathingFiles;

public class Interval {
    public final double start, end;
    public Spline thetaSpline = null;
    public Path thetaPath = null;

    public Interval(double start, double end, Spline thetaSpline) {
        this.start = start;
        this.end = end;
        this.thetaSpline = thetaSpline;
    }

    public Interval(double start, double end, Path thetaPath) {
        this.start = start;
        this.end = end;
        this.thetaPath = thetaPath;
    }
}
