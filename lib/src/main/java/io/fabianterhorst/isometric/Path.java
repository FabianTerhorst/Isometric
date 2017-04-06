package io.fabianterhorst.isometric;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Path {

    //Todo: replace with Point[]
    final List<Point> points;

    public Path() {
        this.points = new ArrayList<>();
    }

    public Path(List<Point> points) {
        this.points = points;
    }

    public Path(Point[] points) {
        this.points = Arrays.asList(points);
    }

    public void push(Point point) {
        this.points.add(point);
    }

    public List<Point> getPoints() {
        return points;
    }

    /**
     * Returns a new path with the points in reverse order
     */
    public Path reverse() {
        List<Point> points = new ArrayList<>();
        for (int i = this.points.size() - 1; i >= 0; i--) {
            points.add(this.points.get(i));
        }
        return new Path(points);
    }

    public Path translate(double dx, double dy, double dz) {
        List<Point> points = new ArrayList<>();
        for (Point point : this.points) {
            points.add(point.translate(dx, dy, dz));
        }
        return new Path(points);
    }

    public Path rotateX(Point origin, double angle) {
        List<Point> points = new ArrayList<>();
        for (Point point : this.points) {
            points.add(point.rotateX(origin, angle));
        }
        return new Path(points);
    }

    public Path rotateY(Point origin, double angle) {
        List<Point> points = new ArrayList<>();
        for (Point point : this.points) {
            points.add(point.rotateY(origin, angle));
        }
        return new Path(points);
    }

    public Path rotateZ(Point origin, double angle) {
        List<Point> points = new ArrayList<>();
        for (Point point : this.points) {
            points.add(point.rotateZ(origin, angle));
        }
        return new Path(points);
    }

    public Path scale(Point origin, double dx, double dy, double dz) {
        List<Point> points = new ArrayList<>();
        for (Point point : this.points) {
            points.add(point.scale(origin, dx, dy, dz));
        }
        return new Path(points);
    }

    public Path scale(Point origin, double dx, double dy) {
        List<Point> points = new ArrayList<>();
        for (Point point : this.points) {
            points.add(point.scale(origin, dx, dy));
        }
        return new Path(points);
    }

    public Path scale(Point origin, double dx) {
        List<Point> points = new ArrayList<>();
        for (Point point : this.points) {
            points.add(point.scale(origin, dx));
        }
        return new Path(points);
    }

    public double depth() {
        int i;
        double total = 0;
        int length = this.points.size();
        for (i = 0; i < length; i++) {
            total += this.points.get(i).depth();
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
    public int closerThan(Path pathA, Point observer) {
        return pathA.countCloserThan(this, observer) - this.countCloserThan(pathA, observer);
    }

    public int countCloserThan(Path pathA, Point observer) {
        // the plane containing pathA is defined by the three points A, B, C
        Vector AB = Vector.fromTwoPoints(pathA.points.get(0), pathA.points.get(1));
        Vector AC = Vector.fromTwoPoints(pathA.points.get(0), pathA.points.get(2));
        Vector n = Vector.crossProduct(AB, AC);

        Vector OA = Vector.fromTwoPoints(Point.ORIGIN, pathA.points.get(0));
        Vector OU = Vector.fromTwoPoints(Point.ORIGIN, observer); //U = user = observer

        // Plane defined by pathA such as ax + by + zc = d
        // Here d = nx*x + ny*y + nz*z = n.OA
        double d = Vector.dotProduct(n, OA);
        double observerPosition = Vector.dotProduct(n, OU) - d;
        int result = 0;
        int result0 = 0;
        int length = this.points.size();
        for (int i = 0; i < length; i++) {
            Vector OP = Vector.fromTwoPoints(Point.ORIGIN, this.points.get(i));
            double pPosition = Vector.dotProduct(n, OP) - d;
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
}
