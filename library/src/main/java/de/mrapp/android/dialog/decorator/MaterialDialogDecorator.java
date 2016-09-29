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

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.Dialog;
import de.mrapp.android.util.ViewUtil;

import static de.mrapp.android.util.Condition.ensureAtLeast;

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
     * True, if the dialog is cancelable, false otherwise.
     */
    private boolean cancelable;

    /**
     * True, if the dialog is canceled, when touching the outside of the window, false otherwise.
     */
    private boolean canceledOnTouchOutside;

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
     * The margin of the dialog.
     */
    private int[] margin = new int[]{0, 0, 0, 0};

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
        boolean rtl = isRtl();
        int width = getLayoutDimension(getWidth(), getLeftMargin() + getRightMargin(),
                windowDimensions.right);
        int height = getLayoutDimension(getHeight(), getTopMargin() + getBottomMargin(),
                windowDimensions.bottom);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.leftMargin = rtl ? getRightMargin() : getLeftMargin();
        layoutParams.topMargin = getTopMargin();
        layoutParams.rightMargin = rtl ? getLeftMargin() : getRightMargin();
        layoutParams.bottomMargin = getBottomMargin();

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
     * Returns, whether a right-to-left layout should be used, or not.
     *
     * @return True, if a right-to-left layout should be used, false otherwise;
     */
    private boolean isRtl() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Configuration configuration = getContext().getResources().getConfiguration();
            return configuration.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
        }

        return false;
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
     */
    private void inflateTitleView() {
        if (getView() != null) {
            titleContainer = (ViewGroup) getView().findViewById(R.id.title_container);
            titleContainer.removeAllViews();

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
        }
    }

    /**
     * Inflates the view, which is used to show the dialog's message. The view may either be the
     * default one or a custom view, if one has been set before.
     */
    private void inflateMessageView() {
        if (getView() != null) {
            messageContainer = (ViewGroup) getView().findViewById(R.id.message_container);
            messageContainer.removeAllViews();

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
        }
    }

    /**
     * Inflates the view, which is used to show the dialog's content. The view may either be the
     * default one or a custom view, if one has been set before.
     */
    private void inflateContentView() {
        if (getView() != null) {
            contentContainer = (ViewGroup) getView().findViewById(R.id.content_container);
            contentContainer.removeAllViews();

            if (customView != null) {
                contentContainer.addView(customView);
            } else if (customViewId != -1) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(customViewId, contentContainer, false);
                contentContainer.addView(view);
            }

            adaptContentContainerVisibility();
        }
    }

    /**
     * Adapts the layout params of the dialog.
     */
    private void adaptLayoutParams() {
        if (getWindow() != null && getView() != null) {
            getView().setLayoutParams(createLayoutParams());
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
        if (titleTextView != null) {
            titleTextView.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        }

        adaptTitleContainerVisibility();
    }

    /**
     * Adapts the visibility of the parent view of the text view, which is used to show the title of
     * the dialog.
     */
    private void adaptTitleContainerVisibility() {
        if (titleContainer != null) {
            if (customTitleView == null && customTitleViewId == -1) {
                titleContainer.setVisibility(
                        !TextUtils.isEmpty(title) || icon != null ? View.VISIBLE : View.GONE);
            } else {
                titleContainer.setVisibility(View.VISIBLE);
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
            if (customMessageView == null && customMessageViewId == -1) {
                messageContainer
                        .setVisibility(!TextUtils.isEmpty(message) ? View.VISIBLE : View.GONE);
            } else {
                messageContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Adapts the dialog's background.
     */
    private void adaptBackground() {
        if (getView() != null) {
            ViewUtil.setBackground(getView(), background);
        }
    }

    /**
     * Adapts the visibility of the parent view of the view, which is used to show the dialog's
     * content.
     */
    private void adaptContentContainerVisibility() {
        if (customView != null || customViewId != -1) {
            contentContainer.setVisibility(View.VISIBLE);
        } else {
            contentContainer.setVisibility(View.GONE);
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
        this.backgroundBitmap = background;
        this.backgroundId = -1;
        this.backgroundColor = -1;
        this.background =
                background != null ? new BitmapDrawable(getContext().getResources(), background) :
                        null;
        adaptBackground();
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId) {
        this.backgroundBitmap = null;
        this.backgroundId = resourceId;
        this.backgroundColor = -1;
        this.background = ContextCompat.getDrawable(getContext(), resourceId);
        adaptBackground();
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color) {
        this.backgroundBitmap = null;
        this.backgroundId = -1;
        this.backgroundColor = color;
        this.background = new ColorDrawable(color);
        adaptBackground();
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
        outState.putInt(TITLE_COLOR_EXTRA, getTitleColor());
        outState.putInt(MESSAGE_COLOR_EXTRA, getMessageColor());
        outState.putCharSequence(TITLE_EXTRA, getTitle());
        outState.putCharSequence(MESSAGE_EXTRA, getMessage());

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
        setTitleColor(savedInstanceState.getInt(TITLE_COLOR_EXTRA));
        setMessageColor(savedInstanceState.getInt(MESSAGE_COLOR_EXTRA));
        setTitle(savedInstanceState.getCharSequence(TITLE_EXTRA));
        setMessage(savedInstanceState.getCharSequence(MESSAGE_EXTRA));

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

    @Override
    protected final void onAttach(@NonNull final Window window, @NonNull final View view) {
        inflateTitleView();
        inflateMessageView();
        inflateContentView();
        adaptLayoutParams();
        adaptTitle();
        adaptTitleColor();
        adaptIcon();
        adaptMessage();
        adaptMessageColor();
        adaptBackground();
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