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

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;

import de.mrapp.android.dialog.animation.DialogAnimation;
import de.mrapp.android.dialog.decorator.AnimateableDialogDecorator;
import de.mrapp.android.dialog.model.AnimateableDialog;

/**
 * An abstract base class for all animateable dialogs, which are designed according to Android 5's
 * Material Design guidelines even on pre-Lollipop devices and are able to show fragments.
 *
 * @author Michael Rapp
 * @since 3.7.0
 */
public abstract class AbstractAnimateableDialogFragment extends AbstractMaterialDialogFragment
        implements AnimateableDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final AnimateableDialogDecorator decorator;

    /**
     * Creates an animateable dialog, which is designed according to Android 5's Material Design
     * guidelines even on pre-Lollipop devices and is able to show fragments.
     */
    public AbstractAnimateableDialogFragment() {
        this.decorator = new AnimateableDialogDecorator(this);
    }

    @Override
    public final DialogAnimation getShowAnimation() {
        return decorator.getShowAnimation();
    }

    @Override
    public final void setShowAnimation(@Nullable final DialogAnimation animation) {
        decorator.setShowAnimation(animation);
    }

    @Override
    public final DialogAnimation getDismissAnimation() {
        return decorator.getDismissAnimation();
    }

    @Override
    public final void setDismissAnimation(@Nullable final DialogAnimation animation) {
        decorator.setDismissAnimation(animation);
    }

    @Override
    public final DialogAnimation getCancelAnimation() {
        return decorator.getCancelAnimation();
    }

    @Override
    public final void setCancelAnimation(@Nullable final DialogAnimation animation) {
        decorator.setCancelAnimation(animation);
    }

    @CallSuper
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        decorator.onSaveInstanceState(outState);
    }

    @CallSuper
    @Override
    protected void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        decorator.onRestoreInstanceState(savedInstanceState);
    }

    @CallSuper
    @Override
    protected void onAttachDecorators(@NonNull final Window window, @NonNull final View view,
                                      @NonNull final FragmentManager fragmentManager) {
        super.onAttachDecorators(window, view, fragmentManager);
        decorator.attach(window, view);
    }

    @CallSuper
    @Override
    protected void onDetachDecorators() {
        super.onDetachDecorators();
        decorator.detach();
    }

}