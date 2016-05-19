/*
 * Copyright 2014 - 2016 Michael Rapp
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package de.mrapp.android.dialog.model;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Defines the interface, a decorator, which allows to modify the view hierarchy of a dialog, which
 * is designed according to Android 5's Material design guidelines even on pre-Lollipop devices and
 * may contain list items, must implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface ListDialogDecorator extends Dialog {

    /**
     * Returns the list view, which is contained by the dialog.
     *
     * @return The list view, which is contained by the dialog, as an instance of the class {@link
     * ListView} or null, if the dialog does not show any list items or has not been shown yet
     */
    ListView getListView();

    /**
     * Returns the adapter of the list view, which is contained by the dialog.
     *
     * @return The adapter of the list view, which is contained by the dialog, as an instance of the
     * type {@link ListAdapter} or null, if the dialog does not show any list items
     */
    ListAdapter getListAdapter();

    /**
     * Returns the color of the list items of the dialog.
     *
     * @return The color of the list items of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    int getItemColor();

    /**
     * Sets the color of the list items of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setItemColor(@ColorInt int color);

    /**
     * Returns the control color of the list items of the dialog.
     *
     * @return The color of the list items of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    int getItemControlColor();

    /**
     * Sets the control color of the list items of the dialog.
     *
     * @param color
     *         The control color, which should be set, as an {@link Integer} value or -1, if no list
     *         items are shown or if the dialog has not been shown yet
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    void setItemControlColor(@ColorInt int color);

    /**
     * Sets the items, which should be shown by the dialog.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     */
    void setItems(@NonNull CharSequence[] items,
                  @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the items, which should be shown by the dialog.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     */
    void setItems(@ArrayRes int resourceId, @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the adapter, which provides the items, which should be shown by the dialog.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     */
    void setAdapter(@NonNull ListAdapter adapter,
                    @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the selectable items, which should be shown by the dialog. Only one of the items can be
     * selected at once.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     */
    void setSingleChoiceItems(@NonNull CharSequence[] items, int checkedItem,
                              @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the selectable items, which should be shown by the dialog. Only one of the items can be
     * selected at once.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     */
    void setSingleChoiceItems(@ArrayRes int resourceId, int checkedItem,
                              @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the adapter, which provides the selectable items, which should be shown by the dialog.
     * Only one of the items can be selected at once.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     */
    void setSingleChoiceItems(@NonNull ListAdapter adapter, int checkedItem,
                              @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the selectable items, which should be shown by the dialog. Multiple items can be
     * selected at once.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnMultiChoiceClickListener} or null, if no listener
     *         should be notified
     */
    void setMultiChoiceItems(@NonNull CharSequence[] items, @Nullable boolean[] checkedItems,
                             @Nullable DialogInterface.OnMultiChoiceClickListener listener);

    /**
     * Sets the selectable items, which should be shown by the dialog. Multiple items can be
     * selected at once.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnMultiChoiceClickListener} or null, if no listener
     *         should be notified
     */
    void setMultiChoiceItems(@ArrayRes int resourceId, @Nullable boolean[] checkedItems,
                             @Nullable DialogInterface.OnMultiChoiceClickListener listener);

    /**
     * Sets the listener, which should be notified, when an item, which is shown by the dialog is
     * selected.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         AdapterView.OnItemSelectedListener} or null, if no listener should be notified
     */
    void setOnItemSelectedListener(@Nullable AdapterView.OnItemSelectedListener listener);

}