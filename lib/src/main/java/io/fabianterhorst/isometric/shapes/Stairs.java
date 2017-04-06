package io.fabianterhorst.isometric.shapes;

import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;

/**
 * Created by fabianterhorst on 02.04.17.
 */

public class Stairs extends Shape {

    public Stairs(Point origin, double stepCount) {
        Path[] paths = new Path[(int) stepCount * 2 + 2];
        Path zigzag = new Path();
        Point[] points = new Point[(int) stepCount * 2 + 2];
        points[0] = origin;
        int i, count = 1;
        for (i = 0; i < stepCount; i++) {
            Point stepCorner = origin.translate(0, i / stepCount, (i + 1) / stepCount);
            paths[count - 1] = new Path(new Point[]{stepCorner, stepCorner.translate(0, 0, -1 / stepCount), stepCorner.translate(1, 0, -1 / stepCount), stepCorner.translate(1, 0, 0)});
            points[count++] = stepCorner;
            paths[count - 1] = new Path(new Point[]{stepCorner, stepCorner.translate(1, 0, 0), stepCorner.translate(1, 1 / stepCount, 0), stepCorner.translate(0, 1 / stepCount, 0)});
            points[count++] = stepCorner.translate(0, 1 / stepCount, 0);
        }
        points[count] = origin.translate(0, 1, 0);
        zigzag.setPoints(points);
        paths[count++ -1] = zigzag;
        paths[count -1] = zigzag.reverse().translatePoints(1, 0, 0);
        setPaths(paths);
    }
}
