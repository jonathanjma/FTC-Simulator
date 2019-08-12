import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Robot extends Pane {

    private Rectangle robotRect;
    private double x1, y1, a, v, av, d, t;
    private double stX, stY;

    private double timestep = 1;
    private double lastTime = (double)System.currentTimeMillis()/1000.0;
    private double x2, y2;

    // use inches velocity, coordinates
    // no timestep, use differences in system time

    public Robot(double x1, double y1, double a, double v, double av) {
        this.x1 = x1; this.y1 = y1; this.a = a; this.v = v; this.av = av;
        stX = x1; stY = y1;

        //timestep = 10 / av;
        x2 = x1;
        y2 = y1;

        robotRect = new Rectangle(x1, y1, 50, 50);
        robotRect.setRotate(a);
        getChildren().add(robotRect);
    }

    public Double[] calcNextPoint() {
        double time = (double) System.currentTimeMillis() / 1000.0;
        double diff = time - lastTime;

        d = v * diff;
        a += av * diff;

        if (a >= 360) a -= 360;

        x2 += Math.cos(Math.toRadians(a)) * d;
        y2 += Math.sin(Math.toRadians(a)) * d;

        /*System.out.println(Double.parseDouble(String.format("%.1f", d)) + " " + a + " "
                + Double.parseDouble(String.format("%.3f", x2)) + ", " + Double.parseDouble(String.format("%.3f", y2)));
        double pythagoreanLeft = Math.pow(x2 - stX, 2) + Math.pow(y2 - stY, 2);
        if (!(Math.abs(pythagoreanLeft - Math.pow(d, 2)) < 0.1)) {
            System.out.println("error");
            System.exit(-1);
        }*/

        Line p = new Line(x1, y1, x2, y2);
        x1 = x2; y1 = y2;

        getChildren().add(p);

        lastTime = time;

        return new Double[]{x2, y2, a};
    }

    public Rectangle getRobotRect() {
        return robotRect;
    }
}