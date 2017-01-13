# ShapeShifter
Provides Polygon shaped imageView

[![API](https://img.shields.io/badge/API-9%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=9)  [![](https://jitpack.io/v/DushyantMainwal/ShapeShifter.svg)](https://jitpack.io/#DushyantMainwal/ShapeShifter) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-ShapeShifter-blue.svg?style=flat)](http://android-arsenal.com/details/1/4866)

# Screenshot
![Screenshot](/screenshot.jpeg)

# Usage
Step 1. Add the JitPack repository to your build file
```groovy
allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
```
Step 2. Add the dependency
```groovy
dependencies {
  compile 'com.github.DushyantMainwal:ShapeShifter:2.1.0'
 }
 ```

# Implementation
###XML Implementation:
```xml
 <com.dushyant.library.PolygonView
        android:id="@+id/polygon"
        android:layout_width="180dp"
        android:layout_height="180dp"
        android:layout_centerHorizontal="true"
        android:layout_marginTop="70dp"
        app:border="true"
        app:borderColor="#fff"
        app:borderWidth="5dp"
        app:rotation="0"
        app:scaleType="centerCrop"
        app:sides="7"
        app:src="@drawable/img" />
        
```

###Java Implementation:
```java
        //xml polygon view
        polygonView = (PolygonView) findViewById(R.id.polygon);
        
        //true if you want to set Border
        polygonView.setBorder(true);
        //to set width of Border
        polygonView.setBorderWidth(10);
        //to set Image Resource
        polygonView.setImageSource(R.drawable.dragon);
        //to rotate the View
        polygonView.setRotateDegree(0);
        //to set Sides of the Polygon
        polygonView.setSides(7);
        //to set Scale Type
        polygonView.setScaleType(PolygonView.ScaleType.CENTRE_CROP);
```

#Licence
```
Copyright (c) 2016 Dushyant Mainwal

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
