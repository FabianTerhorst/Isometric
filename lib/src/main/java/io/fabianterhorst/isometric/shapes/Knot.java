package io.fabianterhorst.isometric.shapes;

import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;

/**
 * Created by fabianterhorst on 03.04.17.
 */

//Todo: needs an depth fix
//Todo: optimize push
public class Knot extends Shape {

    public Knot(Point origin) {
        push(new Prism(Point.ORIGIN, 5, 1, 1).getPaths());
        push(new Prism(new Point(4, 1, 0), 1, 4, 1).getPaths());
        push(new Prism(new Point(4, 4, -2), 1, 1, 3).getPaths());
        push(new Path(new Point[]{new Point(0, 0, 2), new Point(0, 0, 1), new Point(1, 0, 1), new Point(1, 0, 2)}));
        push(new Path(new Point[]{new Point(0, 0, 2), new Point(0, 1, 2), new Point(0, 1, 1), new Point(0, 0, 1)}));
        scalePaths(Point.ORIGIN, 1.0 / 5.0);
        translatePaths(-0.1, 0.15, 0.4);
        translatePaths(origin.getX(), origin.getY(), origin.getZ());
    }
}
