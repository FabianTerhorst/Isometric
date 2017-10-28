package io.fabianterhorst.isometric.sample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import io.fabianterhorst.isometric.Color;
import io.fabianterhorst.isometric.Isometric;
import io.fabianterhorst.isometric.IsometricView;
import io.fabianterhorst.isometric.Path;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.shapes.Octahedron;
import io.fabianterhorst.isometric.shapes.Prism;
import io.fabianterhorst.isometric.shapes.Pyramid;
import io.fabianterhorst.isometric.shapes.Stairs;

public class MainActivity extends AppCompatActivity {

    /*private Runnable runnable;

    private double angle;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*final */
        IsometricView isometricView = findViewById(R.id.isometricView);
        isometricView.setClickListener(new IsometricView.OnItemClickListener() {
            @Override
            public void onClick(@NonNull Isometric.Item item) {

            }
        });
        //Sort false improves performance but requires the correct order while adding
        //Sort true also does not support every state that is possible with sort false
        //isometricView.setSort(false);

        /*final Handler handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                sampleThree(angle += Math.PI / 90, isometricView);
                isometricView.requestLayout();
                isometricView.invalidate();
                handler.postDelayed(runnable, 1000 / 60);
            }
        };
        handler.post(runnable);*/
        sampleThree(0, isometricView);
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
        isometricView.add(new Pyramid(new Point(2, 3, 3)).scale(new Point(2, 4, 3), 0.5), new Color(180, 180, 0));
        isometricView.add(new Pyramid(new Point(4, 3, 3)).scale(new Point(5, 4, 3), 0.5), new Color(180, 0, 180));
        isometricView.add(new Pyramid(new Point(4, 1, 3)).scale(new Point(5, 1, 3), 0.5), new Color(0, 180, 180));
        isometricView.add(new Pyramid(new Point(2, 1, 3)).scale(new Point(2, 1, 3), 0.5), new Color(40, 180, 40));
        isometricView.add(new Prism(new Point(3, 2, 3), 1, 1, 0.2), new Color(50, 50, 50));
        isometricView.add(new Octahedron(new Point(3, 2, 3.2)).rotateZ(new Point(3.5, 2.5, 0), angle), new Color(0, 180, 180));
    }
}
