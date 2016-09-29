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

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import de.mrapp.android.dialog.decorator.MaterialDialogDecorator;
import de.mrapp.android.dialog.model.MaterialDialog;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractMaterialDialog extends Dialog implements MaterialDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final MaterialDialogDecorator decorator;

    /**
     * Inflates the dialog's root view.
     *
     * @return The view, which has been inflated, as an instance of the class {@link View}. The view
     * may not be null
     */
    private View inflateLayout() {
        return View.inflate(getContext(), R.layout.material_dialog, null);
    }

    /**
     * Creates and returns a listener, which allows to cancel the dialog, when touched outside the
     * window.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * View.OnTouchListener}
     */
    private View.OnTouchListener createCanceledOnTouchListener() {
        return new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                if (isCanceledOnTouchOutside()) {
                    cancel();
                    return true;
                }

                return false;
            }

        };
    }

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractMaterialDialog(@NonNull final Context context,
                                     @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        this.decorator = new MaterialDialogDecorator(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
    }

    /**
     * The method, which is invoked when the dialog's decorators should be attached. This method may
     * be overridden by subclasses in order to attach additional decorators.
     *
     * @param window
     *         The window, whose view hierarchy should be modified by the decorators, as an instance
     *         of the class {@link Window}. The window may not be null
     * @param view
     *         The root view of the view hierarchy, which should be modified by the decorators, as
     *         an instance of the class {@link View}. The view may not be null
     */
    @CallSuper
    protected void onAttachDecorators(@NonNull final Window window, @NonNull final View view) {
        decorator.attach(window, view);
    }

    /**
     * The method, which is invoked when the dialog's decorators should be detached. This method
     * must be overridden by subclasses if they attach additional decorators.
     */
    @CallSuper
    protected void onDetachDecorators() {
        decorator.detach();
    }

    @Override
    public final boolean isCanceledOnTouchOutside() {
        return decorator.isCanceledOnTouchOutside();
    }

    @Override
    public final void setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        super.setCanceledOnTouchOutside(canceledOnTouchOutside);
        decorator.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    @Override
    public final boolean isCancelable() {
        return decorator.isCancelable();
    }

    @Override
    public final void setCancelable(final boolean cancelable) {
        super.setCancelable(cancelable);
        decorator.setCancelable(cancelable);
    }

    @Override
    public final int getGravity() {
        return decorator.getGravity();
    }

    @Override
    public final void setGravity(final int gravity) {
        decorator.setGravity(gravity);
    }

    @Override
    public final int getWidth() {
        return decorator.getWidth();
    }

    @Override
    public final void setWidth(final int width) {
        decorator.setWidth(width);
    }

    @Override
    public final int getHeight() {
        return decorator.getHeight();
    }

    @Override
    public final void setHeight(final int height) {
        decorator.setHeight(height);
    }

    @Override
    public final int getLeftMargin() {
        return decorator.getLeftMargin();
    }

    @Override
    public final int getTopMargin() {
        return decorator.getTopMargin();
    }

    @Override
    public final int getRightMargin() {
        return decorator.getRightMargin();
    }

    @Override
    public final int getBottomMargin() {
        return decorator.getBottomMargin();
    }

    @Override
    public final void setMargin(final int left, final int top, final int right, final int bottom) {
        decorator.setMargin(left, top, right, bottom);
    }

    @Override
    public final Drawable getIcon() {
        return decorator.getIcon();
    }

    @Override
    public final void setIcon(@Nullable final Bitmap icon) {
        decorator.setIcon(icon);
    }

    @Override
    public final void setIcon(@DrawableRes final int resourceId) {
        decorator.setIcon(resourceId);
    }

    @Override
    public final void setIconAttribute(@AttrRes final int attributeId) {
        decorator.setIconAttribute(attributeId);
    }

    @Override
    public final int getTitleColor() {
        return decorator.getTitleColor();
    }

    @Override
    public final void setTitleColor(@ColorInt final int color) {
        decorator.setTitleColor(color);
    }

    @Override
    public final int getMessageColor() {
        return decorator.getMessageColor();
    }

    @Override
    public final void setMessageColor(@ColorInt final int color) {
        decorator.setMessageColor(color);
    }

    @Override
    public final Drawable getBackground() {
        return decorator.getBackground();
    }

    @Override
    public final void setBackground(@Nullable final Bitmap background) {
        decorator.setBackground(background);
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId) {
        decorator.setBackground(resourceId);
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color) {
        decorator.setBackgroundColor(color);
    }

    @Override
    public final void setCustomTitle(@Nullable final View view) {
        decorator.setCustomTitle(view);
    }

    @Override
    public final void setCustomTitle(@LayoutRes final int resourceId) {
        decorator.setCustomTitle(resourceId);
    }

    @Override
    public final void setCustomMessage(@Nullable final View view) {
        decorator.setCustomMessage(view);
    }

    @Override
    public final void setCustomMessage(@LayoutRes final int resourceId) {
        decorator.setCustomMessage(resourceId);
    }

    @Override
    public final void setView(@Nullable final View view) {
        decorator.setView(view);
    }

    @Override
    public final void setView(@LayoutRes final int resourceId) {
        decorator.setView(resourceId);
    }

    @Override
    public final CharSequence getMessage() {
        return decorator.getMessage();
    }

    @Override
    public final void setMessage(@Nullable final CharSequence message) {
        decorator.setMessage(message);
    }

    @Override
    public final void setMessage(@StringRes final int resourceId) {
        decorator.setMessage(resourceId);
    }

    @Override
    public final CharSequence getTitle() {
        return decorator.getTitle();
    }

    @Override
    public final void setTitle(@Nullable final CharSequence title) {
        super.setTitle(title);
        decorator.setTitle(title);
    }

    @Override
    public final void onStart() {
        super.onStart();
        View view = inflateLayout();
        view.setOnTouchListener(createCanceledOnTouchListener());
        setContentView(view);
        Window window = getWindow();
        assert window != null;
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View rootView = view.findViewById(R.id.root);
        onAttachDecorators(window, rootView);
    }

    @Override
    public final void onStop() {
        super.onStop();
        onDetachDecorators();
    }

    @CallSuper
    @NonNull
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