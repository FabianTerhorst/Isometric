package io.fabianterhorst.isometric;

import java.util.Arrays;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Shape {

    protected Path[] paths;

    public Shape() {
    }

    public Shape(Path[] paths) {
        this.paths = paths;
    }

    public void push(Path path) {
        if (paths == null) {
            paths = new Path[0];
        }
        paths = add(path, paths);
    }

    public void setPaths(Path[] paths) {
        this.paths = paths;
    }

    public static Path[] add(Path point, Path[] values) {
        Path[] anotherArray = new Path[values.length + 1];
        System.arraycopy(values, 0, anotherArray, 0, values.length);
        anotherArray[values.length] = point;
        return anotherArray;
    }

    public static Path[] concat(Path[] a, Path[] b) {
        Path[] c = new Path[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public void push(Path[] paths) {
        if (this.paths == null) {
            this.paths = new Path[0];
        }
        this.paths = concat(paths, this.paths);
    }

    public Path[] getPaths() {
        return paths;
    }

    public Shape translate(double dx, double dy, double dz) {
        Path[] paths = new Path[this.paths.length];
        Path point;
        for (int i = 0; i < this.paths.length; i++) {
            point = this.paths[i];
            paths[i] = point.translate(dx, dy, dz);
        }
        return new Shape(paths);
    }

    public Shape rotateX(Point origin, double angle) {
        Path[] paths = new Path[this.paths.length];
        Path point;
        for (int i = 0; i < this.paths.length; i++) {
            point = this.paths[i];
            paths[i] = point.rotateX(origin, angle);
        }
        return new Shape(paths);
    }

    public Shape rotateY(Point origin, double angle) {
        Path[] paths = new Path[this.paths.length];
        Path point;
        for (int i = 0; i < this.paths.length; i++) {
            point = this.paths[i];
            paths[i] = point.rotateY(origin, angle);
        }
        return new Shape(paths);
    }

    public Shape rotateZ(Point origin, double angle) {
        Path[] paths = new Path[this.paths.length];
        Path point;
        for (int i = 0; i < this.paths.length; i++) {
            point = this.paths[i];
            paths[i] = point.rotateZ(origin, angle);
        }
        return new Shape(paths);
    }

    public Shape scale(Point origin, double dx, double dy, double dz) {
        Path[] paths = new Path[this.paths.length];
        Path point;
        for (int i = 0; i < this.paths.length; i++) {
            point = this.paths[i];
            paths[i] = point.scale(origin, dx, dy, dz);
        }
        return new Shape(paths);
    }

    public Shape scale(Point origin, double dx, double dy) {
        Path[] paths = new Path[this.paths.length];
        Path point;
        for (int i = 0; i < this.paths.length; i++) {
            point = this.paths[i];
            paths[i] = point.scale(origin, dx, dy);
        }
        return new Shape(paths);
    }

    public Shape scale(Point origin, double dx) {
        Path[] paths = new Path[this.paths.length];
        Path point;
        for (int i = 0; i < this.paths.length; i++) {
            point = this.paths[i];
            paths[i] = point.scale(origin, dx);
        }
        return new Shape(paths);
    }

    public void scalePaths(Point origin, double dx, double dy, double dz) {
        for (int i = 0, length = paths.length; i < length; i++) {
            paths[i] = paths[i].scale(origin, dx, dy, dz);
        }
    }

    public void scalePaths(Point origin, double dx, double dy) {
        for (int i = 0, length = paths.length; i < length; i++) {
            paths[i] = paths[i].scale(origin, dx, dy);
        }
    }

    public void scalePaths(Point origin, double dx) {
        for (int i = 0, length = paths.length; i < length; i++) {
            paths[i] = paths[i].scale(origin, dx);
        }
    }

    public void translatePaths(double dx, double dy, double dz) {
        for (int i = 0, length = paths.length; i < length; i++) {
            paths[i] = paths[i].translate(dx, dy, dz);
        }
    }

    /**
     * Sort the list of faces by distance then map the entries, returning
     * only the path and not the added "further point" from earlier.
     */
    public Path[] orderedPaths() {
        double[] depths = new double[paths.length];
        for (int i = 0; i < depths.length; i++) {
            depths[i] = paths[i].depth();
        }
        boolean swapped = true;
        int j = 0;
        Path tmp;
        double tmp2;
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < paths.length - j; i++) {
                if (depths[i] < depths[i + 1]) {
                    tmp = paths[i];
                    tmp2 = depths[i];
                    paths[i] = paths[i + 1];
                    depths[i] = depths[i + 1];
                    paths[i + 1] = tmp;
                    depths[i + 1] = tmp2;
                    swapped = true;
                }
            }
        }
        return this.paths;
    }

    public static Shape extrude(Path path) {
        return extrude(new Shape(), path, 1);
    }

    public static Shape extrude(Path path, double height) {
        return extrude(new Shape(), path, height);
    }

    public static Shape extrude(Shape shape, Path path) {
        return extrude(shape, path, 1);
    }

    public static Shape extrude(Shape shape, Path path, double height) {
        Path topPath = path.translate(0, 0, height);
        int i;
        int length = path.points.length;

        Path[] paths = new Path[length + 2];

        /* Push the top and bottom faces, top face must be oriented correctly */
        paths[0] = path.reverse();
        paths[1] = topPath;

        /* Push each side face */
        Point[] points;
        for (i = 0; i < length; i++) {
            points = new Point[4];
            points[0] = topPath.points[i];
            points[1] = path.points[i];
            points[2] = path.points[(i + 1) % length];
            points[3] = topPath.points[(i + 1) % length];
            paths[i + 2] = new Path(points);
        }
        shape.setPaths(paths);
        return shape;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shape)) return false;
        Shape shape = (Shape) o;
        return Arrays.equals(paths, shape.paths);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(paths);
    }
}