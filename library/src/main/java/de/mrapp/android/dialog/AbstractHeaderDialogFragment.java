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
package de.mrapp.android.dialog;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;

import de.mrapp.android.dialog.animation.BackgroundAnimation;
import de.mrapp.android.dialog.animation.DrawableAnimation;
import de.mrapp.android.dialog.decorator.HeaderDialogDecorator;
import de.mrapp.android.dialog.model.HeaderDialog;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * Design guidelines even on pre-Lollipop devices, are able to show fragments and may contain a
 * header.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractHeaderDialogFragment extends AbstractMaterialDialogFragment
        implements HeaderDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final HeaderDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices, is able to show fragments and may contain a header.
     */
    public AbstractHeaderDialogFragment() {
        this.decorator = new HeaderDialogDecorator(this);
    }

    @Override
    public final boolean isHeaderShown() {
        return decorator.isHeaderShown();
    }

    @Override
    public final void showHeader(final boolean show) {
        decorator.showHeader(show);
    }

    @Override
    public final void setCustomHeader(@Nullable final View view) {
        decorator.setCustomHeader(view);
    }

    @Override
    public final void setCustomHeader(@LayoutRes final int resourceId) {
        decorator.setCustomHeader(resourceId);
    }

    @Override
    public final int getHeaderHeight() {
        return decorator.getHeaderHeight();
    }

    @Override
    public final void setHeaderHeight(final int height) {
        decorator.setHeaderHeight(height);
    }

    @Override
    public final Drawable getHeaderBackground() {
        return decorator.getHeaderBackground();
    }

    @Override
    public final void setHeaderBackgroundColor(@ColorInt final int color) {
        decorator.setHeaderBackgroundColor(color);
    }

    @Override
    public final void setHeaderBackgroundColor(@ColorInt final int color,
                                               @Nullable final BackgroundAnimation animation) {
        decorator.setHeaderBackgroundColor(color, animation);
    }

    @Override
    public final void setHeaderBackground(@DrawableRes final int resourceId) {
        decorator.setHeaderBackground(resourceId);
    }

    @Override
    public final void setHeaderBackground(@DrawableRes final int resourceId,
                                          @Nullable final BackgroundAnimation animation) {
        decorator.setHeaderBackground(resourceId, animation);
    }

    @Override
    public final void setHeaderBackground(@Nullable final Bitmap background) {
        decorator.setHeaderBackground(background);
    }

    @Override
    public final void setHeaderBackground(@Nullable final Bitmap background,
                                          @Nullable final BackgroundAnimation animation) {
        decorator.setHeaderBackground(background, animation);
    }

    @Override
    public final Drawable getHeaderIcon() {
        return decorator.getHeaderIcon();
    }

    @Override
    public final void setHeaderIcon(@DrawableRes final int resourceId) {
        decorator.setHeaderIcon(resourceId);
    }

    @Override
    public final void setHeaderIcon(@DrawableRes final int resourceId,
                                    @Nullable final DrawableAnimation animation) {
        decorator.setHeaderIcon(resourceId, animation);
    }

    @Override
    public final void setHeaderIcon(@Nullable final Bitmap icon) {
        decorator.setHeaderIcon(icon);
    }

    @Override
    public final void setHeaderIcon(@Nullable final Bitmap icon,
                                    @Nullable final DrawableAnimation animation) {
        decorator.setHeaderIcon(icon, animation);
    }

    @Override
    public final int getHeaderDividerColor() {
        return decorator.getHeaderDividerColor();
    }

    @Override
    public final void setHeaderDividerColor(@ColorInt final int color) {
        decorator.setHeaderDividerColor(color);
    }

    @Override
    public final boolean isHeaderDividerShown() {
        return decorator.isHeaderDividerShown();
    }

    @Override
    public final void showHeaderDivider(final boolean show) {
        decorator.showHeaderDivider(show);
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