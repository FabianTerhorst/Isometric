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

        Path[] paths = new Path[6];

        /* Squares parallel to the x-axis */
        Path face1 = new Path(new Point[]{
                origin,
                new Point(origin.getX() + dx, origin.getY(), origin.getZ()),
                new Point(origin.getX() + dx, origin.getY(), origin.getZ() + dz),
                new Point(origin.getX(), origin.getY(), origin.getZ() + dz)
        });

        /* Push this face and its opposite */
        paths[0] = face1;
        paths[1] = face1.reverse().translatePoints(0, dy, 0);

        /* Square parallel to the y-axis */
        Path face2 = new Path(new Point[]{
                origin,
                new Point(origin.getX(), origin.getY(), origin.getZ() + dz),
                new Point(origin.getX(), origin.getY() + dy, origin.getZ() + dz),
                new Point(origin.getX(), origin.getY() + dy, origin.getZ())
        });
        paths[2] = face2;
        paths[3] = face2.reverse().translatePoints(dx, 0, 0);

        /* Square parallel to the xy-plane */
        Path face3 = new Path(new Point[]{
                origin,
                new Point(origin.getX() + dx, origin.getY(), origin.getZ()),
                new Point(origin.getX() + dx, origin.getY() + dy, origin.getZ()),
                new Point(origin.getX(), origin.getY() + dy, origin.getZ())
        });
        /* This surface is oriented backwards, so we need to reverse the points */
        paths[4] = face3.reverse();
        paths[5] = face3.translatePoints(0, 0, dz);

        setPaths(paths);
    }
}
