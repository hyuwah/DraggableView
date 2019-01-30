# DraggableView
[![](https://jitpack.io/v/hyuwah/DraggableView.svg)](https://jitpack.io/#hyuwah/DraggableView)

DraggableView is an Android library to make floating draggable view easy, currently it only extends from ImageView.

## Setup

On root / project **build.gradle** file add `maven { url 'https://jitpack.io' }`

Example:
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

On app **build.gradle** add `implementation 'com.github.hyuwah:DraggableView:LatestVersion'`

Example:
```
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
On Layout XML file
```
<io.github.hyuwah.draggableviewlib.DraggableImageView
            android:src="@mipmap/ic_launcher_round"
            android:id="@+id/draggableView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
    />
```

On Activity / Fragment file
```
var dv = findViewById<DraggableImageView>(R.id.draggableView)
dv.setOnClickListener {
    // TODO on click
}
```

Check `example` module for actual implementation

## License
`DraggableView` is available under the MIT license. See the [LICENSE](https://github.com/hyuwah/DraggableView/blob/master/LICENSE) file for more info.