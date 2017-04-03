package io.fabianterhorst.isometric.shapes;

import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;

/**
 * Created by fabianterhorst on 01.04.17.
 */

public class Prism extends Shape {

    public Prism(Point origin) {
        this(origin, 1, 1, 1);
    }

    public Prism(Point origin, double dx, double dy, double dz) {
        super();

        /* Squares parallel to the x-axis */
        Path face1 = new Path(new Point[]{
                origin,
                new Point(origin.getX() + dx, origin.getY(), origin.getZ()),
                new Point(origin.getX() + dx, origin.getY(), origin.getZ() + dz),
                new Point(origin.getX(), origin.getY(), origin.getZ() + dz)
        });

        /* Push this face and its opposite */
        push(face1);
        push(face1.reverse().translate(0, dy, 0));

        /* Square parallel to the y-axis */
        Path face2 = new Path(new Point[]{
                origin,
                new Point(origin.getX(), origin.getY(), origin.getZ() + dz),
                new Point(origin.getX(), origin.getY() + dy, origin.getZ() + dz),
                new Point(origin.getX(), origin.getY() + dy, origin.getZ())
        });
        push(face2);
        push(face2.reverse().translate(dx, 0, 0));

        /* Square parallel to the xy-plane */
        Path face3 = new Path(new Point[]{
                origin,
                new Point(origin.getX() + dx, origin.getY(), origin.getZ()),
                new Point(origin.getX() + dx, origin.getY() + dy, origin.getZ()),
                new Point(origin.getX(), origin.getY() + dy, origin.getZ())
        });
        /* This surface is oriented backwards, so we need to reverse the points */
        push(face3.reverse());
        push(face3.translate(0, 0, dz));
    }
}
