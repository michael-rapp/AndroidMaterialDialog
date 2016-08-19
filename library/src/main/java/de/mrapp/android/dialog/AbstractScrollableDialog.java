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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;

import de.mrapp.android.dialog.decorator.ScrollableDialogDecorator;
import de.mrapp.android.dialog.model.ScrollableDialog;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices and may contain a scrollable content.
 *
 * @author Michael Rapp
 * @since 3.6.0
 */
public abstract class AbstractScrollableDialog extends AbstractListDialog
        implements ScrollableDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final ScrollableDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and may contain a scrollable content.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractScrollableDialog(@NonNull final Context context,
                                       @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        this.decorator = new ScrollableDialogDecorator(this);
    }

    @Override
    public final boolean areDividersShownOnScroll() {
        return decorator.areDividersShownOnScroll();
    }

    @Override
    public final void showDividersOnScroll(final boolean show) {
        decorator.showDividersOnScroll(show);
    }

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

    @CallSuper
    @Override
    protected void onAttachDecorators(@NonNull final View view) {
        super.onAttachDecorators(view);
        decorator.attach(view);
    }

    @CallSuper
    @Override
    protected void onDetachDecorators() {
        super.onDetachDecorators();
        decorator.detach();
    }

}