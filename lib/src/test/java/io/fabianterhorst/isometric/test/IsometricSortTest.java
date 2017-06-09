package io.fabianterhorst.isometric.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import io.fabianterhorst.isometric.Color;
import io.fabianterhorst.isometric.Isometric;
import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.Shape;
import io.fabianterhorst.isometric.shapes.Octahedron;
import io.fabianterhorst.isometric.shapes.Prism;
import io.fabianterhorst.isometric.shapes.Pyramid;
import io.fabianterhorst.isometric.shapes.Stairs;

/**
 * Created by fabianterhorst on 07.04.17.
 */

@RunWith(JUnit4.class)
public class IsometricSortTest {

    private Isometric isometric;

    @Before
    public void setup() {
        isometric = new MockableIsometric();
        sampleOne(isometric);
    }

    @Test
    public void testSingleSort() {
        isometric.measure(100, 100, true);
        Shape shape = new Prism(new Point(1, -1, 0), 4, 5, 2);
        List<Isometric.Item> itemList = new ArrayList<>();
        for (Path path : shape.orderedPaths()) {
            itemList.add(isometric.createItem(path, null));
        }
        for (Isometric.Item item : itemList) {
            isometric.transformPoints(item);
        }
        //isometric.sortPathList(itemList);
    }

    public void sampleOne(Isometric iso) {
        sampleOne(0, iso);
    }

    public void sampleOne(double angle, Isometric iso) {
        iso.clear();
        iso.add(new Prism(new Point(1, -1, 0), 4, 5, 2), new Color(33, 150, 243));
        iso.add(new Prism(new Point(0, 0, 0), 1, 4, 1), new Color(33, 150, 243));
        iso.add(new Prism(new Point(-1, 1, 0), 1, 3, 1), new Color(33, 150, 243));
        iso.add(new Stairs(new Point(-1, 0, 0), 10), new Color(33, 150, 243));
        iso.add(new Stairs(new Point(0, 3, 1), 10).rotateZ(new Point(0.5, 3.5, 1), -Math.PI / 2), new Color(33, 150, 243));
        iso.add(new Prism(new Point(3, 0, 2), 2, 4, 1), new Color(33, 150, 243));
        iso.add(new Prism(new Point(2, 1, 2), 1, 3, 1), new Color(33, 150, 243));
        iso.add(new Stairs(new Point(2, 0, 2), 10).rotateZ(new Point(2.5, 0.5, 0), -Math.PI / 2), new Color(33, 150, 243));
        iso.add(new Pyramid(new Point(2, 3, 3)).scale(new Point(2, 4, 3), 0.5), new Color(180, 180, 0));
        iso.add(new Pyramid(new Point(4, 3, 3)).scale(new Point(5, 4, 3), 0.5), new Color(180, 0, 180));
        iso.add(new Pyramid(new Point(4, 1, 3)).scale(new Point(5, 1, 3), 0.5), new Color(0, 180, 180));
        iso.add(new Pyramid(new Point(2, 1, 3)).scale(new Point(2, 1, 3), 0.5), new Color(40, 180, 40));
        iso.add(new Prism(new Point(3, 2, 3), 1, 1, 0.2), new Color(50, 50, 50));
        iso.add(new Octahedron(new Point(3, 2, 3.2)).rotateZ(new Point(3.5, 2.5, 0), angle), new Color(0, 180, 180));
    }
}
