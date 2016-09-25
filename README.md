# AndroidMaterialDialog - README

"AndroidMaterialDialog" is an Android-library, which provides a builder, which allows to create dialogs, which are designed according to Android 5's Material Design guidelines even on pre-Lollipop devices.

![](doc/images/example1.png)

![](doc/images/example2.png)

The library provides the following features:

- The builder has been designed to be similar to the Android SDK's built-in class `android.app.AlertDialog.Builder` and therefore provides all of its functionalities, including the possibilities to create dialogs with a title, icon, message, up to three buttons and optional list items.
- The library uses the AppCompat v7 support library in order to retrieve the color of a dialog's button texts from the applied theme. Alternatively, these colors can be set manually using the builder.
- A dialog's title, message and content can be replaced by any custom view.
- The builder supports to create dialogs, whose buttons are stacked, as introduced in the Material Design guidelines.
- All dialogs can contain a header, which shows a background image or color as well as an optional icon.
- The library provides additional builders, which allow to create progress dialogs or a dialog, which allows to switch between multiple fragments.


## License Agreement

This project is distributed under the Apache License version 2.0. For further information about this license agreement's content please refer to its full version, which is available at http://www.apache.org/licenses/LICENSE-2.0.txt.

Prior to version 3.1.1 this library was distributed under the GNU Lesser General Public License version 3.0 (GLPLv3).

## Download

The latest release of this library can be downloaded as a zip archive from the download section of the project's Github page, which is available [here](https://github.com/michael-rapp/AndroidMaterialDialog/releases). Furthermore, the library's source code is available as a Git repository, which can be cloned using the URL https://github.com/michael-rapp/AndroidMaterialDialog.git.

Alternatively, the library can be added to your Android app as a Gradle dependency by adding the following to the respective module's `build.gradle` file:

```groovy
dependencies {
    compile 'com.github.michael-rapp:android-material-dialog:3.6.9'
}
```

Before version 2.0.0 this project was hosted on [Sourceforge](https://sourceforge.net/projects/androidmaterialdialog). These older versions used the legacy Eclipse ADT folder structure and are not available as Gradle artifacts.

## Examples

### Creating a theme for the dialogs

This library comes with a built-in dark and light theme for the dialogs it provides. By default the light theme is used. It can be referenced by using the resource id `@style/MaterialDialog.Light` (or the id `R.style.MaterialDialog_Light` when referencing it in Java code). The dark theme can be referenced by using the resource id `@style/MaterialDialog` or `R.style.MaterialDialog` respectively. In order to use one of the predefined themes when creating a dialog, the id of the theme has to be passed to the constructor of the builder `MaterialDialog.Builder` (or another builder provided by the library) like shown in the examples below. In order to globally overwrite the theme, which should be used by all of the library's builders, the theme attribute `materialDialogTheme` can be specified in the app's theme.

In addition, it might be useful to extend the predefined themes in order to overwrite some theme attributes. One common use-case is to overwrite the theme attribute `colorAccent`, which specifies the default text color of a dialog's buttons. In such case a new style resource, which extends one of the built-in themes, must be added to your app's `res/values/styles.xml` file. The following example illustrates how such a style can be defined and can be set as the default theme for all of the library's builders:

```xml
<resources>

    <style name="AppTheme" parent="@style/Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/color_primary</item>
        <item name="colorPrimaryDark">@color/color_primary_dark</item>
        <item name="colorAccent">@color/color_accent</item>
        <item name="materialDialogTheme">@style/CustomLightTheme</item>
    </style>

    <style name="CustomLightTheme" parent="@style/MaterialDialog.Light">
        <item name="colorAccent">@color/color_accent</item>
    </style>

</resources>
```

### Creating a typical alert dialog

The code below shows how to create and show an alert dialog by using the library's `MaterialDialog.Builder`. The dialog contains a title and message and can be closed by the user by either using a "OK" button or a "Cancel" button. The `this` parameter, which is passed to the builder's constructor in the example below, must be a `Context`, e.g. an `Activity`. The `null` parameters, which are passed to the `setPositiveButton`- and `setNegativeButton`-methods can be replaced by instances of the type `DialogInterface.OnClickListener` in order to execute some code when the user closes the dialog by clicking the corresponding button. As the methods of the class `MaterialDialog.Builder`, which is provided by this library, are nearly identical to the API of the Android SDK's class [`AlertDialog.Builder`](http://developer.android.com/reference/android/app/AlertDialog.Builder.html), the practices, which are described in [this](http://developer.android.com/guide/topics/ui/dialogs.html#AlertDialog) section of the Android developer guide, do also work together with this library.

```java
MaterialDialog.Builder dialogBuilder = new MaterialDialog.Builder(this, R.style.MaterialDialog_Light); 
dialogBuilder.setTitle(R.string.dialog_title); 
dialogBuilder.setMessage(R.string.dialog_message); 
dialogBuilder.setTitle(R.string.dialog_title); 
dialogBuilder.setPositiveButton(android.R.string.ok, null); 
dialogBuilder.setNegativeButton(android.R.string.cancel, null); 
MaterialDialog dialog = dialogBuilder.create();
dialog.show();
```

The appearance of the dialog, which is created by the given sample code, is shown below:

![](doc/images/example-alert-dialog.png)

### Showing a header

All dialogs can contain a header, which shows a background image or color as well as an optional icon. The following code shows how a dialog can be configured to contain such a header once it has been instantiated like shown above. The resource IDs, which have to be passed as the parameters of the 'setHeaderBackground'- and 'setHeaderIcon'-methods, must correspond to valid drawable resources. Alternatively instances of the class [AlertDialog.Builder](http://developer.android.com/reference/android/app/AlertDialog.Builder.html) can be passed to the methods.

```java
dialogBuilder.showHeader(true); 
dialogBuilder.setHeaderBackground(R.drawable.header_background); 
dialogBuilder.setHeaderIcon(R.drawable.header_icon);
```

The picture below shows a dialog, which contains a header with a background image and an icon:

![](doc/images/example-header-dialog.png)

### Creating a progress dialog

The following source code shows how a progress dialog, which displays a circular progress bar, can be created. Such like a regular `MaterialDialog`, such a dialog can contain a title, a message and up to three buttons. By using the `setProgressBarPosition`-method, the position of the dialog's progress bar can be specified. Possible values are `LEFT`, `TOP`, `RIGHT` and `BOTTOM`, each specifying the position relative to the dialog's message.

```java
ProgressDialog.Builder dialogBuilder = new ProgressDialog.Builder(this, R.style.MaterialDialog_Light); 
dialogBuilder.setTitle(R.string.dialog_title); 
dialogBuilder.setMessage(R.string.dialog_message); 
dialogBuilder.setPositiveButton(android.R.string.ok, null); 
dialogBuilder.setNegativeButton(android.R.string.cancel, null); 
dialogBuilder.setProgressBarPosition(ProgressBarPosition.LEFT) 
ProgressDialog dialog = dialogBuilder.create(); 
dialog.show();
```

The screenshot below shows the appearance of a `ProgressDialog`, which has been created using the given sample code:

![](doc/images/example-progress-dialog.png)

### Creating a wizard dialog

A `WizardDialog` allows to show multiple fragments and provides a navigation to switch between them, either via tabs or via buttons at the bottom of the dialog. The source code below shows how such a dialog can be created. The tabs, which indicate the currently selected fragment, are shown in the dialog's header if possible. This behavior can be customized by using the `setTabPosition`-method. If the value `USE_HEADER` is passed to the method, the tabs are shown in the header, if the dialog does neither contain a title, nor a message. The default value `PREFER_HEADER` causes the tabs to be shown in the header regardless of any title or message are shown. And the value `NO_HEADER` prevents the tabs from being shown in the header at all. By default, the tabs, which are shown by the dialog are not clickable and the currently shown fragment can only be changed using swipe gestures or the button bar. If the tabs should be clickable, the `enableTabLayout`-method has to be used. Furthermore, the detection of swipe gestures can be disabled using the `enableSwipe`-method and visibility of the dialog's button bar can be toggled using the `showButtonBar`-method. 

```java
WizardDialog.Builder dialogBuilder = new WizardDialog.Builder(this, R.style.MaterialDialog_Light); 
dialogBuilder.showHeader(true); 
dialogBuilder.setHeaderBackground(R.drawable.header_background); 
dialogBuilder.addFragment(R.string.fragment1_title, Fragment1.class); 
dialogBuilder.addFragment(R.string.fragment2_title, Fragment2.class); 
dialogBuilder.addFragment(R.string.fragment3_title, Fragment3.class); 
dialogBuilder.setTabPosition(TabPosition.PREFER_HEADER);
dialogBuilder.enableTabLayout(true);
dialogBuilder.enableSwipe(true);
dialogBuilder.showButtonBar(true); 
WizardDialog dialog = dialogBuilder.create(); 
dialog.show();
```

The following screenshot illustrates one possible appearance of a `WizardDialog`:

![](doc/images/example-wizard-dialog.png)

## Contact information

For personal feedback or questions feel free to contact me via the mail address, which is mentioned on my [Github profile](https://github.com/michael-rapp). If you have found any bugs or want to post a feature request please use the [bugtracker](https://github.com/michael-rapp/AndroidMaterialViews/issues) to report them.
