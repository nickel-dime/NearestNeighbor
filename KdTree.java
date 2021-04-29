import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;

    private Node root;
    private int size;

    private static class Node {
        private final Point2D point;
        private Node left;
        private Node right;
        private final RectHV rect;

        public Node(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
        }
    }

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Error");
        if (contains(p)) {
            return;
        }
        size += 1;
        root = insert(root, p, VERTICAL, 0.0, 1.0, 0.0, 1.0);
    }

    private Node insert(Node x, Point2D p, boolean direction, double left, double right, double down, double up) {
        if (x == null) return new Node(p, new RectHV(left, down, right, up));
        if (direction == VERTICAL) {
            if (p.x() < x.point.x()) {
                x.left = insert(x.left, p, !direction, left, x.point.x(), down, up);
            } else {
                x.right = insert(x.right, p, !direction, x.point.x(), right, down, up);
            }
        } else {
            if (p.y() < x.point.y()) {
                x.left = insert(x.left, p, !direction, left, right, down, x.point.y());
            } else {
                x.right = insert(x.right, p, !direction, left, right, x.point.y(), up);
            }
        }
        return x;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Error");
        return contains(root, p, VERTICAL);
    }

    private boolean contains(Node x, Point2D p, boolean direction) {
        if (x == null) return false;
        if (p.compareTo(x.point) == 0) return true;
        if (direction == VERTICAL) {
            if (p.x() < x.point.x()) {
                return contains(x.left, p, !direction);
            } else {
                return contains(x.right, p, !direction);
            }
        } else {
            if (p.y() < x.point.y()) {
                return contains(x.left, p, !direction);
            } else {
                return contains(x.right, p, !direction);
            }
        }
    }

    public void draw() {
        draw(root, VERTICAL);
    }

    private void draw(Node x, boolean direction) {
        StdDraw.setPenRadius(0.005);
        if (x == null) return;
        if (direction == VERTICAL) {
            double bottomC = x.rect.ymin();
            double topC = x.rect.ymax();
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(x.point.x(), bottomC, x.point.x(), topC);
        } else {
            double leftC = x.rect.xmin();
            double rightC = x.rect.xmax();
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(leftC, x.point.y(), rightC, x.point.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.015);
        StdDraw.point(x.point.x(), x.point.y());
        draw(x.left, !direction);
        draw(x.right, !direction);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Error");
        Queue<Point2D> containedInRect = new Queue<>();
        return range(root, rect, containedInRect);
    }

    private Queue<Point2D> range(Node x, RectHV rect, Queue<Point2D> set) {
        if (x == null) return null;
        if (rect.contains(x.point)) {
            set.enqueue(x.point);
        }
        if (x.rect.intersects(rect)) {
            range(x.left, rect, set);
            range(x.right, rect, set);
        }
        return set;
    }

    public Point2D nearest(Point2D point) {
        if (point == null) throw new IllegalArgumentException("Error");
        if (isEmpty()) return null;
        return nearest(root, point, root.point, VERTICAL);
    }

    private Point2D nearest(Node x, Point2D point, Point2D close, boolean direction) {
        Point2D closest = close;
        if (x == null) return closest;
        if (x.point.distanceSquaredTo(point) < closest.distanceSquaredTo(point)) {
            closest = x.point;
        }
        if (x.rect.distanceSquaredTo(point) < closest.distanceSquaredTo(point)) {
            Node near;
            Node far;

            if (direction == VERTICAL) {
                if (point.x() < x.point.x()) {
                    near = x.left;
                    far = x.right;
                } else {
                    near = x.right;
                    far = x.left;
                }
            } else {
                if (point.y() < x.point.y()) {
                    near = x.left;
                    far = x.right;
                } else {
                    near = x.right;
                    far = x.left;
                }
            }

            closest = nearest(near, point, closest, !direction);
            closest = nearest(far, point, closest, !direction);

        }
        return closest;
    }

    public static void main(String[] args) {
        // unit testing
    }
}
