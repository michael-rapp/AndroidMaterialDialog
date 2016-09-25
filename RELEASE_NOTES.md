# AndroidMaterialDialog - RELEASE NOTES

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