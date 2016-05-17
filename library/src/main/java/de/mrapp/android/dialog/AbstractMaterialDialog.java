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
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.mrapp.android.util.DisplayUtil;
import de.mrapp.android.util.ViewUtil;

import static de.mrapp.android.util.DisplayUtil.getDeviceType;
import static de.mrapp.android.util.DisplayUtil.getOrientation;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractMaterialDialog extends Dialog {

    /**
     * An abstract base class for all builders, which allow to create and show dialogs, which are
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param <DialogType>
     *         The type of the dialog, which is created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    public abstract static class AbstractBuilder<DialogType extends AbstractMaterialDialog, BuilderType extends AbstractBuilder<DialogType, ?>> {

        /**
         * The dialog, which is configured by the builder.
         */
        private DialogType dialog;

        /**
         * Initializes the builder.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value, or -1, if the default theme should be used
         */
        private void initialize(@NonNull final Context context,
                                @StyleRes final int themeResourceId) {
            int themeId = themeResourceId != -1 ? themeResourceId : R.style.MaterialDialog_Light;
            dialog = onCreateDialog(context, themeId);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setAttributes(createLayoutParams());
            obtainStyledAttributes(themeId);
        }

        /**
         * Creates and returns the layout params, which should be used by the dialog.
         *
         * @return The layout params, which should be used by the dialog, as an instance of the
         * class {@link WindowManager.LayoutParams}
         */
        private WindowManager.LayoutParams createLayoutParams() {
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

            if (getDeviceType(getContext()) == DisplayUtil.DeviceType.PHONE &&
                    getOrientation(getContext()) == DisplayUtil.Orientation.PORTRAIT) {
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            }

            return layoutParams;
        }

        /**
         * Obtains the background from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the background should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainBackground(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogBackground});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setBackgroundColor(color);
            } else {
                int resourceId = typedArray.getResourceId(0, 0);

                if (resourceId != 0) {
                    setBackground(resourceId);
                }
            }
        }

        /**
         * Obtains the message color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the message color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainMessageColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogMessageColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setMessageColor(color);
            }
        }

        /**
         * Obtains the title color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the title color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainTitleColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogTitleColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setTitleColor(color);
            }
        }

        /**
         * Returns the builder itself, casted to the generic type BuilderType.
         *
         * @return The builder itself as an instance of the generic type BuilderType
         */
        @SuppressWarnings("unchecked")
        protected final BuilderType self() {
            return (BuilderType) this;
        }

        /**
         * Returns the dialog, which is configured by the builder.
         *
         * @return The dialog, which is configured by the builder, as an instance of the generic
         * type BuilderType
         */
        protected final DialogType getDialog() {
            return dialog;
        }

        /**
         * Obtains all relevant attributes from the current theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid style resource
         */
        @CallSuper
        protected void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            obtainBackground(themeResourceId);
            obtainMessageColor(themeResourceId);
            obtainTitleColor(themeResourceId);
        }

        /**
         * The method, which is invoked on subclasses in order to create the dialog, which is
         * configured by the builder.
         *
         * @param context
         *         The context, which should be used by the dialog, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value
         * @return The dialog, which has been created, as an instance of the generic type DialogType
         */
        protected abstract DialogType onCreateDialog(@NonNull final Context context,
                                                     @StyleRes final int themeResourceId);

        /**
         * Creates a new builder, which allows to create and show dialogs, which are designed
         * according to Android 5's Material Design guidelines even on pre-Lollipop devices.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public AbstractBuilder(@NonNull final Context context) {
            this(context, -1);
        }

        /**
         * Creates a new builder, which allows to create and show dialogs, which are designed
         * according to Android 5's Material Design guidelines even on pre-Lollipop devices.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public AbstractBuilder(@NonNull final Context context,
                               @StyleRes final int themeResourceId) {
            initialize(context, themeResourceId);
        }

        /**
         * Returns the context, which is used by the builder.
         *
         * @return The context, which is used by the builder, as an instance of the class {@link
         * Context}
         */
        public final Context getContext() {
            return getDialog().getContext();
        }

        /**
         * Sets, whether the dialog, which is created by the builder, should be cancelable, or not.
         *
         * @param cancelable
         *         True, if the dialog, which is created by the builder, should be cancelable, false
         *         otherwise
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCancelable(final boolean cancelable) {
            getDialog().setCancelable(cancelable);
            return self();
        }

        /**
         * Sets the listener, which should be notified, when the dialog, which is created by the
         * builder, is canceled.
         *
         * Even in a cancelable dialog, the dialog may be dismissed for reasons other than being
         * canceled or one of the supplied choices being selected. If you are interested in
         * listening for all cases where the dialog is dismissed and not just when it is canceled,
         * see {@link #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         * setOnDismissListener}.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnCancelListener}, or null, if no listener should be set
         * @return The builder, the method has been called upon, as an instance of the class
         * BuilderType
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         */
        public BuilderType setOnCancelListener(@Nullable final OnCancelListener listener) {
            getDialog().setOnCancelListener(listener);
            return self();
        }

        /**
         * Sets the listener, which should be notified, when the dialog, which is created by the
         * builder, is dismissed for any reason.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnDismissListener}, or null, if no listener should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setOnDismissListener(@Nullable final OnDismissListener listener) {
            getDialog().setOnDismissListener(listener);
            return self();
        }

        /**
         * Sets the listener, which should be notified, if a key is dispatched to the dialog, which
         * is created by the builder.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnKeyListener}, or null, if no listener should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setOnKeyListener(@Nullable final OnKeyListener listener) {
            getDialog().setOnKeyListener(listener);
            return self();
        }

        /**
         * Sets the color of the title of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setTitleColor(@ColorInt final int color) {
            getDialog().setTitleColor(color);
            return self();
        }

        /**
         * Sets the color of the message of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMessageColor(@ColorInt final int color) {
            getDialog().setMessageColor(color);
            return self();
        }

        /**
         * Sets the background of the dialog, which is created by the builder.
         *
         * @param background
         *         The background, which should be set, as an instance of the class {@link Drawable}
         *         or null, if no custom background should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setBackground(@Nullable final Drawable background) {
            getDialog().setBackground(background);
            return self();
        }

        /**
         * Sets the background of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the background, which should be set, as an {@link Integer}
         *         value. The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setBackground(@DrawableRes final int resourceId) {
            getDialog().setBackground(resourceId);
            return self();
        }

        /**
         * Sets the background color of the dialog, which is created by the builder.
         *
         * @param color
         *         The background color, which should be set, as an {@link Integer} value or -1, if
         *         no custom background color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setBackgroundColor(@ColorInt final int color) {
            getDialog().setBackgroundColor(color);
            return self();
        }

        /**
         * Sets the title of the dialog, which is created by the builder.
         *
         * @param title
         *         The title, which should be set, as an instance of the type {@link CharSequence}
         *         or null, if no title should be shown
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setTitle(@Nullable final CharSequence title) {
            getDialog().setTitle(title);
            return self();
        }

        /**
         * Sets the title of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the title, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setTitle(@StringRes final int resourceId) {
            getDialog().setTitle(resourceId);
            return self();
        }

        /**
         * Sets the message of the dialog, which is created by the builder.
         *
         * @param message
         *         The message, which should be set, as an instance of the type {@link CharSequence}
         *         or null, if no message should be shown
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMessage(@Nullable final CharSequence message) {
            getDialog().setMessage(message);
            return self();
        }

        /**
         * Sets the message of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the message, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMessage(@StringRes final int resourceId) {
            getDialog().setMessage(resourceId);
            return self();
        }

        /**
         * Sets the icon of the dialog, which is created by the builder.
         *
         * @param icon
         *         The icon, which should be set, as an instance of the class {@link Drawable} or
         *         null, if no icon should be shown
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setIcon(@Nullable final Drawable icon) {
            getDialog().setIcon(icon);
            return self();
        }

        /**
         * Sets the icon of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the icon, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setIcon(@DrawableRes final int resourceId) {
            getDialog().setIcon(resourceId);
            return self();
        }

        /**
         * Set the icon of the dialog, which is created by the builder.
         *
         * @param attributeId
         *         The id of the theme attribute, which supplies the icon, which should be set, as
         *         an {@link Integer} value. The id must point to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setIconAttribute(@AttrRes final int attributeId) {
            getDialog().setIconAttribute(attributeId);
            return self();
        }

        /**
         * Sets the custom view, which should be shown by the dialog, which is created by the
         * builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be shown
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setView(@Nullable final View view) {
            getDialog().setView(view);
            return self();
        }

        /**
         * Sets the custom view, which should be shown by the dialog, which is created by the
         * builder.
         *
         * @param resourceId
         *         The resource id of the view, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setView(@LayoutRes final int resourceId) {
            getDialog().setView(resourceId);
            return self();
        }

        /**
         * Sets the custom view, which should be used to show the title of the dialog, which is
         * created by the builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be used to show the title
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCustomTitle(@Nullable final View view) {
            getDialog().setCustomTitle(view);
            return self();
        }

        /**
         * Sets the custom view, which should be used to show the title of the dialog, which is
         * created by the builder.
         *
         * @param resourceId
         *         The resource id of the view, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCustomTitle(@LayoutRes final int resourceId) {
            getDialog().setCustomTitle(resourceId);
            return self();
        }

        /**
         * Sets the custom view, which should be used to show the message of the dialog, which is
         * created by the builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be used to show the message
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCustomMessage(@Nullable final View view) {
            getDialog().setCustomMessage(view);
            return self();
        }

        /**
         * Sets the custom view, which should be used to show the message of the dialog, which is
         * created by the builder.
         *
         * @param resourceId
         *         The resource id of the view, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCustomMessage(@LayoutRes final int resourceId) {
            getDialog().setCustomMessage(resourceId);
            return self();
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder. Calling
         * this method does not display the dialog.
         *
         * @return The dialog, which has been created as an instance of the generic type DialogType
         */
        public final DialogType create() {
            return getDialog();
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @return The dialog, which has been shown, as an instance of the generic type DialogType
         */
        public final DialogType show() {
            getDialog().show();
            return getDialog();
        }

    }

    /**
     * The root view of the dialog.
     */
    private ViewGroup rootView;

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
    private int titleColor = -1;

    /**
     * The color of the message of the dialog.
     */
    private int messageColor = -1;

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
     * Inflates the dialog's root view.
     */
    private void inflateLayout() {
        rootView = (ViewGroup) View.inflate(getContext(), R.layout.material_dialog, null);
    }

    /**
     * Inflates the view, which is used to show the dialog's title. The view may either be the
     * default one or a custom view, if one has been set before.
     */
    private void inflateTitleView() {
        titleContainer = (ViewGroup) rootView.findViewById(R.id.title_container);
        titleContainer.removeAllViews();

        if (customTitleView != null) {
            titleContainer.addView(customTitleView);
        } else if (customTitleViewId != -1) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(customTitleViewId, titleContainer, false);
            titleContainer.addView(view);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view =
                    layoutInflater.inflate(R.layout.material_dialog_title, titleContainer, false);
            titleContainer.addView(view);
        }

        View titleView = titleContainer.findViewById(android.R.id.title);
        titleTextView = titleView instanceof TextView ? (TextView) titleView : null;
    }

    /**
     * Inflates the view, which is used to show the dialog's message. The view may either be the
     * default one or a custom view, if one has been set before.
     */
    private void inflateMessageView() {
        messageContainer = (ViewGroup) rootView.findViewById(R.id.message_container);
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

    /**
     * Inflates the view, which is used to show the dialog's content. The view may either be the
     * default one or a custom view, if one has been set before.
     */
    private void inflateContentView() {
        contentRootView = (ViewGroup) rootView.findViewById(R.id.content_root);
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        contentContainer.removeAllViews();

        if (customView != null) {
            showContentContainer();
            contentContainer.addView(customView);
        } else if (customViewId != -1) {
            showContentContainer();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(customViewId, contentContainer, false);
            contentContainer.addView(view);
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
        if (titleTextView != null && titleColor != -1) {
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

        if (titleContainer != null) {
            LinearLayout.LayoutParams layoutParams =
                    (LinearLayout.LayoutParams) titleContainer.getLayoutParams();
            layoutParams.bottomMargin = !TextUtils.isEmpty(message) ? getContext().getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_spacing) : 0;
            titleContainer.setLayoutParams(layoutParams);
        }

        adaptMessageContainerVisibility();
    }

    /**
     * Adapts the color of the dialog's message.
     */
    private void adaptMessageColor() {
        if (messageTextView != null && messageColor != -1) {
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
        if (rootView != null) {
            ViewUtil.setBackground(rootView, background);
        }
    }

    /**
     * Shows the parent view of the view, which is used to show the dialog's content.
     */
    private void showContentContainer() {
        contentContainer.setVisibility(View.VISIBLE);
        int contentSpacing =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_content_spacing);
        LinearLayout.LayoutParams titleLayoutParams =
                (LinearLayout.LayoutParams) titleContainer.getLayoutParams();
        titleLayoutParams.bottomMargin = contentSpacing;
        titleContainer.setLayoutParams(titleLayoutParams);
        LinearLayout.LayoutParams messageLayoutParams =
                (LinearLayout.LayoutParams) messageContainer.getLayoutParams();
        messageLayoutParams.bottomMargin = contentSpacing;
        messageContainer.setLayoutParams(messageLayoutParams);
    }

    /**
     * Returns the root view of the dialog.
     *
     * @return The root view of the dialog as an instance of the class {@link ViewGroup}
     */
    protected final ViewGroup getRootView() {
        return rootView;
    }

    /**
     * Returns the parent view of the view, which is used to show the dialog's title.
     *
     * @return The parent view of the view, which is used to show the dialog's title, as an instance
     * of the class {@link ViewGroup}
     */
    protected final ViewGroup getTitleContainer() {
        return titleContainer;
    }

    /**
     * Returns the text view, which is used to show the dialog's title.
     *
     * @return The text view, which is used to show the dialog's title, as an instance of the class
     * {@link TextView}
     */
    protected final TextView getTitleTextView() {
        return titleTextView;
    }

    /**
     * Returns the parent view of the view, which is used to show the dialog's message.
     *
     * @return The parent view of the view, which is used to show the dialog's message, as an
     * instance of the class {@link ViewGroup}
     */
    protected final ViewGroup getMessageContainer() {
        return messageContainer;
    }

    /**
     * Returns the text view, which is used to show the dialog's message.
     *
     * @return The text view, which is used to show the dialog's message, as an instance of the
     * class {@link TextView}
     */
    protected final TextView getMessageTextView() {
        return messageTextView;
    }

    /**
     * Returns the root view of all views, which are used to show the dialog's title, message and
     * content.
     *
     * @return The root view of all views, which are used to show the dialog's title, message and
     * content, as an instance of the class {@link ViewGroup}
     */
    protected final ViewGroup getContentRootView() {
        return contentRootView;
    }

    /**
     * Returns the parent view of the view, which is used to show the dialog's content.
     *
     * @return The parent view of the view, which is used to show the dialog's content, as an
     * instance of the class {@link ViewGroup}.
     */
    protected final ViewGroup getContentContainer() {
        return contentContainer;
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
    }

    /**
     * Returns the icon of the dialog.
     *
     * @return The icon of the dialog, as an instance of the class {@link Drawable} or null, if no
     * icon has been set
     */
    public final Drawable getIcon() {
        return icon;
    }

    /**
     * Sets the icon of the dialog.
     *
     * @param icon
     *         The icon, which should be set, as an instance of the class {@link Drawable} or null,
     *         if no icon should be shown
     */
    public final void setIcon(final Drawable icon) {
        this.icon = icon;
        adaptIcon();
    }

    /**
     * Sets the icon of the dialog.
     *
     * @param resourceId
     *         The resource id of the icon, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid drawable resource
     */
    public final void setIcon(@DrawableRes final int resourceId) {
        setIcon(ContextCompat.getDrawable(getContext(), resourceId));
    }

    /**
     * Set the icon of the dialog.
     *
     * @param attributeId
     *         The id of the theme attribute, which supplies the icon, which should be set, as an
     *         {@link Integer} value. The id must point to a valid drawable resource
     */
    public final void setIconAttribute(@AttrRes final int attributeId) {
        TypedArray typedArray =
                getContext().getTheme().obtainStyledAttributes(new int[]{attributeId});
        setIcon(typedArray.getDrawable(0));
    }

    /**
     * Returns the color of the title of the dialog.
     *
     * @return The color of the title of the dialog as an {@link Integer} value or -1, if no custom
     * color has been set
     */
    public final int getTitleColor() {
        return titleColor;
    }

    /**
     * Sets the color of the title of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setTitleColor(@ColorInt final int color) {
        titleColor = color;
        adaptTitleColor();
    }

    /**
     * Returns the color of the message of the dialog.
     *
     * @return The color of the message of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    public final int getMessageColor() {
        return messageColor;
    }

    /**
     * Sets the color of the message of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setMessageColor(@ColorInt final int color) {
        messageColor = color;
        adaptMessageColor();
    }

    /**
     * Returns the background of the dialog.
     *
     * @return The background of the dialog as an instance of the class {@link Drawable} or null, if
     * no custom background has been set
     */
    public final Drawable getBackground() {
        return background;
    }

    /**
     * Sets the background of the dialog.
     *
     * @param background
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no custom background should be set
     */
    public final void setBackground(@Nullable final Drawable background) {
        this.background = background;
        adaptBackground();
    }

    /**
     * Sets the background of the dialog.
     *
     * @param resourceId
     *         The resource id of the background, which should be set, as an {@link Integer} value.
     *         The resource id must correspond to a valid drawable resource
     */
    public final void setBackground(@DrawableRes final int resourceId) {
        setBackground(ContextCompat.getDrawable(getContext(), resourceId));
    }

    /**
     * Sets the background color of the dialog.
     *
     * @param color
     *         The background color, which should be set, as an {@link Integer} value or -1, if no
     *         custom background color should be set
     */
    public final void setBackgroundColor(@ColorInt final int color) {
        setBackground(color != -1 ? new ColorDrawable(color) : null);
    }

    /**
     * Sets the custom view, which should be used to show the title of the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the title
     */
    public final void setCustomTitle(@Nullable final View view) {
        customTitleView = view;
        customTitleViewId = -1;
        adaptTitleView();
    }

    /**
     * Sets the custom view, which should be used to show the title of the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    public final void setCustomTitle(@LayoutRes final int resourceId) {
        customTitleView = null;
        customTitleViewId = resourceId;
        adaptTitleView();
    }

    /**
     * Sets the custom view, which should be used to show the message of the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the title
     */
    public final void setCustomMessage(@Nullable final View view) {
        customMessageView = view;
        customMessageViewId = -1;
        adaptMessageView();
    }

    /**
     * Sets the custom view, which should be used to show the message of the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    public final void setCustomMessage(@LayoutRes final int resourceId) {
        customMessageView = null;
        customMessageViewId = resourceId;
        adaptMessageView();
    }

    /**
     * Sets the custom view, which should be shown by the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be shown
     */
    public final void setView(@Nullable final View view) {
        customView = view;
        customViewId = -1;
        adaptContentView();
    }

    /**
     * Sets the custom view, which should be shown by the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    public final void setView(@LayoutRes final int resourceId) {
        customView = null;
        customViewId = resourceId;
        adaptContentView();
    }

    /**
     * Returns the message of the dialog.
     *
     * @return The message of the dialog as an instance of the type {@link CharSequence} or null, if
     * no message has been set
     */
    public final CharSequence getMessage() {
        return message;
    }

    /**
     * Sets the message of the dialog.
     *
     * @param message
     *         The message, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no message should be shown
     */
    public final void setMessage(@Nullable final CharSequence message) {
        this.message = message;
        adaptMessage();
    }

    /**
     * Sets the message of the dialog.
     *
     * @param resourceId
     *         The resource id of the message, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    public final void setMessage(@StringRes final int resourceId) {
        setMessage(getContext().getText(resourceId));
    }

    /**
     * Returns the title of the dialog.
     *
     * @return The title of the dialog as an instance of the type {@link CharSequence} or null, if
     * no title has been set
     */
    public final CharSequence getTitle() {
        return title;
    }

    @Override
    public final void setTitle(@Nullable final CharSequence title) {
        super.setTitle(title);
        this.title = title;
        adaptTitle();
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        inflateLayout();
        inflateTitleView();
        inflateMessageView();
        inflateContentView();
        setContentView(rootView);
        adaptTitle();
        adaptTitleColor();
        adaptIcon();
        adaptMessage();
        adaptMessageColor();
        adaptBackground();
    }

    @CallSuper
    @Override
    public void onStop() {
        super.onStop();
        rootView = null;
        titleContainer = null;
        messageContainer = null;
        titleTextView = null;
        messageTextView = null;
        contentRootView = null;
        contentContainer = null;
    }

}