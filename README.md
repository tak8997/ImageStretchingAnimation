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
