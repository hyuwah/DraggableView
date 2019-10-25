# DraggableView
[![](https://jitpack.io/v/hyuwah/DraggableView.svg)](https://jitpack.io/#hyuwah/DraggableView)

DraggableView is an Android library to make floating draggable view easy, currently it only extends from ImageView.
If you're using kotlin there's also an extension to make any view draggable.

## Setup

On root / project **build.gradle** file add `maven { url 'https://jitpack.io' }`

Example:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

On app **build.gradle** add `implementation 'com.github.hyuwah:DraggableView:LatestVersion'`

Example:
```gradle
dependencies {
    ...
    implementation 'com.github.hyuwah:DraggableView:LatestVersion'
    ...
}
```

**Note:** check the number on jitpack badge above for latest version

## Usage

### Customizable Attributes

**DraggableImageView**

Attribute | Value (Default) | XML | Code
---|---|---|---
Animate | true, false (false) | animate | setAnimate(boolean isAnimate)
Sticky Axis | NON_STICKY, STICKY_AXIS_X, STICKY_AXIS_Y, STICKY_AXIS_XY (NON_STICKY) | sticky | setStickyAxis(int axis)


### Basic sample

#### Using xml view (DraggableImageView)

On Layout XML file
```xml
<io.github.hyuwah.draggableviewlib.DraggableImageView
            android:src="@mipmap/ic_launcher_round"
            android:id="@+id/draggableView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
```

On Activity / Fragment file
```kotlin
var dv = findViewById<DraggableImageView>(R.id.draggableView)
dv.setOnClickListener {
    // TODO on click
}
```

#### Using extension (Kotlin only)

You can extent any view or viewgroup to be draggable (i.e. Button, FrameLayout, Linearlayout, LottieView, etc)

Here's some example using TextView:

```xml
<TextView
            android:id="@+id/tv_test_draggable"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="DRAG ME!" />
```

```kotlin
var tv = findViewById<TextView>(R.id.tv_test_draggable)

tv.makeDraggable(Constants.STICKY.AXIS_X, true)

// First param is the axis:
// - Constants.STICKY.AXIS_X
// - Constants.STICKY.AXIS_Y
// - Constants.STICKY.AXIS_XY
// - Constants.STICKY.NONE

// Second param is animation toggle
// - true or false
```


Check [example](https://github.com/hyuwah/DraggableView/blob/master/example/src/main/java/io/github/hyuwah/draggableview/MainActivity.kt) module for actual implementation

## Accompanying Article

* [Implementasi DraggableView di Android (Bahasa)](https://medium.com/@hyuwah/implementasi-draggable-view-di-android-eb84e50fbba9)

## License
`DraggableView` is available under the MIT license. See the [LICENSE](https://github.com/hyuwah/DraggableView/blob/master/LICENSE) file for more info.