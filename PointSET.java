import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    private final SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Error");
        points.add(p);
    }

    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    public void draw() {
        for (Point2D p : points) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Error");
        SET<Point2D> containedInRect = new SET<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                containedInRect.add(p);
            }
        }
        return containedInRect;
    }

    public Point2D nearest(Point2D point) {
        if (point == null) throw new IllegalArgumentException("Error");
        if (points.isEmpty()) return null;
        Point2D smallestPoint = null;
        for (Point2D p : points) {
            if (smallestPoint == null) {
                smallestPoint = p;
            }
            if (p.distanceSquaredTo(point) < smallestPoint.distanceSquaredTo(point)) {
                smallestPoint = p;
            }
        }
        return smallestPoint;
    }

    public static void main(String[] args) {
        // unit testing
    }
}
