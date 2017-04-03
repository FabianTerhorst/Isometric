package io.fabianterhorst.isometric.shapes;

import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;

/**
 * Created by fabianterhorst on 02.04.17.
 */

public class Stairs extends Shape {

    public Stairs(Point origin, double stepCount) {
        Path zigzag = new Path();
        zigzag.push(origin);
        int i;
        for (i = 0; i < stepCount; i++) {
            Point stepCorner = origin.translate(0, i / stepCount, (i + 1) / stepCount);
            push(new Path(new Point[]{stepCorner, stepCorner.translate(0, 0, -1 / stepCount), stepCorner.translate(1, 0, -1 / stepCount), stepCorner.translate(1, 0, 0)}));
            push(new Path(new Point[]{stepCorner, stepCorner.translate(1, 0, 0), stepCorner.translate(1, 1 / stepCount, 0), stepCorner.translate(0, 1 / stepCount, 0)}));
            zigzag.push(stepCorner);
            zigzag.push(stepCorner.translate(0, 1 / stepCount, 0));
        }
        zigzag.push(origin.translate(0, 1, 0));
        push(zigzag);
        push(zigzag.reverse().translate(1, 0, 0));
    }
}
