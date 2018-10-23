# ImageStretchingAnimation
Image stretching animation

## Include your project
add build.gradle
```
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
```
dependencies {
	implementation 'com.github.tak8997:ImageStretchingAnimation:0.2.1'
}
```

## Usage

```xml
<com.github.byungtak.library.ImageStretchingAnimation
    android:id="@+id/image_stretching_animation"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
```Java
image_stretching_animation.setContainerViewId(R.id.image_stretching_animation)
image_stretching_animation.setDisplayingImage("avatar_dave")
image_stretching_animation.setStretchingTargetImages(
	"avatar_lucy",
	"avatar_valarie",
	"avatar_henry",
	"avatar_albert"
)
image_stretching_animation.setImagesInterval(250)
image_stretching_animation.setImageClickListener(object : ImageStretchingAnimation.ImageTouchListener {
    override fun onImageDelivered(index: Int) {

    }
})
```

License
-------
```
MIT License

Copyright (c) 2018 ByungTak

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
