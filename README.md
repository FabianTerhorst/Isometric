# Isometric
Isometric drawing library for Android

### Drawing a simple cube

```java
isometricView.add(new Prism(new Point(0, 0, 0)), new Color(33, 150, 243));
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotOne.png?raw=true)

### Drawing multiple Shapes

```java
isometricView.add(new Prism(new Point(0, 0, 0)), new Color(33, 150, 243));
isometricView.add(new Prism(new Point(-1, 1, 0), 1, 2, 1), new Color(33, 150, 243));
isometricView.add(new Prism(new Point(1, -1, 0), 2, 1, 1), new Color(33, 150, 243));
```

![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotTwo.png?raw=true)

### Drawing multiple Paths

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

### How the grid looks like


![Image](https://github.com/FabianTerhorst/Isometric/blob/master/lib/screenshots/io.fabianterhorst.isometric.screenshot.IsometricViewTest_doScreenshotGrid.png?raw=true)

# Include in your project
## Using Maven
#### build.gradle file (currently required until its been published at jcenter)
```groovy
repositories {
    maven {
        url  "http://dl.bintray.com/fabianterhorst/maven" 
    }
}
...
compile 'io.fabianterhorst:Isometric:0.0.2'
```