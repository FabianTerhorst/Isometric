# Isometric
Isometric drawing library for Android

### Drawing a simple cube

```java
isometricView.add(
	new Prism(
		new Point(/* x */ 0, /* y */ 0, /* z */ 0), 
		/* width */ 1, /* length */ 1, /* height */ 1
	), 
	new Color(33, 150, 243)
);
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotOne.png?raw=true)

### Drawing multiple Shapes
#### There are 3 basic components: points, paths and shapes. A shape needs an origin point and 3 measurements for the x, y and z axes. The default Prism constructor is setting all measurements to 1.

```java
isometricView.add(new Prism(new Point(0, 0, 0)), new Color(33, 150, 243));
isometricView.add(new Prism(new Point(-1, 1, 0), 1, 2, 1), new Color(33, 150, 243));
isometricView.add(new Prism(new Point(1, -1, 0), 2, 1, 1), new Color(33, 150, 243));
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotTwo.png?raw=true)

### Drawing multiple Paths
#### Paths are two dimensional. You can draw and color paths the same as shapes.

```java
isometricView.add(new Prism(Point.ORIGIN, 3, 3, 1), new Color(50, 60, 160));
isometricView.add(new Path(new Point[]{
    new Point(1, 1, 1),
    new Point(2, 1, 1),
    new Point(2, 2, 1),
    new Point(1, 2, 1)
}), new Color(50, 160, 60));
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotPath.png?raw=true)

### The grid
#### Here you can see how the grid looks like. The blue grid is the xy-plane. The z-line is the z-axis.

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotGrid.png?raw=true)

### Supports complex structures

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotThree.png?raw=true)

# Include in your project
## Using JCenter
```groovy
compile 'io.fabianterhorst:Isometric:0.0.9'
```

### Available Shapes
#### [Cylinder](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Cylinder.java), [Knot](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Knot.java), [Octahedron](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Octahedron.java), [Prism](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Prism.java), [Pyramid](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Pyramid.java) and [Stairs](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Stairs.java)

### Translate
#### Traslate is translating an point, path or shape to the given x, y and z distance. Translate is returning a new point, path or shape.

```java
Prism prism = new Prism(new Point(0, 0, 0));
isometricView.add(prism, new Color(33, 150, 243));
isometricView.add(prism.translate(0, 0, 1.1), new Color(33, 150, 243));
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotTranslate.png?raw=true)

### Scale
#### Scale is scaling an point, path or shape with the given x, y and z scaling factors. Scale is returning a new point, path or shape.

```java
Color blue = new Color(50, 60, 160);
Color red = new Color(160, 60, 50);
Prism cube = new Prism(Point.ORIGIN);
isometricView.add(cube.scale(Point.ORIGIN, 3.0, 3.0, 0.5), red);
isometricView.add(cube
	.scale(Point.ORIGIN, 3.0, 3.0, 0.5)
	.translate(0, 0, 0.6), blue);
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotScale.png?raw=true)

### RotateZ
#### RotateZ is rotating an point, path or shape with the given angle in radians on the xy-plane (where an angle of 0 runs along the position x-axis). RotateZ is returning a new point, path or shape.

```java
Color blue = new Color(50, 60, 160);
Color red = new Color(160, 60, 50);
Prism cube = new Prism(Point.ORIGIN, 3, 3, 1);
isometricView.add(cube, red);
isometricView.add(cube
	/* (1.5, 1.5) is the center of the prism */
	.rotateZ(new Point(1.5, 1.5, 0), Math.PI / 12)
	.translate(0, 0, 1.1), blue);
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotRotateZ.png?raw=true)

### Shapes from Paths
#### The method ```Shape.extrude``` allows you to create a 3D model by popping out a 2D path along the z-axis.

```java
Color blue = new Color(50, 60, 160);
Color red = new Color(160, 60, 50);
isometricView.add(new Prism(Point.ORIGIN, 3, 3, 1), blue);
isometricView.add(Shape.extrude(new Path(new Point[]{
	new Point(1, 1, 1),
	new Point(2, 1, 1),
	new Point(2, 3, 1)
}), 0.3), red);
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotExtrude.png?raw=true)

### Available Shapes
#### [Cylinder](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Cylinder.java)

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotCylinder.png?raw=true)

[Knot](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Knot.java)

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotKnot.png?raw=true)

[Octahedron](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Octahedron.java)

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotOctahedron.png?raw=true)

[Prism](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Prism.java)

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotPrism.png?raw=true)

[Pyramid](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Pyramid.java) 

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotPyramid.png?raw=true)

[Stairs](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Stairs.java)

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotStairs.png?raw=true)

# Developed By

* Fabian Terhorst
  * [github.com/FabianTerhorst](https://github.com/FabianTerhorst) - <fabian.terhorst@gmail.com>
  * [paypal.me/fabianterhorst](http://paypal.me/fabianterhorst)


# Contributors

* **[cameronwhite08](https://github.com/cameronwhite08)**


# License

    Copyright 2017 Fabian Terhorst

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.