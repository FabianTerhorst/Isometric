package io.fabianterhorst.isometric;

import androidx.annotation.NonNull;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Vector3 {

    private Vector3() {

    }

    public static double[] create() {
        return new double[3];
    }

    public static double[] create(double x, double y, double z) {
        return new double[]{x, y, z};
    }

    public static double[] fromTwoPoints(Point p1, Point p2) {
        return new double[]{p2.x - p1.x, p2.y - p1.y, p2.z - p1.z};
    }

    public static void fromTwoPoints(double[] v, Point p1, Point p2) {
        v[0] = p2.x - p1.x;
        v[1] = p2.y - p1.y;
        v[2] = p2.z - p1.z;
    }

    public static void crossProduct(double[] v1, double[] v2, double[] v3) {
        v3[0] = v1[1] * v2[2] - v2[1] * v1[2];
        v3[1] = -1 * (v1[0] * v2[2] - v2[0] * v1[2]);
        v3[2] = v1[0] * v2[1] - v2[0] * v1[1];
    }

    public static void crossProduct(double[] v1, double[] v2) {
        double x = v1[1] * v2[2] - v2[1] * v1[2];
        double y = -1 * (v1[0] * v2[2] - v2[0] * v1[2]);
        double z = v1[0] * v2[1] - v2[0] * v1[1];
        v1[0] = x;
        v1[1] = y;
        v1[2] = z;
    }

    public static double dotProduct(double[] v1, double[] v2) {
        return v1[0] * v2[0] + v1[1] * v2[1] + v1[2] * v2[2];
    }

    public static double magnitude(double[] v) {
        return Math.sqrt(v[0] * v[0] + v[1] * v[1] + v[2] * v[2]);
    }

    public void normalize(double[] v) {
        double magnitude = magnitude(v);
        //If the magnitude is 0 then return the zero vector instead of dividing by 0
        if (magnitude == 0) {
            v[0] = 0;
            v[1] = 0;
            v[2] = 0;
            return;
        }
        v[0] = v[0] / magnitude;
        v[1] = v[1] / magnitude;
        v[2] = v[2] / magnitude;
    }
}
