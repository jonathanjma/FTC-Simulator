package Utilities;

import javafx.scene.shape.Polygon;

import java.util.ArrayList;

public class Intersect {

    public static boolean isPolygonsIntersecting(Polygon a, Polygon b) {
        for (int x = 0; x < 2; x++) {
            Polygon polygon = (x == 0) ? a : b;

            ArrayList<Point> points = parsePolygon(polygon);
            for (int i1 = 0; i1 < points.size(); i1++) {
                int i2 = (i1 + 1) % points.size();
                Point p1 = points.get(i1);
                Point p2 = points.get(i2);

                Point normal = new Point(p2.y - p1.y, p1.x - p2.x);

                double minA = Double.POSITIVE_INFINITY;
                double maxA = Double.NEGATIVE_INFINITY;

                ArrayList<Point> aPoints = parsePolygon(a);
                for (Point p : aPoints) {
                    double projected = normal.x * p.x + normal.y * p.y;

                    if (projected < minA) {
                        minA = projected;
                    }
                    if (projected > maxA) {
                        maxA = projected;
                    }
                }

                double minB = Double.POSITIVE_INFINITY;
                double maxB = Double.NEGATIVE_INFINITY;

                ArrayList<Point> bPoints = parsePolygon(b);
                for (Point p : bPoints) {
                    double projected = normal.x * p.x + normal.y * p.y;

                    if (projected < minB) {
                        minB = projected;
                    }
                    if (projected > maxB) {
                        maxB = projected;
                    }
                }

                if (maxA < minB || maxB < minA) {
                    return false;
                }
            }
        }

        return true;
    }

    public static ArrayList<Point> parsePolygon(Polygon py) {
        ArrayList<Point> list = new ArrayList<>();

        for (int i = 0; i < py.getPoints().size(); i+=2) {
            Point pt = new Point(py.getPoints().get(i), py.getPoints().get(i+1));
            list.add(pt);
        }
        return list;
    }
}
