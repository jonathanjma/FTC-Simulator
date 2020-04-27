package Utilities;

import App.CombinedApp;

public class ConversionUtil {

    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    public final static double inchToPixel = 4.16;
    public final static double robotLength = 18 * inchToPixel;
    public final static double robotRadius = robotLength / 2;

    // xcor inch to pixel
    public static double getXPixel(double xInch) {
        return  xInch * inchToPixel;
    }

    // ycor inch to pixel
    public static double getYPixel(double yInch) {
        return (144 - yInch) * inchToPixel;
    }

    // xcor pixel to inch
    public static double getXInch(double xPixel) {
        return xPixel / inchToPixel;
    }

    // ycor pixel to inch
    public static double getYInch(double yPixel) {
        return (CombinedApp.sceneWidth - yPixel) / inchToPixel;
    }

    // radians to degrees
    public static double getFXTheta(double thetaRad) {
        double thetaDeg = -(thetaRad * 180/Math.PI);
        if (thetaDeg > 360) {thetaDeg %= 360;}
        return thetaDeg;
    }

    // radians (pi implied) to degrees
    public static double getFXTheta_NoPi(double thetaRad_NoPi) {
        double thetaDeg = -((thetaRad_NoPi * 180) + 0.5);
        if (thetaDeg > 360) {thetaDeg %= 360;}
        return thetaDeg;
    }
}
