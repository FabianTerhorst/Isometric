package io.fabianterhorst.isometric;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by fabianterhorst on 31.03.17.
 */

public class IsometricView extends View {

    private final Isometric isometric = new Isometric();

    private boolean sort = true;

    public IsometricView(Context context) {
        super(context);
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public void add(Path path, Color color) {
        isometric.add(path, color);
    }

    public void add(Shape shape, Color color) {
        isometric.add(shape, color);
    }

    public IsometricView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IsometricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public IsometricView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        long time = System.nanoTime();
        isometric.measure(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec), sort);
        Log.d("measure time", String.valueOf(System.nanoTime() - time));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long time = System.nanoTime();
        isometric.draw(canvas);
        Log.d("draw time", String.valueOf(System.nanoTime() - time));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            Isometric.Item item = isometric.findItemForPosition(new Point(event.getX(), event.getY()));
            if (item != null) {
                Log.d("item", "found");
            }
        }
        return super.onTouchEvent(event);
    }
}
