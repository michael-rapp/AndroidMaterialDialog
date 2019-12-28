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
package de.mrapp.android.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.ArrayRes;
import androidx.annotation.CallSuper;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.recyclerview.widget.RecyclerView;

import de.mrapp.android.dialog.decorator.ListDialogDecorator;
import de.mrapp.android.dialog.model.ListDialog;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices and may contain list items.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractListDialog extends AbstractButtonBarDialog implements ListDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final ListDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and may contain list items.
     *
     * @param context         The context, which should be used by the dialog, as an instance of the class {@link
     *                        Context}. The context may not be null
     * @param themeResourceId The resource id of the theme, which should be used by the dialog, as an {@link
     *                        Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractListDialog(@NonNull final Context context,
                                 @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        decorator = new ListDialogDecorator(this);
        addDecorator(decorator);
    }

    @Override
    public final RecyclerView getListView() {
        return decorator.getListView();
    }

    @Override
    public final RecyclerView.Adapter<?> getListAdapter() {
        return decorator.getListAdapter();
    }

    @Nullable
    @Override
    public final ColorStateList getItemColor() {
        return decorator.getItemColor();
    }

    @Override
    public final void setItemColor(@ColorInt final int color) {
        decorator.setItemColor(color);
    }

    @Override
    public final void setItemColor(@NonNull final ColorStateList colorStateList) {
        decorator.setItemColor(colorStateList);
    }

    @Nullable
    @Override
    public final Typeface getItemTypeface() {
        return decorator.getItemTypeface();
    }

    @Nullable
    @Override
    public final ColorStateList getItemIconTintList() {
        return decorator.getItemIconTintList();
    }

    @Override
    public final void setItemIconTint(final int color) {
        decorator.setItemIconTint(color);
    }

    @Override
    public final void setItemIconTintList(@Nullable final ColorStateList tintList) {
        decorator.setItemIconTintList(tintList);
    }

    @NonNull
    @Override
    public final PorterDuff.Mode getItemIconTintMode() {
        return decorator.getItemIconTintMode();
    }

    @Override
    public final void setItemIconTintMode(@NonNull final PorterDuff.Mode mode) {
        decorator.setItemIconTintMode(mode);
    }

    @Override
    public final void setItemTypeface(@NonNull final Typeface typeface) {
        decorator.setItemTypeface(typeface);
    }

    @Override
    public final int getItemCount() {
        return decorator.getItemCount();
    }

    @Override
    public final boolean isItemChecked(final int position) {
        return decorator.isItemChecked(position);
    }

    @Override
    public final void setItemChecked(final int position, final boolean checked) {
        decorator.setItemChecked(position, checked);
    }

    @Override
    public final boolean isItemEnabled(final int position) {
        return decorator.isItemEnabled(position);
    }

    @Override
    public final void setItemEnabled(final int position, final boolean enabled) {
        decorator.setItemEnabled(position, enabled);
    }

    @Override
    public final void setItems(@Nullable final CharSequence[] items,
                               @Nullable final OnClickListener listener) {
        decorator.setItems(items, listener);
    }

    @Override
    public final void setItems(@Nullable final CharSequence[] items,
                               @Nullable final int[] iconResourceIds,
                               @Nullable final OnClickListener listener) {
        decorator.setItems(items, iconResourceIds, listener);
    }

    @Override
    public final void setItems(@ArrayRes final int resourceId,
                               @Nullable final OnClickListener listener) {
        decorator.setItems(resourceId, listener);
    }

    @Override
    public final void setItems(@ArrayRes final int resourceId,
                               @Nullable final int[] iconResourceIds,
                               @Nullable final OnClickListener listener) {
        decorator.setItems(resourceId, iconResourceIds, listener);
    }

    @Override
    public final <VH extends RecyclerView.ViewHolder> void setAdapter(
            @Nullable final RecyclerView.Adapter<VH> adapter,
            @Nullable final RecyclerView.LayoutManager layoutManager,
            @Nullable final OnClickListener listener) {
        decorator.setAdapter(adapter, layoutManager, listener);
    }

    @Override
    public final void setSingleChoiceItems(@Nullable final CharSequence[] items,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        decorator.setSingleChoiceItems(items, checkedItem, listener);
    }

    @Override
    public final void setSingleChoiceItems(@Nullable final CharSequence[] items,
                                           @Nullable final int[] iconResourceIds,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        decorator.setSingleChoiceItems(items, iconResourceIds, checkedItem, listener);
    }

    @Override
    public final void setSingleChoiceItems(@ArrayRes final int resourceId, final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        decorator.setSingleChoiceItems(resourceId, checkedItem, listener);
    }

    @Override
    public final void setSingleChoiceItems(@ArrayRes final int resourceId,
                                           @Nullable final int[] iconResourceIds,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        decorator.setSingleChoiceItems(resourceId, iconResourceIds, checkedItem, listener);
    }

    @Override
    public final <VH extends RecyclerView.ViewHolder> void setSingleChoiceItems(
            @Nullable final RecyclerView.Adapter<VH> adapter,
            @Nullable final RecyclerView.LayoutManager layoutManager, final int checkedItem,
            @Nullable final OnClickListener listener) {
        decorator.setSingleChoiceItems(adapter, layoutManager, checkedItem, listener);
    }

    @Override
    public final void setMultiChoiceItems(@Nullable final CharSequence[] items,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        decorator.setMultiChoiceItems(items, checkedItems, listener);
    }

    @Override
    public final void setMultiChoiceItems(@Nullable final CharSequence[] items,
                                          @Nullable final int[] iconResourceIds,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        decorator.setMultiChoiceItems(items, iconResourceIds, checkedItems, listener);
    }

    @Override
    public final void setMultiChoiceItems(@ArrayRes final int resourceId,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        decorator.setMultiChoiceItems(resourceId, checkedItems, listener);
    }

    @Override
    public final void setMultiChoiceItems(@ArrayRes final int resourceId,
                                          @Nullable final int[] iconResourceIds,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        decorator.setMultiChoiceItems(resourceId, iconResourceIds, checkedItems, listener);
    }

    @Override
    public final <VH extends RecyclerView.ViewHolder> void setMultiChoiceItems(
            @Nullable final RecyclerView.Adapter<VH> adapter,
            @Nullable final RecyclerView.LayoutManager layoutManager,
            @Nullable final boolean[] checkedItems,
            @Nullable final OnMultiChoiceClickListener listener) {
        decorator.setMultiChoiceItems(adapter, layoutManager, checkedItems, listener);
    }

    @Override
    public final void setOnItemSelectedListener(
            @Nullable final ListDialog.OnItemSelectedListener listener) {
        decorator.setOnItemSelectedListener(listener);
    }

    @Override
    public final void setOnItemEnabledListener(
            @Nullable final ListDialog.OnItemEnabledListener listener) {
        decorator.setOnItemEnabledListener(listener);
    }

    @NonNull
    @CallSuper
    @Override
    public Bundle onSaveInstanceState() {
        Bundle outState = super.onSaveInstanceState();
        decorator.onSaveInstanceState(outState);
        return outState;
    }

    @CallSuper
    @Override
    public void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        decorator.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

}