/*
 * Copyright 2014 - 2018 Michael Rapp
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
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Map;

import de.mrapp.android.dialog.ScrollableArea.Area;
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
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractListDialog(@NonNull final Context context,
                                 @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        decorator = new ListDialogDecorator(this);
        addDecorator(decorator);
    }

    @Override
    public final ListView getListView() {
        return decorator.getListView();
    }

    @Override
    public final ListAdapter getListAdapter() {
        return decorator.getListAdapter();
    }

    @Override
    public final int getItemColor() {
        return decorator.getItemColor();
    }

    @Override
    public final void setItemColor(@ColorInt final int color) {
        decorator.setItemColor(color);
    }

    @Override
    public final void setItems(@NonNull final CharSequence[] items,
                               @Nullable final OnClickListener listener) {
        decorator.setItems(items, listener);
    }

    @Override
    public final void setItems(@ArrayRes final int resourceId,
                               @Nullable final OnClickListener listener) {
        decorator.setItems(resourceId, listener);
    }

    @Override
    public final void setAdapter(@NonNull final ListAdapter adapter,
                                 @Nullable final OnClickListener listener) {
        decorator.setAdapter(adapter, listener);
    }

    @Override
    public final void setSingleChoiceItems(@NonNull final CharSequence[] items,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        decorator.setSingleChoiceItems(items, checkedItem, listener);
    }

    @Override
    public final void setSingleChoiceItems(@ArrayRes final int resourceId, final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        decorator.setSingleChoiceItems(resourceId, checkedItem, listener);
    }

    @Override
    public final void setSingleChoiceItems(@NonNull final ListAdapter adapter,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        decorator.setSingleChoiceItems(adapter, checkedItem, listener);
    }

    @Override
    public final void setMultiChoiceItems(@NonNull final CharSequence[] items,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        decorator.setMultiChoiceItems(items, checkedItems, listener);
    }

    @Override
    public final void setMultiChoiceItems(@ArrayRes final int resourceId,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        decorator.setMultiChoiceItems(resourceId, checkedItems, listener);
    }

    @Override
    public final void setMultiChoiceItems(@NonNull final ListAdapter adapter,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        decorator.setMultiChoiceItems(adapter, checkedItems, listener);
    }

    @Override
    public final void setOnItemSelectedListener(
            @Nullable final AdapterView.OnItemSelectedListener listener) {
        decorator.setOnItemSelectedListener(listener);
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