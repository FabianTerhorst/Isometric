package io.fabianterhorst.isometric;

import android.support.annotation.Nullable;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Point {

    public static final Point ORIGIN = new Point(0, 0, 0);

    protected Double x, y, z;

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

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
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
    public Point scale(Point origin, @Nullable Double dx, @Nullable Double dy, @Nullable Double dz) {
        Point p = this.translate(-origin.x, -origin.y, -origin.z);
        if (dy == null && dz == null) {
             /* If both dy and dz are left out, scale all coordinates equally */
            dy = dz = dx;
        } else {
            /* If just dz is missing, set it equal to 1 */
            dz = dz == null ? 1 : dz;
        }

        p.x *= dx;
        p.y *= dy;
        p.z *= dz;
        return p.translate(origin.x, origin.y, origin.z);
    }

    /**
     * Rotate about origin on the X axis
     */
    public Point rotateX(Point origin, double angle) {
        Point p = this.translate(-origin.x, -origin.y, -origin.z);

        double z = p.z * Math.cos(angle) - p.y * Math.sin(angle);
        double y = p.z * Math.sin(angle) + p.y * Math.cos(angle);
        p.z = z;
        p.y = y;

        return p.translate(origin.x, origin.y, origin.z);
    }

    /**
     * Rotate about origin on the Y axis
     */
    public Point rotateY(Point origin, double angle) {
        Point p = this.translate(-origin.x, -origin.y, -origin.z);

        double x = p.x * Math.cos(angle) - p.z * Math.sin(angle);
        double z = p.x * Math.sin(angle) + p.z * Math.cos(angle);
        p.x = x;
        p.z = z;

        return p.translate(origin.x, origin.y, origin.z);
    }

    /**
     * Rotate about origin on the Y axis
     */
    public Point rotateZ(Point origin, double angle) {
        Point p = this.translate(-origin.x, -origin.y, -origin.z);

        double x = p.x * Math.cos(angle) - p.y * Math.sin(angle);
        double y = p.x * Math.sin(angle) + p.y * Math.cos(angle);
        p.x = x;
        p.y = y;

        return p.translate(origin.x, origin.y, origin.z);
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
