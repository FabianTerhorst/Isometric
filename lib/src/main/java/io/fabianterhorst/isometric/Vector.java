package io.fabianterhorst.isometric;

import androidx.annotation.NonNull;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Vector {

    protected double i, j, k;

    public Vector(double i, double j, double k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public double getX() {
        return i;
    }

    public double getY() {
        return j;
    }

    public double getZ() {
        return k;
    }

    @NonNull
    public static Vector fromTwoPoints(Point p1, Point p2) {
        return new Vector(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z);
    }

    @NonNull
    public static Vector crossProduct(Vector v1, Vector v2) {
        double i = v1.j * v2.k - v2.j * v1.k;
        double j = -1 * (v1.i * v2.k - v2.i * v1.k);
        double k = v1.i * v2.j - v2.i * v1.j;

        return new Vector(i, j, k);
    }

    public static double dotProduct(Vector v1, Vector v2) {
        return v1.i * v2.i + v1.j * v2.j + v1.k * v2.k;
    }

    public static Vector project(Vector v1, Vector v2){
        double mag = v2.magnitude();
        double mult = Vector.dotProduct(v1, v2)/(mag*mag);
        return v2.multiply(mult);
    }

    public double magnitude() {
        return Math.sqrt(this.i * this.i + this.j * this.j + this.k * this.k);
    }

    //can be used for division too
    public Vector multiply(double a){
        return new Vector(this.i * a, this.j * a, this.k * a);
    }

    public Vector normalize() {
        double magnitude = this.magnitude();
        //If the magnitude is 0 then return the zero vector instead of dividing by 0
        if (magnitude == 0) {
            return new Vector(0, 0, 0);
        }
        this.i = this.i / magnitude;
        this.j = this.j / magnitude;
        this.k = this.k / magnitude;
        return this;//new Vector(this.i / magnitude, this.j / magnitude, this.k / magnitude);
    }
}
