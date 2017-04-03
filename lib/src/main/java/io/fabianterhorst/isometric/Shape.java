package io.fabianterhorst.isometric;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Shape {

    private List<Path> paths;

    public Shape() {
        this.paths = new ArrayList<>();
    }

    public Shape(List<Path> paths) {
        this.paths = paths;
    }

    public void push(Path path) {
        this.paths.add(path);
    }

    public Shape translate(double dx, double dy, double dz) {
        List<Path> paths = new ArrayList<>();
        for (Path path : this.paths) {
            paths.add(path.translate(dx, dy, dz));
        }
        return new Shape(paths);
    }

    public Shape rotateX(Point origin, double angle) {
        List<Path> paths = new ArrayList<>();
        for (Path path : this.paths) {
            paths.add(path.rotateX(origin, angle));
        }
        return new Shape(paths);
    }

    public Shape rotateY(Point origin, double angle) {
        List<Path> paths = new ArrayList<>();
        for (Path path : this.paths) {
            paths.add(path.rotateY(origin, angle));
        }
        return new Shape(paths);
    }

    public Shape rotateZ(Point origin, double angle) {
        List<Path> paths = new ArrayList<>();
        for (Path path : this.paths) {
            paths.add(path.rotateZ(origin, angle));
        }
        return new Shape(paths);
    }

    public Shape scale(Point origin, Double dx, Double dy, Double dz) {
        List<Path> paths = new ArrayList<>();
        for (Path path : this.paths) {
            paths.add(path.scale(origin, dx, dy, dz));
        }
        return new Shape(paths);
    }

    public void scalePaths(Point origin, double dx, double dy, double dz) {
        for (int i = 0, length = paths.size();i < length;i++) {
            paths.set(i, paths.get(i).scale(origin, dx, dy, dz));
        }
    }

    /**
     * Sort the list of faces by distance then map the entries, returning
     * only the path and not the added "further point" from earlier.
     */
    public List<Path> orderedPaths() {
        Collections.sort(this.paths, new Comparator<Path>() {
            @Override
            public int compare(Path pathA, Path pathB) {
                return Double.compare(pathB.depth(), pathA.depth());
            }
        });
        return this.paths;
    }

    public static Shape extrude(Path path, @Nullable Double height) {
        height = height != null ? height : 1;

        Path topPath = path.translate(0, 0, height);
        int i;
        int length = path.points.size();
        Shape shape = new Shape();

        /* Push the top and bottom faces, top face must be oriented correctly */
        shape.push(path.reverse());
        shape.push(topPath);

        /* Push each side face */
        for (i = 0; i < length; i++) {
            List<Point> points = new ArrayList<>();
            points.add(topPath.points.get(i));
            points.add(path.points.get(i));
            points.add(path.points.get((i + 1) % length));
            points.add(topPath.points.get((i + 1) % length));
            shape.push(new Path(points));
        }

        return shape;
    }
}