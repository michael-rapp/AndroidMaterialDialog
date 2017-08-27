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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;

import de.mrapp.android.dialog.animation.BackgroundAnimation;
import de.mrapp.android.dialog.animation.DrawableAnimation;
import de.mrapp.android.dialog.decorator.HeaderDialogDecorator;
import de.mrapp.android.dialog.model.HeaderDialog;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * Design guidelines even on pre-Lollipop devices and may contain a header.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractHeaderDialog extends AbstractMaterialDialog implements HeaderDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final HeaderDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and may contain a header.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractHeaderDialog(@NonNull final Context context,
                                   @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
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

    @CallSuper
    @Override
    protected void onAttachDecorators(@NonNull final Window window, @NonNull final View view) {
        super.onAttachDecorators(window, view);
        decorator.attach(window, view);

    }

    @CallSuper
    @Override
    protected void onDetachDecorators() {
        super.onDetachDecorators();
        decorator.detach();
    }

}