package io.fabianterhorst.isometric;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabianterhorst on 31.03.17.
 */

//Todo: implement item ids to reuse objects for animations
public class Isometric {

    private final double angle, scale;

    private double[][] transformation;

    private double originX, originY;

    private List<Item> items = new ArrayList<>();

    private final Vector lightAngle;

    private final double colorDifference;

    private final Color lightColor;

    public Isometric() {
        this.angle = Math.PI / 6;
        this.scale = 70;
        this.transformation = new double[][]{
                {
                        this.scale * Math.cos(this.angle),
                        this.scale * Math.sin(this.angle)
                },
                {
                        this.scale * Math.cos(Math.PI - this.angle),
                        this.scale * Math.sin(Math.PI - this.angle)}};
        Vector lightPosition = new Vector(2, -1, 3);
        this.lightAngle = lightPosition.normalize();
        this.colorDifference = 0.20;
        this.lightColor = new Color(255, 255, 255);

    }

    /**
     * X rides along the angle extended from the origin
     * Y rides perpendicular to this angle (in isometric view: PI - angle)
     * Z affects the y coordinate of the drawn point
     */
    //Todo: use less object creation and maybe reuse the point
    public Point translatePoint(Point point) {
        Point xMap = new Point(point.x * this.transformation[0][0],
                point.x * this.transformation[0][1]);

        Point yMap = new Point(point.y * this.transformation[1][0],
                point.y * this.transformation[1][1]);

        double x = this.originX + xMap.x + yMap.x;
        double y = this.originY - xMap.y - yMap.y - (point.z * this.scale);
        return new Point(x, y);
    }

    public void add(Path path, Color color) {
        addPath(path, color);
    }

    public void add(List<Path> paths, Color color) {
        for (Path path : paths) {
            add(path, color);
        }
    }

    public void add(Shape shape, Color color) {
        /* Fetch paths ordered by distance to prevent overlaps */
        List<Path> paths = shape.orderedPaths();

        for (int j = 0; j < paths.size(); j++) {
            addPath(paths.get(j), color);
        }
    }

    public void clear() {
        items.clear();
    }

    private void addPath(Path path, Color color) {
        /* Compute color */
        Vector v1 = Vector.fromTwoPoints(path.points.get(1), path.points.get(0));
        Vector v2 = Vector.fromTwoPoints(path.points.get(2), path.points.get(1));

        Vector normal = Vector.crossProduct(v1, v2).normalize();

        /**
         * Brightness is between -1 and 1 and is computed based
         * on the dot product between the light source vector and normal.
         */
        double brightness = Vector.dotProduct(normal, this.lightAngle);
        color = color.lighten(brightness * this.colorDifference, this.lightColor);
        this.items.add(new Item(path, color));
    }

    public void measure(int width, int height, boolean sort) {
        this.originX = width / 2;
        this.originY = height * 0.9;

        for (Item item : items) {

            if (item.transformedPoints.size() > 0) {
                item.transformedPoints.clear();
            }

            if (!item.drawPath.isEmpty()) {
                item.drawPath.rewind();//Todo: test if .reset isn´ needed and rewind is enougth
            }

            for (Point point : item.path.points) {
                item.transformedPoints.add(translatePoint(point));
            }

            item.drawPath.moveTo(item.transformedPoints.get(0).x.floatValue(), item.transformedPoints.get(0).y.floatValue());

            for (int i = 1, length = item.transformedPoints.size(); i < length; i++) {
                item.drawPath.lineTo(item.transformedPoints.get(i).x.floatValue(), item.transformedPoints.get(i).y.floatValue());
            }

            //Todo: check if needed
            item.drawPath.close();
        }

        if (sort) {
            this.items = sortPaths();
        }
    }


    private List<Item> sortPaths() {
        ArrayList<Item> sortedItems = new ArrayList<>();
        Point observer = new Point(-10, -10, 20);
        int length = items.size();
        List<List<Integer>> drawBefore = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            drawBefore.add(i, new ArrayList<Integer>());
        }
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < i; j++) {
                if (hasIntersection(items.get(i).transformedPoints, items.get(j).transformedPoints)) {
                    int cmpPath = items.get(i).path.closerThan(items.get(j).path, observer);
                    if (cmpPath < 0) {
                        drawBefore.get(i).add(drawBefore.get(i).size(), j);
                    }
                    if (cmpPath > 0) {
                        drawBefore.get(j).add(drawBefore.get(j).size(), i);
                    }
                }
            }
        }
        int drawThisTurn = 1;
        while (drawThisTurn == 1) {
            drawThisTurn = 0;
            for (int i = 0; i < length; i++) {
                if (items.get(i).drawn == 0) {
                    int canDraw = 1;
                    for (int j = 0; j < drawBefore.get(i).size(); j++) {
                        if (items.get(drawBefore.get(i).get(j)).drawn == 0) {
                            canDraw = 0;
                        }
                    }
                    if (canDraw == 1) {
                        Item item = new Item(items.get(i).path, items.get(i).baseColor);
                        item.drawPath = items.get(i).drawPath;
                        item.transformedPoints = items.get(i).transformedPoints;
                        sortedItems.add(item);
                        Item currItem = items.get(i);
                        currItem.drawn = 1;
                        items.set(i, currItem);
                        drawThisTurn = 1;
                    }
                }
            }
        }
        for (int i = 0; i < length; i++) {
            if (items.get(i).drawn == 0) {
                Item item = new Item(items.get(i).path, items.get(i).baseColor);
                item.drawPath = items.get(i).drawPath;
                item.transformedPoints = items.get(i).transformedPoints;
                sortedItems.add(item);
            }
        }
        return sortedItems;
    }

    public void draw(Canvas canvas) {
        for (Item item : items) {
            /*this.ctx.globalAlpha = color.a;
            this.ctx.fillStyle = this.ctx.strokeStyle = color.toHex();
            this.ctx.stroke();
            this.ctx.fill();
            this.ctx.restore();*/
            canvas.drawPath(item.drawPath, item.paint);
        }
    }

    @Nullable
    public Item findItemForPosition(Point position) {
        //Items are already sorted for depth sort so break should not be a problem here
        for (Item item : this.items) {
            List<Point> items = new ArrayList<>();
            Point top = null,
                    bottom = null,
                    left = null,
                    right = null;
            for (Point point : item.transformedPoints) {
                if (top == null || point.y > top.y) {
                    if (top == null) {
                        top = new Point(point.x, point.y);
                    } else {
                        top.y = point.y;
                        top.x = point.x;
                    }
                }
                if (bottom == null || point.y < bottom.y) {
                    if (bottom == null) {
                        bottom = new Point(point.x, point.y);
                    } else {
                        bottom.y = point.y;
                        bottom.x = point.x;
                    }
                }
                if (left == null || point.x < left.x) {
                    if (left == null) {
                        left = new Point(point.x, point.y);
                    } else {
                        left.x = point.x;
                        left.y = point.y;
                    }
                }
                if (right == null || point.x > right.x) {
                    if (right == null) {
                        right = new Point(point.x, point.y);
                    } else {
                        right.x = point.x;
                        right.y = point.y;
                    }
                }
            }

            items.add(left);
            items.add(top);
            items.add(right);
            items.add(bottom);

            //search for equal points that are above or below for left and right or left and right for bottom and top
            for (Point point : item.transformedPoints) {
                if (point.x.equals(left.x)) {
                    if (!point.y.equals(left.y)) {
                        items.add(point);
                    }
                }
                if (point.x.equals(right.x)) {
                    if (!point.y.equals(right.y)) {
                        items.add(point);
                    }
                }
                if (point.y.equals(top.y)) {
                    if (!point.y.equals(top.y)) {
                        items.add(point);
                    }
                }
                if (point.y.equals(bottom.y)) {
                    if (!point.y.equals(bottom.y)) {
                        items.add(point);
                    }
                }
            }

            int i;
            int j;
            boolean result = false;
            int length = items.size();
            for (i = 0, j = length - 1; i < length; j = i++) {
                Point polygonItemI = items.get(i);
                Point polygonItemJ = items.get(j);
                if ((polygonItemI.y > position.y) != (polygonItemJ.y > position.y) &&
                        (position.x < (polygonItemJ.x - polygonItemI.x) * (position.y - polygonItemI.y) / (polygonItemJ.y - polygonItemI.y) + polygonItemI.x)) {
                    result = !result;
                }
            }
            if (result) {
                return item;
            }
        }
        return null;
    }

    static class Item {
        Path path;
        Color baseColor;
        Paint paint;
        int drawn = 0;
        boolean updated = false;
        List<Point> transformedPoints = new ArrayList<>();
        android.graphics.Path drawPath = new android.graphics.Path();

        Item(Path path, Color baseColor) {
            this.paint = new Paint();
            this.path = path;
            this.baseColor = baseColor;
            this.paint.setColor(android.graphics.Color.argb((int) baseColor.a, (int) baseColor.r, (int) baseColor.g, (int) baseColor.b));
        }
    }

    private boolean isPointInPoly(List<Point> poly, Point pt) {
        boolean c = false;
        for (int i = -1, l = poly.size(), j = l - 1; ++i < l; j = i) {
            boolean bla = ((poly.get(i).y <= pt.y && pt.y < poly.get(j).y) || (poly.get(j).y <= pt.y && pt.y < poly.get(i).y))
                    && (pt.x < (poly.get(j).x - poly.get(i).x) * (pt.y - poly.get(i).y) / (poly.get(j).y - poly.get(i).y) + poly.get(i).x)
                    && (c = !c);
        }
        return c;
    }

    private boolean hasIntersection(List<Point> pointsA, List<Point> pointsB) {
        int i, j, lengthA = pointsA.size(), lengthB = pointsB.size(), lengthPolyA, lengthPolyB;
        double AminX = pointsA.get(0).x;
        double AminY = pointsA.get(0).y;
        double AmaxX = AminX;
        double AmaxY = AminY;
        double BminX = pointsB.get(0).x;
        double BminY = pointsB.get(0).y;
        double BmaxX = BminX;
        double BmaxY = BminY;
        for (i = 0; i < lengthA; i++) {
            AminX = Math.min(AminX, pointsA.get(i).x);
            AminY = Math.min(AminY, pointsA.get(i).y);
            AmaxX = Math.max(AmaxX, pointsA.get(i).x);
            AmaxY = Math.max(AmaxY, pointsA.get(i).y);
        }
        for (i = 0; i < lengthB; i++) {
            BminX = Math.min(BminX, pointsB.get(i).x);
            BminY = Math.min(BminY, pointsB.get(i).y);
            BmaxX = Math.max(BmaxX, pointsB.get(i).x);
            BmaxY = Math.max(BmaxY, pointsB.get(i).y);
        }

        if (((AminX <= BminX && BminX <= AmaxX) || (BminX <= AminX && AminX <= BmaxX)) &&
                ((AminY <= BminY && BminY <= AmaxY) || (BminY <= AminY && AminY <= BmaxY))) {
            // now let's be more specific
            List<Point> polyA = cloneList(pointsA);
            List<Point> polyB = cloneList(pointsB);
            polyA.add(pointsA.get(0));
            polyB.add(pointsB.get(0));

            // see if edges cross, or one contained in the other
            List<Double> deltaAX = new ArrayList<>();
            List<Double> deltaAY = new ArrayList<>();
            List<Double> deltaBX = new ArrayList<>();
            List<Double> deltaBY = new ArrayList<>();
            List<Double> rA = new ArrayList<>();
            List<Double> rB = new ArrayList<>();
            lengthPolyA = polyA.size();
            for (i = 0; i <= lengthPolyA - 2; i++) {
                deltaAX.add(i, polyA.get(i + 1).x - polyA.get(i).x);
                deltaAY.add(i, polyA.get(i + 1).y - polyA.get(i).y);
                //equation written as deltaY.x - deltaX.y + r = 0
                rA.add(i, deltaAX.get(i) * polyA.get(i).y - deltaAY.get(i) * polyA.get(i).x);
            }
            lengthPolyB = polyB.size();
            for (i = 0; i <= lengthPolyB - 2; i++) {
                deltaBX.add(i, polyB.get(i + 1).x - polyB.get(i).x);
                deltaBY.add(i, polyB.get(i + 1).y - polyB.get(i).y);
                rB.add(i, deltaBX.get(i) * polyB.get(i).y - deltaBY.get(i) * polyB.get(i).x);
            }

            for (i = 0; i <= lengthPolyA - 2; i++) {
                for (j = 0; j <= lengthPolyB - 2; j++) {
                    if (deltaAX.get(i) * deltaBY.get(j) != deltaAY.get(i) * deltaBX.get(j)) {
                        //case when vectors are colinear, or one polygon included in the other, is covered after
                        //two segments cross each other if and only if the points of the first are on each side of the line defined by the second and vice-versa
                        if ((deltaAY.get(i) * polyB.get(j).x - deltaAX.get(i) * polyB.get(j).y + rA.get(i)) * (deltaAY.get(i) * polyB.get(j + 1).x - deltaAX.get(i) * polyB.get(j + 1).y + rA.get(i)) < -0.000000001 &&
                                (deltaBY.get(j) * polyA.get(i).x - deltaBX.get(j) * polyA.get(i).y + rB.get(j)) * (deltaBY.get(j) * polyA.get(i + 1).x - deltaBX.get(j) * polyA.get(i + 1).y + rB.get(j)) < -0.000000001) {
                            return true;
                        }
                    }
                }
            }

            for (i = 0; i <= lengthPolyA - 2; i++) {
                if (isPointInPoly(polyB, new Point(polyA.get(i).x, polyA.get(i).y))) {
                    return true;
                }
            }
            for (i = 0; i <= lengthPolyB - 2; i++) {
                if (isPointInPoly(polyA, new Point(polyB.get(i).x, polyB.get(i).y))) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    private List<Point> cloneList(List<Point> points) {
        List<Point> clonedList = new ArrayList<>(points.size());
        for (Point point : points) {
            Point newPoint = new Point();
            if (point.x != null) {
                newPoint.x = point.x;
            }
            if (point.y != null) {
                newPoint.y = point.y;
            }
            if (point.z != null) {
                newPoint.z = point.z;
            }
            clonedList.add(newPoint);
        }
        return clonedList;
    }
}
