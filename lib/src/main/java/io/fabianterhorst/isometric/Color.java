package io.fabianterhorst.isometric;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class Color {

    protected double r, g, b, a, h, s, l;

    private static final double colorDifference = 0.20;

    public Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;//Math.round(a * 100) / 100;
        this.loadHSL();
    }

    public Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;
        this.loadHSL();
    }

    private void loadHSL() {
        double r = this.r / 255;
        double g = this.g / 255;
        double b = this.b / 255;

        double max = max(r, g, b);
        double min = min(r, g, b);

        double h = 0, s, l = (max + min) / 2.0;

        if (max == min) {
            h = s = 0;  // achromatic
        } else {
            double d = max - min;
            s = l > 0.5 ? d / (2.0 - max - min) : d / (max + min);
            if (max == r) {
                h = (g - b) / d + (g < b ? 6.0 : 0.0);
            } else if (max == g) {
                h = (b - r) / d + 2.0;
            } else if (max == b) {
                h = (r - g) / d + 4.0;
            }
            h /= 6.0;
        }

        this.h = h;
        this.s = s;
        this.l = l;
    }

    /*private Color transformColor(Path path, Color color) {
        Vector v1 = Vector.fromTwoPoints(path.points[1], path.points[0]);
        Vector v2 = Vector.fromTwoPoints(path.points[2], path.points[1]);

        Vector normal = Vector.crossProduct(v1, v2).normalize();

        double brightness = Vector.dotProduct(normal, this.lightAngle);
        return color.lighten(brightness * this.colorDifference, this.lightColor);
    }*/

    public static Color transformColor(Path path, Color color) {
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
        double brightness = i * Isometric.lightAngle.i + j * Isometric.lightAngle.j + k * Isometric.lightAngle.k;
        return color.lighten(brightness * colorDifference, Isometric.lightColor);
    }

    private double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    private double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    public Color lighten(double percentage, Color lightColor) {
        Color newColor = new Color(
                (lightColor.r / 255.0) * this.r,
                (lightColor.g / 255.0) * this.g,
                (lightColor.b / 255.0) * this.b,
                this.a
        );

        newColor.l = Math.min(newColor.l + percentage, 1);

        newColor.loadRGB();
        return newColor;
    }

    private void loadRGB() {
        double r, g, b;
        double h = this.h;
        double s = this.s;
        double l = this.l;

        if (s == 0) {
            r = g = b = l;  // achromatic
        } else {
            double q = l < 0.5 ? l * (1 + s) : l + s - l * s;
            double p = 2.0 * l - q;
            r = this.hue2rgb(p, q, h + 1 / 3.0);
            g = this.hue2rgb(p, q, h);
            b = this.hue2rgb(p, q, h - 1 / 3.0);
        }

        this.r = r * 255.0;
        this.g = g * 255.0;
        this.b = b * 255.0;
    }

    private double hue2rgb(double p, double q, double t) {
        if (t < 0) t += 1;
        if (t > 1) t -= 1;
        if (t < 1 / 6.0) return p + (q - p) * 6.0 * t;
        if (t < 1 / 2.0) return q;
        if (t < 2 / 3.0) return p + (q - p) * (2.0 / 3.0 - t) * 6.0;
        return p;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Color)) return false;
        Color color = (Color) o;
        return Double.compare(color.r, r) == 0 &&
                Double.compare(color.g, g) == 0 &&
                Double.compare(color.b, b) == 0 &&
                Double.compare(color.a, a) == 0 &&
                Double.compare(color.h, h) == 0 &&
                Double.compare(color.s, s) == 0 &&
                Double.compare(color.l, l) == 0;
    }

    @Override
    public int hashCode() {
        return  Double.valueOf(r).hashCode() ^
                Double.valueOf(g).hashCode() ^
                Double.valueOf(b).hashCode() ^
                Double.valueOf(a).hashCode() ^
                Double.valueOf(h).hashCode() ^
                Double.valueOf(s).hashCode() ^
                Double.valueOf(l).hashCode();
    }
}
