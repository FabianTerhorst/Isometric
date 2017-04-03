package io.fabianterhorst.isometric.shapes;

import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;

/**
 * Created by fabianterhorst on 02.04.17.
 */

public class Octahedron extends Shape {

    public Octahedron(Point origin) {
        super();
        Point center = origin.translate(0.5, 0.5, 0.5);
        Path upperTriangle = new Path(new Point[]{origin.translate(0, 0, 0.5), origin.translate(0.5, 0.5, 1), origin.translate(0, 1, 0.5)});
        Path lowerTriangle = new Path(new Point[]{origin.translate(0, 0, 0.5), origin.translate(0, 1, 0.5), origin.translate(0.5, 0.5, 0)});
        for (int i = 0; i < 4; i++) {
            push(upperTriangle.rotateZ(center, i * Math.PI / 2.0));
            push(lowerTriangle.rotateZ(center, i * Math.PI / 2.0));
        }
        scalePaths(center, Math.sqrt(2) / 2.0, Math.sqrt(2) / 2.0, 1.0);
    }
}
