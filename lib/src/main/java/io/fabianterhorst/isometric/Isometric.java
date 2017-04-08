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
    public Point translatePoint(Point point) {
        return new Point(this.originX + point.x * this.transformation[0][0] + point.y * this.transformation[1][0],
                this.originY - point.x * this.transformation[0][1] - point.y * this.transformation[1][1] - (point.z * this.scale));
    }

    public void add(Path path, Color color) {
        addPath(path, color);
    }

    public void add(Path[] paths, Color color) {
        for (Path path : paths) {
            add(path, color);
        }
    }

    public void add(Shape shape, Color color) {
        /* Fetch paths ordered by distance to prevent overlaps */
        Path[] paths = shape.orderedPaths();

        for (Path path : paths) {
            addPath(path, color);
        }
    }

    public void clear() {
        items.clear();
    }

    private void addPath(Path path, Color color) {
        this.items.add(new Item(path, transformColor(path, color)));
    }

    private Color transformColor(Path path, Color color) {
        Point p1 = path.points[1];
        Point p2 = path.points[0];
        double i = p2.x - p1.x;
        double j = p2.y - p1.y;
        double k = p2.z - p1.z;
        p1 = path.points[2];
        p2 = path.points[1];
        double i2 = p2.x - p1.x;
        double j2 = p2.y - p1.y;
        double k2 = p2.z - p1.z;
        double i3 = j * k2 - j2 * k;
        double j3 = -1 * (i * k2 - i2 * k);
        double k3 = i * j2 - i2 * j;
        double magnitude = Math.sqrt(i3 * i3 + j3 * j3 + k3 * k3);
        i = magnitude == 0 ? 0 : i3 / magnitude;
        j = magnitude == 0 ? 0 : j3 / magnitude;
        k = magnitude == 0 ? 0 : k3 / magnitude;
        double brightness = i * lightAngle.i + j * lightAngle.j + k * lightAngle.k;
        return color.lighten(brightness * this.colorDifference, this.lightColor);
    }

    public void measure(int width, int height, boolean sort) {
        this.originX = width / 2;
        this.originY = height * 0.9;

        for (Item item : items) {

            item.transformedPoints = new Point[item.path.points.length];

            if (!item.drawPath.isEmpty()) {
                item.drawPath.rewind();//Todo: test if .reset is not needed and rewind is enough
            }

            Point point;
            for (int i = 0;i < item.path.points.length;i++) {
                point = item.path.points[i];
                item.transformedPoints[i] = translatePoint(point);
            }

            item.drawPath.moveTo((float) item.transformedPoints[0].x, (float) item.transformedPoints[0].y);

            for (int i = 1, length = item.transformedPoints.length; i < length; i++) {
                item.drawPath.lineTo((float) item.transformedPoints[i].x, (float) item.transformedPoints[i].y);
            }

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
        Item itemA;
        Item itemB;
        for (int i = 0; i < length; i++) {
            itemA = items.get(i);
            for (int j = 0; j < i; j++) {
                itemB = items.get(j);
                if (hasIntersection(itemA.transformedPoints, itemB.transformedPoints)) {
                    int cmpPath = itemA.path.closerThan(itemB.path, observer);
                    if (cmpPath < 0) {
                        drawBefore.get(i).add(j);
                    } else if (cmpPath > 0) {
                        drawBefore.get(j).add(i);
                    }
                }
            }
        }
        int drawThisTurn = 1;
        Item currItem;
        List<Integer> integers;
        while (drawThisTurn == 1) {
            drawThisTurn = 0;
            for (int i = 0; i < length; i++) {
                currItem = items.get(i);
                integers = drawBefore.get(i);
                if (currItem.drawn == 0) {
                    int canDraw = 1;
                    for (int j = 0, lengthIntegers = integers.size(); j < lengthIntegers; j++) {
                        if (items.get(integers.get(j)).drawn == 0) {
                            canDraw = 0;
                            break;
                        }
                    }
                    if (canDraw == 1) {
                        Item item = new Item(currItem);
                        sortedItems.add(item);
                        currItem.drawn = 1;
                        items.set(i, currItem);
                        drawThisTurn = 1;
                    }
                }
            }
        }

        for (int i = 0; i < length; i++) {
            currItem = items.get(i);
            if (currItem.drawn == 0) {
                sortedItems.add(new Item(currItem));
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

    //Todo: use android.grphics region object to check if point is inside region
    //Todo: use path.op to check if the path intersects with another path
    @Nullable
    public Item findItemForPosition(Point position) {
        //Todo: reverse sorting for click detection, because hidden object is getting drawed first und will be returned as the first as well
        //Items are already sorted for depth sort so break should not be a problem here
        for (Item item : this.items) {
            if (item.transformedPoints == null) continue;
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
                if (point.x == left.x) {
                    if (point.y != left.y) {
                        items.add(point);
                    }
                }
                if (point.x == right.x) {
                    if (point.y != right.y) {
                        items.add(point);
                    }
                }
                if (point.y == top.y) {
                    if (point.y != top.y) {
                        items.add(point);
                    }
                }
                if (point.y == bottom.y) {
                    if (point.y != bottom.y) {
                        items.add(point);
                    }
                }
            }

            if (isPointInPoly(items, position.x, position.y)) {
                return item;
            }
        }
        return null;
    }

    static class Item {
        Path path;
        Color baseColor;
        Paint paint;
        int drawn;
        Point[] transformedPoints;
        android.graphics.Path drawPath;

        Item(Item item) {
            transformedPoints = item.transformedPoints;
            drawPath = item.drawPath;
            drawn = item.drawn;
            this.paint = item.paint;
            this.path = item.path;
            this.baseColor = item.baseColor;
        }

        Item(Path path, Color baseColor) {
            drawPath = new android.graphics.Path();
            drawn = 0;
            this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStrokeWidth(1);
            this.path = path;
            this.baseColor = baseColor;
            this.paint.setColor(android.graphics.Color.argb((int) baseColor.a, (int) baseColor.r, (int) baseColor.g, (int) baseColor.b));
        }
    }

    private boolean isPointInPoly(List<Point> poly, double x, double y) {
        boolean c = false;
        for (int i = -1, l = poly.size(), j = l - 1; ++i < l; j = i) {
            if (((poly.get(i).y <= y && y < poly.get(j).y) || (poly.get(j).y <= y && y < poly.get(i).y))
                    && (x < (poly.get(j).x - poly.get(i).x) * (y - poly.get(i).y) / (poly.get(j).y - poly.get(i).y) + poly.get(i).x)) {
                c = !c;
            }
        }
        return c;
    }

    private boolean isPointInPoly(Point[] poly, double x, double y) {
        boolean c = false;
        for (int i = -1, l = poly.length, j = l - 1; ++i < l; j = i) {
            if (((poly[i].y <= y && y < poly[j].y) || (poly[j].y <= y && y < poly[i].y))
                    && (x < (poly[j].x - poly[i].x) * (y - poly[i].y) / (poly[j].y - poly[i].y) + poly[i].x)) {
                c = !c;
            }
        }
        return c;
    }

    private boolean hasIntersection(Point[] pointsA, Point[] pointsB) {
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
            Point[] polyA = Path.add(pointsA[0], pointsA);
            Point[] polyB = Path.add(pointsB[0], pointsB);

            // see if edges cross, or one contained in the other
            lengthPolyA = polyA.length;
            lengthPolyB = polyB.length;

            double[] deltaAX = new double[lengthPolyA];
            double[] deltaAY = new double[lengthPolyA];
            double[] deltaBX = new double[lengthPolyB];
            double[] deltaBY = new double[lengthPolyB];

            double[] rA = new double[lengthPolyA];
            double[] rB = new double[lengthPolyB];

            for (i = 0; i <= lengthPolyA - 2; i++) {
                point = polyA[i];
                deltaAX[i] = polyA[i + 1].x - point.x;
                deltaAY[i] = polyA[i + 1].y - point.y;
                //equation written as deltaY.x - deltaX.y + r = 0
                rA[i] = deltaAX[i] * point.y - deltaAY[i] * point.x;
            }

            for (i = 0; i <= lengthPolyB - 2; i++) {
                point = polyB[i];
                deltaBX[i] = polyB[i + 1].x - point.x;
                deltaBY[i] = polyB[i + 1].y - point.y;
                rB[i] = deltaBX[i] * point.y - deltaBY[i] * point.x;
            }

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
