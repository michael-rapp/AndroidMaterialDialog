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

import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.Dialog;
import de.mrapp.android.util.ViewUtil;

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
     * The root view of all views, which are used to show the dialog's title, message and content.
     */
    private ViewGroup contentRootView;

    /**
     * The parent view of the view, which is used to show the dialog's content.
     */
    private ViewGroup contentContainer;

    /**
     * The divider, which is located above the dialog's custom view.
     */
    private View contentDivider;

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
     * True, if the divider, which is located above the dialog's custom view, is shown, false
     * otherwise.
     */
    private boolean showContentDivider;

    /**
     * The color of the divider, which is located above the dialog's custom view.
     */
    private int contentDividerColor;

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
            contentRootView = (ViewGroup) getView().findViewById(R.id.content_root);
            contentDivider = getView().findViewById(R.id.content_divider);
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
            adaptContentDividerVisibility();
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
     * Adapts the color of the divider, which is shown above the dialog's custom view.
     */
    private void adaptContentDividerColor() {
        if (contentDivider != null) {
            contentDivider.setBackgroundColor(contentDividerColor);
        }
    }

    /**
     * Adapts the visibility of the divider, which is shown above the dialog's custom view.
     */
    private void adaptContentDividerVisibility() {
        if (contentDivider != null) {
            if (showContentDivider && (customView != null || customViewId != -1)) {
                contentDivider.setVisibility(View.VISIBLE);
            } else {
                contentDivider.setVisibility(View.GONE);
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
    public final Drawable getIcon() {
        return icon;
    }

    @Override
    public final void setIcon(@Nullable final Drawable icon) {
        this.icon = icon;
        adaptIcon();
    }

    @Override
    public final void setIcon(@DrawableRes final int resourceId) {
        setIcon(ContextCompat.getDrawable(getContext(), resourceId));
    }

    @Override
    public final void setIconAttribute(@AttrRes final int attributeId) {
        TypedArray typedArray =
                getContext().getTheme().obtainStyledAttributes(new int[]{attributeId});
        setIcon(typedArray.getDrawable(0));
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
    public final void setBackground(@Nullable final Drawable background) {
        this.background = background;
        adaptBackground();
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId) {
        setBackground(ContextCompat.getDrawable(getContext(), resourceId));
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color) {
        setBackground(new ColorDrawable(color));
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
    public final boolean isContentDividerShown() {
        return showContentDivider;
    }

    @Override
    public final void showContentDivider(final boolean show) {
        this.showContentDivider = show;
        adaptContentDividerVisibility();
    }

    @Override
    public final int getContentDividerColor() {
        return contentDividerColor;
    }

    @Override
    public final void setContentDividerColor(@ColorInt final int color) {
        this.contentDividerColor = color;
        adaptContentDividerColor();
    }

    @Override
    protected final void onAttach(@NonNull final View view) {
        inflateTitleView();
        inflateMessageView();
        inflateContentView();
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
        contentRootView = null;
        contentContainer = null;
    }

}