package Utilities;

public class InchToPixelUtil {

    // 36 tiles, each tile 2ft x 2ft, field length/height = 6 tiles, side length = 12ft || 144in
    // field- 600x600, every 50 pixels = 1 ft, every 4.16 pixels ~1 in
    public final static double inchToPixel = 4.16;
    public final static double robotLength = 18 * inchToPixel;
    public final static double robotRadius = robotLength / 2;

    public static double getXPixel(double x) {
        return  x * inchToPixel;
    }

    public static double getYPixel(double y) {
        return (144 - y) * inchToPixel;
    }

    public static double getTheta(double th) {
        th = -((th * 180/Math.PI) + 0.5);
        if (th > 360) {th %= 360;}
        return th;
    }
}
