package io.fabianterhorst.isometric.shapes;

import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;

/**
 * Created by fabianterhorst on 02.04.17.
 */

public class Pyramid extends Shape {

    public Pyramid(Point origin) {
        this(origin, 1, 1, 1);
    }

    public Pyramid(Point origin, double dx, double dy, double dz) {
        super();

        Path[] paths = new Path[4];

        /* Path parallel to the x-axis */
        Path face1 = new Path(new Point[]{
                origin,
                new Point(origin.getX() + dx, origin.getY(), origin.getZ()),
                new Point(origin.getX() + dx / 2.0, origin.getY() + dy / 2.0, origin.getZ() + dz)
        });
        /* Push the face, and its opposite face, by rotating around the Z-axis */
        paths[0] = face1;
        paths[1] = face1.rotateZ(origin.translate(dx / 2.0, dy / 2.0, 0), Math.PI);

        /* Path parallel to the y-axis */
        Path face2 = new Path(new Point[]{
                origin,
                new Point(origin.getX() + dx / 2, origin.getY() + dy / 2, origin.getZ() + dz),
                new Point(origin.getX(), origin.getY() + dy, origin.getZ())
        });
        paths[2] = face2;
        paths[3] = face2.rotateZ(origin.translate(dx / 2.0, dy / 2.0, 0), Math.PI);
        setPaths(paths);
    }
}
