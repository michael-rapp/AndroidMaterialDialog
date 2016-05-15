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
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import de.mrapp.android.util.ThemeUtil;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices and may contain a header. Such a dialog consists of a title, a message and
 * up to three buttons. Furthermore, the dialog can be used to show list items. It is possible to
 * customize the color of the dialog's title and button texts and the title as well as the dialog's
 * content can be replaced with a custom view. The dialog's header displays a background color or
 * image and may contain an optional icon.
 *
 * For creating or showing such dialogs, the methods {@link HeaderDialog#create()} or {@link
 * HeaderDialog#show()} of the builder {@link HeaderDialog.Builder} can be used.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class HeaderDialog extends AbstractListDialog {

    /**
     * A builder, which allows to create and show dialogs, which are designed according to Android
     * 5's Material Design guidelines even on pre-Lollipop devices and may contain a header. Such a
     * dialog consists of a title, a message and up to three buttons. Furthermore, the dialog can be
     * used to show list items. It is possible to customize the color of the dialog's title and
     * button texts and the title as well as the dialog's content can be replaced with a custom
     * view. The dialog's header displays a background color or image and may contain an optional
     * icon.
     */
    public static class Builder extends AbstractListDialog.AbstractBuilder<HeaderDialog, Builder> {

        /**
         * Obtains the height of the dialog's header from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the height should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainHeaderHeight(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogHeaderHeight});
            int defaultHeight =
                    getContext().getResources().getDimensionPixelSize(R.dimen.dialog_header_height);
            setHeaderHeight(typedArray.getDimensionPixelSize(0, defaultHeight));
        }

        /**
         * Obtains the background of the dialog's header from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the background should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainHeaderBackground(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogHeaderBackground});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setHeaderBackgroundColor(color);
            } else {
                int resourceId = typedArray.getResourceId(0, 0);

                if (resourceId != 0) {
                    setHeaderBackground(resourceId);
                } else {
                    setHeaderBackgroundColor(
                            ThemeUtil.getColor(getContext(), themeResourceId, R.attr.colorPrimary));
                }
            }
        }

        /**
         * Obtains the icon of the dialog's header from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the icon should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainHeaderIcon(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogHeaderIcon});
            int resourceId = typedArray.getResourceId(0, 0);

            if (resourceId != 0) {
                setHeaderIcon(resourceId);
            }
        }

        /**
         * Obtains the color of the divider of the dialog's header from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the color should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainHeaderDividerColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogHeaderDividerColor});
            int defaultColor = ContextCompat.getColor(getContext(), R.color.header_divider_color);
            setHeaderDividerColor(typedArray.getColor(0, defaultColor));
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices and may contain a
         * header.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices and may contain a
         * header.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public Builder(@NonNull final Context context, @StyleRes final int themeResourceId) {
            super(context, themeResourceId);
        }

        /**
         * Sets the height of the header of the dialog, which is created by the builder.
         *
         * @param height
         *         The height, which should be set, in pixels as an {@link Integer} value. The
         *         height must be at least 0
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setHeaderHeight(final int height) {
            getDialog().setHeaderHeight(height);
            return self();
        }

        /**
         * Sets the background color of the header of the dialog, which is created by the builder.
         *
         * @param color
         *         The background color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setHeaderBackgroundColor(@ColorInt final int color) {
            getDialog().setHeaderBackground(new ColorDrawable(color));
            return self();
        }

        /**
         * Sets the background of the header of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the background, which should be set, as an {@link Integer}
         *         value. The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setHeaderBackground(@DrawableRes final int resourceId) {
            getDialog().setHeaderBackground(ContextCompat.getDrawable(getContext(), resourceId));
            return self();
        }

        /**
         * Sets the background of the header of the dialog, which is created by the builder.
         *
         * @param background
         *         The background, which should be set, as an instance of the class {@link
         *         Drawable}. The background may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setHeaderBackground(@NonNull final Drawable background) {
            getDialog().setHeaderBackground(background);
            return self();
        }

        /**
         * Sets the icon of the header of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the icon, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of class {@link
         * Builder}
         */
        public final Builder setHeaderIcon(@DrawableRes final int resourceId) {
            getDialog().setHeaderIcon(ContextCompat.getDrawable(getContext(), resourceId));
            return self();
        }

        /**
         * Sets the icon of the header of the dialog, which is created by the builder.
         *
         * @param icon
         *         The icon, which should be set, as an instance of the class {@link Drawable} or
         *         null, if no icon should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setHeaderIcon(@Nullable final Drawable icon) {
            getDialog().setHeaderIcon(icon);
            return self();
        }

        /**
         * Sets the color of the divider of the header of the dialog, which is created by the
         * builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setHeaderDividerColor(@ColorInt final int color) {
            getDialog().setHeaderDividerColor(color);
            return self();
        }

        @Override
        protected final HeaderDialog onCreateDialog(@NonNull final Context context,
                                                    @StyleRes final int themeResourceId) {
            return new HeaderDialog(context, themeResourceId);
        }

        @Override
        protected final void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            super.obtainStyledAttributes(themeResourceId);
            obtainHeaderHeight(themeResourceId);
            obtainHeaderBackground(themeResourceId);
            obtainHeaderIcon(themeResourceId);
            obtainHeaderDividerColor(themeResourceId);
        }

    }

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
     * The height of the dialog's header.
     */
    private int headerHeight;

    /**
     * The background of the dialog's header.
     */
    private Drawable headerBackground;

    /**
     * The icon of the dialog's header.
     */
    private Drawable headerIcon;

    /**
     * The color of the divider of the dialog's header.
     */
    private int headerDividerColor;

    /**
     * Inflates the dialog's header.
     */
    private void inflateHeader() {
        ViewGroup rootView = getRootView();
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        headerContainer = (ViewGroup) layoutInflater
                .inflate(R.layout.material_dialog_header, rootView, false);
        headerBackgroundImageView =
                (ImageView) headerContainer.findViewById(R.id.header_background_image_view);
        headerIconImageView = (ImageView) headerContainer.findViewById(R.id.header_icon_image_view);
        rootView.addView(headerContainer, 0);
    }

    /**
     * Adapts the height of the dialog's header.
     */
    private void adaptHeaderHeight() {
        if (headerContainer != null) {
            headerContainer.setVisibility(headerHeight > 0 ? View.VISIBLE : View.GONE);
            ViewGroup.LayoutParams layoutParams = headerContainer.getLayoutParams();
            layoutParams.height = headerHeight;
        }
    }

    /**
     * Adapts the background of the dialog's header.
     */
    private void adaptHeaderBackground() {
        if (headerBackgroundImageView != null) {
            headerBackgroundImageView.setImageDrawable(headerBackground);
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
    private void adaptHeaderDivider() {
        if (headerDivider != null) {
            headerDivider.setBackgroundColor(headerDividerColor);
        }
    }

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
    protected HeaderDialog(@NonNull final Context context, @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
    }

    /**
     * Returns the height of the dialog's header.
     *
     * @return The height of the dialog's header in pixels as an {@link Integer} value
     */
    public final int getHeaderHeight() {
        return headerHeight;
    }

    /**
     * Sets the height of the dialog's header.
     *
     * @param height
     *         The height, which should be set, in pixels as an {@link Integer} value. The height
     *         must be at least 0
     */
    public final void setHeaderHeight(final int height) {
        ensureAtLeast(height, 0, "The height must be at least 0");
        this.headerHeight = height;
        adaptHeaderHeight();
    }

    /**
     * Returns the background of the dialog's header.
     *
     * @return The background of the dialog's header as an instance of the class {@link Drawable}
     */
    public final Drawable getHeaderBackground() {
        return headerBackground;
    }

    /**
     * Sets the background color of the dialog's header.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value
     */
    public final void setHeaderBackgroundColor(@ColorInt final int color) {
        setHeaderBackground(new ColorDrawable(color));
    }

    /**
     * Sets the background of the dialog's header.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    public final void setHeaderBackground(@DrawableRes final int resourceId) {
        setHeaderBackground(ContextCompat.getDrawable(getContext(), resourceId));
    }

    /**
     * Sets the background of the dialog's header.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable}.
     *         The background may not be null
     */
    public final void setHeaderBackground(@NonNull final Drawable background) {
        ensureNotNull(background, "The background may not be null");
        this.headerBackground = background;
        adaptHeaderBackground();
    }

    /**
     * Returns the icon of the dialog's header.
     *
     * @return The icon of the dialog's header as an instance of the class {@link Drawable} or null,
     * if no icon has been set
     */
    public final Drawable getHeaderIcon() {
        return headerIcon;
    }

    /**
     * Sets the icon of the dialog's header.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    public final void setHeaderIcon(@DrawableRes final int resourceId) {
        setHeaderIcon(ContextCompat.getDrawable(getContext(), resourceId));
    }

    /**
     * Sets the icon of the dialog's header.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Drawable} or null,
     *         if no icon should be set
     */
    public final void setHeaderIcon(@Nullable final Drawable icon) {
        this.headerIcon = icon;
        adaptHeaderIcon();
    }

    /**
     * Returns the color of the divider of the dialog's header.
     *
     * @return The color of the divider of the dialog's header as an {@link Integer} value
     */
    public final int getHeaderDividerColor() {
        return headerDividerColor;
    }

    /**
     * Sets the color of the divider of the dialog's header.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setHeaderDividerColor(@ColorInt final int color) {
        this.headerDividerColor = color;
        adaptHeaderDivider();
    }

    @Override
    public final void onStart() {
        super.onStart();
        inflateHeader();
        adaptHeaderBackground();
        adaptHeaderDivider();
        adaptHeaderIcon();
        adaptHeaderHeight();
    }

    @Override
    public final void onStop() {
        super.onStop();
        headerBackgroundImageView = null;
        headerIconImageView = null;
    }

}