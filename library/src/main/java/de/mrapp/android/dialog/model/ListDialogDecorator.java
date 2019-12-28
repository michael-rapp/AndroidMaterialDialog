/*
 * Copyright 2014 - 2019 Michael Rapp
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

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;

import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
     * Returns the recycler view, which is contained by the dialog.
     *
     * @return The recycler view, which is contained by the dialog, as an instance of the class
     * RecyclerView or null, if the dialog does not show any list items or has not been shown yet
     */
    RecyclerView getListView();

    /**
     * Returns the adapter of the recycler view, which is contained by the dialog.
     *
     * @return The adapter of the recycler view, which is contained by the dialog, as an instance of
     * the type RecyclerView.Adapter or null, if the dialog does not show any list items
     */
    RecyclerView.Adapter<?> getListAdapter();

    /**
     * Returns the text color of the dialog's list items.
     *
     * @return The text color of the dialog's list items as an instance of the class
     * {@link ColorStateList} or null, if the default text color is used
     */
    @Nullable
    ColorStateList getItemColor();

    /**
     * Sets the text color of the dialog's list items.
     *
     * @param color The color, which should be set, as an {@link Integer} value
     */
    void setItemColor(@ColorInt int color);

    /**
     * Sets the text color of the dialog's list items.
     *
     * @param colorStateList The color, which should be set, as an instance of the class {@link ColorStateList}.
     *                       The color state list may not be null
     */
    void setItemColor(@NonNull ColorStateList colorStateList);

    /**
     * Sets the typeface of the list items of the dialog.
     * <p>
     * Note, that the typeface is not stored using a dialog's <code>onSaveInstanceState</code>-method,
     * because it is not serializable. Therefore this method must be called again after
     * configuration changes, e.g when the orientation of the device has changed, in order to re-set
     * the typeface.
     *
     * @param typeface The typeface, which should be set, as an instance of the class {@link Typeface}. The
     *                 typeface may not be null
     */
    void setItemTypeface(@NonNull Typeface typeface);

    /**
     * Returns the typeface of the list items of the dialog.
     *
     * @return The typeface of the list items of the dialog as an instance of the class {@link
     * Typeface} or null, if the default typeface is used
     */
    @Nullable
    Typeface getItemTypeface();

    /**
     * Returns the number of list items that are shown by the dialog.
     *
     * @return The number of list items that are shown by the dialog as an {@link Integer} value
     */
    int getItemCount();

    /**
     * Returns, whether the list item at a specific position is checked, or not. If no list items
     * are shown by the dialog, an {@link IndexOutOfBoundsException} will be thrown.
     *
     * @param position The position of the list item as an {@link Integer} value
     * @return True, if the list item at the given position is checked, false otherwise
     */
    boolean isItemChecked(int position);

    /**
     * Sets, whether the list item at a specific position should be checked, or not. If no list
     * items, are shown by the dialog, an {@link IndexOutOfBoundsException} will be thrown.
     *
     * @param position The position of the list item as an {@link Integer} value
     * @param checked True, if the list item at the given position should be checked, false
     *                otherwise
     */
    void setItemChecked(int position, boolean checked);

    /**
     * Sets the items, which should be shown by the dialog.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param items    The items, which should be set, as an array of the type {@link CharSequence} or null,
     *                 if no items should be shown by the dialog
     * @param listener The listener, which should be notified, when an item is clicked, as an instance of
     *                 the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *                 notified
     */
    void setItems(@Nullable CharSequence[] items,
                  @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the items, which should be shown by the dialog.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId The resource id of the items, which should be set, as an {@link Integer} value. The
     *                   resource id must correspond to a valid array resource
     * @param listener   The listener, which should be notified, when an item is clicked, as an instance of
     *                   the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *                   notified
     */
    void setItems(@ArrayRes int resourceId, @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the adapter, which provides the items, which should be shown by the dialog.
     * <p>
     * Note, that the adapter and the attached listener are not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because they are not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-set the adapter and re-register the listener.
     *
     * @param <VH>          The type of the adapter's view holder
     * @param adapter       The adapter, which should be set, as an instance of the type RecyclerView.Adapter or
     *                      null, if no items should be shown by the dialog
     * @param layoutManager The layout manager, which should be used to layout the items, as an instance of the
     *                      class RecyclerView.LayoutManager or null, if the default layout manager should be
     *                      used
     * @param listener      The listener, which should be notified, when an item is clicked, as an instance of
     *                      the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *                      notified
     */
    <VH extends RecyclerView.ViewHolder> void setAdapter(@Nullable RecyclerView.Adapter<VH> adapter,
                                                         @Nullable RecyclerView.LayoutManager layoutManager,
                                                         @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the selectable items, which should be shown by the dialog. Only one of the items can be
     * selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param items       The items, which should be set, as an array of the type {@link CharSequence} or null,
     *                    if no items should be shown by the dialog
     * @param checkedItem The index of the item, which should be selected by default, as an {@link Integer}
     *                    value or -1, if no item should be selected by default
     * @param listener    The listener, which should be notified, when an item is clicked, as an instance of
     *                    the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *                    notified
     */
    void setSingleChoiceItems(@Nullable CharSequence[] items, int checkedItem,
                              @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the selectable items, which should be shown by the dialog. Only one of the items can be
     * selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId  The resource id of the items, which should be set, as an {@link Integer} value. The
     *                    resource id must correspond to a valid array resource
     * @param checkedItem The index of the item, which should be selected by default, as an {@link Integer}
     *                    value or -1, if no item should be selected by default
     * @param listener    The listener, which should be notified, when an item is clicked, as an instance of
     *                    the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *                    notified
     */
    void setSingleChoiceItems(@ArrayRes int resourceId, int checkedItem,
                              @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the adapter, which provides the selectable items, which should be shown by the dialog.
     * Only one of the items can be selected at once.
     * <p>
     * Note, that the adapter and the attached listener are not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because they are not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-set the adapter and re-register the listener.
     *
     * @param <VH>          The type of the adapter's view holder
     * @param adapter       The adapter, which should be set, as an instance of the type RecyclerView.Adapter or
     *                      null, if no items should be shown by the dialog
     * @param layoutManager The layout manager, which should be used to layout the items, as an instance of the
     *                      class RecyclerView.LayoutManager or null, if the default layout manager should be
     *                      used
     * @param checkedItem   The index of the item, which should be selected by default, as an {@link Integer}
     *                      value or -1, if no item should be selected by default
     * @param listener      The listener, which should be notified, when an item is clicked, as an instance of
     *                      the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *                      notified
     */
    <VH extends RecyclerView.ViewHolder> void setSingleChoiceItems(
            @Nullable RecyclerView.Adapter<VH> adapter,
            @Nullable RecyclerView.LayoutManager layoutManager, int checkedItem,
            @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the selectable items, which should be shown by the dialog. Multiple items can be
     * selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param items        The items, which should be set, as an array of the type {@link CharSequence} or null,
     *                     if no items should be shown by the dialog
     * @param checkedItems An array, which contains, whether the items, which correspond to the corresponding
     *                     indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *                     if no items should be selected by default
     * @param listener     The listener, which should be notified, when an item is clicked, as an instance of
     *                     the type {@link DialogInterface.OnMultiChoiceClickListener} or null, if no listener
     *                     should be notified
     */
    void setMultiChoiceItems(@Nullable CharSequence[] items, @Nullable boolean[] checkedItems,
                             @Nullable DialogInterface.OnMultiChoiceClickListener listener);

    /**
     * Sets the selectable items, which should be shown by the dialog. Multiple items can be
     * selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId   The resource id of the items, which should be set, as an {@link Integer} value. The
     *                     resource id must correspond to a valid array resource
     * @param checkedItems An array, which contains, whether the items, which correspond to the corresponding
     *                     indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *                     if no items should be selected by default
     * @param listener     The listener, which should be notified, when an item is clicked, as an instance of
     *                     the type {@link DialogInterface.OnMultiChoiceClickListener} or null, if no listener
     *                     should be notified
     */
    void setMultiChoiceItems(@ArrayRes int resourceId, @Nullable boolean[] checkedItems,
                             @Nullable DialogInterface.OnMultiChoiceClickListener listener);

    /**
     * Sets the adapter, which provides the selectable items, which should be shown by the dialog.
     * Multiple items can be selected at once.
     * <p>
     * Note, that the adapter and the attached listener are not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because they are not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-set the adapter and re-register the listener.
     *
     * @param <VH>          The type of the adapter's view holder
     * @param adapter       The adapter, which should be set, as an instance of the type RecyclerView.Adapter or
     *                      null, if no items should be shown by the dialog
     * @param layoutManager The layout manager, which should be used to layout the items, as an instance of the
     *                      class RecyclerView.LayoutManager or null, if the default layout manager should be
     *                      used
     * @param checkedItems  An array, which contains, whether the items, which correspond to the corresponding
     *                      indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *                      if no items should be selected by default
     * @param listener      The listener, which should be notified, when an item is clicked, as an instance of
     *                      the type {@link DialogInterface.OnMultiChoiceClickListener} or null, if no listener
     *                      should be notified
     */
    <VH extends RecyclerView.ViewHolder> void setMultiChoiceItems(
            @Nullable RecyclerView.Adapter<VH> adapter,
            @Nullable RecyclerView.LayoutManager layoutManager, @Nullable boolean[] checkedItems,
            @Nullable DialogInterface.OnMultiChoiceClickListener listener);

    /**
     * Sets the listener, which should be notified, when an item, which is shown by the dialog is
     * selected.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param listener The listener, which should be set, as an instance of the type {@link
     *                 ListDialog.OnItemSelectedListener} or null, if no listener should be notified
     */
    void setOnItemSelectedListener(@Nullable ListDialog.OnItemSelectedListener listener);

}