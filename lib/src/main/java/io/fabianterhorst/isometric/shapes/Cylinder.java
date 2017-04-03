package io.fabianterhorst.isometric.shapes;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;
import io.fabianterhorst.isometric.paths.Circle;

/**
 * Created by fabianterhorst on 01.04.17.
 */

public class Cylinder extends Shape {

    public Cylinder(Point origin, Double radius, double vertices, double height) {
        super();
        radius = radius != null ? radius : 1;
        Circle circle = new Circle(origin, radius, vertices);
        extrudePath(circle, height);
    }

    //Todo: reuse same method from Shape.extrude
    private void extrudePath(Path path, @Nullable Double height) {
        height = height != null ? height : 1;

        Path topPath = path.translate(0, 0, height);
        int i;
        int length = path.getPoints().size();

        /* Push the top and bottom faces, top face must be oriented correctly */
        push(path.reverse());
        push(topPath);

        /* Push each side face */
        for (i = 0; i < length; i++) {
            List<Point> points = new ArrayList<>();
            points.add(topPath.getPoints().get(i));
            points.add(path.getPoints().get(i));
            points.add(path.getPoints().get((i + 1) % length));
            points.add(topPath.getPoints().get((i + 1) % length));
            push(new Path(points));
        }
    }
}
