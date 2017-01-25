/*
 * Copyright 2014 - 2017 Michael Rapp
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
import android.content.res.TypedArray;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.ScrollableDialog;

/**
 * An abstract base class for all builders, which allow to create and show dialogs, which are
 * designed according to Android Material Design guidelines even on pre-Lollipop devices and may
 * contain a scrollable content.
 *
 * @param <DialogType>
 *         The type of the dialog, which is created by the builder
 * @param <BuilderType>
 *         The type of the builder
 * @author Michael Rapp
 * @since 3.6.0
 */
public abstract class AbstractScrollableDialogBuilder<DialogType extends ScrollableDialog, BuilderType extends AbstractScrollableDialogBuilder<DialogType, ?>>
        extends AbstractListDialogBuilder<DialogType, BuilderType> {

    /**
     * Obtains, whether the dividers, which are located above and below the dialog's list view,
     * should be shown, when the list view is scrolled, or not, from a specific theme.
     *
     * @param themeResourceId
     *         The resource id of the theme, the boolean value should be obtained from, as an {@link
     *         Integer} value
     */
    private void obtainShowDividersOnScroll(@StyleRes final int themeResourceId) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                new int[]{R.attr.materialDialogShowDividersOnScroll});
        showDividersOnScroll(typedArray.getBoolean(0, true));
    }

    /**
     * Creates a new builder, which allows to create dialogs, which allow to create and show
     * dialogs, which are designed according to Android 5's Material Design guidelines even on
     * pre-Lollipop devices and may contain a scrollable content.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public AbstractScrollableDialogBuilder(@NonNull final Context context) {
        super(context);
    }

    /**
     * Creates a new builder, which allows to create dialogs, which allow to create and show
     * dialogs, which are designed according to Android 5's Material Design guidelines even on
     * pre-Lollipop devices and may contain a scrollable content.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    public AbstractScrollableDialogBuilder(@NonNull final Context context,
                                           @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
    }

    /**
     * Sets, whether dividers, which are located above and below the list view of the dialog, which
     * is created by the builder, should be shown, when the list view is scrolled, or not.
     *
     * @param show
     *         True, if the dividers, which are located above and below the dialog's list view,
     *         should be shown, when the list view is scrolled, false otherwise
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType showDividersOnScroll(boolean show) {
        getProduct().showDividersOnScroll(show);
        return self();
    }

    @CallSuper
    @Override
    protected void obtainStyledAttributes(@StyleRes final int themeResourceId) {
        super.obtainStyledAttributes(themeResourceId);
        obtainShowDividersOnScroll(themeResourceId);
    }

}