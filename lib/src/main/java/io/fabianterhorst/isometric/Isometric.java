package io.fabianterhorst.isometric;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;

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
        /* Compute color */
        Vector v1 = Vector.fromTwoPoints(path.points[1], path.points[0]);
        Vector v2 = Vector.fromTwoPoints(path.points[2], path.points[1]);

        Vector normal = Vector.crossProduct(v1, v2).normalize();

        /**
         * Brightness is between -1 and 1 and is computed based
         * on the dot product between the light source vector and normal.
         */
        double brightness = Vector.dotProduct(normal, this.lightAngle);
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
            for (int i = 0; i < item.path.points.length; i++) {
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
                if (IntersectionUtils.hasIntersection(itemA.transformedPoints, itemB.transformedPoints)) {
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
                if (top == null) {
                    top = new Point(point.x, point.y);
                } else if (point.y > top.y) {
                    top.y = point.y;
                    top.x = point.x;
                }

                if (bottom == null) {
                    bottom = new Point(point.x, point.y);
                } else if (point.y < bottom.y) {
                    bottom.y = point.y;
                    bottom.x = point.x;
                }

                if (left == null) {
                    left = new Point(point.x, point.y);
                } else if (point.x < left.x) {
                    left.x = point.x;
                    left.y = point.y;
                }

                if (right == null) {
                    right = new Point(point.x, point.y);
                } else if (point.x > right.x) {
                    right.x = point.x;
                    right.y = point.y;
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

            if (IntersectionUtils.isPointInPoly(items, position.x, position.y)) {
                return item;
            }
        }
        return null;
    }

    public static class Item {
        Path path;
        Color baseColor;
        Paint paint;
        int drawn;
        Point[] transformedPoints;
        android.graphics.Path drawPath;

        Item(Item item) {
            this.transformedPoints = item.transformedPoints;
            this.drawPath = item.drawPath;
            this.drawn = item.drawn;
            this.paint = item.paint;
            this.path = item.path;
            this.baseColor = item.baseColor;
        }

        Item(Path path, Color baseColor) {
            this.drawPath = new android.graphics.Path();
            this.drawn = 0;
            this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStrokeWidth(1);
            this.path = path;
            this.baseColor = baseColor;
            this.paint.setColor(android.graphics.Color.argb((int) baseColor.a, (int) baseColor.r, (int) baseColor.g, (int) baseColor.b));
        }
    }
}
