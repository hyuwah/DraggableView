# DraggableView
[![](https://jitpack.io/v/hyuwah/DraggableView.svg)](https://jitpack.io/#hyuwah/DraggableView)

DraggableView is an Android library to make floating draggable view easy.

![Preview](https://miro.medium.com/max/314/1*dMzIJlT12hmSTkVkzNnxEQ.gif)

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

**Note:** check the number on Jitpack badge above for latest version

## Usage

### CustomView (XML)

Currently i've only provide CustomView that extends ImageView. For other view, see **Programmatically** usage below

#### Customizable Attributes

**DraggableImageView**

Attribute | Value (Default) | XML | Code
---|---|---|---
Animate | true, false (false) | animate | setAnimate(boolean isAnimate)
Sticky Axis | NON_STICKY, STICKY_AXIS_X, STICKY_AXIS_Y, STICKY_AXIS_XY (NON_STICKY) | sticky | setStickyAxis(int axis)

#### On Layout XML file
```xml
<io.github.hyuwah.draggableviewlib.DraggableImageView
            android:src="@mipmap/ic_launcher_round"
            android:id="@+id/draggableView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"/>
```

#### On Activity / Fragment file
```kotlin
var dv = findViewById<DraggableImageView>(R.id.draggableView)
dv.setOnClickListener {
    // TODO on click
}
```

You can add a DraggableListener programmatically via `setListener()` method directly on the view, see below explanation about the listener

### Programmatically

#### Using extension (Kotlin)

You can extent any view or viewgroup to be draggable (i.e. Button, FrameLayout, Linearlayout, LottieView, etc)

```kotlin
// Method signature
fun View.makeDraggable(
    stickyAxis: Draggable.STICKY = Draggable.STICKY.NONE,
    animated: Boolean = true,
    draggableListener: DraggableListener? = null
){
    ...
}
```

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

tv.makeDraggable(Draggable.STICKY.AXIS_X, false) // set sticky axis to x & animation to false
tv.makeDraggable(Draggable.STICKY.AXIS_XY) // set sticky axis to xy
tv.makeDraggable() // all default

// First param is the axis (optional)
// - Draggable.STICKY.AXIS_X
// - Draggable.STICKY.AXIS_Y
// - Draggable.STICKY.AXIS_XY
// - Draggable.STICKY.NONE (default)

// Second param is animation flag (optional)
// - true or false (default is true)
// *Sticky.NONE doesn't get affected by this flag

// Third param is listener (optional)
// - DraggableListener implementation (default is null)
```

#### Using DraggableUtils (Java)

If you're on java class, you could do it with the help of DraggableUtils

```java
class DraggableUtils {
    
    // Method signature
    public static void makeDraggable(
        View $self,
        Draggable.STICKY stickyAxis,
        boolean animated,
        DraggableListener draggableListener
        ) {
            ...
        }
}
 
```

Here's some example using Button:

```xml
<Button
            android:id="@+id/tv_test_draggable"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="DRAGGABLE BUTTON!" />
```

```java
Button button = findViewById(R.id.tv_test_draggable);

DraggableUtils.makeDraggable(button, Draggable.STICKY.AXIS_X, false) // set sticky axis to x & animation to false
DraggableUtils.makeDraggable(button, Draggable.STICKY.AXIS_XY) // set sticky axis to xy
DraggableUtils.makeDraggable(button) // all default

// First param is the view

// Second param is the axis (optional)
// - Draggable.STICKY.AXIS_X
// - Draggable.STICKY.AXIS_Y
// - Draggable.STICKY.AXIS_XY
// - Draggable.STICKY.NONE (default)

// Third param is animation flag (optional)
// - true or false (default is true)
// *Sticky.NONE doesn't get affected by this flag

// Fourth param is listener (optional)
// - DraggableListener implementation (default is null)
```

#### DraggableListener
There's an interface `DraggableListener` to listen to the `View` while being dragged / moved

```kotlin
interface DraggableListener {
    fun onPositionChanged(view: View)
}
```
Just pass the implementation of the interface to `makeDraggable` method

```kotlin
someView.makeDraggable(object: DraggableListener{
    override fun onPositionChanged(view: View){
        // Do something, get coordinates of view, etc
    }
})

// *Java counterpart must supply all 3 other params to use the listener
```

Check example module [kotlin](https://github.com/hyuwah/DraggableView/blob/master/example/src/main/java/io/github/hyuwah/draggableview/MainActivity.kt), [java](https://github.com/hyuwah/DraggableView/blob/master/example/src/main/java/io/github/hyuwah/draggableview/JavaMainActivity.java) for actual implementation

## Accompanying Article

* [Implementasi DraggableView di Android (Bahasa)](https://medium.com/@hyuwah/implementasi-draggable-view-di-android-eb84e50fbba9)

## License
`DraggableView` is available under the MIT license. See the [LICENSE](https://github.com/hyuwah/DraggableView/blob/master/LICENSE) file for more info.