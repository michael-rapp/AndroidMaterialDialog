# AndroidMaterialDialog - RELEASE NOTES

## Version 5.2.3 (Feb. 2nd 2020)

A bugfix release, which fixes the following issues:

- Changed the default margin of dialogs on smartphones.

## Version 5.2.2 (Dec. 29th 2019)

A bugfix release, which fixes the following issues:

- The enable states of a `ListDialog`'s list items are now properly restored after orientation changes.
- The listeners that are notified when a `ListDialog`'s list items are clicked are not reset after orientation changes.

## Version 5.2.1 (Dec. 29th 2019)

A bugfix release, which fixes the following issues:

- The icons of a `ListDialog`'s list items are now properly restored after orientation changes.

## Version 5.2.0 (Dec. 28th 2019)

A feature release, which introduces the following changes:

- A `ColorStateList` can now be used to specify the text color of a `ListDialog`'s list items.
- Added `getItemCount`-, `isItemChecked`- and `setItemChecked`-methods to the `ListDialog`.
- Added `isItemEnabled`- and `setItemEnabled`-methods to the `ListDialog`.
- Added the functionality to specify icons for the list items of a `ListDialog`.
- Updated dependency "AndroidUtil" to version 2.1.0.

## Version 5.1.6 (May. 1st 2019)

A bugfix release, which introduces the following changes:

- Fixed the style of an `EditTextDialog`'s `TextInputLayout` when using a version of the Material Design components newer than 1.0.0.

## Version 5.1.5 (Feb. 23th 2019)

A minor release, which introduces the following changes:

- Updated dependency "AndroidMaterialValidation" to version 3.0.1.
- Updated dependency "AndroidMaterialViews" to version 3.0.1.
- Updated dependency "AndroidUtil" to version 2.0.1.

## Version 5.1.4 (Jan. 11th 2019)

A bugfix release, which fixes the following issues:

- The appearance of dialogs on devices with xhdpi screens has been fixed (see https://github.com/michael-rapp/AndroidMaterialDialog/issues/30).

## Version 5.1.3 (Nov. 30th 2018)

A minor release, which introduces the following changes:
.
- The `EditTextDialog` is now cancelable by default.
- The touchable areas of dialogs are now calculated more accurately.

## Version 5.1.2 (Nov. 27th 2018)

A minor release, which introduces the following changes:

- The background of an `EditTextDialog`'s `TextInputLayout` is now hidden.

## Version 5.1.1 (Nov. 26th 2018)

A minor release, which introduces the following changes:

- The keyboard is now shown when an `EditTextDialog` is shown.

## Version 5.1.0 (Nov. 26th 2018)

A feature release, which introduces the following changes:

- Added the dialog `EditTextDialog`.
- Removed the functionality to set the text color of a dialog's buttons when disabled. As an alternative the text color can now be specified as a `ColorStateList`.
- Added the library "AndroidMaterialValidation" as a dependency.

## Version 5.0.0 (Nov. 6th 2018)

A major release, which introduces the following changes:

- Added methods for setting the typeface of a dialog's title, message, items and buttons (see https://github.com/michael-rapp/AndroidMaterialDialog/issues/27)
- The background of dialogs is now styled according to the Material Design 2 guidelines.
- `RecyclerView`s are now used instead of `ListView`s to display the list items of dialogs.
- Migrated the library to Android X.
- Replaced AppCompat themes with MaterialComponent themes.
- Updated dependency "AndroidUtil" to version 2.0.0.
- Updated dependency "AndroidMaterialViews" to version 3.0.0.
- Updated targetSdkVersion to 28.

## Version 4.3.4 (May 2nd 2018)

A minor release, which introduces the following changes:

- Migrated the example app to use the Preference support library.
- Fixed options in the example app not being applied immediately.
- Updated AppCompat v7 support library to version 27.1.1.
- Updated Android Design support library to version 27.1.1.
- Updated dependency "AndroidMaterialViews" to version 2.1.11.
- Updated dependency "AndroidUtil" to version 1.20.2.

## Version 4.3.3 (Feb. 28th 2018)

A bugfix release, which fixes the following issues:

- Fixed possible crashes when showing the same dialog multiple times.

## Version 4.3.2 (Feb. 23th 2018)

A bugfix release, which fixes the following issues:

- Previously set button listeners are not set to `null` anymore when calling a dialog's `onRestoreInstanceState`-method.

## Version 4.3.1 (Feb. 9th 2018)

A bugfix release, which fixes the following issues:

- Fixed a possible exception when using vector drawables on pre-Lollipop devices.
- Updated dependency "AndroidMaterialViews" to version 2.1.10.
- Updated dependency "AndroidUtil" to version 1.20.1.

## Version 4.3.0 (Feb. 9th 2018)

A feature release, which introduces the following changes:

- Vector drawables can now be used as the icon and header icon of dialogs even on pre-Lollipop devices.
- The icon and header icon of dialogs can now be tinted (also even on pre-Lollipop devices).

## Version 4.2.3 (Jan. 26th 2018)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 27 (Android 8.1).
- Updated dependency "AndroidUtil" to version 1.19.0.
- The data structure `ListenerList` is now used for managing event listeners.

## Version 4.2.2 (Dec. 28th 2017)

A bugfix release, which fixes the following issues:

- Fixed an issue, which prevented custom content views such as `EditText` widgets from being selected.
- The divider above the button bar is now prevented from being shown when scrolling, if the button bar is scrollable itself.

## Version 4.2.1 (Dec. 28th 2017)

A bugfix release, which fixes the following issues:

- Fixed an issue, which prevented custom content views from being shown.

## Version 4.2.0 (Dec. 28th 2017)

A feature release, which introduces the following changes:

- A custom window background can now be set. This allows to customize the shape of dialogs (https://github.com/michael-rapp/AndroidMaterialDialog/issues/14). 
- A scrollable area can now be specified for dialogs (https://github.com/michael-rapp/AndroidMaterialDialog/issues/15).
- The maximum height of a dialog's icon is now adjusted to the title height (https://github.com/michael-rapp/AndroidMaterialDialog/issues/16).
- The width, height, padding, margin and other properties of a `WizardDialog` are now properly restored after orientation changes.
- Dividers are now faded in/out when scrolling instead of simply toggling the visibility.
- The `setButtonBarDividerMargin`- and `setButtonBarDividerColor`-methods have been replaced with `setDividerMargin`- and `setDividerColor`-methods.
- Updated AppCompat v7 support library to version 27.0.2.
- Updated Android Design support library to version 27.0.2.
- Updated dependency "AndroidUtil" to version 1.18.2.
- Updated dependency "AndroidMaterialViews" to version 2.1.8.

## Version 4.1.2 (Dec. 23th 2017)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidMaterialDialog/issues/21

## Version 4.1.1 (Dec. 13th 2017)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidMaterialDialog/issues/19

## Version 4.1.0 (Aug. 31th 2017)

A feature release, which introduces the following changes:

- Dialogs can now be shown or hidden using a `FadeAnimation`.
- Custom layouts can now be used to display a dialog's header using the `setCustomHeader`-methods as requested in https://github.com/michael-rapp/AndroidMaterialDialog/issues/13
- Omitted the use of deprecated methods.

## Version 4.0.3 (Aug. 26th 2017)

A minor release, which introduces the following changes:

- Fixed an issue, which prevented the divider above a `WizardDialog`'s button bar from being shown.
- Added setter-method and theme attribute for customizing the left and right margin of the divider above a dialog's button bar.

## Version 4.0.2 (Jul. 20th 2017)

A minor release, which introduces the following changes:

- Added setter-methods and theme attributes for customizing the padding of dialogs.
- Updated `targetSdkVersion` to API level 26 (Android 8.0).
- Updated AppCompat v7 support library to version 25.3.1.
- Updated Android Design support library to version 25.3.1.
- Updated dependency "AndroidUtil" to version 1.17.0.

## Version 4.0.1 (Jan. 26th 2017)

A bugfix release, which introduces the following changes:

- Fixed a crash on tablets in portrait mode.
- Updated `targetSdkVersion` to API level 25 (Android 7.1).
- Updated AppCompat v7 support library to version 25.1.0.
- Updated dependency "AndroidMaterialViews" to version 2.1.4.
- Updated dependency "AndroidUtil" to version 1.12.3.

## Version 4.0.0 (Oct. 14th 2016)

A major release, which introduces the following features:

- Implemented support for built-in animations.
- The size, margin, and gravity of dialogs can now be specified.
- Added fullscreen themes.

## Version 3.6.10 (Sep. 27th 2016)

A bugfix release, which fixes the following issues:

- When a `DialogInterface.OnClickListener` is registered at a button of a dialog, the listener is now only invoked when the validation of the dialog succeeded. 

## Version 3.6.9 (Sep. 26th 2016)

A bugfix release, which fixes the following issues:

- The theme, which is passed to a builder's constructor is now actually used. 

## Version 3.6.8 (Sep. 15th 2016)

A minor release, which introduces the following changes:

- Revised width of dialogs depending the device's screen size.

## Version 3.6.7 (Sep. 14th 2016)

A minor release, which introduces the following changes:

- Added `setCustomButtonBar`-methods, which allow to replace the buttons of a dialog.

## Version 3.6.6 (Sep. 13th 2016)

A bugfix release, which fixes the following issues:

- The disabled text color of a dialog's buttons is now stored/restored in its `onSaveInstanceState`-/`onRestoreInstanceState`-method.

## Version 3.6.5 (Sep. 13th 2016)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidMaterialDialog/issues/6
- https://github.com/michael-rapp/AndroidMaterialDialog/issues/5
- Fixed background color of dialogs when using the dark theme together with AppCompat v7 support library 24.2.0.
- The buttons of dialogs are now grayed-out when disabled

## Version 3.6.4 (Sep. 12th 2016)

A minor release, which introduces the following changes:

- Updated `targetSdkVersion` to API level 24 (Android 7.0).
- Updated AppCompat v7 support library to version 24.2.0.
- Updated dependency "AndroidUtil" to version 1.11.1.
- Updated dependency "AndroidMaterialViews" to version 2.1.2.

## Version 3.6.3 (Aug. 19th 2016)

A bugfix release, which fixes the following issues:

- When `showDividersOnScroll` is to `true`, the visibility of the dividers is now adapted correctly.

## Version 3.6.2 (Aug. 19th 2016)

A bugfix release, which fixes the following issues:

- Potential crashes when using custom content views with `showDividersOnScroll` set to `true` are now prevented.

## Version 3.6.1 (Aug. 19th 2016)

A bugfix release, which fixes the following issues:

- The `WizardDialog` does not crash anymore on configuration changes with `setRetainInstance` set to `true`.

## Version 3.6.0 (Aug. 19th 2016)

A feature release, which introduces the following changes:

- The properties of dialogs are now stored in their `onSaveInstanceState`-method. This does not include unserializable properties, e.g. listeners.
- The `WizardDialog` is not dismissed on configuration changes anymore. Instead, its state is automatically restored. This is in accordance with the default behavior of a `DialogFragment`.
- The `showDividersOnScrollMethod` of a `MaterialDialog` does now also take effect for custom views, if an `AbsListView` is contained by the view hierarchy.
- Updated dependency "AndroidUtil" to version 1.11.0.

## Version 3.5.3 (Jun. 16th 2016)

A bugfix release, which fixes the following issues:

- Fixed a possible crash of the `WizardDialog` on orientation changes.

## Version 3.5.2 (Jun. 16th 2016)

A minor release, which introduces the following changes:

- Improved lifecycle behavior of the class `AbstractMaterialDialogFragment`: The dialog is not dismissed in the `onPause`-method anymore, but only after orientation changes occurred.
- Updated dependency "AndroidUtil" to version 1.6.1.

## Version 3.5.1 (May 27th 2016)

A minor release, which introduces the following changes:

- Added `setOnShowListener`-method to the class `AbstractMaterialDialogFragment`.

## Version 3.5.0 (May 26th 2016)

A feature release, which introduces the following changes:

- It is now possible to show dividers above and below the list view of a `MaterialDialog`, depending on the list view's scroll position.
- The functionality to show a divider above a `MaterialDialog`'s custom view is not publicly available anymore.
- The size and paddings of a dialog's buttons have been adjusted.
- The `WizardDialog` does not crash anymore on orientation changes.

## Version 3.4.2 (May 25th 2016)

A minor release, which introduces the following changes:

- Added the theme attribute `materialDialogTheme`, which allows to specify the default theme, which should be used by all of the library's builders. 

## Version 3.4.1 (May 24th 2016)

A bugfix release, which fixes the following issues:

- When the value -1 is passed to a setter, which expects a color, the value is not ignored anymore.

## Version 3.4.0 (May 24th 2016)

A feature release, which introduces the following changes:

- The text style of a `MaterialDialog`'s title is now bold.
- Revised the paddings and margins of a `MaterialDialog`'s views.
- It is now possible to show a divider above a `MaterialDialog`'s custom view.
- Removed the functionality to customize the control color of a `MaterialDialog`'s list items as it caused some problems.

## Version 3.3.0 (May 23th 2016)

A feature release, which introduces the following changes:

- Added the dialog `ProgressDialog`, which allows to indicate a running progress.
- Added the dialog `WizardDialog`, which allows to show multiple fragments.
- Updated dependency "AndroidUtil" to version 1.4.11.
- Updated AppCompat v7 support library to version 23.4.0.

## Version 3.2.3 (Mar. 18th 2016)

A minor release, which introduces the following changes:

- When using custom content or title views, the views' width and height are not overridden anymore.

## Version 3.2.2 (Mar. 17th 2016)

A minor release, which introduces the following changes:

- Updated dependency "AndroidUtil" to version 1.4.5.
- Fixed some deprecation warnings.

## Version 3.2.1 (Mar. 15th 2016)

A minor release, which introduces the following changes:

- When setting a `MaterialDialog`'s item control color, the color is also used when no custom item color has been set.
- Updated dependency "AndroidUtil" to version 1.4.4.
- Updated AppCompat support library to version 23.2.1.
- Minor changes of the example app.

## Version 3.2.0 (Mar. 15th 2016)

A feature release, which introduces the following changes:

- The width of dialogs on smartphones has been increased. It is now equal to the width of the Android SDK's `AlertDialog`.
- The setter and getter methods of a `MaterialDialog` can now be used even if the dialog has not been shown yet.
- Added the dialog, which is validated by an implementation of the interface `MaterialDialog.Validator`, as a parameter to its `validate`-method.

## Version 3.1.1 (Feb. 24th 2016)

A minor release, which introduces the following changes:

- The library is from now on distributed under the Apache License version 2.0. 
- Updated dependency "AndroidUtil" to version 1.4.3.
- Updated AppCompat support library to version 23.1.1.
- Minor changes of the example app.

## Version 3.1.0 (Feb. 13th 2016)

A feature release, which provides the following changes:

- It is now possible to use theme attributes in order to specify the appearance of dialogs (see documentation for further details).

## Version 3.0.2 (Dec. 23th 2015)

A minor release, which introduces the following changes:

- Added `getContext`-method to class `MaterialDialog.Builder`.
- Added `setCancelable`-method to class `MaterialDialog.Builder`.
- Added `setOnCancelListener`-, `setOnDismissListener`- and `setOnKeyListener`-methods to class `MaterialDialog.Builder`. 

## Version 3.0.1 (Dec. 13th 2015)

A minor release, which introduces the following changes:

- The dividers of a `MaterialDialog`'s list view are not visible anymore by default as this is more consistent with the Material design guidelines (http://www.google.com/design/spec/components/dialogs.html).
- Version 1.2.2 of the library "AndroidUtil" is now used.

## Version 3.0.0 (Nov. 12th 2015)

A major release, which introduces the following changes:

- The library has been completely reworked in order to make it independent from the class `AlertDialog` of the Android SDK. Dialogs, which are created using the library are now instances of the newly introduced class `MaterialDialog`. The class `MaterialDialogBuilder` has been removed. In order to create dialogs, the builder `MaterialDialog.Builder` has to be used now. The decision to rework the library has been made because of the problem discussed in https://github.com/michael-rapp/AndroidMaterialDialog/issues/4.  

## Version 2.0.0 (Oct. 19th 2015)

A major release, which introduces the following changes:

- The project has been migrated from the legacy Eclipse ADT folder structure to Android Studio. It now uses the Gradle build system and the library as well as the example app are contained by one single project.
- The library can now be added to Android apps using the Gradle dependency `com.github.michael-rapp:android-material-dialog:2.0.0`

## Version 1.2.1 (Sept. 18th 2015)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidMaterialDialog/issues/3

## Version 1.2.0 (Sept. 13th 2015)

A feature release, which introduces the following changes:
 
 - Added setter methods, which allow to customize various colors of a dialog. The colors of the dialog's background, message, list items and item controls can now be specified (https://github.com/michael-rapp/AndroidMaterialDialog/issues/2).
 - Added an additional constructor to the class `MaterialDialogBuilder`, which allows to specify the theme, which should be used.

## Version 1.1.0 (Apr. 1st 2015)

A feature release, which introduces the following features:

- Added the possibility to validate a dialog's content

## Version 1.0.2 (Nov. 16th 2015)

A minor release, which introduces the following changes:

- Changed the appearance of a dialog's buttons to be identical on Lollipop-devices, compared to pre-Lollipop devices.
- A dialog's list view is now scrolled to the first selected item.

## Version 1.0.1 (Nov. 13th 2015)

A bugfix release, which fixes the following issues:

- https://github.com/michael-rapp/AndroidMaterialDialog/issues/1

## Version 1.0.0 (Nov. 12th 2015)

The first stable release, which provides a builder, which allows to create dialogs, which are designed according to Android 5.0's Material Design guidelines even on pre-Lollipop devices. The implementation initially provides the following features:

- The builder is extended from the Android SDK's built-in class `android.app.AlertDialog.Builder` and therefore provides all functionalities of its base class, including the possibilities to create dialogs with a title, icon, message, up to three buttons and optional list items. 
- The library uses the AppCompat v7 revision 21 support library in order to retrieve the color of a dialog's title and button texts from the applied theme. Alternatively, these colors can be set manually using the builder.
- A dialog's title and content can be replaced by any custom view.  
- The builder supports to create dialogs, whose buttons are stacked, as introduces in the Material Design guidelines.