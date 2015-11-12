# AndroidMaterialDialog - RELEASE NOTES

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