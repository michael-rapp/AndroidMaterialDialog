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
package de.mrapp.android.dialog.decorator;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.animation.BackgroundAnimation;
import de.mrapp.android.dialog.animation.CircleTransitionAnimation;
import de.mrapp.android.dialog.animation.CrossFadeTransitionAnimation;
import de.mrapp.android.dialog.animation.DrawableAnimation;
import de.mrapp.android.dialog.animation.ScaleTransitionAnimation;
import de.mrapp.android.dialog.drawable.AbstractTransitionDrawable;
import de.mrapp.android.dialog.drawable.CircleTransitionDrawable;
import de.mrapp.android.dialog.drawable.CrossFadeTransitionDrawable;
import de.mrapp.android.dialog.drawable.ScaleTransitionDrawable;
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
     * The view group, which contains all views of the dialog's header.
     */
    private ViewGroup header;

    /**
     * The view group, which contains the content of the dialog's header.
     */
    private ViewGroup headerContentContainer;

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
     * The custom header view of the dialog.
     */
    private View customHeaderView;

    /**
     * The resource id of the custom header view of the dialog.
     */
    private int customHeaderViewId = -1;

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
     *
     * @return The view, which has been inflated, as an instance of the class {@link View} or null,
     * if no view has been inflated
     */
    private View inflateHeader() {
        if (getRootView() != null) {
            if (header == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                header = (ViewGroup) layoutInflater
                        .inflate(R.layout.material_dialog_header, getRootView(), false);
                headerBackgroundImageView = header.findViewById(R.id.header_background_image_view);
                headerContentContainer = header.findViewById(R.id.header_content_container);
                headerDivider = header.findViewById(R.id.header_divider);
            }

            headerContentContainer.removeAllViews();

            if (customHeaderView != null) {
                headerContentContainer.addView(customHeaderView);
            } else if (customHeaderViewId != -1) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view =
                        layoutInflater.inflate(customHeaderViewId, headerContentContainer, false);
                headerContentContainer.addView(view);
            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater
                        .inflate(R.layout.header_icon_image_view, headerContentContainer, false);
                headerContentContainer.addView(view);
            }

            View iconView = headerContentContainer.findViewById(android.R.id.icon);
            headerIconImageView = iconView instanceof ImageView ? (ImageView) iconView : null;
            return header;
        }

        return null;
    }

    /**
     * Adapts the view, which is used to show the dialog's header.
     */
    private void adaptHeaderView() {
        if (header != null) {
            inflateHeader();
            adaptHeaderIcon(null);
        }
    }

    /**
     * Adapts the visibility of the dialog's header.
     */
    private void adaptHeaderVisibility() {
        if (header != null) {
            if (showHeader) {
                header.setVisibility(View.VISIBLE);
                notifyOnAreaShown(Area.HEADER);
            } else {
                header.setVisibility(View.GONE);
                notifyOnAreaHidden(Area.HEADER);
            }
        }
    }

    /**
     * Adapts the height of the dialog's header.
     */
    private void adaptHeaderHeight() {
        if (header != null) {
            ViewGroup.LayoutParams layoutParams = header.getLayoutParams();
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
                    if (previousBackground instanceof AbstractTransitionDrawable) {
                        previousBackground =
                                ((AbstractTransitionDrawable) previousBackground).getDrawable(1);
                    }

                    if (animation instanceof CircleTransitionAnimation) {
                        CircleTransitionAnimation circleTransitionAnimation =
                                (CircleTransitionAnimation) animation;
                        CircleTransitionDrawable transition = new CircleTransitionDrawable(
                                new Drawable[]{previousBackground, newBackground});
                        transition.setRadius(circleTransitionAnimation.getRadius());
                        transition.setListener(circleTransitionAnimation.getListener());

                        if (circleTransitionAnimation.getX() != null) {
                            transition.setX(circleTransitionAnimation.getX());
                        }

                        if (circleTransitionAnimation.getY() != null) {
                            transition.setY(circleTransitionAnimation.getY());
                        }

                        transition.startTransition(circleTransitionAnimation.getDuration());
                        newBackground = transition;
                    } else if (animation instanceof CrossFadeTransitionAnimation) {
                        CrossFadeTransitionDrawable transition = new CrossFadeTransitionDrawable(
                                new Drawable[]{previousBackground, newBackground});
                        transition.setListener(animation.getListener());
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
     *
     * @param animation
     *         The animation, which should be used to change the icon, as an instance of the class
     *         {@link DrawableAnimation} or null, if no animation should be used
     */
    private void adaptHeaderIcon(@Nullable final DrawableAnimation animation) {
        if (headerIconImageView != null) {
            Drawable newIcon = headerIcon;

            if (animation != null && newIcon != null) {
                Drawable previousIcon = headerIconImageView.getDrawable();

                if (previousIcon != null) {
                    if (previousIcon instanceof AbstractTransitionDrawable) {
                        previousIcon = ((AbstractTransitionDrawable) previousIcon).getDrawable(1);
                    }

                    if (animation instanceof ScaleTransitionAnimation) {
                        ScaleTransitionDrawable transition =
                                new ScaleTransitionDrawable(new Drawable[]{previousIcon, newIcon});
                        transition.setListener(animation.getListener());
                        transition.startTransition(animation.getDuration());
                        newIcon = transition;
                    } else if (animation instanceof CircleTransitionAnimation) {
                        CircleTransitionAnimation circleTransitionAnimation =
                                (CircleTransitionAnimation) animation;
                        CircleTransitionDrawable transition =
                                new CircleTransitionDrawable(new Drawable[]{previousIcon, newIcon});
                        transition.setRadius(circleTransitionAnimation.getRadius());
                        transition.setListener(circleTransitionAnimation.getListener());

                        if (circleTransitionAnimation.getX() != null) {
                            transition.setX(circleTransitionAnimation.getX());
                        }

                        if (circleTransitionAnimation.getY() != null) {
                            transition.setY(circleTransitionAnimation.getY());
                        }

                        transition.startTransition(circleTransitionAnimation.getDuration());
                        newIcon = transition;
                    } else if (animation instanceof CrossFadeTransitionAnimation) {
                        CrossFadeTransitionDrawable transition = new CrossFadeTransitionDrawable(
                                new Drawable[]{previousIcon, newIcon});
                        transition.setCrossFade(true);
                        transition.setListener(animation.getListener());
                        transition.startTransition(animation.getDuration());
                        newIcon = transition;
                    } else {
                        throw new RuntimeException("Unknown type of animation: " +
                                animation.getClass().getSimpleName());
                    }
                }
            }

            headerIconImageView.setImageDrawable(newIcon);
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
        getDialog().setFitsSystemWindows(getDialog().isFitsSystemWindowsLeft(), !show,
                getDialog().isFitsSystemWindowsRight(), getDialog().isFitsSystemWindowsBottom());
        adaptHeaderVisibility();
    }

    @Override
    public final void setCustomHeader(@Nullable final View view) {
        customHeaderView = view;
        customHeaderViewId = -1;
        adaptHeaderView();
    }

    @Override
    public final boolean isCustomHeaderUsed() {
        return customHeaderView != null || customHeaderViewId != -1;
    }

    @Override
    public final void setCustomHeader(@LayoutRes final int resourceId) {
        customHeaderView = null;
        customHeaderViewId = resourceId;
        adaptHeaderView();
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
        setHeaderIcon(icon, null);
    }

    @Override
    public final void setHeaderIcon(@Nullable final Bitmap icon,
                                    @Nullable final DrawableAnimation animation) {
        this.headerIconBitmap = icon;
        this.headerIconId = -1;
        this.headerIcon = new BitmapDrawable(getContext().getResources(), icon);
        adaptHeaderIcon(animation);
    }

    @Override
    public final void setHeaderIcon(@DrawableRes final int resourceId) {
        setHeaderIcon(resourceId, null);
    }

    @Override
    public void setHeaderIcon(@DrawableRes final int resourceId,
                              @Nullable final DrawableAnimation animation) {
        this.headerIconBitmap = null;
        this.headerIconId = resourceId;
        this.headerIcon = ContextCompat.getDrawable(getContext(), resourceId);
        adaptHeaderIcon(animation);
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

    @NonNull
    @Override
    protected final Map<Area, View> onAttach(@NonNull final Window window, @NonNull final View view,
                                             @NonNull final Map<Area, View> areas,
                                             final Void param) {
        View inflatedView = inflateHeader();

        if (inflatedView != null) {
            adaptHeaderVisibility();
            adaptHeaderBackground(null);
            adaptHeaderDividerColor();
            adaptHeaderDividerVisibility();
            adaptHeaderIcon(null);
            adaptHeaderHeight();
            Map<Area, View> result = new HashMap<>();
            result.put(Area.HEADER, inflatedView);
            return result;
        }

        return Collections.emptyMap();
    }

    @Override
    protected final void onDetach() {
        header = null;
        headerContentContainer = null;
        headerBackgroundImageView = null;
        headerIconImageView = null;
        headerDivider = null;
    }

}