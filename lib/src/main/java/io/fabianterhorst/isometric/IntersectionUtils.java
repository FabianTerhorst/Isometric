package io.fabianterhorst.isometric;

import java.util.List;

public class IntersectionUtils {

    private IntersectionUtils() {

    }

    public static boolean isPointCloseToPoly(List<Point> poly, double x, double y, double radius) {

        Point p = new Point(x, y);

        //iterate over each line segment
        for (int i = 0, j = i + 1, len = poly.size(); i < len; i++) {
            //make j wrap around to front
            if (j == len)
                j = 0;

            Point v = poly.get(i);
            Point w = poly.get(j);

            double dist = Point.distancetoSegment(p, v, w);

            if (dist < radius) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPointInPoly(List<Point> poly, double x, double y) {
        boolean c = false;
        for (int i = -1, l = poly.size(), j = l - 1; ++i < l; j = i) {
            if (((poly.get(i).y <= y && y < poly.get(j).y) || (poly.get(j).y <= y && y < poly.get(i).y))
                    && (x < (poly.get(j).x - poly.get(i).x) * (y - poly.get(i).y) / (poly.get(j).y - poly.get(i).y) + poly.get(i).x)) {
                c = !c;
            }
        }
        return c;
    }

    private static boolean isPointInPolyOriginal(Point[] poly, double x, double y) {
        boolean c = false;
        for (int i = -1, l = poly.length, j = l - 1; ++i < l; j = i) {
            if (((poly[i].y <= y && y < poly[j].y) || (poly[j].y <= y && y < poly[i].y))
                    && (x < (poly[j].x - poly[i].x) * (y - poly[i].y) / (poly[j].y - poly[i].y) + poly[i].x)) {
                c = !c;
            }
        }
        return c;
    }

    private static boolean intersects(Point A, Point B, double PX, double PY) {
        if (A.y > B.y)
            return intersects(B, A, PX, PY);

        if (PY == A.y || PY == B.y)
            PY += 0.0001;

        if (PY > B.y || PY < A.y || PX >= Math.max(A.x, B.x))
            return false;

        if (PX < Math.min(A.x, B.x))
            return true;

        double red = (PY - A.y) / (PX - A.x);
        double blue = (B.y - A.y) / (B.x - A.x);
        return red >= blue;
    }

    private static boolean isPointInPolyRaycast(Point[] shape, double pntX, double pntY) {
        boolean inside = false;
        int len = shape.length;
        for (int i = 0; i < len; i++) {
            if (intersects(shape[i], shape[(i + 1) % len], pntX, pntY))
                inside = !inside;
        }
        return inside;
    }

    // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
    private static boolean isPointInPoly(Point[] polygon, double x, double y) {
        boolean inside = false;
        double xi, yi, xj, yj;
        for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            xi = polygon[i].x;
            yi = polygon[i].y;
            xj = polygon[j].x;
            yj = polygon[j].y;
            if (((yi > y) != (yj > y))
                    && (x < (xj - xi) * (y - yi) / (yj - yi) + xi)) inside = !inside;
        }
        return inside;
    }

    public static boolean hasIntersection(Point[] pointsA, Point[] pointsB, Point[] polyA, Point[] polyB,
                                          double[] deltaAX, double[] deltaBX, double[] deltaAY, double[] deltaBY,
                                          double[] rA, double[] rB) {
        int i, j, lengthA = pointsA.length, lengthB = pointsB.length, lengthPolyA, lengthPolyB;
        double AminX = pointsA[0].x;
        double AminY = pointsA[0].y;
        double AmaxX = AminX;
        double AmaxY = AminY;
        double BminX = pointsB[0].x;
        double BminY = pointsB[0].y;
        double BmaxX = BminX;
        double BmaxY = BminY;

        Point point;

        for (i = 0; i < lengthA; i++) {
            point = pointsA[i];
            AminX = Math.min(AminX, point.x);
            AminY = Math.min(AminY, point.y);
            AmaxX = Math.max(AmaxX, point.x);
            AmaxY = Math.max(AmaxY, point.y);
        }
        for (i = 0; i < lengthB; i++) {
            point = pointsB[i];
            BminX = Math.min(BminX, point.x);
            BminY = Math.min(BminY, point.y);
            BmaxX = Math.max(BmaxX, point.x);
            BmaxY = Math.max(BmaxY, point.y);
        }

        if (((AminX <= BminX && BminX <= AmaxX) || (BminX <= AminX && AminX <= BmaxX)) &&
                ((AminY <= BminY && BminY <= AmaxY) || (BminY <= AminY && AminY <= BmaxY))) {
            // now let's be more specific
            // see if edges cross, or one contained in the other
            lengthPolyA = polyA.length;
            lengthPolyB = polyB.length;

            for (i = 0; i <= lengthPolyA - 2; i++) {
                for (j = 0; j <= lengthPolyB - 2; j++) {
                    if (deltaAX[i] * deltaBY[j] != deltaAY[i] * deltaBX[j]) {
                        //case when vectors are colinear, or one polygon included in the other, is covered after
                        //two segments cross each other if and only if the points of the first are on each side of the line defined by the second and vice-versa
                        if ((deltaAY[i] * polyB[j].x - deltaAX[i] * polyB[j].y + rA[i]) * (deltaAY[i] * polyB[j + 1].x - deltaAX[i] * polyB[j + 1].y + rA[i]) < -0.000000001 &&
                                (deltaBY[j] * polyA[i].x - deltaBX[j] * polyA[i].y + rB[j]) * (deltaBY[j] * polyA[i + 1].x - deltaBX[j] * polyA[i + 1].y + rB[j]) < -0.000000001) {
                            return true;
                        }
                    }
                }
            }

            for (i = 0; i <= lengthPolyA - 2; i++) {
                point = polyA[i];
                if (isPointInPoly(polyB, point.x, point.y)) {
                    return true;
                }
            }
            for (i = 0; i <= lengthPolyB - 2; i++) {
                point = polyB[i];
                if (isPointInPoly(polyA, point.x, point.y)) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }
}
