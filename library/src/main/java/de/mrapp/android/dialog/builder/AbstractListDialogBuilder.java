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
package de.mrapp.android.dialog.builder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.widget.ListAdapter;

import androidx.annotation.ArrayRes;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;
import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.ListDialog;
import de.mrapp.android.util.ThemeUtil;

/**
 * An abstract base class for all builders, which allow to create and show dialogs, which are
 * designed according to Android Material Design guidelines even on pre-Lollipop devices and may
 * contain list items.
 *
 * @param <DialogType>
 *         The type of the dialog, which is created by the builder
 * @param <BuilderType>
 *         The type of the builder
 * @author Michael Rapp
 * @since 3.3.0
 */
public abstract class AbstractListDialogBuilder<DialogType extends ListDialog, BuilderType extends AbstractListDialogBuilder<DialogType, ?>>
        extends AbstractButtonBarDialogBuilder<DialogType, BuilderType> {

    /**
     * Obtains the item color from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the item color should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainItemColor(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(themeResourceId, new int[]{R.attr.materialDialogItemColor});
        ColorStateList colorStateList = typedArray.getColorStateList(0);

        if (colorStateList == null) {
            colorStateList = ThemeUtil.getColorStateList(getContext(), themeResourceId,
                    android.R.attr.textColorSecondary);
        }

        setItemColor(colorStateList);
    }

    /**
     * Creates a new builder, which allows to create dialogs, which allow to create and show
     * dialogs, which are designed according to Android 5's Material Design guidelines even on
     * pre-Lollipop devices and may contain list items.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public AbstractListDialogBuilder(@NonNull final Context context) {
        super(context);
    }

    /**
     * Creates a new builder, which allows to create dialogs, which allow to create and show
     * dialogs, which are designed according to Android 5's Material Design guidelines even on
     * pre-Lollipop devices and may contain list items.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    public AbstractListDialogBuilder(@NonNull final Context context,
                                     @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
    }

    /**
     * Sets the color of the list items of the dialog, which is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value or -1, if no custom color
     *         should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItemColor(@ColorInt final int color) {
        getProduct().setItemColor(color);
        return self();
    }

    /**
     * Sets the color of the list items of the dialog, which is created by the builder.
     *
     * @param colorStateList
     *          The color, which should be set, as an instance of the class {@link ColorStateList}.
     *          The color may not be null
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItemColor(@NonNull final ColorStateList colorStateList) {
        getProduct().setItemColor(colorStateList);
        return self();
    }

    /**
     * Sets the typeface of the list items of the dialog, which is created by the builder.
     *
     * @param typeface
     *         The typeface, which should be set, as an instance of the class {@link Typeface}. The
     *         typeface may not be null
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItemTypeface(@NonNull final Typeface typeface) {
        getProduct().setItemTypeface(typeface);
        return self();
    }

    /**
     * Sets the color, which should be used to tint the icons of the list items of the dialog, which
     * is created by the builder.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItemIconTint(@ColorInt final int color) {
        getProduct().setItemIconTint(color);
        return self();
    }

    /**
     * Sets the color state list, which should be used to tint the icons of the list items of the
     * dialog, which is created by the builder.
     *
     * @param tintList
     *         The color state list, which should be set, as an instance of the class {@link
     *         ColorStateList} or null, if no color state list should be set
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItemIconTintList(@Nullable final ColorStateList tintList) {
        getProduct().setItemIconTintList(tintList);
        return self();
    }

    /**
     * Sets the mode, which should be used to tint the icons of the list items of the dialog, which
     * is created by the builder.
     *
     * @param mode
     *         The mode, which should be set, as a value of the enum {@link PorterDuff.Mode}. The
     *         mode may not be null
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItemIconTintMode(@NonNull final PorterDuff.Mode mode) {
        getProduct().setItemIconTintMode(mode);
        return self();
    }

    /**
     * Sets, whether the list item at a specific position of the dialog, which is created by the
     * builder, should be checked, or not.
     *
     * @param position The position of the list item as an {@link Integer} value
     * @param checked True, if the list item should be checked, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItemChecked(final int position, final boolean checked) {
        getProduct().setItemChecked(position, checked);
        return self();
    }

    /**
     * Sets, whether the list item at a specific position of the dialog, which is created by the
     * builder, should be enabled, or not.
     *
     * @param position The position of the list item as an {@link Integer} value
     * @param enabled True, if the list item should be enabled, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItemEnabled(final int position, final boolean enabled) {
        getProduct().setItemEnabled(position, enabled);
        return self();
    }

    /**
     * Sets the items, which should be shown by the dialog, which is created by the builder.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItems(@NonNull final CharSequence[] items,
                                      @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setItems(items, listener);
        return self();
    }

    /**
     * Sets the items, which should be shown by the dialog, which is created by the builder.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param iconResourceIds
     *         An array, which contains the resource ids of the items' icons, as an {@link Integer}
     *         array or null, if no icons should be displayed
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItems(@NonNull final CharSequence[] items,
                                      @Nullable final int[] iconResourceIds,
                                      @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setItems(items, iconResourceIds, listener);
        return self();
    }

    /**
     * Sets the items, which should be shown by the dialog, which is created by the builder.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItems(@ArrayRes final int resourceId,
                                      @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setItems(resourceId, listener);
        return self();
    }

    /**
     * Sets the items, which should be shown by the dialog, which is created by the builder.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param iconResourceIds
     *         An array, which contains the resource ids of the items' icons, as an {@link Integer}
     *         array or null, if no icons should be displayed
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setItems(@ArrayRes final int resourceId,
                                      @Nullable final int[] iconResourceIds,
                                      @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setItems(resourceId, iconResourceIds, listener);
        return self();
    }

    /**
     * Sets the adapter, which provides the items, which should be shown by the dialog, which is
     * created by the builder.
     * <p>
     * Note, that the adapter and the attached listener are not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because they are not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-set the adapter and re-register the listener.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final <VH extends RecyclerView.ViewHolder> BuilderType setAdapter(
            @Nullable final RecyclerView.Adapter<VH> adapter,
            @Nullable final RecyclerView.LayoutManager layoutManager,
            @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setAdapter(adapter, layoutManager, listener);
        return self();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog, which is created by the
     * builder. Only one of the items can be selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
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
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setSingleChoiceItems(@NonNull final CharSequence[] items,
                                                  final int checkedItem,
                                                  @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setSingleChoiceItems(items, checkedItem, listener);
        return self();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog, which is created by the
     * builder. Only one of the items can be selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param iconResourceIds
     *         An array, which contains the resource ids of the items' icons, as an {@link Integer}
     *         array or null, if no icons should be displayed
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setSingleChoiceItems(@NonNull final CharSequence[] items,
                                                  @Nullable final int[] iconResourceIds,
                                                  final int checkedItem,
                                                  @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setSingleChoiceItems(items, iconResourceIds, checkedItem, listener);
        return self();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog, which is created by the
     * builder. Only one of the items can be selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
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
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setSingleChoiceItems(@ArrayRes final int resourceId,
                                                  final int checkedItem,
                                                  @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setSingleChoiceItems(resourceId, checkedItem, listener);
        return self();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog, which is created by the
     * builder. Only one of the items can be selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param iconResourceIds
     *         An array, which contains the resource ids of the items' icons, as an {@link Integer}
     *         array or null, if no icons should be displayed
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setSingleChoiceItems(@ArrayRes final int resourceId,
                                                  @Nullable final int[] iconResourceIds,
                                                  final int checkedItem,
                                                  @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setSingleChoiceItems(resourceId, iconResourceIds, checkedItem, listener);
        return self();
    }

    /**
     * Sets the adapter, which provides the selectable items, which should be shown by the dialog,
     * which is created by the builder. Only one of the items can be selected at once.
     * <p>
     * Note, that the adapter and the attached listener are not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because they are not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-set the adapter and re-register the listener.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final <VH extends RecyclerView.ViewHolder> BuilderType setSingleChoiceItems(
            @Nullable final RecyclerView.Adapter<VH> adapter,
            @Nullable final RecyclerView.LayoutManager layoutManager, final int checkedItem,
            @Nullable final DialogInterface.OnClickListener listener) {
        getProduct().setSingleChoiceItems(adapter, layoutManager, checkedItem, listener);
        return self();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog, which is created by the
     * builder. Multiple items can be selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
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
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMultiChoiceItems(@NonNull final CharSequence[] items,
                                                 @Nullable final boolean[] checkedItems,
                                                 @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        getProduct().setMultiChoiceItems(items, checkedItems, listener);
        return self();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog, which is created by the
     * builder. Multiple items can be selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param iconResourceIds
     *         An array, which contains the resource ids of the items' icons, as an {@link Integer}
     *         array or null, if no icons should be displayed
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMultiChoiceItems(@NonNull final CharSequence[] items,
                                                 @Nullable final int[] iconResourceIds,
                                                 @Nullable final boolean[] checkedItems,
                                                 @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        getProduct().setMultiChoiceItems(items, iconResourceIds, checkedItems, listener);
        return self();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog, which is created by the
     * builder. Multiple items can be selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
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
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMultiChoiceItems(@ArrayRes final int resourceId,
                                                 @Nullable final boolean[] checkedItems,
                                                 @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        getProduct().setMultiChoiceItems(resourceId, checkedItems, listener);
        return self();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog, which is created by the
     * builder. Multiple items can be selected at once.
     * <p>
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param iconResourceIds
     *         An array, which contains the resource ids of the items' icons, as an {@link Integer}
     *         array or null, if no icons should be displayed
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnClickListener} or null, if no listener should be
     *         notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setMultiChoiceItems(@ArrayRes final int resourceId,
                                                 @Nullable final int[] iconResourceIds,
                                                 @Nullable final boolean[] checkedItems,
                                                 @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        getProduct().setMultiChoiceItems(resourceId, iconResourceIds, checkedItems, listener);
        return self();
    }

    /**
     * Sets the adapter, which provides the selectable items, which should be shown by the dialog,
     * which is created by the builder. Multiple items can be selected at once.
     * <p>
     * Note, that the adapter and the attached listener are not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because they are not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-set the adapter and re-register the listener.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link DialogInterface.OnMultiChoiceClickListener} or null, if no listener
     *         should be notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final <VH extends RecyclerView.ViewHolder> BuilderType setMultiChoiceItems(
            @Nullable final RecyclerView.Adapter<VH> adapter,
            @Nullable final RecyclerView.LayoutManager layoutManager,
            @Nullable final boolean[] checkedItems,
            @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        getProduct().setMultiChoiceItems(adapter, layoutManager, checkedItems, listener);
        return self();
    }

    /**
     * Sets the listener, which should be notified, when an item, which is shown by the dialog,
     * which is created by the builder, is selected.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         ListDialog.OnItemSelectedListener} or null, if no listener should be notified
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType setOnItemSelectedListener(
            @Nullable final ListDialog.OnItemSelectedListener listener) {
        getProduct().setOnItemSelectedListener(listener);
        return self();
    }

    @CallSuper
    @Override
    protected void obtainStyledAttributes(@StyleRes final int themeResourceId) {
        super.obtainStyledAttributes(themeResourceId);
        obtainItemColor(themeResourceId);
    }

}