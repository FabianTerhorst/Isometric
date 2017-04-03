package io.fabianterhorst.isometric.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.fabianterhorst.isometric.Color;
import io.fabianterhorst.isometric.IsometricView;
import io.fabianterhorst.isometric.Point;
import io.fabianterhorst.isometric.shapes.Octahedron;
import io.fabianterhorst.isometric.shapes.Prism;
import io.fabianterhorst.isometric.shapes.Pyramid;
import io.fabianterhorst.isometric.shapes.Stairs;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IsometricView isometricView = (IsometricView) findViewById(R.id.isometricView);
        //Sort false improves performance but requires the correct order while adding
        //Sort true also does not support every state that is possible with sort false
        //isometricView.setSort(false);
        //isometricView.add(new Cylinder(Point.ORIGIN, 3d, 3d, 1d), new Color(160, 60, 50));
        //isometricView.add(new Prism(Point.ORIGIN, 3d, 3d, 1d), new Color(160, 60, 50));
        isometricView.add(new Prism(new Point(2, 4, 0), 3d, 3d, 1d), new Color(160, 60, 50));
        isometricView.add(new Prism(new Point(2, 0, 1), null, null, null), new Color(50, 60, 160));
        isometricView.add(new Octahedron(new Point(2,2,1)), new Color(0,180,180));
        isometricView.add(new Pyramid(new Point(2, 2, 0), null, null, null), new Color(180,180,0));
        isometricView.add(new Stairs(new Point(2,3, 0), 10), new Color(180, 180, 0));
    }
}
