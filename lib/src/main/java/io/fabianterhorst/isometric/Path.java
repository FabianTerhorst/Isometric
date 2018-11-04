package io.fabianterhorst.isometric;

import java.util.Arrays;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Path {

    Point[] points;

    public Path() {
    }

    public Path(Point[] points) {
        this.points = points;
    }

    public void push(Point point) {
        if (points == null) {
            points = new Point[0];
        }
        points = add(point, points);
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }

    public Point[] getPoints() {
        return points;
    }

    public static Point[] add(Point point, Point[] values) {
        Point[] anotherArray = new Point[values.length + 1];
        System.arraycopy(values, 0, anotherArray, 0, values.length);
        anotherArray[values.length] = point;
        return anotherArray;
    }

    private static Point[] concat(Point[] a, Point[] b) {
        Point[] c = new Point[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    private static Point[] reverseArray(Point[] array) {
        if (array.length == 0) {
            return new Point[]{};
        } else if (array.length == 1) {
            return array;
        } else {
            // recursion: concatenate the reverse of the end of the array
            // to the first element (put at the end)
            return concat(
                    reverseArray(Arrays.copyOfRange(array, 1, array.length)),
                    new Point[]{array[0]}
            );
        }
    }

    /**
     * Returns a new path with the points in reverse order
     */
    public Path reverse() {
        return new Path(reverseArray(this.points));
    }

    public Path translate(double dx, double dy, double dz) {
        Point[] points = new Point[this.points.length];
        Point point;
        for (int i = 0; i < this.points.length; i++) {
            point = this.points[i];
            points[i] = point.translate(dx, dy, dz);
        }
        return new Path(points);
    }

    public Path rotateX(Point origin, double angle) {
        Point[] points = new Point[this.points.length];
        Point point;
        for (int i = 0; i < this.points.length; i++) {
            point = this.points[i];
            points[i] = point.rotateX(origin, angle);
        }
        return new Path(points);
    }

    public Path rotateY(Point origin, double angle) {
        Point[] points = new Point[this.points.length];
        Point point;
        for (int i = 0; i < this.points.length; i++) {
            point = this.points[i];
            points[i] = point.rotateY(origin, angle);
        }
        return new Path(points);
    }

    public Path rotateZ(Point origin, double angle) {
        Point[] points = new Point[this.points.length];
        Point point;
        for (int i = 0; i < this.points.length; i++) {
            point = this.points[i];
            points[i] = point.rotateZ(origin, angle);
        }
        return new Path(points);
    }

    public Path scale(Point origin, double dx, double dy, double dz) {
        Point[] points = new Point[this.points.length];
        Point point;
        for (int i = 0; i < this.points.length; i++) {
            point = this.points[i];
            points[i] = point.scale(origin, dx, dy, dz);
        }
        return new Path(points);
    }

    public Path scale(Point origin, double dx, double dy) {
        Point[] points = new Point[this.points.length];
        Point point;
        for (int i = 0; i < this.points.length; i++) {
            point = this.points[i];
            points[i] = point.scale(origin, dx, dy);
        }
        return new Path(points);
    }

    public Path scale(Point origin, double dx) {
        Point[] points = new Point[this.points.length];
        Point point;
        for (int i = 0; i < this.points.length; i++) {
            point = this.points[i];
            points[i] = point.scale(origin, dx);
        }
        return new Path(points);
    }

    public Path translatePoints(double dx, double dy, double dz) {
        Point point;
        for (int i = 0; i < this.points.length; i++) {
            point = this.points[i];
            points[i] = point.translate(dx, dy, dz);
        }
        return this;
    }

    public double depth() {
        int i;
        double total = 0;
        int length = this.points.length;
        for (i = 0; i < length; i++) {
            total += this.points[i].depth();
        }
        if (length == 0) {
            length = 1;
        }
        return total / length;
    }

    /**
     * If pathB ("this") is closer from the observer than pathA, it must be drawn after.
     * It is closer if one of its vertices and the observer are on the same side of the plane defined by pathA.
     */
    public int closerThan(Path pathA, Point observer, double[] v1, double[] v2, double[] v3, double[] v4, double[] v5, double[] v6) {
        return pathA.countCloserThan(v1, v2, v3, this, observer) - this.countCloserThan(v4, v5, v6, pathA, observer);
    }

    public int countCloserThan(double[] v1, double[] v2, double[] v3, Path pathA, Point observer) {
        // the plane containing pathA is defined by the three points A, B, C
        Vector3.fromTwoPoints(v1, pathA.points[0], pathA.points[1]);
        Vector3.fromTwoPoints(v2, pathA.points[0], pathA.points[2]);
        Vector3.crossProduct(v1, v2); // v1 is now cross product
        Vector3.fromTwoPoints(v2, Point.ORIGIN, pathA.points[0]);
        Vector3.fromTwoPoints(v3, Point.ORIGIN, observer);

        // Plane defined by pathA such as ax + by + zc = d
        // Here d = nx*x + ny*y + nz*z = n.OA
        double d = Vector3.dotProduct(v1, v2);
        double observerPosition = Vector3.dotProduct(v1, v3) - d;
        int result = 0;
        int result0 = 0;
        int length = this.points.length;
        for (int i = 0; i < length; i++) {
            Vector3.fromTwoPoints(v3, Point.ORIGIN, this.points[i]);
            double pPosition = Vector3.dotProduct(v1, v3) - d;
            if (observerPosition * pPosition >= 0.000000001) { //careful with rounding approximations
                result++;
            }
            if (observerPosition * pPosition >= -0.000000001 && observerPosition * pPosition < 0.000000001) {
                result0++;
            }
        }

        if (result == 0) {
            return 0;
        } else {
            return ((result + result0) / length);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Arrays.equals(points, path.points);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(points);
    }
}
