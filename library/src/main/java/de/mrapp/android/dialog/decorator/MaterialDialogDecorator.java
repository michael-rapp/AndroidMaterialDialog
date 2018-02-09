/*
 * Copyright 2014 - 2018 Michael Rapp
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

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v4.widget.ImageViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.ScrollableArea;
import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.animation.BackgroundAnimation;
import de.mrapp.android.dialog.animation.CircleTransitionAnimation;
import de.mrapp.android.dialog.animation.CrossFadeTransitionAnimation;
import de.mrapp.android.dialog.drawable.AbstractTransitionDrawable;
import de.mrapp.android.dialog.drawable.CircleTransitionDrawable;
import de.mrapp.android.dialog.drawable.CrossFadeTransitionDrawable;
import de.mrapp.android.dialog.model.Dialog;
import de.mrapp.android.dialog.view.DialogRootView;
import de.mrapp.android.dialog.view.DialogRootView.AreaViewType;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;
import de.mrapp.android.util.ViewUtil;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material Design guidelines even on pre-Lollipop devices.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class MaterialDialogDecorator extends AbstractDialogDecorator<Dialog>
        implements de.mrapp.android.dialog.model.MaterialDialogDecorator {

    /**
     * The name of the extra, which is used to store the resource id of the dialog's window
     * background within a bundle.
     */
    private static final String WINDOW_BACKGROUND_ID_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::windowBackgroundId";

    /**
     * The name of the extra, which is used to store the bitmap of the dialog's window
     * background within a bundle.
     */
    private static final String WINDOW_BACKGROUND_BITMAP_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::windowBackgroundBitmap";

    /**
     * The name of the extra, which is used to store, whether the dialog is cancelable, or not,
     * within a bundle.
     */
    private static final String CANCELABLE_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::cancelable";

    /**
     * The name of the extra, which is used to store, whether the dialog is canceled when touching
     * the outside the window, or not, within a bundle.
     */
    private static final String CANCEL_ON_TOUCH_OUTSIDE_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::cancelOnTouchOutside";

    /**
     * The name of the extra, which is used to store, whether the dialog is shown fullscreen, or
     * not, within a bundle.
     */
    private static final String FULLSCREEN_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::fullscreen";

    /**
     * The name of the extra, which is used to store the gravity of the dialog within a bundle.
     */
    private static final String GRAVITY_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::gravity";

    /**
     * The name of the extra, which is used to store the width of the dialog within a bundle.
     */
    private static final String WIDTH_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::width";

    /**
     * The name of the extra, which is used to store the height of the dialog within a bundle.
     */
    private static final String HEIGHT_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::height";

    /**
     * The name of the extra, which is used to store the maximum width of the dialog within a
     * bundle.
     */
    private static final String MAX_WIDTH_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::maxWidth";

    /**
     * The name of the extra, which is used to store the maximum height of the dialog within a
     * bundle.
     */
    private static final String MAX_HEIGHT_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::maxHeight";

    /**
     * The name of the extra, which is used to store the left margin within a bundle.
     */
    private static final String LEFT_MARGIN_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::leftMargin";

    /**
     * The name of the extra, which is used to store the top margin within a bundle.
     */
    private static final String TOP_MARGIN_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::topMargin";

    /**
     * The name of the extra, which is used to store the right margin within a bundle.
     */
    private static final String RIGHT_MARGIN_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::rightMargin";

    /**
     * The name of the extra, which is used to store the bottom margin within a bundle.
     */
    private static final String BOTTOM_MARGIN_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::bottomMargin";

    /**
     * The name of the extra, which is used to store the left padding within a bundle.
     */
    private static final String LEFT_PADDING_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::leftPadding";

    /**
     * The name of the extra, which is used to store the top padding within a bundle.
     */
    private static final String TOP_PADDING_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::topPadding";

    /**
     * The name of the extra, which is used to store the right padding within a bundle.
     */
    private static final String RIGHT_PADDING_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::rightPadding";

    /**
     * The name of the extra, which is used to store the bottom padding within a bundle.
     */
    private static final String BOTTOM_PADDING_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::bottomPadding";

    /**
     * The name of the extra, which is used to store the top scrollable area within a bundle.
     */
    private static final String TOP_SCROLLABLE_AREA_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::topScrollableArea";

    /**
     * The name of the extra, which is used to store the bottom scrollable area within a bundle.
     */
    private static final String BOTTOM_SCROLLABLE_AREA_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::bottomScrollableArea";

    /**
     * The name of the extra, which is used to store, whether the dividers, which are located above
     * and below the dialog's scrollable areas, should be shown when scrolling, or not, within
     * a bundle.
     */
    private static final String SHOW_DIVIDERS_ON_SCROLL_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::showDividersOnScroll";

    /**
     * The name of the extra, which is used to store the color of the dividers, within a bundle.
     */
    private static final String DIVIDER_COLOR_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::dividerColor";

    /**
     * The name of the extra, which is used to store the margin of dividers, within a bundle.
     */
    private static final String DIVIDER_MARGIN_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::dividerMargin";

    /**
     * The name of the extra, which is used to store the color of the title of the dialog within a
     * bundle.
     */
    private static final String TITLE_COLOR_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::titleColor";

    /**
     * The name of the extra, which is used to store the color of the message of the dialog within a
     * bundle.
     */
    private static final String MESSAGE_COLOR_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::messageColor";

    /**
     * The name of the extra, which is used to store the title of the dialog within a bundle.
     */
    private static final String TITLE_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::title";

    /**
     * The name of the extra, which is used to store the message of the dialog within a bundle.
     */
    private static final String MESSAGE_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::message";

    /**
     * The name of the extra, which is used to store the bitmap of the dialog's icon within a
     * bundle.
     */
    private static final String ICON_BITMAP_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::iconBitmap";

    /**
     * The name of the extra, which is used to store the resource id of the dialog's icon within a
     * bundle.
     */
    private static final String ICON_ID_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::iconId";

    /**
     * The name of the extra, which is used to store the attribute id of the dialog's icon within a
     * bundle.
     */
    private static final String ICON_ATTRIBUTE_ID_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::iconAttribute";

    /**
     * The name of the extra, which is used to store the color state list, which is used to tint the
     * icon of the dialog, within a bundle.
     */
    private static final String ICON_TINT_LIST_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::iconTintList";

    /**
     * The name of the extra, which is used to store the mode, which is used to tint the icon of the
     * dialog, within a bundle.
     */
    private static final String ICON_TINT_MODE_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::iconTintMode";

    /**
     * The name of the extra, which is used to store the bitmap of the dialog's background within a
     * bundle.
     */
    private static final String BACKGROUND_BITMAP_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::backgroundBitmap";

    /**
     * The name of the extra, which is used to store the resource id of the dialog's background
     * within a bundle.
     */
    private static final String BACKGROUND_ID_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::backgroundId";

    /**
     * The name of the extra, which is used to store the color of the dialog's background within a
     * bundle.
     */
    private static final String BACKGROUND_COLOR_EXTRA =
            MaterialDialogDecorator.class.getSimpleName() + "::backgroundColor";

    /**
     * The parent view of the view, which is used to show the dialog's title.
     */
    private ViewGroup titleContainer;

    /**
     * The parent view of the view, which is used to show the dialog's message.
     */
    private ViewGroup messageContainer;

    /**
     * The image view, which is used to show the icon of the dialog.
     */
    private ImageView iconImageView;

    /**
     * The text view, which is used to show the title of the dialog.
     */
    private TextView titleTextView;

    /**
     * The text view, which is used to show the dialog's message.
     */
    private TextView messageTextView;

    /**
     * The parent view of the view, which is used to show the dialog's content.
     */
    private ViewGroup contentContainer;

    /**
     * The resource id of the dialog's window background.
     */
    private int windowBackgroundId = -1;

    /**
     * The bitmap of the dialog's window background.
     */
    private Bitmap windowBackgroundBitmap;

    /**
     * The background of the dialog's window.
     */
    private Drawable windowBackground;

    /**
     * The insets of the dialog's content, depending on the padding of the dialog's window
     * background.
     */
    private Rect windowInsets;

    /**
     * True, if the dialog is cancelable, false otherwise.
     */
    private boolean cancelable;

    /**
     * True, if the dialog is canceled, when touching the outside of the window, false otherwise.
     */
    private boolean canceledOnTouchOutside;

    /**
     * True, if the dialog is shown fullscreen, false otherwise.
     */
    private boolean fullscreen;

    /**
     * The gravity of the dialog.
     */
    private int gravity = Gravity.CENTER;

    /**
     * The width of the dialog.
     */
    private int width = Dialog.WRAP_CONTENT;

    /**
     * The height of the dialog.
     */
    private int height = Dialog.WRAP_CONTENT;

    /**
     * The maximum width of the dialog.
     */
    private int maxWidth = -1;

    /**
     * The maximum height of the dialog.
     */
    private int maxHeight = -1;

    /**
     * The margin of the dialog.
     */
    private int[] margin = new int[]{0, 0, 0, 0};

    /**
     * The padding of the dialog.
     */
    private int[] padding = new int[]{0, 0, 0, 0};

    /**
     * The scrollable area of the dialog.
     */
    private ScrollableArea scrollableArea;

    /**
     * True, if the dividers, which are located above and below the dialog's scrollable areas, are
     * shown, when scrolling, false otherwise
     */
    private boolean showDividersOnScroll;

    /**
     * The color of dividers.
     */
    private int dividerColor;

    /**
     * The left and right margin of dividers.
     */
    private int dividerMargin;

    /**
     * The title of the dialog.
     */
    private CharSequence title;

    /**
     * The message of the dialog.
     */
    private CharSequence message;

    /**
     * The icon of the dialog.
     */
    private Drawable icon;

    /**
     * The id of the dialog's icon.
     */
    private int iconId = -1;

    /**
     * The attribute id of the dialog's icon.
     */
    private int iconAttributeId = -1;

    /**
     * The bitmap of the dialog's icon.
     */
    private Bitmap iconBitmap;

    /**
     * The color state list, which is used to tint the icon of the dialog.
     */
    private ColorStateList iconTintList;

    /**
     * The mode, which is used to tint the icon of the dialog.
     */
    private PorterDuff.Mode iconTintMode = PorterDuff.Mode.SRC_ATOP;

    /**
     * The color of the title of the dialog.
     */
    private int titleColor;

    /**
     * The color of the message of the dialog.
     */
    private int messageColor;

    /**
     * The background of the dialog.
     */
    private Drawable background;

    /**
     * The bitmap of the dialog's background.
     */
    private Bitmap backgroundBitmap;

    /**
     * The resource id of the dialog's background.
     */
    private int backgroundId = -1;

    /**
     * The color of the dialog's background.
     */
    private int backgroundColor = -1;

    /**
     * The custom content view of the dialog.
     */
    private View customView;

    /**
     * The resource id of the custom content view of the dialog. builder.
     */
    private int customViewId = -1;

    /**
     * The custom title view of the dialog.
     */
    private View customTitleView;

    /**
     * The resource id of the custom title view of the dialog.
     */
    private int customTitleViewId = -1;

    /**
     * The custom message view of the dialog.
     */
    private View customMessageView;

    /**
     * The resource id of the custom message view of the dialog.
     */
    private int customMessageViewId = -1;

    /**
     * A boolean array, which contains, whether the dialog should inset its content as specific
     * edges, or not.
     */
    private boolean[] fitsSystemWindows = new boolean[]{true, true, true, true};

    /**
     * The window insets, which have been applied to the root view of the view hierarchy, which is
     * modified by the decorator.
     */
    private Rect systemWindowInsets;

    /**
     * Creates and returns a listener, which allows to observe when window insets are applied to the
     * root view of the view hierarchy, which is modified by the decorator.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnApplyWindowInsetsListener}
     */
    private OnApplyWindowInsetsListener createWindowInsetsListener() {
        return new OnApplyWindowInsetsListener() {

            @Override
            public WindowInsetsCompat onApplyWindowInsets(final View v,
                                                          final WindowInsetsCompat insets) {
                systemWindowInsets = insets.hasSystemWindowInsets() ?
                        new Rect(insets.getSystemWindowInsetLeft(),
                                insets.getSystemWindowInsetTop(),
                                insets.getSystemWindowInsetRight(),
                                insets.getSystemWindowInsetBottom()) : null;
                adaptLayoutParams();
                return insets;
            }

        };
    }

    /**
     * Creates and returns the layout params, which should be used by the dialog's root view.
     *
     * @return The layout params, which have been created, as an instance of the class {@link
     * RelativeLayout.LayoutParams}
     */
    private RelativeLayout.LayoutParams createLayoutParams() {
        Rect windowDimensions = new Rect();
        Window window = getWindow();
        assert window != null;
        window.getDecorView().getWindowVisibleDisplayFrame(windowDimensions);
        int leftInset = isFitsSystemWindowsLeft() && isFullscreen() && systemWindowInsets != null ?
                systemWindowInsets.left : 0;
        int topInset = isFitsSystemWindowsTop() && isFullscreen() && systemWindowInsets != null ?
                systemWindowInsets.top : 0;
        int rightInset =
                isFitsSystemWindowsRight() && isFullscreen() && systemWindowInsets != null ?
                        systemWindowInsets.right : 0;
        int bottomInset =
                isFitsSystemWindowsBottom() && isFullscreen() && systemWindowInsets != null ?
                        systemWindowInsets.bottom : 0;
        int leftMargin = getLeftMargin() - getWindowInsetLeft() + leftInset;
        int topMargin = getTopMargin() - getWindowInsetTop() + topInset;
        int rightMargin = getRightMargin() - getWindowInsetRight() + rightInset;
        int bottomMargin = getBottomMargin() - getWindowInsetBottom() + bottomInset;
        int width =
                getLayoutDimension(getWidth(), leftMargin + rightMargin, windowDimensions.right);
        int height =
                getLayoutDimension(getHeight(), topMargin + bottomMargin, windowDimensions.bottom);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.leftMargin = leftMargin;
        layoutParams.topMargin = topMargin;
        layoutParams.rightMargin = rightMargin;
        layoutParams.bottomMargin = bottomMargin;

        if ((getGravity() & Gravity.CENTER_HORIZONTAL) == Gravity.CENTER_HORIZONTAL) {
            layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        }

        if ((getGravity() & Gravity.CENTER_VERTICAL) == Gravity.CENTER_VERTICAL) {
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        }

        if ((getGravity() & Gravity.LEFT) == Gravity.LEFT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        }

        if ((getGravity() & Gravity.TOP) == Gravity.TOP) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        }

        if ((getGravity() & Gravity.RIGHT) == Gravity.RIGHT) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        }

        if ((getGravity() & Gravity.BOTTOM) == Gravity.BOTTOM) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        }

        return layoutParams;
    }

    /**
     * Returns a dimension (width or height) of the dialog, depending of the margin of the
     * corresponding orientation and the display space, which is available in total.
     *
     * @param dimension
     *         The dimension, which should be used, in pixels as an {@link Integer value}
     * @param margin
     *         The margin in pixels as an {@link Integer} value
     * @param total
     *         The display space, which is available in total, in pixels as an {@link Integer}
     *         value
     * @return The dimension of the dialog as an {@link Integer} value
     */
    private int getLayoutDimension(final int dimension, final int margin, final int total) {
        if (dimension == Dialog.MATCH_PARENT) {
            return RelativeLayout.LayoutParams.MATCH_PARENT;
        } else if (dimension == Dialog.WRAP_CONTENT) {
            return RelativeLayout.LayoutParams.WRAP_CONTENT;
        } else {
            return Math.min(dimension, total - margin);
        }
    }

    /**
     * Inflates the view, which is used to show the dialog's title. The view may either be the
     * default one or a custom view, if one has been set before.
     *
     * @return The view, which has been inflated, as an instance of the class {@link View} or null,
     * if no view has been inflated
     */
    private View inflateTitleView() {
        if (getRootView() != null) {
            inflateTitleContainer();

            if (customTitleView != null) {
                titleContainer.addView(customTitleView);
            } else if (customTitleViewId != -1) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(customTitleViewId, titleContainer, false);
                titleContainer.addView(view);
            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater
                        .inflate(R.layout.material_dialog_title, titleContainer, false);
                titleContainer.addView(view);
            }

            View titleView = titleContainer.findViewById(android.R.id.title);
            titleTextView = titleView instanceof TextView ? (TextView) titleView : null;
            View iconView = titleContainer.findViewById(android.R.id.icon);
            iconImageView = iconView instanceof ImageView ? (ImageView) iconView : null;
            return titleContainer;
        }

        return null;
    }

    /**
     * Inflates the container, which contains the dialog's title, if it is not yet inflated.
     */
    private void inflateTitleContainer() {
        if (titleContainer == null) {
            titleContainer = new RelativeLayout(getContext());
            titleContainer.setId(R.id.title_container);
            titleContainer.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            titleContainer.removeAllViews();
        }
    }

    /**
     * Inflates the view, which is used to show the dialog's message. The view may either be the
     * default one or a custom view, if one has been set before.
     *
     * @return The view, which has been inflated, as an instance of the class {@link View} or null,
     * if no view has been inflated
     */
    private View inflateMessageView() {
        if (getRootView() != null) {
            inflateMessageContainer();

            if (customMessageView != null) {
                messageContainer.addView(customMessageView);
            } else if (customMessageViewId != -1) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(customMessageViewId, messageContainer, false);
                messageContainer.addView(view);
            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater
                        .inflate(R.layout.material_dialog_message, messageContainer, false);
                messageContainer.addView(view);
            }

            View messageView = messageContainer.findViewById(android.R.id.message);
            messageTextView = messageView instanceof TextView ? (TextView) messageView : null;
            return messageContainer;
        }

        return null;
    }

    /**
     * Inflates the container, which contains the dialog's message, if it is not yet inflated.
     */
    private void inflateMessageContainer() {
        if (messageContainer == null) {
            messageContainer = new RelativeLayout(getContext());
            messageContainer.setId(R.id.message_container);
            messageContainer.setLayoutParams(
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            messageContainer.removeAllViews();
        }
    }

    /**
     * Inflates the view, which is used to show the dialog's content. The view may either be the
     * default one or a custom view, if one has been set before.
     *
     * @return The view, which has been inflated, as an instance of the class {@link View} or null,
     * if no view has been inflated
     */
    private View inflateContentView() {
        if (getRootView() != null) {
            inflateContentContainer();

            if (customView != null) {
                contentContainer.addView(customView);
            } else if (customViewId != -1) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(customViewId, contentContainer, false);
                contentContainer.addView(view);
            }

            adaptContentContainerVisibility();
            return contentContainer;
        }

        return null;
    }

    /**
     * Inflates the container, which contains the dialog's content, if it is not yet inflated.
     */
    private void inflateContentContainer() {
        if (contentContainer == null) {
            contentContainer = new RelativeLayout(getContext());
            contentContainer.setId(R.id.content_container);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);
            layoutParams.weight = 1;
            contentContainer.setLayoutParams(layoutParams);
        } else {
            contentContainer.removeAllViews();
        }
    }

    /**
     * Adapts the background and inset of the dialog's window.
     */
    private void adaptWindowBackgroundAndInset() {
        DialogRootView rootView = getRootView();

        if (rootView != null) {
            rootView.setWindowBackgroundAndInset(windowBackground, windowInsets);
        }
    }

    /**
     * Adapts the layout params of the dialog.
     */
    private void adaptLayoutParams() {
        DialogRootView rootView = getRootView();

        if (getWindow() != null && rootView != null) {
            rootView.setLayoutParams(createLayoutParams());
            rootView.setFullscreen(isFullscreen());
            rootView.setMaxWidth(getMaxWidth());
            rootView.setMaxHeight(getMaxHeight());
        }
    }

    /**
     * Adapts the padding of the dialog.
     */
    private void adaptPadding() {
        ViewGroup dialogRootView = getRootView();

        if (dialogRootView != null) {
            dialogRootView.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                    getPaddingBottom());
        }
    }

    /**
     * Adapts the scrollable area of the dialog.
     */
    private void adaptScrollableArea() {
        DialogRootView dialogRootView = getRootView();

        if (dialogRootView != null) {
            dialogRootView.setScrollableArea(scrollableArea);
        }
    }

    /**
     * Adapts the visibility of dividers.
     */
    private void adaptDividerVisibility() {
        DialogRootView dialogRootView = getRootView();

        if (dialogRootView != null) {
            dialogRootView.showDividersOnScroll(showDividersOnScroll);
        }
    }

    /**
     * Adapts the color of dividers.
     */
    private void adaptDividerColor() {
        DialogRootView dialogRootView = getRootView();

        if (dialogRootView != null) {
            dialogRootView.setDividerColor(dividerColor);
        }
    }

    /**
     * Adapts the left and right margin of dividers.
     */
    private void adaptDividerMargin() {
        DialogRootView dialogRootView = getRootView();

        if (dialogRootView != null) {
            dialogRootView.setDividerMargin(dividerMargin);
        }
    }

    /**
     * Adapts the view, which is used to show the dialog's content.
     */
    private void adaptContentView() {
        if (contentContainer != null) {
            inflateContentView();
        }
    }

    /**
     * Adapts the view, which is used to show the dialog's title.
     */
    private void adaptTitleView() {
        if (titleContainer != null) {
            inflateTitleView();
            adaptTitle();
            adaptTitleColor();
            adaptIcon();
        }
    }

    /**
     * Adapts the view, which is used to show the dialog's message.
     */
    private void adaptMessageView() {
        if (messageContainer != null) {
            inflateMessageView();
            adaptMessage();
            adaptMessageColor();
        }
    }

    /**
     * Adapts the color of the dialog's title.
     */
    private void adaptTitleColor() {
        if (titleTextView != null) {
            titleTextView.setTextColor(titleColor);
        }
    }

    /**
     * Adapts the dialog's title.
     */
    private void adaptTitle() {
        if (titleTextView != null) {
            titleTextView.setText(title);
        }

        adaptTitleContainerVisibility();
    }

    /**
     * Adapts the dialog's icon.
     */
    private void adaptIcon() {
        if (iconImageView != null) {
            ImageViewCompat.setImageTintList(iconImageView, iconTintList);
            ImageViewCompat.setImageTintMode(iconImageView, iconTintMode);
            iconImageView.setImageDrawable(icon);
            iconImageView.setVisibility(icon != null ? View.VISIBLE : View.GONE);
        }

        adaptTitleContainerVisibility();
    }

    /**
     * Adapts the visibility of the parent view of the text view, which is used to show the title of
     * the dialog.
     */
    private void adaptTitleContainerVisibility() {
        if (titleContainer != null) {
            boolean visible = isCustomTitleUsed() || !TextUtils.isEmpty(title) || icon != null;

            if (visible) {
                titleContainer.setVisibility(View.VISIBLE);
                notifyOnAreaShown(Area.TITLE);
            } else {
                titleContainer.setVisibility(View.GONE);
                notifyOnAreaHidden(Area.TITLE);
            }
        }
    }

    /**
     * Adapts the dialog's message.
     */

    private void adaptMessage() {
        if (messageTextView != null) {
            messageTextView.setText(message);
            messageTextView.setVisibility(!TextUtils.isEmpty(message) ? View.VISIBLE : View.GONE);
        }

        adaptMessageContainerVisibility();
    }

    /**
     * Adapts the color of the dialog's message.
     */
    private void adaptMessageColor() {
        if (messageTextView != null) {
            messageTextView.setTextColor(messageColor);
        }
    }

    /**
     * Adapts the visibility of the parent view of the text view, which is used to show the message
     * of the dialog.
     */
    private void adaptMessageContainerVisibility() {
        if (titleContainer != null) {
            boolean visible = isCustomMessageUsed() || !TextUtils.isEmpty(message);

            if (visible) {
                messageContainer.setVisibility(View.VISIBLE);
                notifyOnAreaShown(Area.MESSAGE);
            } else {
                messageContainer.setVisibility(View.GONE);
                notifyOnAreaHidden(Area.MESSAGE);
            }
        }
    }

    /**
     * Adapts the dialog's background.
     *
     * @param animation
     *         The animation, which should be used to change the background, as an instance of the
     *         class {@link BackgroundAnimation} or null, if no animation should be used
     */
    private void adaptBackground(@Nullable final BackgroundAnimation animation) {
        if (getRootView() != null && getWindow() != null) {
            Drawable newBackground = background;

            if (animation != null && newBackground != null) {
                View animatedView = isFullscreen() ? getWindow().getDecorView() : getRootView();
                Drawable previousBackground = animatedView.getBackground();

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

            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            ViewUtil.setBackground(getRootView(), isFullscreen() ? null : newBackground);
            ViewUtil.setBackground(getWindow().getDecorView(),
                    isFullscreen() ? newBackground : null);
        }
    }

    /**
     * Adapts the visibility of the parent view of the view, which is used to show the dialog's
     * content.
     */
    private void adaptContentContainerVisibility() {
        if (contentContainer != null) {
            if (isCustomViewUsed()) {
                contentContainer.setVisibility(View.VISIBLE);
                notifyOnAreaShown(Area.CONTENT);
            } else {
                contentContainer.setVisibility(View.GONE);
                notifyOnAreaHidden(Area.CONTENT);
            }
        }
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the type {@link Dialog}. The dialog may not be null
     */
    public MaterialDialogDecorator(@NonNull final Dialog dialog) {
        super(dialog);
    }

    @Override
    public final Drawable getWindowBackground() {
        return windowBackground;
    }

    @Override
    public final void setWindowBackground(@DrawableRes final int resourceId) {
        this.windowBackgroundId = resourceId;
        this.windowBackgroundBitmap = null;
        this.windowBackground = ContextCompat.getDrawable(getContext(), resourceId);
        this.windowInsets = new Rect();
        this.windowBackground.getPadding(this.windowInsets);
        adaptWindowBackgroundAndInset();
    }

    @Override
    public final void setWindowBackground(@Nullable final Bitmap windowBackground) {
        this.windowBackgroundId = -1;
        this.windowBackgroundBitmap = windowBackground;
        this.windowBackground =
                windowBackground != null ? new BitmapDrawable(windowBackground) : null;
        this.windowInsets = new Rect();

        if (this.windowBackground != null) {
            this.windowBackground.getPadding(this.windowInsets);
        }

        adaptWindowBackgroundAndInset();
    }

    @Override
    public final int getWindowInsetLeft() {
        return isFullscreen() || windowInsets == null ? 0 : windowInsets.left;
    }

    @Override
    public final int getWindowInsetTop() {
        return isFullscreen() || windowInsets == null ? 0 : windowInsets.top;
    }

    @Override
    public final int getWindowInsetRight() {
        return isFullscreen() || windowInsets == null ? 0 : windowInsets.right;
    }

    @Override
    public final int getWindowInsetBottom() {
        return isFullscreen() || windowInsets == null ? 0 : windowInsets.bottom;
    }

    @Override
    public final boolean isCanceledOnTouchOutside() {
        return canceledOnTouchOutside;
    }

    @Override
    public final void setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        this.canceledOnTouchOutside = canceledOnTouchOutside;

        if (canceledOnTouchOutside) {
            setCancelable(true);
        }
    }

    @Override
    public final boolean isCancelable() {
        return cancelable;
    }

    @Override
    public final void setCancelable(final boolean cancelable) {
        this.cancelable = cancelable;

        if (!cancelable) {
            setCanceledOnTouchOutside(false);
        }
    }

    @Override
    public final boolean isFullscreen() {
        return fullscreen;
    }

    @Override
    public final void setFullscreen(final boolean fullscreen) {
        this.fullscreen = fullscreen;
        adaptLayoutParams();
        adaptBackground(null);
    }

    @Override
    public final int getGravity() {
        return gravity;
    }

    @Override
    public final void setGravity(final int gravity) {
        this.gravity = gravity;
        adaptLayoutParams();
    }

    @Override
    public final int getWidth() {
        return width;
    }

    @Override
    public final void setWidth(final int width) {
        if (width != Dialog.MATCH_PARENT && width != Dialog.WRAP_CONTENT) {
            ensureAtLeast(width, 1, "The width must be at least 1");
        }

        this.width = width;
        adaptLayoutParams();
    }

    @Override
    public final int getHeight() {
        return height;
    }

    @Override
    public final void setHeight(final int height) {
        if (height != Dialog.MATCH_PARENT && height != Dialog.WRAP_CONTENT) {
            ensureAtLeast(height, 1, "The height must be at least 1");
        }

        this.height = height;
        adaptLayoutParams();
    }

    @Override
    public final int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public final void setMaxWidth(final int maxWidth) {
        if (maxWidth != -1) {
            ensureAtLeast(maxWidth, 1, "The maximum width must be at least 1");
        }

        this.maxWidth = maxWidth;
        adaptLayoutParams();
    }

    @Override
    public final int getMaxHeight() {
        return maxHeight;
    }

    @Override
    public final void setMaxHeight(final int maxHeight) {
        if (maxHeight != -1) {
            ensureAtLeast(maxHeight, 1, "The maximum height must be at least 1");
        }

        this.maxHeight = maxHeight;
        adaptLayoutParams();
    }

    @Override
    public final int getLeftMargin() {
        return margin[0];
    }

    @Override
    public final int getTopMargin() {
        return margin[1];
    }

    @Override
    public final int getRightMargin() {
        return margin[2];
    }

    @Override
    public final int getBottomMargin() {
        return margin[3];
    }

    @Override
    public final void setMargin(final int left, final int top, final int right, final int bottom) {
        ensureAtLeast(left, 0, "The left margin must be at least 0");
        ensureAtLeast(top, 0, "The top margin must be at least 0");
        ensureAtLeast(right, 0, "The right margin must be at least 0");
        ensureAtLeast(bottom, 0, "The bottom margin must be at least 0");
        this.margin = new int[]{left, top, right, bottom};
        adaptLayoutParams();
    }

    @Override
    public final int getPaddingLeft() {
        return padding[0];
    }

    @Override
    public final int getPaddingTop() {
        return padding[1];
    }

    @Override
    public final int getPaddingRight() {
        return padding[2];
    }

    @Override
    public final int getPaddingBottom() {
        return padding[3];
    }

    @Override
    public final void setPadding(final int left, final int top, final int right, final int bottom) {
        ensureAtLeast(left, 0, "The left padding must be at least 0");
        ensureAtLeast(top, 0, "The top padding must be at least 0");
        ensureAtLeast(right, 0, "The right padding must be at least 0");
        ensureAtLeast(bottom, 0, "The bottom padding must be at least 0");
        this.padding = new int[]{left, top, right, bottom};
        adaptPadding();
    }

    @Override
    public final boolean isFitsSystemWindowsLeft() {
        return fitsSystemWindows[0];
    }

    @Override
    public final boolean isFitsSystemWindowsTop() {
        return fitsSystemWindows[1];
    }

    @Override
    public final boolean isFitsSystemWindowsRight() {
        return fitsSystemWindows[2];
    }

    @Override
    public final boolean isFitsSystemWindowsBottom() {
        return fitsSystemWindows[3];
    }

    @Override
    public final void setFitsSystemWindows(final boolean fitsSystemWindows) {
        setFitsSystemWindows(fitsSystemWindows, fitsSystemWindows, fitsSystemWindows,
                fitsSystemWindows);
    }

    @Override
    public final void setFitsSystemWindows(final boolean left, final boolean top,
                                           final boolean right, final boolean bottom) {
        this.fitsSystemWindows = new boolean[]{left, top, right, bottom};
        adaptLayoutParams();
    }

    @NonNull
    @Override
    public final ScrollableArea getScrollableArea() {
        return scrollableArea;
    }

    @Override
    public final void setScrollableArea(@Nullable final Area area) {
        this.scrollableArea = ScrollableArea.create(area);
        adaptScrollableArea();
    }

    @Override
    public final void setScrollableArea(@Nullable final Area top, @Nullable final Area bottom) {
        this.scrollableArea = ScrollableArea.create(top, bottom);
        adaptScrollableArea();
    }

    @Override
    public final boolean areDividersShownOnScroll() {
        return showDividersOnScroll;
    }

    @Override
    public final void showDividersOnScroll(final boolean show) {
        this.showDividersOnScroll = show;
        adaptDividerVisibility();
    }

    @Override
    public final int getDividerColor() {
        return dividerColor;
    }

    @Override
    public final void setDividerColor(@ColorInt final int color) {
        this.dividerColor = color;
        adaptDividerColor();
    }

    @Override
    public final int getDividerMargin() {
        return dividerMargin;
    }

    @Override
    public final void setDividerMargin(final int margin) {
        ensureAtLeast(margin, 0, "The margin must be at least 0");
        this.dividerMargin = margin;
        adaptDividerMargin();
    }

    @Override
    public final Drawable getIcon() {
        return icon;
    }

    @Override
    public final void setIcon(@Nullable final Bitmap icon) {
        this.iconBitmap = icon;
        this.iconId = -1;
        this.iconAttributeId = -1;
        this.icon = icon != null ? new BitmapDrawable(getContext().getResources(), icon) : null;
        adaptIcon();
    }

    @Override
    public final void setIcon(@DrawableRes final int resourceId) {
        this.iconBitmap = null;
        this.iconId = resourceId;
        this.iconAttributeId = -1;
        this.icon = ContextCompat.getDrawable(getContext(), resourceId);
        adaptIcon();
    }

    @Override
    public final void setIconAttribute(@AttrRes final int attributeId) {
        this.iconBitmap = null;
        this.iconId = -1;
        this.iconAttributeId = attributeId;
        TypedArray typedArray =
                getContext().getTheme().obtainStyledAttributes(new int[]{attributeId});
        this.icon = typedArray.getDrawable(0);
        adaptIcon();
    }

    @Override
    public final ColorStateList getIconTintList() {
        return iconTintList;
    }

    @Override
    public final void setIconTint(final int color) {
        setIconTintList(ColorStateList.valueOf(color));
    }

    @Override
    public final void setIconTintList(@Nullable final ColorStateList tintList) {
        this.iconTintList = tintList;
        adaptIcon();
    }

    @NonNull
    @Override
    public final PorterDuff.Mode getIconTintMode() {
        return iconTintMode;
    }

    @Override
    public final void setIconTintMode(@NonNull final PorterDuff.Mode mode) {
        ensureNotNull(mode, "The icon tint mode may not be null");
        this.iconTintMode = mode;
        adaptIcon();
    }

    @Override
    public final int getTitleColor() {
        return titleColor;
    }

    @Override
    public final void setTitleColor(@ColorInt final int color) {
        titleColor = color;
        adaptTitleColor();
    }

    @Override
    public final int getMessageColor() {
        return messageColor;
    }

    @Override
    public final void setMessageColor(@ColorInt final int color) {
        messageColor = color;
        adaptMessageColor();
    }

    @Override
    public final Drawable getBackground() {
        return background;
    }

    @Override
    public final void setBackground(@Nullable final Bitmap background) {
        setBackground(background, null);
    }

    @Override
    public final void setBackground(@Nullable final Bitmap background,
                                    @Nullable final BackgroundAnimation animation) {
        this.backgroundBitmap = background;
        this.backgroundId = -1;
        this.backgroundColor = -1;
        this.background =
                background != null ? new BitmapDrawable(getContext().getResources(), background) :
                        null;
        adaptBackground(animation);
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId) {
        setBackground(resourceId, null);
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId,
                                    @Nullable final BackgroundAnimation animation) {
        this.backgroundBitmap = null;
        this.backgroundId = resourceId;
        this.backgroundColor = -1;
        this.background = ContextCompat.getDrawable(getContext(), resourceId);
        adaptBackground(animation);
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color) {
        setBackgroundColor(color, null);
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color,
                                         @Nullable final BackgroundAnimation animation) {
        this.backgroundBitmap = null;
        this.backgroundId = -1;
        this.backgroundColor = color;
        this.background = new ColorDrawable(color);
        adaptBackground(animation);
    }

    @Override
    public final boolean isCustomTitleUsed() {
        return customTitleView != null || customTitleViewId != -1;
    }

    @Override
    public final void setCustomTitle(@Nullable final View view) {
        customTitleView = view;
        customTitleViewId = -1;
        adaptTitleView();
    }

    @Override
    public final void setCustomTitle(@LayoutRes final int resourceId) {
        customTitleView = null;
        customTitleViewId = resourceId;
        adaptTitleView();
    }

    @Override
    public final boolean isCustomMessageUsed() {
        return customMessageView != null || customMessageViewId != -1;
    }

    @Override
    public final void setCustomMessage(@Nullable final View view) {
        customMessageView = view;
        customMessageViewId = -1;
        adaptMessageView();
    }

    @Override
    public final void setCustomMessage(@LayoutRes final int resourceId) {
        customMessageView = null;
        customMessageViewId = resourceId;
        adaptMessageView();
    }

    @Override
    public final boolean isCustomViewUsed() {
        return customView != null || customViewId != -1;
    }

    @Override
    public final void setView(@Nullable final View view) {
        customView = view;
        customViewId = -1;
        adaptContentView();
    }

    @Override
    public final void setView(@LayoutRes final int resourceId) {
        customView = null;
        customViewId = resourceId;
        adaptContentView();
    }

    @Override
    public final CharSequence getMessage() {
        return message;
    }

    @Override
    public final void setMessage(@Nullable final CharSequence message) {
        this.message = message;
        adaptMessage();
    }

    @Override
    public final void setMessage(@StringRes final int resourceId) {
        setMessage(getContext().getText(resourceId));
    }

    @Override
    public final CharSequence getTitle() {
        return title;
    }

    @Override
    public final void setTitle(@Nullable final CharSequence title) {
        this.title = title;
        adaptTitle();
    }

    @Override
    public final void setTitle(@StringRes final int resourceId) {
        setTitle(getContext().getText(resourceId));
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putBoolean(CANCELABLE_EXTRA, isCancelable());
        outState.putBoolean(CANCEL_ON_TOUCH_OUTSIDE_EXTRA, isCanceledOnTouchOutside());
        outState.putBoolean(FULLSCREEN_EXTRA, isFullscreen());
        outState.putInt(GRAVITY_EXTRA, getGravity());
        outState.putInt(WIDTH_EXTRA, getWidth());
        outState.putInt(HEIGHT_EXTRA, getHeight());
        outState.putInt(MAX_WIDTH_EXTRA, getMaxWidth());
        outState.putInt(MAX_HEIGHT_EXTRA, getMaxHeight());
        outState.putInt(LEFT_MARGIN_EXTRA, getLeftMargin());
        outState.putInt(TOP_MARGIN_EXTRA, getTopMargin());
        outState.putInt(RIGHT_MARGIN_EXTRA, getRightMargin());
        outState.putInt(BOTTOM_MARGIN_EXTRA, getBottomMargin());
        outState.putInt(LEFT_PADDING_EXTRA, getPaddingLeft());
        outState.putInt(TOP_PADDING_EXTRA, getPaddingTop());
        outState.putInt(RIGHT_PADDING_EXTRA, getPaddingRight());
        outState.putInt(BOTTOM_PADDING_EXTRA, getPaddingBottom());
        outState.putSerializable(TOP_SCROLLABLE_AREA_EXTRA, scrollableArea.getTopScrollableArea());
        outState.putSerializable(BOTTOM_SCROLLABLE_AREA_EXTRA,
                scrollableArea.getBottomScrollableArea());
        outState.putBoolean(SHOW_DIVIDERS_ON_SCROLL_EXTRA, areDividersShownOnScroll());
        outState.putInt(DIVIDER_COLOR_EXTRA, getDividerColor());
        outState.putInt(DIVIDER_MARGIN_EXTRA, getDividerMargin());
        outState.putInt(TITLE_COLOR_EXTRA, getTitleColor());
        outState.putInt(MESSAGE_COLOR_EXTRA, getMessageColor());
        outState.putCharSequence(TITLE_EXTRA, getTitle());
        outState.putCharSequence(MESSAGE_EXTRA, getMessage());
        outState.putParcelable(ICON_TINT_LIST_EXTRA, getIconTintList());
        outState.putSerializable(ICON_TINT_MODE_EXTRA, getIconTintMode());

        if (windowBackgroundBitmap != null) {
            outState.putParcelable(WINDOW_BACKGROUND_BITMAP_EXTRA, windowBackgroundBitmap);
        } else if (windowBackgroundId != -1) {
            outState.putInt(WINDOW_BACKGROUND_ID_EXTRA, windowBackgroundId);
        }

        if (iconBitmap != null) {
            outState.putParcelable(ICON_BITMAP_EXTRA, iconBitmap);
        } else if (iconId != -1) {
            outState.putInt(ICON_ID_EXTRA, iconId);
        } else if (iconAttributeId != -1) {
            outState.putInt(ICON_ATTRIBUTE_ID_EXTRA, iconAttributeId);
        }

        if (backgroundBitmap != null) {
            outState.putParcelable(BACKGROUND_BITMAP_EXTRA, backgroundBitmap);
        } else if (backgroundId != -1) {
            outState.putInt(BACKGROUND_ID_EXTRA, backgroundId);
        } else if (backgroundColor != -1) {
            outState.putInt(BACKGROUND_COLOR_EXTRA, backgroundColor);
        }
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        setCancelable(savedInstanceState.getBoolean(CANCELABLE_EXTRA));
        setCanceledOnTouchOutside(savedInstanceState.getBoolean(CANCEL_ON_TOUCH_OUTSIDE_EXTRA));
        setFullscreen(savedInstanceState.getBoolean(FULLSCREEN_EXTRA));
        setGravity(savedInstanceState.getInt(GRAVITY_EXTRA));
        setWidth(savedInstanceState.getInt(WIDTH_EXTRA));
        setHeight(savedInstanceState.getInt(HEIGHT_EXTRA));
        setMaxWidth(savedInstanceState.getInt(MAX_WIDTH_EXTRA));
        setMaxHeight(savedInstanceState.getInt(MAX_HEIGHT_EXTRA));
        setMargin(savedInstanceState.getInt(LEFT_MARGIN_EXTRA),
                savedInstanceState.getInt(TOP_MARGIN_EXTRA),
                savedInstanceState.getInt(RIGHT_MARGIN_EXTRA),
                savedInstanceState.getInt(BOTTOM_MARGIN_EXTRA));
        setPadding(savedInstanceState.getInt(LEFT_PADDING_EXTRA),
                savedInstanceState.getInt(TOP_PADDING_EXTRA),
                savedInstanceState.getInt(RIGHT_PADDING_EXTRA),
                savedInstanceState.getInt(BOTTOM_PADDING_EXTRA));
        setScrollableArea((Area) savedInstanceState.getSerializable(TOP_SCROLLABLE_AREA_EXTRA),
                (Area) savedInstanceState.getSerializable(BOTTOM_SCROLLABLE_AREA_EXTRA));
        showDividersOnScroll(savedInstanceState.getBoolean(SHOW_DIVIDERS_ON_SCROLL_EXTRA));
        setDividerColor(savedInstanceState.getInt(DIVIDER_COLOR_EXTRA));
        setDividerMargin(savedInstanceState.getInt(DIVIDER_MARGIN_EXTRA));
        setTitleColor(savedInstanceState.getInt(TITLE_COLOR_EXTRA));
        setMessageColor(savedInstanceState.getInt(MESSAGE_COLOR_EXTRA));
        setTitle(savedInstanceState.getCharSequence(TITLE_EXTRA));
        setMessage(savedInstanceState.getCharSequence(MESSAGE_EXTRA));
        setIconTintList((ColorStateList) savedInstanceState.getParcelable(ICON_TINT_LIST_EXTRA));
        PorterDuff.Mode iconTintMode =
                (PorterDuff.Mode) savedInstanceState.getSerializable(ICON_TINT_MODE_EXTRA);

        if (iconTintMode != null) {
            setIconTintMode(iconTintMode);
        }

        if (savedInstanceState.containsKey(WINDOW_BACKGROUND_BITMAP_EXTRA)) {
            setWindowBackground(
                    (Bitmap) savedInstanceState.getParcelable(WINDOW_BACKGROUND_BITMAP_EXTRA));
        } else if (savedInstanceState.containsKey(WINDOW_BACKGROUND_ID_EXTRA)) {
            setWindowBackground(savedInstanceState.getInt(WINDOW_BACKGROUND_ID_EXTRA));
        }

        if (savedInstanceState.containsKey(ICON_BITMAP_EXTRA)) {
            setIcon((Bitmap) savedInstanceState.getParcelable(ICON_BITMAP_EXTRA));
        } else if (savedInstanceState.containsKey(ICON_ID_EXTRA)) {
            setIcon(savedInstanceState.getInt(ICON_ID_EXTRA));
        } else if (savedInstanceState.containsKey(ICON_ATTRIBUTE_ID_EXTRA)) {
            setIconAttribute(savedInstanceState.getInt(ICON_ATTRIBUTE_ID_EXTRA));
        }

        if (savedInstanceState.containsKey(BACKGROUND_BITMAP_EXTRA)) {
            setBackground(savedInstanceState.getInt(BACKGROUND_BITMAP_EXTRA));
        } else if (savedInstanceState.containsKey(BACKGROUND_ID_EXTRA)) {
            setBackground(savedInstanceState.getInt(BACKGROUND_ID_EXTRA));
        } else if (savedInstanceState.containsKey(BACKGROUND_COLOR_EXTRA)) {
            setBackgroundColor(savedInstanceState.getInt(BACKGROUND_COLOR_EXTRA));
        }
    }

    @NonNull
    @Override
    protected final Map<ViewType, View> onAttach(@NonNull final Window window,
                                                 @NonNull final View view,
                                                 @NonNull final Map<ViewType, View> areas,
                                                 final Void param) {
        ViewCompat.setOnApplyWindowInsetsListener(view, createWindowInsetsListener());
        View titleView = inflateTitleView();
        View messageView = inflateMessageView();
        View contentView = inflateContentView();

        if (titleView != null && messageView != null && contentView != null) {
            adaptWindowBackgroundAndInset();
            adaptLayoutParams();
            adaptPadding();
            adaptScrollableArea();
            adaptDividerVisibility();
            adaptTitle();
            adaptTitleColor();
            adaptIcon();
            adaptMessage();
            adaptMessageColor();
            adaptBackground(null);
            Map<ViewType, View> result = new HashMap<>();
            result.put(new AreaViewType(Area.TITLE), titleContainer);
            result.put(new AreaViewType(Area.MESSAGE), messageContainer);
            result.put(new AreaViewType(Area.CONTENT), contentContainer);
            return result;
        }

        return Collections.emptyMap();
    }

    @Override
    protected final void onDetach() {
        titleContainer = null;
        messageContainer = null;
        titleTextView = null;
        messageTextView = null;
        contentContainer = null;
    }

}