package io.fabianterhorst.isometric.shapes;

import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;
import io.fabianterhorst.isometric.paths.Circle;

/**
 * Created by fabianterhorst on 01.04.17.
 */

public class Cylinder extends Shape {

    public Cylinder(Point origin, double vertices, double height) {
        this(origin, 1, vertices, height);
    }

    public Cylinder(Point origin, double radius, double vertices, double height) {
        super();
        Circle circle = new Circle(origin, radius, vertices);
        extrude(this, circle, height);
    }
}
