# Isometric
Isometric drawing library for Android

### Drawing a simple cube

```java
isometricView.add(new Prism(new Point(0, 0, 0)), new Color(33, 150, 243));
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
compile 'io.fabianterhorst:Isometric:0.0.3'
```

### Available Shapes
#### [Cylinder](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Cylinder.java), [Knot](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Knot.java), [Octahedron](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Octahedron.java), [Prism](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Prism.java), [Pyramid](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Pyramid.java) and [Stairs](https://github.com/FabianTerhorst/Isometric/blob/master/lib/src/main/java/io/fabianterhorst/isometric/shapes/Stairs.java)

### Translate
#### Traslate is translating an point, path and shape to the given x, y and z distance. Translate is returning a new point, path or shape.

```java
Prism prism = new Prism(new Point(0, 0, 0));
isometricView.add(prism, new Color(33, 150, 243));
isometricView.add(prism.translate(0, 0, 1.1), new Color(33, 150, 243));
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotTranslate.png?raw=true)