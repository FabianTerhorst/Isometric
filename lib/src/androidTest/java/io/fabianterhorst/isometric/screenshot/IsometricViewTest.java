package io.fabianterhorst.isometric.screenshot;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.facebook.testing.screenshot.Screenshot;
import com.facebook.testing.screenshot.ViewHelpers;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.fabianterhorst.isometric.Color;
import io.fabianterhorst.isometric.IsometricView;
import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;
import io.fabianterhorst.isometric.shapes.Cylinder;
import io.fabianterhorst.isometric.shapes.Octahedron;
import io.fabianterhorst.isometric.shapes.Prism;
import io.fabianterhorst.isometric.shapes.Pyramid;
import io.fabianterhorst.isometric.shapes.Stairs;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * Created by fabianterhorst on 03.04.17.
 */

@RunWith(AndroidJUnit4.class)
@SmallTest
public class IsometricViewTest {
    @Test
    public void doScreenshotOne() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        sampleOne(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(220)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotTwo() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        sampleTwo(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(540)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotThree() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        sampleThree(0, view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(820)
                .setExactHeightPx(680)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotGrid() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        grid(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(540)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotPath() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        path(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(440)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotTranslate() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        translate(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(440)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotScale() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        scale(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(440)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotRotateZ() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        rotateZ(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(440)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotExtrude() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        extrude(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(440)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    @Test
    public void doScreenshotCylinder() {
        IsometricView view = new IsometricView(getInstrumentation().getTargetContext());
        cylinder(view);
        ViewHelpers.setupView(view)
                .setExactWidthPx(680)
                .setExactHeightPx(440)
                .layout();
        Screenshot.snap(view)
                .record();
    }

    public void grid(IsometricView isometricView) {
        for (int x = 0; x < 10; x++) {
            isometricView.add(new Path(new Point[]{
                    new Point(x, 0, 0),
                    new Point(x, 10, 0),
                    new Point(x, 0, 0)
            }), new Color(50, 160, 60));
        }
        for (int y = 0; y < 10; y++) {
            isometricView.add(new Path(new Point[]{
                    new Point(0, y, 0),
                    new Point(10, y, 0),
                    new Point(0, y, 0)
            }), new Color(50, 160, 60));
        }
        isometricView.add(new Prism(Point.ORIGIN), new Color(33, 150, 243));
        isometricView.add(new Path(new Point[]{
                Point.ORIGIN,
                new Point(0, 0, 10),
                Point.ORIGIN
        }), new Color(160, 50, 60));
    }

    public void path(IsometricView isometricView) {
        isometricView.add(new Prism(Point.ORIGIN, 3, 3, 1), new Color(50, 60, 160));
        isometricView.add(new Path(new Point[]{
                new Point(1, 1, 1),
                new Point(2, 1, 1),
                new Point(2, 2, 1),
                new Point(1, 2, 1)
        }), new Color(50, 160, 60));
    }

    public void sampleOne(IsometricView isometricView) {
        isometricView.add(new Prism(new Point(0, 0, 0)), new Color(33, 150, 243));
    }

    public void sampleTwo(IsometricView isometricView) {
        isometricView.add(new Prism(new Point(0, 0, 0), 4, 4, 2), new Color(33, 150, 243));
        isometricView.add(new Prism(new Point(-1, 1, 0), 1, 2, 1), new Color(33, 150, 243));
        isometricView.add(new Prism(new Point(1, -1, 0), 2, 1, 1), new Color(33, 150, 243));
    }

    public void sampleThree(double angle, IsometricView isometricView) {
        isometricView.clear();
        isometricView.add(new Prism(new Point(1, -1, 0), 4, 5, 2), new Color(33, 150, 243));
        isometricView.add(new Prism(new Point(0, 0, 0), 1, 4, 1), new Color(33, 150, 243));
        isometricView.add(new Prism(new Point(-1, 1, 0), 1, 3, 1), new Color(33, 150, 243));
        isometricView.add(new Stairs(new Point(-1, 0, 0), 10), new Color(33, 150, 243));
        isometricView.add(new Stairs(new Point(0, 3, 1), 10).rotateZ(new Point(0.5, 3.5, 1), -Math.PI / 2), new Color(33, 150, 243));
        isometricView.add(new Prism(new Point(3, 0, 2), 2, 4, 1), new Color(33, 150, 243));
        isometricView.add(new Prism(new Point(2, 1, 2), 1, 3, 1), new Color(33, 150, 243));
        isometricView.add(new Stairs(new Point(2, 0, 2), 10).rotateZ(new Point(2.5, 0.5, 0), -Math.PI / 2), new Color(33, 150, 243));
        isometricView.add(new Pyramid(new Point(2, 3, 3)).scale(new Point(2, 4, 3), 0.5, null, null), new Color(180, 180, 0));
        isometricView.add(new Pyramid(new Point(4, 3, 3)).scale(new Point(5, 4, 3), 0.5, null, null), new Color(180, 0, 180));
        isometricView.add(new Pyramid(new Point(4, 1, 3)).scale(new Point(5, 1, 3), 0.5, null, null), new Color(0, 180, 180));
        isometricView.add(new Pyramid(new Point(2, 1, 3)).scale(new Point(2, 1, 3), 0.5, null, null), new Color(40, 180, 40));
        isometricView.add(new Prism(new Point(3, 2, 3), 1, 1, 0.2), new Color(50, 50, 50));
        isometricView.add(new Octahedron(new Point(3, 2, 3.2)).rotateZ(new Point(3.5, 2.5, 0), angle), new Color(0, 180, 180));
    }

    public void translate(IsometricView isometricView) {
        Color blue = new Color(50, 60, 160);
        Color red = new Color(160, 60, 50);
        Prism cube = new Prism(new Point(0, 0, 0));
        isometricView.add(cube, red);
        isometricView.add(cube.translate(0, 0, 1.1), blue);
        isometricView.add(cube.translate(0, 0, 2.2), red);
    }

    public void scale(IsometricView isometricView) {
        Color blue = new Color(50, 60, 160);
        Color red = new Color(160, 60, 50);
        Prism cube = new Prism(Point.ORIGIN);
        isometricView.add(cube.scale(Point.ORIGIN, 3.0, 3.0, 0.5), red);
        isometricView.add(cube
                .scale(Point.ORIGIN, 3.0, 3.0, 0.5)
                .translate(0, 0, 0.6), blue);
    }

    public void rotateZ(IsometricView isometricView) {
        Color blue = new Color(50, 60, 160);
        Color red = new Color(160, 60, 50);
        Prism cube = new Prism(Point.ORIGIN, 3, 3, 1);
        isometricView.add(cube, red);
        isometricView.add(cube
                /* (1.5, 1.5) is the center of the prism */
                .rotateZ(new Point(1.5, 1.5, 0), Math.PI / 12)
                .translate(0, 0, 1.1), blue);
    }

    public void extrude(IsometricView isometricView) {
        Color blue = new Color(50, 60, 160);
        Color red = new Color(160, 60, 50);
        isometricView.add(new Prism(Point.ORIGIN, 3, 3, 1), blue);
        isometricView.add(Shape.extrude(new Path(new Point[]{
                new Point(1, 1, 1),
                new Point(2, 1, 1),
                new Point(2, 3, 1)
        }), 0.3), red);
    }

    public void cylinder(IsometricView isometricView) {
        Color blue = new Color(50, 60, 160);
        isometricView.add(new Cylinder(new Point(1, 1, 1), 30, 1), blue);
    }
}
