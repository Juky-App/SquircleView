<h1 align="center">SquircleView</h1></br>
<p align="center"><img src="./assets/icon.svg" alt="Icon" height="200px"></p>
<p align="center">SquircleView is a library which provides you with Squircle views to use for buttons, views, etc.</p>

<br/>

<p align="center">
  <a href="https://opensource.org/licenses/MIT"><img alt="License" src="https://img.shields.io/badge/License-MIT-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=23"><img alt="API" src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat"/></a>
  <a><img alt="Release version" src="https://img.shields.io/github/v/release/Juky-App/SquircleView"/></a>
  <a><img alt="Build status" src="https://img.shields.io/github/workflow/status/Juky-App/SquircleView/release"/></a>
  <a><img alt="Contributors" src="https://img.shields.io/github/contributors/Juky-App/SquircleView"/></a>
  <a><img alt="Stars" src="https://img.shields.io/github/stars/Juky-App/SquircleView"/></a>
  <a><img alt="Issues" src="https://img.shields.io/github/issues/Juky-App/SquircleView"/></a>
</p>

## Screenshots

Different kinds of buttons, layouts and images you can create
<p>
<img src="./assets/screenshots.png" alt="Icon">
</p>

---

## Table of Contents

1. [How to use](#how-to-use)
2. [Usage](#usage)
    1. [SquircleImageView](#squircleimageview)
    2. [SquircleButton](#squirclebutton)
    3. [SquircleConstraintLayout](#squircleconstraintlayout)
    4. [Load image](#load-image)
    5. [Attributes](#attributes)
    6. [Methods](#methods)
3. [Todo](#todo)
4. [Contributing](#contributing)
5. [Contributors](#contributors)
6. [Showcase](#showcase)
7. [Changelog](#changelog)
8. [Attribution](#attribution)
9. [License](#license)

---

## How to use

<a><img alt="Maven Central" src="https://img.shields.io/maven-central/v/app.juky/squircleview"/></a>
<a><img alt="Maven" src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fcom%2Fapp%2Fjuky%2Fsquircleview%2Fmaven-metadata.xml"/></a>
<!--<a><img alt="Jitpack" src="https://img.shields.io/jitpack/v/github/jitpack/maven-simple"/></a>-->

Add the Maven repository to your root build.gradle file:

```groovy
allprojects {
    repositories {
        mavenCentral()
    }
}
```

Also add the SquircleView dependency to your app build.gradle

```groovy
dependencies {
    implementation "app.juky:squircleview:0.0.1"
}
```

## Usage

For all use cases, check out the [sample](/sample) app which contains a bunch of different configurations.

#### SquircleImageView

This view extends the `AppCompatImageView`, which you can use to have a squircle image.

```xml

<squircleview.views.SquircleImageView
	android:id="@+id/imageButton"
	android:layout_width="100dp"
	android:layout_height="100dp"
	app:squircle_background_color="#FF00FF"
	app:squircle_background_image="@drawable/first_image"
	app:squircle_border_color="#000000"
	app:squircle_border_width="4dp"/>
```

#### SquircleButton

This view extends the `AppCompatTextView`, which you can use to have a squircle button

```xml

<squircleview.views.SquircleButton
	android:id="@+id/normalButton"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"
	android:padding="16dp"
	android:text="Normal button"
	android:textColor="#FFFFFF"
	app:squircle_gradient_end_color="#415FFF"
	app:squircle_gradient_start_color="#5BA7FF"
	app:squircle_shadow_elevation="2dp"/>
```

#### SquircleConstraintLayout

This view extends the `ConstraintLayout`, which you can use to add all sorts of view to your squircle, like an icon or a
complex layout with texts and icons.

````xml

<squircleview.views.SquircleConstraintLayout
	android:layout_width="72dp"
	android:layout_height="72dp"
	android:padding="16dp"
	app:squircle_gradient_end_color="#415FFF"
	app:squircle_gradient_start_color="#5BA7FF"
	app:squircle_shadow_elevation="2dp">

	<!-- Embed whatever widget you would like, in this case an icon -->
	<androidx.appcompat.widget.AppCompatImageView
		android:layout_width="32dp"
		android:layout_height="32dp"
		android:src="@drawable/ic_emoji"
		android:tint="@color/white"
		app:layout_constraintBottom_toBottomOf="parent"
		app:layout_constraintEnd_toEndOf="parent"
		app:layout_constraintStart_toStartOf="parent"
		app:layout_constraintTop_toTopOf="parent"/>

</squircleview.views.SquircleConstraintLayout>
````

#### Load image

You can load an image in every view using the `setImage` method, but you can also use your favorite image loading
library to load it in for you. We have out of the box support for Glide, Picasso, Fresco, Coil, etc.

##### Load image normally

```kotlin
my_squircle_image_view.setImage(ContextCompat.getDrawable(context, R.drawable.my_image))
```

##### Load image using an image loading library like Glide:

```kotlin
Glide.with(this).load(R.drawable.my_image)
	.diskCacheStrategy(DiskCacheStrategy.ALL)
	.into(my_squircle_image_view)
```

### Attributes

| Attribute                       | Type      | Default                            | Description                                             |
|---------------------------------|-----------|------------------------------------|---------------------------------------------------------|
| squircle_background_image       | reference |                                    | Background image of view                                |
| squircle_background_color       | color     | #000000                            | Background color of view                                |
| squircle_gradient_drawable      | reference |                                    | Gradient drawable displayed in view                     |
| squircle_gradient_start_color   | color     |                                    | Gradient start color                                    |
| squircle_gradient_end_color     | color     |                                    | Gradient end color                                      |
| squircle_gradient_direction     | enum      | TOP_LEFT_BOTTOM_RIGHT              | Direction of the gradient (only for the color gradient) |
| squircle_shadow_elevation       | dimension | Default of the super view          | Shadow elevation                                        |
| squircle_shadow_elevation_color | color     | #42000000                          | Shadow elevation color                                  |
| squircle_border_color           | color     |                                    | Border color                                            |
| squircle_border_width           | dimension | 0                                  | Border width                                            |
| squircle_ripple_enabled         | boolean   | true (false for SquircleImageView) | Ripple enabled or disabled                              |

### Methods

```kotlin
// Common methods
fun setImage(drawable: Drawable?)
```

## Todo

- [ ] Inner shadow support
- [ ] Layouts other than ConstraintLayout
- [ ] Expose all attributes via methods
- [ ] Ensure it works on API 21 - 30
- [ ] Check Java support
- [ ] Performance testing with lots of bitmaps
- [ ] Add tests
- [ ] Code documentation
- [ ] Option to determine text color by background / image
- [ ] Use precise angle of gradient instead of matching it to a segment
- [ ] Improve outer shadow boundaries

## Contributing

Check out the [CONTRIBUTING.md](CONTRIBUTING.md) file to know more

## Contributors

<table>
<tr>
    <td align="center">
        <a href="https://github.com/Nielssg">
            <img src="https://avatars.githubusercontent.com/u/20494853?v=4?s=100" width="100px;" alt="" /><br />
            <sub><b>Niels G</b></sub>
        </a>
        <br />
        <a href="https://github.com/Juky-App/SquircleView/commits?author=DemianD" title="Code">💻</a>
        <a href="https://github.com/Juky-App/SquircleView/commits?author=nielssg" title="Documentation">📖</a>
        <a href="#" title="Maintenance">🚧</a>
    </td>
    <td align="center">
        <a href="https://github.com/ThomasBakker">
            <img src="https://avatars.githubusercontent.com/u/22427167?v=4?s=100" width="100px;" alt="" /><br />
            <sub><b>Thomas Bakker</b></sub>
        </a>
        <br />
        <a href="https://github.com/Juky-App/SquircleView/pulls?q=is%3Apr+reviewed-by%3thomasbakker" title="Reviewed Pull Requests">👀</a>
    </td>
</tr>

</table>

## Showcase

<!-- TODO OWN APP -->

## Changelog

- V0.0.1 (10 june 2021): Initial release

## Attribution

Images used in the sample app originate from Unsplash:

- [Llama](https://unsplash.com/photos/CAjfBVgaMZ4)
- [Parrot](https://unsplash.com/photos/lylCw4zcA7I)
- [Panda](https://unsplash.com/photos/lJYi_7NUe04)

This library is inspired by the [cupertino_rounded_corners](https://pub.dev/packages/cupertino_rounded_corners) Flutter
library

## License

```
MIT License

Copyright (c) 2021] Juky

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
