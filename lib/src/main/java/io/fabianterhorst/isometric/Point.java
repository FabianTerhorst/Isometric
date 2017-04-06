package io.fabianterhorst.isometric;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Point {

    public static final Point ORIGIN = new Point(0, 0, 0);

    protected double x, y, z;

    Point() {

    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    /**
     * Translate a point from a given dx, dy, and dz
     */
    public Point translate(double dx, double dy, double dz) {
        return new Point(
                this.x + dx,
                this.y + dy,
                this.z + dz);
    }

    /**
     * Scale a point about a given origin
     */
    public Point scale(Point origin, double dx, double dy, double dz) {
        return new Point((this.x - origin.x) * dx + origin.x,
                (this.y - origin.y) * dy + origin.y,
                (this.z - origin.z) * dz + origin.z);
    }

    public Point scale(Point origin, double dx) {
        return scale(origin, dx, dx, dx);
    }

    public Point scale(Point origin, double dx, double dy) {
        return scale(origin, dx, dy, 1);
    }

    /**
     * Rotate about origin on the X axis
     */
    public Point rotateX(Point origin, double angle) {
        double pY = this.y - origin.y;
        double pZ = this.z - origin.z;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double z = pZ * cos - pY * sin;
        double y = pZ * sin + pY * cos;
        pZ = z;
        pY = y;
        return new Point(this.x, pY + origin.y, pZ + origin.z);
    }

    /**
     * Rotate about origin on the Y axis
     */
    public Point rotateY(Point origin, double angle) {
        double pX = this.x - origin.x;
        double pZ = this.z - origin.z;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = pX * cos - pZ * sin;
        double z = pX * sin + pZ * cos;
        pX = x;
        pZ = z;
        return new Point(pX + origin.x, this.y, pZ  + origin.z);
    }

    /**
     * Rotate about origin on the Y axis
     */
    public Point rotateZ(Point origin, double angle) {
        double pX = this.x - origin.x;
        double pY = this.y - origin.y;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = pX * cos - pY * sin;
        double y = pX * sin + pY * cos;
        pX = x;
        pY = y;
        return new Point(pX + origin.x, pY + origin.y, this.z);
    }

    /**
     * The depth of a point in the isometric plane
     */
    public double depth() {
        /* z is weighted slightly to accommodate |_ arrangements */
        return this.x + this.y - 2 * this.z;
    }

    /**
     * Distance between two points
     */
    public double distance(Point p1, Point p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double dz = p2.z - p1.z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
