package io.fabianterhorst.isometric.test;

import io.fabianterhorst.isometric.Color;
import io.fabianterhorst.isometric.Isometric;
import io.fabianterhorst.isometric.Path;

/**
 * Created by fabianterhorst on 07.04.17.
 */

public class MockableIsometric extends Isometric {

    @Override
    public Item createItem(Path path, Color color) {
        return new MockableItem(path);
    }

    @Override
    public void createPath(Item item) {
        //path creation is not mockable
    }

    class MockableItem extends Item {

        MockableItem(Path path) {
            super(null);
            setDrawn(0);
            setPath(path);
        }

    }
}
