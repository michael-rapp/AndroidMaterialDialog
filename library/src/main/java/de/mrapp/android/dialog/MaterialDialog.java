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

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

import de.mrapp.android.dialog.adapter.ArrayAdapter;
import de.mrapp.android.dialog.listener.OnClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnMultiChoiceClickListenerWrapper;
import de.mrapp.android.util.DisplayUtil.DeviceType;
import de.mrapp.android.util.DisplayUtil.Orientation;

import static de.mrapp.android.util.Condition.ensureNotNull;
import static de.mrapp.android.util.DisplayUtil.getDeviceType;
import static de.mrapp.android.util.DisplayUtil.getOrientation;

/**
 * A dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices. Such a dialog consists of a title, a message and up to three buttons.
 * Furthermore, the dialog can be used to show list items. It is possible to customize the color of
 * the dialog's title and button texts and the title as well as the dialog's content can be replaced
 * with a custom view.
 *
 * For creating or showing such dialogs, the methods {@link MaterialDialog#create()} or {@link
 * MaterialDialog#show()} of the builder {@link MaterialDialog.Builder} can be used.
 *
 * @author Michael Rapp
 * @since 3.0.0
 */
public class MaterialDialog extends Dialog implements DialogInterface {

    /**
     * A builder, which allows to create and show dialogs, which are designed according to Android
     * 5's Material Design guidelines even on pre-Lollipop devices. Such a dialog consists of a
     * title, a message and up to three buttons. Furthermore, the dialog can be used to show list
     * items. It is possible to customize the color of the dialog's title and button texts and the
     * title as well as the dialog's content can be replaced with a custom view.
     */
    public static class Builder {

        /**
         * The dialog, which is created by the builder.
         */
        private MaterialDialog dialog;

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
            int themeId = themeResourceId;

            if (themeResourceId == -1) {
                TypedValue typedValue = new TypedValue();
                context.getTheme().resolveAttribute(R.attr.alertDialogTheme, typedValue, true);
                themeId = typedValue.resourceId;
            }

            dialog = new MaterialDialog(context, themeId);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            obtainStyledAttributes(themeId);
        }

        /**
         * Obtains all relevant attributes from the current theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid style resource
         */
        private void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            obtainBackground(themeResourceId);
            obtainButtonTextColor(themeResourceId);
            obtainItemColor(themeResourceId);
            obtainItemControlColor(themeResourceId);
            obtainMessageColor(themeResourceId);
            obtainTitleColor(themeResourceId);
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
         * Obtains the button text color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the button text color should be obtained from, as
         *         an {@link Integer} value
         */
        private void obtainButtonTextColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogButtonTextColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setButtonTextColor(color);
            }
        }

        /**
         * Obtains the item color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the item color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainItemColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogItemColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setItemColor(color);
            }
        }

        /**
         * Obtains the item control color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the item control color should be obtained from, as
         *         an {@link Integer} value
         */
        private void obtainItemControlColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogItemControlColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setItemControlColor(color);
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
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            this(context, -1);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public Builder(@NonNull final Context context, @StyleRes final int themeResourceId) {
            initialize(context, themeResourceId);
        }

        /**
         * Returns the context, which is used by the builder.
         *
         * @return The context, which is used by the builder, as an instance of the class {@link
         * Context}
         */
        public final Context getContext() {
            return dialog.getContext();
        }

        /**
         * Sets, whether the dialog, which is created by the builder, should be cancelable, or not.
         *
         * @param cancelable
         *         True, if the dialog, which is created by the builder, should be cancelable, false
         *         otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setCancelable(final boolean cancelable) {
            dialog.setCancelable(cancelable);
            return this;
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
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         */
        public Builder setOnCancelListener(@Nullable final OnCancelListener listener) {
            dialog.setOnCancelListener(listener);
            return this;
        }

        /**
         * Sets the listener, which should be notified, when the dialog, which is created by the
         * builder, is dismissed for any reason.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnDismissListener}, or null, if no listener should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setOnDismissListener(@Nullable final OnDismissListener listener) {
            dialog.setOnDismissListener(listener);
            return this;
        }

        /**
         * Sets the listener, which should be notified, if a key is dispatched to the dialog, which
         * is created by the builder.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnKeyListener}, or null, if no listener should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setOnKeyListener(@Nullable final OnKeyListener listener) {
            dialog.setOnKeyListener(listener);
            return this;
        }

        /**
         * Sets the color of the title of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTitleColor(@ColorInt final int color) {
            dialog.setTitleColor(color);
            return this;
        }

        /**
         * Sets the color of the message of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMessageColor(@ColorInt final int color) {
            dialog.setMessageColor(color);
            return this;
        }

        /**
         * Sets the color of the list items of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setItemColor(@ColorInt final int color) {
            dialog.setItemColor(color);
            return this;
        }

        /**
         * Sets the control color of the list items of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public final Builder setItemControlColor(@ColorInt final int color) {
            dialog.setItemControlColor(color);
            return this;
        }

        /**
         * Sets the background of the dialog, which is created by the builder.
         *
         * @param background
         *         The background, which should be set, as an instance of the class {@link Drawable}
         *         or null, if no custom background should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setBackground(@Nullable final Drawable background) {
            dialog.setBackground(background);
            return this;
        }

        /**
         * Sets the background of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the background, which should be set, as an {@link Integer}
         *         value. The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setBackground(@DrawableRes final int resourceId) {
            dialog.setBackground(resourceId);
            return this;
        }

        /**
         * Sets the background color of the dialog, which is created by the builder.
         *
         * @param color
         *         The background color, which should be set, as an {@link Integer} value or -1, if
         *         no custom background color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setBackgroundColor(@ColorInt final int color) {
            dialog.setBackgroundColor(color);
            return this;
        }

        /**
         * Sets the color of the button texts of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         button color should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setButtonTextColor(@ColorInt final int color) {
            dialog.setButtonTextColor(color);
            return this;
        }

        /**
         * Sets, whether the buttons of the dialog, which is created by the builder, should be
         * aligned vertically, or not.
         *
         * @param stackButtons
         *         True, if the buttons of the dialog, which is created by the builder, should be
         *         aligned vertically, false otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder stackButtons(final boolean stackButtons) {
            dialog.stackButtons(stackButtons);
            return this;
        }

        /**
         * Adds a new validator, which should be executed when the positive button of the dialog,
         * which is created by the builder, is clicked.
         *
         * @param validator
         *         The validator, which should be added, as an instance of the type {@link
         *         Validator}. The validator may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addValidator(@NonNull final Validator validator) {
            ensureNotNull(validator, "The validator may not be null");
            dialog.addValidator(validator);
            return this;
        }

        /**
         * Adds all validators, which are contained by a specific collection and should be executed
         * when the positive button of the dialog, which is created by the builder, is clicked.
         *
         * @param validators
         *         A collection, which contains all validators, which should be added, as an
         *         instance of the type {@link Collection} or an empty collection, if no validators
         *         should be added
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder addAllValidators(@NonNull final Collection<Validator> validators) {
            ensureNotNull(validators, "The collection may not be null");
            dialog.addAllValidators(validators);
            return this;
        }

        /**
         * Sets the title of the dialog, which is created by the builder.
         *
         * @param title
         *         The title, which should be set, as an instance of the type {@link CharSequence}
         *         or null, if no title should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTitle(@Nullable final CharSequence title) {
            dialog.setTitle(title);
            return this;
        }

        /**
         * Sets the title of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the title, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setTitle(@StringRes final int resourceId) {
            dialog.setTitle(resourceId);
            return this;
        }

        /**
         * Sets the message of the dialog, which is created by the builder.
         *
         * @param message
         *         The message, which should be set, as an instance of the type {@link CharSequence}
         *         or null, if no message should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMessage(@Nullable final CharSequence message) {
            dialog.setMessage(message);
            return this;
        }

        /**
         * Sets the message of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the message, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMessage(@StringRes final int resourceId) {
            dialog.setMessage(resourceId);
            return this;
        }

        /**
         * Sets the icon of the dialog, which is created by the builder.
         *
         * @param icon
         *         The icon, which should be set, as an instance of the class {@link Drawable} or
         *         null, if no icon should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setIcon(@Nullable final Drawable icon) {
            dialog.setIcon(icon);
            return this;
        }

        /**
         * Sets the icon of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the icon, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setIcon(@DrawableRes final int resourceId) {
            dialog.setIcon(resourceId);
            return this;
        }

        /**
         * Set the icon of the dialog, which is created by the builder.
         *
         * @param attributeId
         *         The id of the theme attribute, which supplies the icon, which should be set, as
         *         an {@link Integer} value. The id must point to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setIconAttribute(@AttrRes final int attributeId) {
            dialog.setIconAttribute(attributeId);
            return this;
        }

        /**
         * Sets the text of the negative button of the dialog, which is created by the builder.
         *
         * @param text
         *         The text, which should be set, as an instance of the type {@link CharSequence} or
         *         null, if no negative button should be shown
         * @param listener
         *         The listener, which should be notified, when the negative button is clicked, as
         *         an instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNegativeButton(@Nullable final CharSequence text,
                                               @Nullable final OnClickListener listener) {
            dialog.setNegativeButton(text, listener);
            return this;
        }

        /**
         * Sets the text of the negative button of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @param listener
         *         The listener, which should be notified, when the negative button is clicked, as
         *         an instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNegativeButton(@StringRes final int resourceId,
                                               @Nullable final OnClickListener listener) {
            dialog.setNegativeButton(resourceId, listener);
            return this;
        }

        /**
         * Sets the text of the positive button of the dialog, which is created by the builder.
         *
         * @param text
         *         The text, which should be set, as an instance of the type {@link CharSequence} or
         *         null, if no positive button should be shown
         * @param listener
         *         The listener, which should be notified, when the positive button is clicked, as
         *         an instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setPositiveButton(@Nullable final CharSequence text,
                                               @Nullable final OnClickListener listener) {
            dialog.setPositiveButton(text, listener);
            return this;
        }

        /**
         * Sets the text of the positive button of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @param listener
         *         The listener, which should be notified, when the positive button is clicked, as
         *         an instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setPositiveButton(@StringRes final int resourceId,
                                               @Nullable final OnClickListener listener) {
            dialog.setPositiveButton(resourceId, listener);
            return this;
        }

        /**
         * Sets the text of the neutral button of the dialog, which is created by the builder.
         *
         * @param text
         *         The text, which should be set, as an instance of the type {@link CharSequence} or
         *         null, if no neutral button should be shown
         * @param listener
         *         The listener, which should be notified, when the neutral button is clicked, as an
         *         instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNeutralButton(@Nullable final CharSequence text,
                                              @Nullable final OnClickListener listener) {
            dialog.setNeutralButton(text, listener);
            return this;
        }

        /**
         * Sets the text of the neutral button of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the text, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @param listener
         *         The listener, which should be notified, when the neutral button is clicked, as an
         *         instance of the type {@link OnClickListener} or null, if no listener should be
         *         notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setNeutralButton(@StringRes final int resourceId,
                                              @Nullable final OnClickListener listener) {
            dialog.setNeutralButton(resourceId, listener);
            return this;
        }

        /**
         * Sets the items, which should be shown by the dialog, which is created by the builder.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setItems(@NonNull final CharSequence[] items,
                                      @Nullable final OnClickListener listener) {
            dialog.setItems(items, listener);
            return this;
        }

        /**
         * Sets the items, which should be shown by the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setItems(@ArrayRes final int resourceId,
                                      @Nullable final OnClickListener listener) {
            dialog.setItems(resourceId, listener);
            return this;
        }

        /**
         * Sets the adapter, which provides the items, which should be shown by the dialog, which is
         * created by the builder.
         *
         * @param adapter
         *         The adapter, which should be set, as an instance of the type {@link ListAdapter}.
         *         The adapter may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setAdapter(@NonNull final ListAdapter adapter,
                                        @Nullable final OnClickListener listener) {
            dialog.setAdapter(adapter, listener);
            return this;
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Only one of the items can be selected at once.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param checkedItem
         *         The index of the item, which should be selected by default, as an {@link Integer}
         *         value or -1, if no item should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setSingleChoiceItems(@NonNull final CharSequence[] items,
                                                  final int checkedItem,
                                                  @Nullable final OnClickListener listener) {
            dialog.setSingleChoiceItems(items, checkedItem, listener);
            return this;
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Only one of the items can be selected at once.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param checkedItem
         *         The index of the item, which should be selected by default, as an {@link Integer}
         *         value or -1, if no item should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setSingleChoiceItems(@ArrayRes final int resourceId,
                                                  final int checkedItem,
                                                  @Nullable final OnClickListener listener) {
            dialog.setSingleChoiceItems(resourceId, checkedItem, listener);
            return this;
        }

        /**
         * Sets the adapter, which provides the selectable items, which should be shown by the
         * dialog, which is created by the builder. Only one of the items can be selected at once.
         *
         * @param adapter
         *         The adapter, which should be set, as an instance of the type {@link ListAdapter}.
         *         The adapter may not be null
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setSingleChoiceItems(@NonNull final ListAdapter adapter,
                                                  final int checkedItem,
                                                  @Nullable final OnClickListener listener) {
            dialog.setSingleChoiceItems(adapter, checkedItem, listener);
            return this;
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Multiple items can be selected at once.
         *
         * @param items
         *         The items, which should be set, as an array of the type {@link CharSequence}. The
         *         items may not be null
         * @param checkedItems
         *         An array, which contains, whether the items, which correspond to the
         *         corresponding indices, should be selected by default, or not, as a {@link
         *         Boolean} array or null, if no items should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMultiChoiceItems(@NonNull final CharSequence[] items,
                                                 @Nullable final boolean[] checkedItems,
                                                 @Nullable final OnMultiChoiceClickListener listener) {
            dialog.setMultiChoiceItems(items, checkedItems, listener);
            return this;
        }

        /**
         * Sets the selectable items, which should be shown by the dialog, which is created by the
         * builder. Multiple items can be selected at once.
         *
         * @param resourceId
         *         The resource id of the items, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid array resource
         * @param checkedItems
         *         An array, which contains, whether the items, which correspond to the
         *         corresponding indices, should be selected by default, or not, as a {@link
         *         Boolean} array or null, if no items should be selected by default
         * @param listener
         *         The listener, which should be notified, when an item is clicked, as an instance
         *         of the type {@link OnClickListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setMultiChoiceItems(@ArrayRes final int resourceId,
                                                 @Nullable final boolean[] checkedItems,
                                                 @Nullable final OnMultiChoiceClickListener listener) {
            dialog.setMultiChoiceItems(resourceId, checkedItems, listener);
            return this;
        }

        /**
         * Sets the listener, which should be notified, when an item, which is shown by the dialog,
         * which is created by the builder, is selected.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnItemSelectedListener} or null, if no listener should be notified
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setOnItemSelectedListener(
                @Nullable final OnItemSelectedListener listener) {
            dialog.setOnItemSelectedListener(listener);
            return this;
        }

        /**
         * Sets the custom view, which should be shown by the dialog, which is created by the
         * builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setView(@Nullable final View view) {
            dialog.setView(view);
            return this;
        }

        /**
         * Sets the custom view, which should be shown by the dialog, which is created by the
         * builder.
         *
         * @param resourceId
         *         The resource id of the view, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setView(@LayoutRes final int resourceId) {
            dialog.setView(resourceId);
            return this;
        }

        /**
         * Sets the custom view, which should be used to show the title of the dialog, which is
         * created by the builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be used to show the title
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setCustomTitle(@Nullable final View view) {
            dialog.setCustomTitle(view);
            return this;
        }

        /**
         * Sets the custom view, which should be used to show the title of the dialog, which is
         * created by the builder.
         *
         * @param resourceId
         *         The resource id of the vieww, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setCustomTitle(@LayoutRes final int resourceId) {
            dialog.setCustomTitle(resourceId);
            return this;
        }

        /**
         * Creates a dialog with the arguments, which have been suppplied to the builder. Calling
         * this method does not display the dialog.
         *
         * @return The dialog, which has been create as an instance of the class {@link
         * MaterialDialog}
         */
        public final MaterialDialog create() {
            return dialog;
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @return The dialog, which has been shown, as an instance of the class {@link
         * MaterialDialog}
         */
        public final MaterialDialog show() {
            dialog.show();
            return dialog;
        }

    }

    /**
     * Defines the interface, a class, which should be able to validate the content of a dialog,
     * must implement.
     */
    public interface Validator {

        /**
         * Validates the content of a dialog.
         *
         * @param dialog
         *         The dialog, whose content should be validated, as an instance of the class {@link
         *         MaterialDialog}. The dialog may not be null
         * @return True, if the content of the dialog is valid, false otherwise
         */
        boolean validate(@NonNull MaterialDialog dialog);

    }

    /**
     * A set, which contains the validators of the dialog.
     */
    private final Set<Validator> validators;

    /**
     * The root view of the dialog.
     */
    private ViewGroup rootView;

    /**
     * The parent view of the layout, which is used to show the dialog's title.
     */
    private ViewGroup titleContainer;

    /**
     * The text view, which is used to show the title of the dialog.
     */
    private TextView titleTextView;

    /**
     * The text view, which is used to show the dialog's message.
     */
    private TextView messageTextView;

    /**
     * The root view of all layouts, which are used to show the dialog's title, message and
     * content.
     */
    private ViewGroup contentRootView;

    /**
     * The parent view of the layout, which used to show the dialog's content.
     */
    private ViewGroup contentContainer;

    /**
     * The list view, which is used to show the dialog's list items.
     */
    private ListView listView;

    /**
     * The parent view of the layout, which is used to show the dialog's buttons.
     */
    private ViewGroup buttonBarContainer;

    /**
     * The positive button of the dialog.
     */
    private Button positiveButton;

    /**
     * The negative button of the dialog.
     */
    private Button negativeButton;

    /**
     * The neutral button of the dialog.
     */
    private Button neutralButton;

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
     * The color of the list items of the dialog.
     */
    private int itemColor = -1;

    /**
     * The control color of the list items of the dialog.
     */
    private int itemControlColor = -1;

    /**
     * The background of the dialog.
     */
    private Drawable background;

    /**
     * The color of the button texts of the dialog.
     */
    private int buttonTextColor = -1;

    /**
     * True, if the buttons of the dialog are aligned vertically, false otherwise.
     */
    private boolean stackButtons;

    /**
     * The text of the negative button of the dialog.
     */
    private CharSequence negativeButtonText;

    /**
     * The text of the neutral button of the dialog.
     */
    private CharSequence neutralButtonText;

    /**
     * The text of the positive button of the dialog.
     */
    private CharSequence positiveButtonText;

    /**
     * The listener, which is notified, when the negative button of the dialog, is clicked.
     */
    private OnClickListener negativeButtonListener;

    /**
     * The listener, which is notified, when the neutral button of the dialog, is clicked.
     */
    private OnClickListener neutralButtonListener;

    /**
     * The listener, which is notified, when the positive button of the dialog, is clicked.
     */
    private OnClickListener positiveButtonListener;

    /**
     * The adapter, which is used to manage the list items of the dialog.
     */
    private ListAdapter listAdapter;

    /**
     * The choice mode of the list view, which is used to show the list items of the dialog.
     */
    private int listViewChoiceMode = -1;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_SINGLE</code>.
     */
    private OnClickListener listViewSingleChoiceListener;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_MULTIPLE</code>.
     */
    private OnMultiChoiceClickListener listViewMultiChoiceListener;

    /**
     * The listener, which is notified, when a list item of the dialog becomes selected,
     * irrespective of the list view's choice mode.
     */
    private OnItemSelectedListener listViewItemSelectedListener;

    /**
     * An array, which is used to identify the list items of the dialog, which is created by the
     * builder, which are by selected by default.
     */
    private boolean[] checkedListItems;

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
     * Creates and returns the layout params, which should be used by the dialog.
     *
     * @return The layout params, which should be used by the dialog, as an instance of the class
     * {@link WindowManager.LayoutParams}
     */
    private WindowManager.LayoutParams createLayoutParameters() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();

        if (getDeviceType(getContext()) == DeviceType.PHONE &&
                getOrientation(getContext()) == Orientation.PORTRAIT) {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        }

        return layoutParams;
    }

    /**
     * Inflates the dialog's layout.
     */
    private void inflateLayout() {
        rootView = (ViewGroup) View.inflate(getContext(), R.layout.material_dialog, null);
    }

    /**
     * Inflates the layout, which is used to show the dialog's title. The layout may either be the
     * default one or a custom view, if one has been set before.
     */
    private void inflateTitleView() {
        titleContainer = (ViewGroup) rootView.findViewById(R.id.title_container);
        titleContainer.removeAllViews();

        if (customTitleView != null) {
            titleContainer.addView(customTitleView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (customTitleViewId != -1) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(customTitleViewId, titleContainer, false);
            titleContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view =
                    layoutInflater.inflate(R.layout.material_dialog_title, titleContainer, false);
            titleContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        View titleView = titleContainer.findViewById(android.R.id.title);
        titleTextView = titleView instanceof TextView ? (TextView) titleView : null;
    }

    /**
     * Inflates the layout, which is used to show the dialog's content. The layout may either be the
     * default one or a custom view, if one has been set before.
     */
    private void inflateContentView() {
        messageTextView = (TextView) rootView.findViewById(android.R.id.message);
        contentRootView = (ViewGroup) rootView.findViewById(R.id.content_root);
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        contentContainer.removeAllViews();

        if (customView != null) {
            showContentContainer();
            contentContainer.addView(customView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        } else if (customViewId != -1) {
            showContentContainer();
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater.inflate(customViewId, contentContainer, false);
            contentContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater
                    .inflate(R.layout.material_dialog_list_view, contentContainer, false);
            contentContainer.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        showListView();
    }

    /**
     * Shows the parent view of the layout, which is used to show the dialog's content.
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
                (LinearLayout.LayoutParams) messageTextView.getLayoutParams();
        messageLayoutParams.bottomMargin = contentSpacing;
        messageTextView.setLayoutParams(messageLayoutParams);
    }

    /**
     * Shows the list view, which is used to show the dialog's list items.
     */
    private void showListView() {
        listView = (ListView) contentContainer.findViewById(android.R.id.list);

        if (listAdapter != null && !listAdapter.isEmpty() && listView != null) {
            showContentContainer();
            listView.setVisibility(View.VISIBLE);
            listView.setChoiceMode(listViewChoiceMode);
            listView.setAdapter(listAdapter);
            listView.setOnItemSelectedListener(listViewItemSelectedListener);
            initializeListViewSelectionListener();
            initializeListViewCheckedItems();
        }
    }

    /**
     * Initializes the list items, which are selected by default.
     */
    private void initializeListViewCheckedItems() {
        if (checkedListItems != null) {
            for (int i = 0; i < checkedListItems.length; i++) {
                listView.setItemChecked(i, checkedListItems[i]);

                if (checkedListItems[i]) {
                    listView.setSelection(i);
                }
            }
        }
    }

    /**
     * Initializes the listener, which should be notified, when the selection of a list item of the
     * dialog has been changed.
     */
    private void initializeListViewSelectionListener() {
        if (listViewChoiceMode == ListView.CHOICE_MODE_NONE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(listViewSingleChoiceListener, this,
                            DialogInterface.BUTTON_POSITIVE));
        } else if (listViewChoiceMode == ListView.CHOICE_MODE_SINGLE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(listViewSingleChoiceListener, this, 0));
        } else if (listViewChoiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            listView.setOnItemClickListener(
                    new OnMultiChoiceClickListenerWrapper(listViewMultiChoiceListener, this, 0));
        }
    }

    /**
     * Inflates the layout, which is used to show the dialog's buttons.
     */
    private void inflateButtonBar() {
        buttonBarContainer = (ViewGroup) rootView.findViewById(R.id.button_bar_container);
        buttonBarContainer.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view = layoutInflater.inflate(
                stackButtons ? R.layout.stacked_button_bar : R.layout.horizontal_button_bar,
                buttonBarContainer, false);
        buttonBarContainer.addView(view);
        positiveButton = (Button) view.findViewById(android.R.id.button1);
        negativeButton = (Button) view.findViewById(android.R.id.button2);
        neutralButton = (Button) view.findViewById(android.R.id.button3);
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
     * Adapts the button bar.
     */
    private void adaptButtonBar() {
        if (buttonBarContainer != null) {
            inflateButtonBar();
            adaptPositiveButton();
            adaptNegativeButton();
            adaptNeutralButton();
            adaptButtonTextColor();
            adaptButtonBarContainerVisibility();
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
     * Adapts the dialog's background.
     */
    @SuppressWarnings("deprecation")
    private void adaptBackground() {
        if (rootView != null) {
            rootView.setBackgroundDrawable(background);
        }
    }

    /**
     * Adapts the color of the dialog's list items.
     */
    private void adaptItemColor() {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter instanceof ArrayAdapter<?>) {
            ((ArrayAdapter<?>) listAdapter).setItemColor(itemColor);
        }
    }

    /**
     * Adapts the control color of the dialog's list items.
     */
    private void adaptItemControlColor() {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter instanceof ArrayAdapter<?>) {
            ((ArrayAdapter<?>) listAdapter).setItemControlColor(itemControlColor);
        }
    }

    /**
     * Adapts the text color of the dialog's buttons.
     */
    private void adaptButtonTextColor() {
        if (buttonTextColor != -1) {
            if (positiveButton != null) {
                positiveButton.setTextColor(buttonTextColor);
            }

            if (neutralButton != null) {
                neutralButton.setTextColor(buttonTextColor);
            }

            if (negativeButton != null) {
                negativeButton.setTextColor(buttonTextColor);
            }
        }
    }

    /**
     * Adapts the dialog's positive button.
     */
    private void adaptPositiveButton() {
        if (positiveButton != null) {
            positiveButton.setText(positiveButtonText != null ?
                    positiveButtonText.toString().toUpperCase(Locale.getDefault()) : null);
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(positiveButtonListener, true, this,
                            DialogInterface.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(onClickListener);
            positiveButton.setVisibility(
                    !TextUtils.isEmpty(positiveButtonText) ? View.VISIBLE : View.GONE);
            adaptButtonBarContainerVisibility();
        }
    }

    /**
     * Adapts the dialog's neutral button.
     */
    private void adaptNeutralButton() {
        if (neutralButton != null) {
            neutralButton.setText(neutralButtonText != null ?
                    neutralButtonText.toString().toUpperCase(Locale.getDefault()) : null);
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(neutralButtonListener, false, this,
                            DialogInterface.BUTTON_NEUTRAL);
            neutralButton.setOnClickListener(onClickListener);
            neutralButton.setVisibility(
                    !TextUtils.isEmpty(neutralButtonText) ? View.VISIBLE : View.GONE);
            adaptButtonBarContainerVisibility();
        }
    }

    /**
     * Adapts the dialog's negative button.
     */
    private void adaptNegativeButton() {
        if (negativeButton != null) {
            negativeButton.setText(negativeButtonText != null ?
                    negativeButtonText.toString().toUpperCase(Locale.getDefault()) : null);
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(negativeButtonListener, false, this,
                            DialogInterface.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(onClickListener);
            negativeButton.setVisibility(
                    !TextUtils.isEmpty(negativeButtonText) ? View.VISIBLE : View.GONE);
            adaptButtonBarContainerVisibility();
        }
    }

    /**
     * Adapts the visibility of the parent view, which contains the dialog's buttons.
     */
    private void adaptButtonBarContainerVisibility() {
        boolean show =
                !TextUtils.isEmpty(positiveButtonText) || !TextUtils.isEmpty(neutralButtonText) ||
                        !TextUtils.isEmpty(negativeButtonText);

        if (buttonBarContainer != null) {
            buttonBarContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        if (contentRootView != null) {
            int paddingLeft = show ? getContext().getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_padding_left) : 0;
            int paddingTop = show ? getContext().getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_padding_top) : 0;
            int paddingRight = show ? getContext().getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_padding_right) : 0;
            int paddingBottom = show ? getContext().getResources()
                    .getDimensionPixelSize(R.dimen.dialog_content_padding_bottom) : 0;
            contentRootView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
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
    protected MaterialDialog(@NonNull final Context context, @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        this.validators = new LinkedHashSet<>();
    }

    /**
     * Returns one of the buttons, which is shown by the dialog.
     *
     * @param whichButton
     *         The identifier of the button, which should be returned, as an {@link Integer} value.
     *         The identifier must either be {@link DialogInterface#BUTTON_POSITIVE}, {@link
     *         DialogInterface#BUTTON_NEGATIVE} or {@link DialogInterface#BUTTON_NEUTRAL}
     * @return The button, which corresponds to the given identifier, as an instance of the class
     * {@link Button} or null, if the button is not shown or if the dialog has not been shown yet
     */
    public final Button getButton(final int whichButton) {
        switch (whichButton) {
            case DialogInterface.BUTTON_POSITIVE:
                return (positiveButton != null && positiveButton.getVisibility() == View.VISIBLE) ?
                        positiveButton : null;
            case DialogInterface.BUTTON_NEGATIVE:
                return (negativeButton != null && negativeButton.getVisibility() == View.VISIBLE) ?
                        negativeButton : null;
            case DialogInterface.BUTTON_NEUTRAL:
                return (neutralButton != null && neutralButton.getVisibility() == View.VISIBLE) ?
                        neutralButton : null;
            default:
                return null;
        }
    }

    /**
     * Sets the text of the positive button of the dialog.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no positive button should be shown
     * @param listener
     *         The listener, which should be notified, when the positive button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setPositiveButton(@Nullable final CharSequence text,
                                        @Nullable final OnClickListener listener) {
        positiveButtonText = text;
        positiveButtonListener = listener;
        adaptPositiveButton();
    }

    /**
     * Sets the text of the positive button of the dialog.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the positive button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setPositiveButton(@StringRes final int resourceId,
                                        @Nullable final OnClickListener listener) {
        setPositiveButton(getContext().getText(resourceId), listener);
    }

    /**
     * Sets the text of the negative button of the dialog.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no negative button should be shown
     * @param listener
     *         The listener, which should be notified, when the negative button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setNegativeButton(@Nullable final CharSequence text,
                                        @Nullable final OnClickListener listener) {
        negativeButtonText = text;
        negativeButtonListener = listener;
        adaptNegativeButton();
    }

    /**
     * Sets the text of the negative button of the dialog.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the negative button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setNegativeButton(@StringRes final int resourceId,
                                        @Nullable final OnClickListener listener) {
        setNegativeButton(getContext().getText(resourceId), listener);
    }

    /**
     * Sets the text of the neutral button of the dialog.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no neutral button should be shown
     * @param listener
     *         The listener, which should be notified, when the neutral button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setNeutralButton(@Nullable final CharSequence text,
                                       @Nullable final OnClickListener listener) {
        neutralButtonText = text;
        neutralButtonListener = listener;
        adaptNeutralButton();
    }

    /**
     * Sets the text of the neutral button of the dialog.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the neutral button is clicked, as an
     *         instance of the type {@link OnClickListener} or null, if no listener should be
     *         notified
     */
    public final void setNeutralButton(@StringRes final int resourceId,
                                       @Nullable final OnClickListener listener) {
        setNeutralButton(getContext().getText(resourceId), listener);
    }

    /**
     * Returns, whether the buttons of the dialog are aligned vertically, or not.
     *
     * @return True, if the buttons of the dialog are aligned vertically, false otherwise
     */
    public final boolean areButtonsStacked() {
        return stackButtons;
    }

    /**
     * Sets, whether the buttons of the dialog should be aligned vertically, or not.
     *
     * @param stackButtons
     *         True, if the buttons of the dialog should be aligned vertically, false otherwise
     */
    public final void stackButtons(final boolean stackButtons) {
        this.stackButtons = stackButtons;
        adaptButtonBar();
    }

    /**
     * Returns a set, which contains the validators, which are executed when the positive button of
     * the dialog is clicked.
     *
     * @return A set, which contains the validators, which are executed when the positive button of
     * the dialog is clicked, as an instance of the type {@link Set} or an empty set, if no
     * validators are executed
     */
    public final Set<Validator> getValidators() {
        return validators;
    }

    /**
     * Adds a new validator, which should be executed when the positive button of the dialog is
     * clicked.
     *
     * @param validator
     *         The validator, which should be added, as an instance of the type {@link Validator}.
     *         The validator may not be null
     */
    public final void addValidator(@NonNull final Validator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.add(validator);
    }

    /**
     * Adds all validators, which are contained by a specific collection and should be executed when
     * the positive button of the dialog is clicked.
     *
     * @param validators
     *         A collection, which contains all validators, which should be added, as an instance of
     *         the type {@link Collection} or an empty collection, if no validators should be added
     */
    public final void addAllValidators(@NonNull final Collection<Validator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        this.validators.addAll(validators);
    }

    /**
     * Removes a specific validator, which should not be executed, when the positive button of the
     * dialog is clicked, anymore.
     *
     * @param validator
     *         The validator, which should be removed, as an instance of the type {@link Validator}.
     *         The validator may not be null
     */
    public final void removeValidator(@NonNull final Validator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.remove(validator);
    }

    /**
     * Removes all validators, which are contained by a specific collection and should not be
     * executed, when the positive button of the dialog is clicked, anymore.
     *
     * @param validators
     *         A collection, which contains the validators, which should be removed, as an instance
     *         of the type {@link Collection} or an empty collection, if no validators should be
     *         removed
     */
    public final void removeAllValidators(@NonNull final Collection<Validator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        this.validators.removeAll(validators);
    }

    /**
     * Returns the list view, which is contained by the dialog.
     *
     * @return The list view, which is contained by the dialog, as an instance of the class {@link
     * ListView} or null, if the dialog does not show any list items or has not been shown yet
     */
    public final ListView getListView() {
        return listView != null && listView.getVisibility() == View.VISIBLE ? listView : null;
    }

    /**
     * Returns the adapter of the list view, which is contained by the dialog.
     *
     * @return The adapter of the list view, which is contained by the dialog, as an instance of the
     * type {@link ListAdapter} or null, if the dialog does not show any list items
     */
    public final ListAdapter getListAdapter() {
        return listAdapter;
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
     * Returns the color of the list items of the dialog.
     *
     * @return The color of the list items of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    public final int getItemColor() {
        return itemColor;
    }

    /**
     * Sets the color of the list items of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setItemColor(@ColorInt final int color) {
        itemColor = color;
        adaptItemColor();
    }

    /**
     * Returns the control color of the list items of the dialog.
     *
     * @return The color of the list items of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    public final int getItemControlColor() {
        return itemControlColor;
    }

    /**
     * Sets the control color of the list items of the dialog.
     *
     * @param color
     *         The control color, which should be set, as an {@link Integer} value or -1, if no list
     *         items are shown or if the dialog has not been shown yet
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public final void setItemControlColor(@ColorInt final int color) {
        itemControlColor = color;
        adaptItemControlColor();
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
     * Returns the color of the button texts of the dialog.
     *
     * @return The color of the button texts of the dialog as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    public final int getButtonTextColor() {
        return buttonTextColor;
    }

    /**
     * Sets the color of the button texts of the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setButtonTextColor(@ColorInt final int color) {
        buttonTextColor = color;
        adaptButtonTextColor();
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
     * Sets the custom view, which should be shown by the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be shown
     */
    public final void setView(@Nullable final View view) {
        customView = view;
        customViewId = -1;
        listAdapter = null;
        listViewSingleChoiceListener = null;
        listViewChoiceMode = -1;
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
        listAdapter = null;
        listViewSingleChoiceListener = null;
        listViewChoiceMode = -1;
        adaptContentView();
    }

    /**
     * Sets the items, which should be shown by the dialog.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setItems(@NonNull final CharSequence[] items,
                               @Nullable final OnClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_NONE;
        customView = null;
        customViewId = -1;
        adaptContentView();
    }

    /**
     * Sets the items, which should be shown by the dialog.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setItems(@ArrayRes final int resourceId,
                               @Nullable final OnClickListener listener) {
        setItems(getContext().getResources().getTextArray(resourceId), listener);
    }

    /**
     * Sets the adapter, which provides the items, which should be shown by the dialog.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setAdapter(@NonNull final ListAdapter adapter,
                                 @Nullable final OnClickListener listener) {
        ensureNotNull(adapter, "The adapter may not be null");
        listAdapter = adapter;
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_NONE;
        customView = null;
        customViewId = -1;
        adaptContentView();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog. Only one of the items can be
     * selected at once.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setSingleChoiceItems(@NonNull final CharSequence[] items,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice,
                        items);
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
        checkedListItems = new boolean[items.length];
        checkedListItems[checkedItem] = true;
        customView = null;
        customViewId = -1;
        adaptContentView();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog. Only one of the items can be
     * selected at once.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param checkedItem
     *         The index of the item, which should be selected by default, as an {@link Integer}
     *         value or -1, if no item should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setSingleChoiceItems(@ArrayRes final int resourceId, final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        setSingleChoiceItems(getContext().getResources().getTextArray(resourceId), checkedItem,
                listener);
    }

    /**
     * Sets the adapter, which provides the selectable items, which should be shown by the dialog.
     * Only one of the items can be selected at once.
     *
     * @param adapter
     *         The adapter, which should be set, as an instance of the type {@link ListAdapter}. The
     *         adapter may not be null
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnClickListener} or null, if no listener should be notified
     */
    public final void setSingleChoiceItems(@NonNull final ListAdapter adapter,
                                           final int checkedItem,
                                           @Nullable final OnClickListener listener) {
        ensureNotNull(adapter, "The adapter may not be null");
        listAdapter = adapter;
        listViewSingleChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_SINGLE;
        checkedListItems = new boolean[adapter.getCount()];
        checkedListItems[checkedItem] = true;
        customView = null;
        customViewId = -1;
        adaptContentView();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog. Multiple items can be
     * selected at once.
     *
     * @param items
     *         The items, which should be set, as an array of the type {@link CharSequence}. The
     *         items may not be null
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnMultiChoiceClickListener} or null, if no listener should be
     *         notified
     */
    public final void setMultiChoiceItems(@NonNull final CharSequence[] items,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        ensureNotNull(items, "The items may not be null");
        listAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice,
                        items);
        listViewMultiChoiceListener = listener;
        listViewChoiceMode = ListView.CHOICE_MODE_MULTIPLE;
        checkedListItems = checkedItems;
        customView = null;
        customViewId = -1;
        adaptContentView();
    }

    /**
     * Sets the selectable items, which should be shown by the dialog. Multiple items can be
     * selected at once.
     *
     * @param resourceId
     *         The resource id of the items, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid array resource
     * @param checkedItems
     *         An array, which contains, whether the items, which correspond to the corresponding
     *         indices, should be selected by default, or not, as a {@link Boolean} array or null,
     *         if no items should be selected by default
     * @param listener
     *         The listener, which should be notified, when an item is clicked, as an instance of
     *         the type {@link OnMultiChoiceClickListener} or null, if no listener should be
     *         notified
     */
    public final void setMultiChoiceItems(@ArrayRes final int resourceId,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final OnMultiChoiceClickListener listener) {
        setMultiChoiceItems(getContext().getResources().getTextArray(resourceId), checkedItems,
                listener);
    }

    /**
     * Sets the listener, which should be notified, when an item, which is shown by the dialog is
     * selected.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         OnItemSelectedListener} or null, if no listener should be notified
     */
    public final void setOnItemSelectedListener(@Nullable final OnItemSelectedListener listener) {
        listViewItemSelectedListener = listener;

        if (listView != null) {
            listView.setOnItemSelectedListener(listViewItemSelectedListener);
        }
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

    @Override
    public final void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setAttributes(createLayoutParameters());
        inflateLayout();
        inflateTitleView();
        inflateContentView();
        inflateButtonBar();
        setContentView(rootView);
        adaptTitle();
        adaptTitleColor();
        adaptIcon();
        adaptMessage();
        adaptMessageColor();
        adaptBackground();
        adaptItemColor();
        adaptItemControlColor();
        adaptButtonTextColor();
        adaptPositiveButton();
        adaptNeutralButton();
        adaptNegativeButton();
    }

    @Override
    public final void onStop() {
        super.onStop();
        rootView = null;
        titleContainer = null;
        titleTextView = null;
        messageTextView = null;
        contentRootView = null;
        contentContainer = null;
        listView = null;
        buttonBarContainer = null;
        positiveButton = null;
        negativeButton = null;
        neutralButton = null;
    }

}