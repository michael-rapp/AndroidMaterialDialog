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

import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;

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
     * The decorator, which is used by the decorator.
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
    public final void setHeaderBackground(@DrawableRes final int resourceId) {
        decorator.setHeaderBackground(resourceId);
    }

    @Override
    public final void setHeaderBackground(@NonNull final Drawable background) {
        decorator.setHeaderBackground(background);
    }

    @Override
    public final Drawable getHeaderIcon() {
        return decorator.getHeaderIcon();
    }

    @Override
    public final void setHeaderIcon(@DrawableRes final int resourceId) {
        setHeaderIcon(ContextCompat.getDrawable(getContext(), resourceId));
    }

    @Override
    public final void setHeaderIcon(@Nullable final Drawable icon) {
        decorator.setHeaderIcon(icon);
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
    protected void onAttachDecorators(@NonNull final View view,
                                      @NonNull final FragmentManager fragmentManager) {
        super.onAttachDecorators(view, fragmentManager);
        decorator.attach(view);
    }

    @CallSuper
    @Override
    protected void onDetachDecorators() {
        super.onDetachDecorators();
        decorator.detach();
    }

}