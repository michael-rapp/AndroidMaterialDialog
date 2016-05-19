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
package de.mrapp.android.dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

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
     * An abstract base class for all builders, which allow to create and show dialogs, which are
     * designed according to Android Material Design guidelines even on pre-Lollipop devices and may
     * contain list items.
     *
     * @param <DialogType>
     *         The type of the dialog, which is created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    public static abstract class AbstractBuilder<DialogType extends AbstractListDialog, BuilderType extends AbstractBuilder<DialogType, ?>>
            extends AbstractButtonBarDialog.AbstractBuilder<DialogType, BuilderType> {

        /**
         * Obtains the item color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the item color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainItemColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogItemColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setItemColor(color);
            }
        }

        /**
         * Obtains the item control color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the item control color should be obtained from, as
         *         an {@link Integer} value
         */
        private void obtainItemControlColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogItemControlColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setItemControlColor(color);
            }
        }

        /**
         * Creates a new builder, which allows to create dialogs, which allow to create and show
         * dialogs, which are designed according to Android 5's Material Design guidelines even on
         * pre-Lollipop devices and may contain list items.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public AbstractBuilder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which allow to create and show
         * dialogs, which are designed according to Android 5's Material Design guidelines even on
         * pre-Lollipop devices and may contain list items.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public AbstractBuilder(@NonNull final Context context,
                               @StyleRes final int themeResourceId) {
            super(context, themeResourceId);
        }

        /**
         * Sets the color of the list items of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setItemColor(@ColorInt final int color) {
            getDialog().setItemColor(color);
            return self();
        }

        /**
         * Sets the control color of the list items of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public final BuilderType setItemControlColor(@ColorInt final int color) {
            getDialog().setItemControlColor(color);
            return self();
        }

        /**
         * Sets the items, which should be shown by the dialog, which is created by the builder.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setItems(@NonNull final CharSequence[] items,
                                          @Nullable final OnClickListener listener) {
            getDialog().setItems(items, listener);
            return self();
        }

        /**
         * Sets the items, which should be shown by the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final AbstractBuilder setItems(@ArrayRes final int resourceId,
                                              @Nullable final OnClickListener listener) {
            getDialog().setItems(resourceId, listener);
            return self();
        }

        /**
         * Sets the adapter, which provides the items, which should be shown by the dialog, which is
         * created by the builder.
         *
         * @param adapter
         *         The adapter, which should be set, as an instance of the type {@link ListAdapter}.
         *         The adapter may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setAdapter(@NonNull final ListAdapter adapter,
                                            @Nullable final OnClickListener listener) {
            getDialog().setAdapter(adapter, listener);
            return self();
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Only one of the items can be selected at once.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param checkedItem
         *         The index of the item, which should be selected by default, as an {@link Integer}
         *         value or -1, if no item should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setSingleChoiceItems(@NonNull final CharSequence[] items,
                                                      final int checkedItem,
                                                      @Nullable final OnClickListener listener) {
            getDialog().setSingleChoiceItems(items, checkedItem, listener);
            return self();
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Only one of the items can be selected at once.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param checkedItem
         *         The index of the item, which should be selected by default, as an {@link Integer}
         *         value or -1, if no item should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setSingleChoiceItems(@ArrayRes final int resourceId,
                                                      final int checkedItem,
                                                      @Nullable final OnClickListener listener) {
            getDialog().setSingleChoiceItems(resourceId, checkedItem, listener);
            return self();
        }

        /**
         * Sets the adapter, which provides the selectable items, which should be shown by the
         * dialog, which is created by the builder. Only one of the items can be selected at once.
         *
         * @param adapter
         *         The adapter, which should be set, as an instance of the type {@link ListAdapter}.
         *         The adapter may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setSingleChoiceItems(@NonNull final ListAdapter adapter,
                                                      final int checkedItem,
                                                      @Nullable final OnClickListener listener) {
            getDialog().setSingleChoiceItems(adapter, checkedItem, listener);
            return self();
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Multiple items can be selected at once.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param checkedItems
         *         An array, which contains, whether the items, which correspond to the
         *         corresponding indices, should be selected by default, or not, as a {@link
         *         Boolean} array or null, if no items should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMultiChoiceItems(@NonNull final CharSequence[] items,
                                                     @Nullable final boolean[] checkedItems,
                                                     @Nullable final OnMultiChoiceClickListener listener) {
            getDialog().setMultiChoiceItems(items, checkedItems, listener);
            return self();
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Multiple items can be selected at once.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param checkedItems
         *         An array, which contains, whether the items, which correspond to the
         *         corresponding indices, should be selected by default, or not, as a {@link
         *         Boolean} array or null, if no items should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMultiChoiceItems(@ArrayRes final int resourceId,
                                                     @Nullable final boolean[] checkedItems,
                                                     @Nullable final OnMultiChoiceClickListener listener) {
            getDialog().setMultiChoiceItems(resourceId, checkedItems, listener);
            return self();
        }

        /**
         * Sets the listener, which should be notified, when an item, which is shown by the dialog,
         * which is created by the builder, is selected.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         AdapterView.OnItemSelectedListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setOnItemSelectedListener(
                @Nullable final AdapterView.OnItemSelectedListener listener) {
            getDialog().setOnItemSelectedListener(listener);
            return self();
        }

        @CallSuper
        @Override
        protected void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            super.obtainStyledAttributes(themeResourceId);
            obtainItemColor(themeResourceId);
            obtainItemControlColor(themeResourceId);
        }

    }

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
        setView(R.layout.material_dialog_list_view);
        this.decorator = new ListDialogDecorator(this);
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
    public final int getItemControlColor() {
        return decorator.getItemControlColor();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public final void setItemControlColor(@ColorInt final int color) {
        decorator.setItemControlColor(color);
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
    public final void setOnItemSelectedListener(
            @Nullable final AdapterView.OnItemSelectedListener listener) {
        decorator.setOnItemSelectedListener(listener);
    }

    @CallSuper
    @Override
    protected void onAttachDecorators(@NonNull final View view) {
        super.onAttachDecorators(view);
        decorator.attach(view);
    }

    @CallSuper
    @Override
    public void onDetachDecorators() {
        super.onDetachDecorators();
        decorator.detach();
    }

}