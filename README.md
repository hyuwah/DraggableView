# DraggableView
[![](https://jitpack.io/v/hyuwah/DraggableView.svg)](https://jitpack.io/#hyuwah/DraggableView)

DraggableView is an Android library to make floating draggable view easy. 

This library basically wrap your view into DraggableView object, overriding it's onTouchListener and adds some extra utilities

## Preview

<pre>
<img src="https://res.cloudinary.com/hyuwah-github-io/image/upload/v1611740627/DraggableView/basic_example.gif" width="30%">   <img src="https://res.cloudinary.com/hyuwah-github-io/image/upload/v1611740628/DraggableView/scrolling_example.gif" width="30%">   <img src="https://res.cloudinary.com/hyuwah-github-io/image/upload/v1611740632/DraggableView/overlay_other_app_example.gif" width="30%">
</pre>

## Setup 
In your root gradle add dependency to Jitpack:
```gradle
// project build.gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
In you module gradle add dependency to library:
```gradle
// module / app build.gradle
dependencies {
    ...
    implementation 'com.github.hyuwah:DraggableView:LatestVersion'
}
```

<div style="display:flex">
    <p style="margin-right:8px">Latest Version:</p>
    <a href="https://jitpack.io/#hyuwah/DraggableView">
        <img src="https://jitpack.io/v/hyuwah/DraggableView.svg" />
    </a>
</div>

**Breaking changes from `0.5.0` to `1.0.0`**



---

## Docs

### Basic Usage

```kotlin
// Need to declare draggable view in your activity / fragment
private lateinit var someDraggableView: DraggableView<ImageView> // can be other type of View or ViewGroup

// ... When setting up the view (i.e. onCreate) ...

// assign via DraggableView class
someDraggableView = DraggableView.Builder(someView).build()

// or assign via extensions
someDraggableView = someView.setupDraggable().build() // setupDraggable() returns DraggableView.Builder

// Above code will make `someView` draggable with default settings (Non-sticky / floating)
```

**Builder options**

Options|Parameter|Default|Description
---|---|---|---
setStickyMode() | `Draggable.Mode.NON_STICKY`, `Draggable.Mode.STICKY_X`, `Draggable.Mode.STICKY_Y`, `Draggable.Mode.STICKY_XY` | `Draggable.Mode.NON_STICKY` | Set stickiness mode
setAnimated() | `true`, `false` | `true` | To animate the sticky movement, doesn't affect NON_STICKY
setListener() | `null`, `DraggableListener` implementation | `null` | Listener when view position's changed


```kotlin
private lateinit var someDraggableView: DraggableView<ImageView> // can be other View or ViewGroup

// ... inside onCreate
// via DraggableView class with builder options
someDraggableView = DraggableView.Builder(someView)
                        .setStickyMode(DraggableView.Mode.NON_STICKY)
                        .setListener(this)
                        .build()
// or via extensions with builder options
someDraggableView = someView.setupDraggable()
                        .setStickyMode(DraggableView.Mode.STICKY_X)
                        .setAnimated(true)
                        .build()

```


**Methods**

Method|Description
---|---
getView() | returns the original view
setViewPosition(x, y) | programmatically set view position
enableDrag() | enable draggable / override the onTouchListener
disableDrag() | disable draggable / set the onTouchListener to null
show(duration) | show the view with expanding animation (duration default to 300ms if omitted)
hide(duration) | hide the view with shrinking animation (duration default to 300ms if omitted)
dockToEdge() | dock the view to left or right (only applies to MODE.STICKY_X for now)
undock() | undock the view if it's docked


You can still add click listener to your view as usual with `originalView.setOnClickListener` directly to your original view or via `wrappedDraggableView.getView().setOnClickListener`

**Listener**

```kotlin
private var someDraggableListener = object: DraggableListener {
    override fun onPositionChanged(view: View) {
        // Here you can access x & y of the view while moving
        Log.d(TAG, "X: ${view.x.toString()}, Y: ${view.y.toString()}")
    }
}


// set the listener when creating the draggable view
someDraggableView = DraggableView.Builder(someView)
    .setListener(someDraggableListener)
    .build()

// or to existing one
anotherDraggableView.listener = someDraggableListener
```

Check example module for actual implementation
- [Basic Example (Kotlin)](https://github.com/hyuwah/DraggableView/blob/master/example/src/main/java/io/github/hyuwah/draggableview/BasicExampleActivity.kt)
- [Basic Example (Java)](https://github.com/hyuwah/DraggableView/blob/master/example/src/main/java/io/github/hyuwah/draggableview/JavaMainActivity.java)
- [Scrolling Activity (Kotlin)](https://github.com/hyuwah/DraggableView/blob/master/example/src/main/java/io/github/hyuwah/draggableview/ScrollingActivity.kt)

### Draggable view over other App (Overlay)

> Tested working on API 25, 28 & 29
> Not working as of now on API 19 (on investigation)

This is the simplest way to setup an overlay draggable view, assuming it will be started from an activity.

It's best to use foreground service for the overlay draggable view

Some notes:
* On the service, implement `OverlayDraggableListener`
* We need to make the view programmatically, here i'm creating a TextView, you can also inflate a layout


```kotlin
class OverlayService : Service(), OverlayDraggableListener {

    private lateinit var overlayView: TextView
    private var isOverlayOn = false
    private var params: WindowManager.LayoutParams? = null
    private lateinit var windowManager: WindowManager

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isOverlayOn = if(isOverlayOn){
            windowManager.removeViewImmediate(overlayView)
            false
        } else {
            windowManager.addView(overlayView, params)
            true
        }
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        initOverlayView()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isOverlayOn){
            windowManager.removeViewImmediate(overlayView)
        }
        isOverlayOn = false
    }

    override fun onParamsChanged(updatedParams: WindowManager.LayoutParams) {
        windowManager.updateViewLayout(overlayView, updatedParams)
    }

    private fun initOverlayView() {
        overlayView = TextView(this)
        overlayView.text = "Overlay Text View"
        overlayView.textSize = 32f
        overlayView.setTextColor(Color.rgb(255, 255, 0))
        overlayView.setShadowLayer(10f, 5f, 5f, Color.rgb(56, 56, 56))
        overlayView.setOnClickListener {
            Toast.makeText(this, "Overlay view clicked", Toast.LENGTH_SHORT).show()
        }

        params = overlayView.makeOverlayDraggable(this)

    }
```

You also need to add some permission and define your service in manifest
```xml
<manifest>
    ...
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.ACTION_MANAGE_OVERLAY_PERMISSION" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    ...
    <application>
        ...
        <service
            android:name=".overlay.OverlayService"
            android:enabled="true"
            android:exported="false"></service>
        ...
    </application>
</manifest>
```

Also, before starting the service, you need to check if the device support overlay and required permission

```kotlin
// in Activity

private var isOverlayOn = false // Global flag to toggle start / stop service

// Check draw over other app permission before starting the service
private fun toggleOverlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !Settings.canDrawOverlays(this)
        ) {
            // Get permission first on Android M & above
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivityForResult(intent, 1234)
        } else {
            val i = Intent(this, OverlayService::class.java)
            if (!isOverlayOn) {
                // Show
                startService(i)
            } else {
                // Hide
                stopService(i)
            }
            isOverlayOn = !isOverlayOn
        }
    }
```

Check the example here 
- [Activity (Kotlin)](https://github.com/hyuwah/DraggableView/blob/master/example/src/main/java/io/github/hyuwah/draggableview/OverlayDraggableActivity.kt)
- [Service (Kotlin)](https://github.com/hyuwah/DraggableView/blob/master/example/src/main/java/io/github/hyuwah/draggableview/overlay/OverlayService.kt)

---
## Related Article

* [Implementasi DraggableView di Android (Bahasa)](https://medium.com/@hyuwah/implementasi-draggable-view-di-android-eb84e50fbba9)

## License
**DraggableView** is available under the MIT license. See the [LICENSE](https://github.com/hyuwah/DraggableView/blob/master/LICENSE) file for more info.