package io.fabianterhorst.isometric.screenshot.util;

import android.view.View;

import com.facebook.testing.screenshot.Screenshot;
import com.facebook.testing.screenshot.ViewHelpers;

/**
 * Created by fabianterhorst on 05.04.17.
 */

public class ScreenshotHelper {

    public static void measureAndScreenshotView(View view, int width, int height) {
        ViewHelpers.setupView(view)
                .setExactWidthPx(width)
                .setExactHeightPx(height)
                .layout();
        Screenshot.snap(view)
                .record();
    }
}
