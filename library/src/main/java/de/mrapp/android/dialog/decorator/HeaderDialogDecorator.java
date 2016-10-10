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
package de.mrapp.android.dialog.decorator;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.animation.BackgroundAnimation;
import de.mrapp.android.dialog.animation.CircleTransitionAnimation;
import de.mrapp.android.dialog.animation.CrossFadeAnimation;
import de.mrapp.android.dialog.drawable.CircleTransitionDrawable;
import de.mrapp.android.dialog.model.MaterialDialog;

import static de.mrapp.android.util.Condition.ensureAtLeast;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material Design guidelines even on pre-Lollipop devices and may contain a header.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class HeaderDialogDecorator extends AbstractDialogDecorator<MaterialDialog>
        implements de.mrapp.android.dialog.model.HeaderDialogDecorator {

    /**
     * The name of the extra, which is used to store, whether the dialog's header should be shown,
     * or not, within a bundle.
     */
    private static final String SHOW_HEADER_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::showHeader";

    /**
     * The name of the extra, which is used to store the height of the dialog's header within a
     * bundle.
     */
    private static final String HEADER_HEIGHT_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::headerHeight";

    /**
     * The name of the extra, which is used to store, whether the divider of the dialog's header
     * should be shown, or not, within a bundle.
     */
    private static final String SHOW_HEADER_DIVIDER_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::showHeaderDivider";

    /**
     * The name of the extra, which is used to store the color of the divider of the dialog's header
     * within a bundle.
     */
    private static final String HEADER_DIVIDER_COLOR_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::headerDividerColor";

    /**
     * The name of the extra, which is used to store the bitmap of the background of the dialog's
     * header within a bundle.
     */
    private static final String HEADER_BACKGROUND_BITMAP_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::headerBackgroundBitmap";

    /**
     * The name of the extra, which is used to store the resource id of the background of the
     * dialog's header within a bundle.
     */
    private static final String HEADER_BACKGROUND_ID_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::headerBackgroundId";

    /**
     * The name of the extra, which is used to store the color of the background of the dialog's
     * header.
     */
    private static final String HEADER_BACKGROUND_COLOR_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::headerBackgroundColor";

    /**
     * The name of the extra, which is used to store the bitmap of the icon of the dialog's header
     * within a bundle.
     */
    private static final String HEADER_ICON_BITMAP_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::headerIconBitmap";

    /**
     * The name of the extra, which is used to store the resource id of the icon of the dialog's
     * header within a bundle.
     */
    private static final String HEADER_ICON_ID_EXTRA =
            HeaderDialogDecorator.class.getSimpleName() + "::headerIconId";

    /**
     * The view group, which contains the views of the dialog's header.
     */
    private ViewGroup headerContainer;

    /**
     * The image view, which is used to show the background of the dialog's header.
     */
    private ImageView headerBackgroundImageView;

    /**
     * The image view, which is used to show the icon of the dialog's header.
     */
    private ImageView headerIconImageView;

    /**
     * The view, which is used to show the divider of the dialog's header.
     */
    private View headerDivider;

    /**
     * True, if the dialog's header is shown, false otherwise.
     */
    private boolean showHeader;

    /**
     * The height of the dialog's header.
     */
    private int headerHeight;

    /**
     * The background of the dialog's header.
     */
    private Drawable headerBackground;

    /**
     * The bitmap of the background of the dialog's header
     */
    private Bitmap headerBackgroundBitmap;

    /**
     * The resource id of the background of the dialog's header.
     */
    private int headerBackgroundId = -1;

    /**
     * The color of the background of the dialog's header.
     */
    private int headerBackgroundColor = -1;

    /**
     * The icon of the dialog's header.
     */
    private Drawable headerIcon;

    /**
     * The bitmap of the icon of the dialog's header.
     */
    private Bitmap headerIconBitmap;

    /**
     * The resource id of the icon of the dialog's header.
     */
    private int headerIconId = -1;

    /**
     * True, if the divider of the dialog's header is shown, false otherwise.
     */
    private boolean showHeaderDivider;

    /**
     * The color of the divider of the dialog's header.
     */
    private int headerDividerColor;

    /**
     * Inflates the dialog's header.
     */
    private void inflateHeader() {
        ViewGroup rootView = (ViewGroup) getView();

        if (rootView != null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            headerContainer = (ViewGroup) layoutInflater
                    .inflate(R.layout.material_dialog_header, rootView, false);
            headerBackgroundImageView =
                    (ImageView) headerContainer.findViewById(R.id.header_background_image_view);
            headerIconImageView =
                    (ImageView) headerContainer.findViewById(R.id.header_icon_image_view);
            headerDivider = headerContainer.findViewById(R.id.header_divider);
            rootView.addView(headerContainer, 0);
        }
    }

    /**
     * Adapts the visibility of the dialog's header.
     */
    private void adaptHeaderVisibility() {
        if (headerContainer != null) {
            headerContainer.setVisibility(showHeader ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Adapts the height of the dialog's header.
     */
    private void adaptHeaderHeight() {
        if (headerContainer != null) {
            ViewGroup.LayoutParams layoutParams = headerContainer.getLayoutParams();
            layoutParams.height = headerHeight;
        }
    }

    /**
     * Adapts the background of the dialog's header.
     *
     * @param animation
     *         The animation, which should be used to change the background, as an instance of the
     *         class {@link BackgroundAnimation} or null, if no animation should be used
     */
    private void adaptHeaderBackground(@Nullable final BackgroundAnimation animation) {
        if (headerBackgroundImageView != null) {
            Drawable newBackground = headerBackground;

            if (animation != null && newBackground != null) {
                Drawable previousBackground = headerBackgroundImageView.getDrawable();

                if (previousBackground != null) {
                    if (previousBackground instanceof TransitionDrawable ||
                            previousBackground instanceof CircleTransitionDrawable) {
                        previousBackground = ((LayerDrawable) previousBackground).getDrawable(1);
                    }

                    if (animation instanceof CircleTransitionAnimation) {
                        CircleTransitionAnimation circleTransitionAnimation =
                                (CircleTransitionAnimation) animation;
                        CircleTransitionDrawable transition = new CircleTransitionDrawable(
                                new Drawable[]{previousBackground, newBackground});
                        transition.setRadius(circleTransitionAnimation.getRadius());

                        if (circleTransitionAnimation.getX() != null) {
                            transition.setX(circleTransitionAnimation.getX());
                        }

                        if (circleTransitionAnimation.getY() != null) {
                            transition.setY(circleTransitionAnimation.getY());
                        }

                        transition.startTransition(circleTransitionAnimation.getDuration());
                        newBackground = transition;
                    } else if (animation instanceof CrossFadeAnimation) {
                        TransitionDrawable transition = new TransitionDrawable(
                                new Drawable[]{previousBackground, newBackground});
                        transition.startTransition(animation.getDuration());
                        newBackground = transition;
                    } else {
                        throw new RuntimeException("Unknown type of animation: " +
                                animation.getClass().getSimpleName());
                    }
                }
            }

            headerBackgroundImageView.setImageDrawable(newBackground);
        }
    }

    /**
     * Adapt's the icon of the dialog's header.
     */
    private void adaptHeaderIcon() {
        if (headerIconImageView != null) {
            headerIconImageView.setImageDrawable(headerIcon);
        }
    }

    /**
     * Adapt's the color of the divider of the dialog's header.
     */
    private void adaptHeaderDividerColor() {
        if (headerDivider != null) {
            headerDivider.setBackgroundColor(headerDividerColor);
        }
    }

    /**
     * Adapts the visibility of the divider of the dialog's header.
     */
    private void adaptHeaderDividerVisibility() {
        if (headerDivider != null) {
            headerDivider.setVisibility(showHeaderDivider ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
     * may contain a header.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the type {@link MaterialDialog}. The dialog may not be null
     */
    public HeaderDialogDecorator(@NonNull final MaterialDialog dialog) {
        super(dialog);
    }

    @Override
    public final boolean isHeaderShown() {
        return showHeader;
    }

    @Override
    public final void showHeader(final boolean show) {
        this.showHeader = show;
        adaptHeaderVisibility();
    }

    @Override
    public final int getHeaderHeight() {
        return headerHeight;
    }

    @Override
    public final void setHeaderHeight(final int height) {
        ensureAtLeast(height, 0, "The height must be at least 0");
        this.headerHeight = height;
        adaptHeaderHeight();
    }

    @Override
    public final Drawable getHeaderBackground() {
        return headerBackground;
    }

    @Override
    public final void setHeaderBackground(@Nullable final Bitmap background) {
        setHeaderBackground(background, null);
    }

    @Override
    public final void setHeaderBackground(@Nullable final Bitmap background,
                                          @Nullable final BackgroundAnimation animation) {
        this.headerBackgroundBitmap = background;
        this.headerBackgroundId = -1;
        this.headerBackgroundColor = -1;
        this.headerBackground = new BitmapDrawable(getContext().getResources(), background);
        adaptHeaderBackground(animation);
    }

    @Override
    public final void setHeaderBackground(@DrawableRes final int resourceId) {
        setHeaderBackground(resourceId, null);
    }

    @Override
    public final void setHeaderBackground(@DrawableRes final int resourceId,
                                          @Nullable final BackgroundAnimation animation) {
        this.headerBackgroundBitmap = null;
        this.headerBackgroundId = resourceId;
        this.headerBackgroundColor = -1;
        this.headerBackground = ContextCompat.getDrawable(getContext(), resourceId);
        adaptHeaderBackground(animation);
    }

    @Override
    public final void setHeaderBackgroundColor(@ColorInt final int color) {
        setHeaderBackgroundColor(color, null);
    }

    @Override
    public void setHeaderBackgroundColor(@ColorInt final int color,
                                         @Nullable final BackgroundAnimation animation) {
        this.headerBackgroundBitmap = null;
        this.headerBackgroundId = -1;
        this.headerBackgroundColor = color;
        this.headerBackground = new ColorDrawable(color);
        adaptHeaderBackground(animation);
    }

    @Override
    public final Drawable getHeaderIcon() {
        return headerIcon;
    }

    @Override
    public final void setHeaderIcon(@Nullable final Bitmap icon) {
        this.headerIconBitmap = icon;
        this.headerIconId = -1;
        this.headerIcon = new BitmapDrawable(getContext().getResources(), icon);
        adaptHeaderIcon();
    }

    @Override
    public final void setHeaderIcon(@DrawableRes final int resourceId) {
        this.headerIconBitmap = null;
        this.headerIconId = resourceId;
        this.headerIcon = ContextCompat.getDrawable(getContext(), resourceId);
        adaptHeaderIcon();
    }

    @Override
    public final int getHeaderDividerColor() {
        return headerDividerColor;
    }

    @Override
    public final void setHeaderDividerColor(@ColorInt final int color) {
        this.headerDividerColor = color;
        adaptHeaderDividerColor();
    }

    @Override
    public final boolean isHeaderDividerShown() {
        return showHeaderDivider;
    }

    @Override
    public final void showHeaderDivider(final boolean show) {
        this.showHeaderDivider = show;
        adaptHeaderDividerVisibility();
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putBoolean(SHOW_HEADER_EXTRA, isHeaderShown());
        outState.putInt(HEADER_HEIGHT_EXTRA, getHeaderHeight());
        outState.putBoolean(SHOW_HEADER_DIVIDER_EXTRA, isHeaderDividerShown());
        outState.putInt(HEADER_DIVIDER_COLOR_EXTRA, getHeaderDividerColor());

        if (headerBackgroundBitmap != null) {
            outState.putParcelable(HEADER_BACKGROUND_BITMAP_EXTRA, headerBackgroundBitmap);
        } else if (headerBackgroundId != -1) {
            outState.putInt(HEADER_BACKGROUND_ID_EXTRA, headerBackgroundId);
        } else if (headerBackgroundColor != -1) {
            outState.putInt(HEADER_BACKGROUND_COLOR_EXTRA, headerBackgroundColor);
        }

        if (headerIconBitmap != null) {
            outState.putParcelable(HEADER_ICON_BITMAP_EXTRA, headerIconBitmap);
        } else if (headerIconId != -1) {
            outState.putInt(HEADER_ICON_ID_EXTRA, headerIconId);
        }
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        showHeader(savedInstanceState.getBoolean(SHOW_HEADER_EXTRA));
        setHeaderHeight(savedInstanceState.getInt(HEADER_HEIGHT_EXTRA));
        showHeaderDivider(savedInstanceState.getBoolean(SHOW_HEADER_DIVIDER_EXTRA));
        setHeaderDividerColor(savedInstanceState.getInt(HEADER_DIVIDER_COLOR_EXTRA));

        if (savedInstanceState.containsKey(HEADER_BACKGROUND_BITMAP_EXTRA)) {
            setHeaderBackground(
                    (Bitmap) savedInstanceState.getParcelable(HEADER_BACKGROUND_BITMAP_EXTRA));
        } else if (savedInstanceState.containsKey(HEADER_BACKGROUND_ID_EXTRA)) {
            setHeaderBackground(savedInstanceState.getInt(HEADER_BACKGROUND_ID_EXTRA));
        } else if (savedInstanceState.containsKey(HEADER_BACKGROUND_COLOR_EXTRA)) {
            setHeaderBackgroundColor(savedInstanceState.getInt(HEADER_BACKGROUND_COLOR_EXTRA));
        }

        if (savedInstanceState.containsKey(HEADER_ICON_BITMAP_EXTRA)) {
            setHeaderIcon((Bitmap) savedInstanceState.getParcelable(HEADER_ICON_BITMAP_EXTRA));
        } else if (savedInstanceState.containsKey(HEADER_ICON_ID_EXTRA)) {
            setHeaderIcon(savedInstanceState.getInt(HEADER_ICON_ID_EXTRA));
        }
    }

    @Override
    protected final void onAttach(@NonNull final Window window, @NonNull final View view) {
        inflateHeader();
        adaptHeaderVisibility();
        adaptHeaderBackground(null);
        adaptHeaderDividerColor();
        adaptHeaderDividerVisibility();
        adaptHeaderIcon();
        adaptHeaderHeight();
    }

    @Override
    protected final void onDetach() {
        headerBackgroundImageView = null;
        headerIconImageView = null;
    }

}