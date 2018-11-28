package io.fabianterhorst.isometric;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by fabianterhorst on 31.03.17.
 */

//Todo: implement item ids to reuse objects for animations
public class Isometric {

    private final double angle, scale;

    //Iso coords to View Coords
    private double[][] transformationIsoView;
    //View coords to Iso Coords
    private double[][] transformationViewIso;

    private double originX, originY;

    private List<Item> items = new ArrayList<>();

    static final Vector lightAngle = new Vector(2, -1, 3).normalize();

    static final Color lightColor = new Color(255, 255, 255);

    private int currentWidth, currentHeight;

    private boolean itemsChanged;

    public Isometric() {
        this.angle = Math.PI / 6;
        this.scale = 70;
        this.transformationIsoView = new double[][]{
                //a                                 //c
                { this.scale * Math.cos(this.angle), this.scale * Math.sin(this.angle) },
                //b                                            //d
                { this.scale * Math.cos(Math.PI - this.angle), this.scale * Math.sin(Math.PI - this.angle)}
        };
        this.transformationViewIso = invertTransformationIsoView();
        this.currentWidth = -1;
        this.currentHeight = -1;
        this.itemsChanged = true;

    }

    //https://www.mathsisfun.com/algebra/matrix-inverse.html
    private double[][] invertTransformationIsoView() {
        //these were determined from the current preset structure of transformationIsoView
        //and usage in translateIsoToViewPoint
        double a = this.transformationIsoView[0][0];
        double b = this.transformationIsoView[1][0];
        double c = this.transformationIsoView[0][1];
//        double b = this.transformationIsoView[1][0];
//        double c = this.transformationIsoView[0][1];
        double d = this.transformationIsoView[1][1];

        double determinant = a*d - b*c;

        a = a/determinant;
        b = b/determinant;
        c = c/determinant;
        d = d/determinant;

        //preserving original format
        return new double[][]{
                {d,-b},
                {-c,a}
        };
    }

    /**
     * X rides along the angle extended from the origin
     * Y rides perpendicular to this angle (in isometric view: PI - angle)
     * Z affects the y coordinate of the drawn point
     */
    public Point translateIsoToViewPoint(Point point) {
        //example of how this calculation is performed https://www.math.hmc.edu/calculus/tutorials/changebasis/
        return new Point(this.originX + point.x * this.transformationIsoView[0][0] + point.y * this.transformationIsoView[1][0],
                //for the y coordinate, the subtractions are performed since you have to start at the bottom of the screen (max y) and subtract away
                this.originY - point.x * this.transformationIsoView[0][1] - point.y * this.transformationIsoView[1][1] - (point.z * this.scale));
    }

    public Point translateViewToIsoPoint(Point point) {
        double workingX = point.getX() - this.originX;
        double workingY = -(point.getY() - this.originY);

        return new Point(workingX * this.transformationViewIso[0][0] + workingY * this.transformationViewIso[1][0],
                //for the y coordinate, the subtractions are performed since you have to start at the bottom of the screen (max y) and subtract away
                workingX * this.transformationViewIso[0][1] + workingY * this.transformationViewIso[1][1] + (point.z * this.scale));
    }

    public void add(Path path, Color color) {
        addPath(path, color, null);
    }

    public void add(Path path, Color color, Shape originalShape) {
        addPath(path, color, originalShape);
    }

    public void add(Path[] paths, Color color) {
        for (Path path : paths) {
            add(path, color);
        }
    }

    public void add(Path[] paths, Color color, Shape originalShape) {
        for (Path path : paths) {
            add(path, color, originalShape);
        }
    }

    public void add(Shape shape, Color color) {
        /* Fetch paths ordered by distance to prevent overlaps */
        Path[] paths = shape.orderedPaths();

        for (Path path : paths) {
            addPath(path, color, shape);
        }
    }

    public void clear() {
        this.itemsChanged = true;
        this.items.clear();
    }

    private void addPath(Path path, Color color, Shape originalShape) {
        this.itemsChanged = true;
        this.items.add(Item.createItem(path, color, originalShape));
    }

    public void measure(int width, int height, boolean sort, boolean cull, boolean boundsCheck) {

        //only perform measure operation:
        //if the bounds have changed
        //OR if the items have changed
        if (this.currentWidth == width && this.currentHeight == height && !this.itemsChanged)
            return;

        this.currentWidth = width;
        this.currentHeight = height;
        this.itemsChanged = false;

        this.originX = width / 2;
        this.originY = height * 0.9;

        transformItems(this.items, cull, boundsCheck);

        if (sort) {
            this.items = sortPaths();
        }
    }

    /**
     * Use this to have the isometric library recalculate the paths of a provided list of items.
     *
     * Use this method when directly manipulating the items list returned by getCurrentItems().
     * This is a potentially 'dangerous' action, because you need to consider when you are
     * manipulating an item that is covered by another item.
     */
    public void updateItems(List<Item> items, boolean cull, boolean boundsCheck) {
        //this.itemsChanged = true;
        //
        // only want to update these items instead of all items
        transformItems(items, cull, boundsCheck);
    }

    //allow user to update particular items
    public void transformItems(List<Item> items, boolean cull, boolean boundsCheck) {

        int itemIndex = 0, itemSize = items.size();
        while (itemIndex < itemSize) {
            Item item = items.get(itemIndex);

            item.transformedPoints = new Point[item.path.points.length];

            if (!item.drawPath.isEmpty()) {
                item.drawPath.rewind();//Todo: test if .reset is not needed and rewind is enough
            }

            Point point;
            for (int i = 0, length = item.path.points.length; i < length; i++) {
                point = item.path.points[i];
                item.transformedPoints[i] = translateIsoToViewPoint(point);
            }

            //remove item if not in view
            //the if conditions here are ordered carefully to save computation, fail fast approach
            if ((cull && cullPath(item)) || (boundsCheck && !this.itemInDrawingBounds(item))) {
                //the path is invisible. It does not need to be considered any more
                items.remove(itemIndex);
                itemSize--;
                continue;
            }
            else
            {
                itemIndex++;
            }

            item.drawPath.moveTo((float) item.transformedPoints[0].x, (float) item.transformedPoints[0].y);

            for (int i = 1, length = item.transformedPoints.length; i < length; i++) {
                item.drawPath.lineTo((float) item.transformedPoints[i].x, (float) item.transformedPoints[i].y);
            }

            item.drawPath.close();
        }
    }

    public List<Item> getCurrentItems() {
        return this.items;
    }

    private boolean cullPath(Item item) {
        double a = item.transformedPoints[0].getX() * item.transformedPoints[1].getY();
        double b = item.transformedPoints[1].getX() * item.transformedPoints[2].getY();
        double c = item.transformedPoints[2].getX() * item.transformedPoints[0].getY();

        double d = item.transformedPoints[1].getX() * item.transformedPoints[0].getY();
        double e = item.transformedPoints[2].getX() * item.transformedPoints[1].getY();
        double f = item.transformedPoints[0].getX() * item.transformedPoints[2].getY();

        double z = a + b + c - d - e - f;
        return z > 0;
    }

    private boolean itemInDrawingBounds(Item item) {
        for (int i = 0, len = item.transformedPoints.length; i< len; i++)
        {
            //if any point is in bounds, the item is worth drawing
            if (item.transformedPoints[i].getX() >= 0 &&
                item.transformedPoints[i].getX() <= this.currentWidth  &&
                item.transformedPoints[i].getY() >= 0 &&
                item.transformedPoints[i].getY() <= this.currentHeight)
                return true;
        }
        return false;
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
                        Item item = Item.copyItem(currItem);
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
                sortedItems.add(Item.copyItem(currItem));
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
    public Item findItemForPosition(Point position, boolean reverseSort, boolean touchPosition, double radius) {

        //get iterator for the items list, and start either at the front or back
        //The items are already sorted back-to-front, by iterating the items list backwards
        //you check the items closer to the user first
        ListIterator<Item> itr = this.items.listIterator(reverseSort ? this.items.size() : 0);

        //Items are already sorted for depth sort so break should not be a problem here
        //iterate through the list in one direction or the other
        while (reverseSort ? itr.hasPrevious() : itr.hasNext()) {
            Item item = reverseSort ? itr.previous() : itr.next();

            if (item.transformedPoints == null) continue;
            int initialSize = 4;
            int itemSize = 0;
            List<Point> items = new ArrayList<>(initialSize);
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
            itemSize += 4;

            //search for equal points that are above or below for left and right or left and right for bottom and top
            for (Point point : item.transformedPoints) {
                if (point.x == left.x) {
                    if (point.y != left.y) {
                        items.add(point);
                        itemSize++;
                    }
                }
                if (point.x == right.x) {
                    if (point.y != right.y) {
                        items.add(point);
                        itemSize++;
                    }
                }
                if (point.y == top.y) {
                    if (point.y != top.y) {
                        items.add(point);
                        itemSize++;
                    }
                }
                if (point.y == bottom.y) {
                    if (point.y != bottom.y) {
                        items.add(point);
                        itemSize++;
                    }
                }
            }

            //need to remove nulls if we never filled the initial capacity of the list
            if (itemSize < initialSize)
                items.removeAll(Collections.singleton(null));

            //perform one method of touch position lookup
            //it is faster to check the individual segments first (disabled by default).
            // its possible the touch center is inside poly, but not close to
            // an edge so finish by checking if center of circle is in poly
            if ((touchPosition && IntersectionUtils.isPointCloseToPoly(items, position.x, position.y, radius)) || IntersectionUtils.isPointInPoly(items, position.x, position.y)) {
                return item;
            }
        }
        return null;
    }

    public static class Item {
        Path path;
        Color baseColor;
        Paint paint;
        Shape originalShape;
        int drawn;
        Point[] transformedPoints;
        android.graphics.Path drawPath;

        private Item(Item item) {
            this.transformedPoints = item.transformedPoints;
            this.drawPath = item.drawPath;
            this.drawn = item.drawn;
            this.paint = item.paint;
            this.path = item.path;
            this.baseColor = item.baseColor;
            this.originalShape = item.originalShape;
        }

        private Item(Path path, Color baseColor, Shape originalShape) {
            this.drawPath = new android.graphics.Path();
            this.drawn = 0;
            this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            this.paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.paint.setStrokeWidth(1);
            this.path = path;
            this.baseColor = baseColor;
            this.originalShape = originalShape;
            this.paint.setColor(android.graphics.Color.argb((int) baseColor.a, (int) baseColor.r, (int) baseColor.g, (int) baseColor.b));
        }

        public static Item createItem(Path path, Color color, Shape originalShape){
            return new Item(path, Color.transformColor(path, color), originalShape);
        }

        public static Item copyItem(Item oldItem){
            return new Item(oldItem);
        }

        public Path getPath() {
            return path;
        }

        public Shape getOriginalShape() {
            return originalShape;
        }
    }
}
