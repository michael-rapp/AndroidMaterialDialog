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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.ScrollView;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.animation.BackgroundAnimation;
import de.mrapp.android.dialog.decorator.AbstractDecorator;
import de.mrapp.android.dialog.decorator.MaterialDialogDecorator;
import de.mrapp.android.dialog.model.MaterialDialog;
import de.mrapp.android.dialog.view.DialogRootView;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;

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
     * A collection, which contains the decorators, which are applied to the dialog.
     */
    private final Collection<AbstractDecorator> decorators;

    /**
     * The root view of the dialog.
     */
    private DialogRootView rootView;

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

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(final View v, final MotionEvent event) {
                return isCanceledOnTouchOutside() && !isFullscreen() && onCanceledOnTouchOutside();
            }

        };
    }

    /**
     * Attaches all registered decorators to the dialog.
     *
     * @param window
     *         The window, the dialog belongs to, as an instance of the class {@link Window}. The
     *         window may not be null
     * @param rootView
     *         The root view of the dialog as an instance of the class {@link DialogRootView}. The
     *         root view may not be null
     * @param view
     *         The view of the dialog as an instance of the class {@link View}. The view may not be
     *         null
     * @return A map, which contains the views, which have been inflated by the decorators, mapped
     * to their view types, as an instance of the type {@link Map} or null, if the decorator has not
     * inflated any views
     */
    private Map<ViewType, View> attachDecorators(@NonNull final Window window,
                                                 @NonNull final DialogRootView rootView,
                                                 @NonNull final View view) {
        Map<ViewType, View> result = new HashMap<>();

        for (AbstractDecorator<?, ?> decorator : decorators) {
            result.putAll(decorator.attach(window, view, result, null));
            decorator.addAreaListener(rootView);
        }

        return result;
    }

    /**
     * Detaches all registered decorators from the dialog.
     *
     * @param rootView
     *         The root view of the dialog as an instance of the class {@link DialogRootView}. The
     *         root view may not be null
     */
    private void detachDecorators(@NonNull final DialogRootView rootView) {
        for (AbstractDecorator<?, ?> decorator : decorators) {
            decorator.removeAreaListener(rootView);
            decorator.detach();
        }
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
        this.decorators = new LinkedList<>();
        addDecorator(decorator);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
    }

    /**
     * Adds a decorator to the dialog.
     *
     * @param decorator
     *         The decorator, which should be added, as an instance of the class {@link
     *         AbstractDecorator}. The decorator may not be null
     */
    protected final void addDecorator(@NonNull final AbstractDecorator decorator) {
        this.decorators.add(decorator);
    }

    /**
     * The method, which is invoked when the dialog is about to be canceled,because it is touched
     * outside the window.
     *
     * @return True, if the dialog has been cancelled, false otherwise
     */
    protected boolean onCanceledOnTouchOutside() {
        cancel();
        return true;
    }

    @Override
    public final ScrollView getScrollView() {
        return rootView != null ? rootView.getScrollView() : null;
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
    public final Drawable getWindowBackground() {
        return decorator.getWindowBackground();
    }

    @Override
    public final void setWindowBackground(@DrawableRes final int resourceId) {
        decorator.setWindowBackground(resourceId);
    }

    @Override
    public final void setWindowBackground(@Nullable final Bitmap windowBackground) {
        decorator.setWindowBackground(windowBackground);
    }

    @Override
    public final int getWindowInsetLeft() {
        return decorator.getWindowInsetLeft();
    }

    @Override
    public final int getWindowInsetTop() {
        return decorator.getWindowInsetTop();
    }

    @Override
    public final int getWindowInsetRight() {
        return decorator.getWindowInsetRight();
    }

    @Override
    public final int getWindowInsetBottom() {
        return decorator.getWindowInsetBottom();
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
    public final boolean isFullscreen() {
        return decorator.isFullscreen();
    }

    @Override
    public final void setFullscreen(final boolean fullscreen) {
        decorator.setFullscreen(fullscreen);
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
    public final int getMaxWidth() {
        return decorator.getMaxWidth();
    }

    @Override
    public final void setMaxWidth(final int maxWidth) {
        decorator.setMaxWidth(maxWidth);
    }

    @Override
    public final int getMaxHeight() {
        return decorator.getMaxHeight();
    }

    @Override
    public final void setMaxHeight(final int maxHeight) {
        decorator.setMaxHeight(maxHeight);
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
    public final int getPaddingLeft() {
        return decorator.getPaddingLeft();
    }

    @Override
    public final int getPaddingTop() {
        return decorator.getPaddingTop();
    }

    @Override
    public final int getPaddingRight() {
        return decorator.getPaddingRight();
    }

    @Override
    public final int getPaddingBottom() {
        return decorator.getPaddingBottom();
    }

    @Override
    public final void setPadding(final int left, final int top, final int right, final int bottom) {
        decorator.setPadding(left, top, right, bottom);
    }

    @Override
    public final boolean isFitsSystemWindowsLeft() {
        return decorator.isFitsSystemWindowsLeft();
    }

    @Override
    public final boolean isFitsSystemWindowsTop() {
        return decorator.isFitsSystemWindowsTop();
    }

    @Override
    public final boolean isFitsSystemWindowsRight() {
        return decorator.isFitsSystemWindowsRight();
    }

    @Override
    public final boolean isFitsSystemWindowsBottom() {
        return decorator.isFitsSystemWindowsBottom();
    }

    @Override
    public final void setFitsSystemWindows(final boolean fitsSystemsWindows) {
        decorator.setFitsSystemWindows(fitsSystemsWindows);
    }

    @Override
    public final void setFitsSystemWindows(final boolean left, final boolean top,
                                           final boolean right, final boolean bottom) {
        decorator.setFitsSystemWindows(left, top, right, bottom);
    }

    @NonNull
    @Override
    public final ScrollableArea getScrollableArea() {
        return decorator.getScrollableArea();
    }

    @Override
    public final void setScrollableArea(@Nullable final Area area) {
        decorator.setScrollableArea(area);
    }

    @Override
    public final void setScrollableArea(@Nullable final Area top, @Nullable final Area bottom) {
        decorator.setScrollableArea(top, bottom);
    }

    @Override
    public final boolean areDividersShownOnScroll() {
        return decorator.areDividersShownOnScroll();
    }

    @Override
    public final void showDividersOnScroll(final boolean show) {
        decorator.showDividersOnScroll(show);
    }

    @Override
    public final int getDividerColor() {
        return decorator.getDividerColor();
    }

    @Override
    public final void setDividerColor(@ColorInt final int color) {
        decorator.setDividerColor(color);
    }

    @Override
    public final int getDividerMargin() {
        return decorator.getDividerMargin();
    }

    @Override
    public final void setDividerMargin(final int margin) {
        decorator.setDividerMargin(margin);
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
    public final void setBackground(@Nullable final Bitmap background,
                                    @Nullable final BackgroundAnimation animation) {
        decorator.setBackground(background, animation);
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId) {
        decorator.setBackground(resourceId);
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId,
                                    @Nullable final BackgroundAnimation animation) {
        decorator.setBackground(resourceId, animation);
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color) {
        decorator.setBackgroundColor(color);
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color,
                                         @Nullable final BackgroundAnimation animation) {
        decorator.setBackgroundColor(color, animation);
    }

    @Override
    public final boolean isCustomTitleUsed() {
        return decorator.isCustomTitleUsed();
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
    public final boolean isCustomMessageUsed() {
        return decorator.isCustomMessageUsed();
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
    public final boolean isCustomViewUsed() {
        return decorator.isCustomViewUsed();
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
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView = view.findViewById(R.id.dialog_root_view);
        assert rootView != null;
        Map<ViewType, View> areas = attachDecorators(window, rootView, view);
        rootView.addAreas(areas);
    }

    @Override
    public final void onStop() {
        super.onStop();
        detachDecorators(rootView);
        rootView = null;
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