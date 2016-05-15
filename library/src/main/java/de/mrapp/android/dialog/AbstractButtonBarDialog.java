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
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;

import de.mrapp.android.dialog.listener.OnClickListenerWrapper;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices and may contain up to three buttons.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractButtonBarDialog extends AbstractValidateableDialog
        implements DialogInterface {

    /**
     * An abstract base class for all builders, which allow to create and show dialogs, which are
     * designed according to Android Material Design guidelines even on pre-Lollipop devices and may
     * contain up to three buttons.
     *
     * @param <DialogType>
     *         The type of the dialog, which is created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    public static abstract class AbstractBuilder<DialogType extends AbstractButtonBarDialog, BuilderType extends AbstractBuilder<DialogType, ?>>
            extends AbstractValidateableDialog.AbstractBuilder<DialogType, BuilderType> {

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
         * Obtains, whether the divider, which is located above the dialog's buttons, should be
         * shown, or not, from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the visibility should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainShowButtonBarDivider(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogShowButtonBarDivider});
            showButtonBarDivider(typedArray.getBoolean(0, false));
        }

        /**
         * Obtains the color of the divider, which is located above the dialog's buttons, from a
         * specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the color should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainButtonBarDividerColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogButtonBarDividerColor});
            int defaultColor =
                    ContextCompat.getColor(getContext(), R.color.button_bar_divider_color_light);
            setButtonBarDividerColor(typedArray.getColor(0, defaultColor));
        }

        /**
         * Creates a new builder, which allows to create dialogs, which allow to create and show
         * dialogs, which are designed according to Android 5's Material Design guidelines even on
         * pre-Lollipop devices and may contain up to three buttons.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public AbstractBuilder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which allow to create and show
         * dialogs, which are designed according to Android 5's Material Design guidelines even on
         * pre-Lollipop devices and may contain up to three buttons.
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
            super(context, themeResourceId);
        }

        /**
         * Sets the color of the button texts of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         button color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setButtonTextColor(@ColorInt final int color) {
            getDialog().setButtonTextColor(color);
            return self();
        }

        /**
         * Sets, whether the buttons of the dialog, which is created by the builder, should be
         * aligned vertically, or not.
         *
         * @param stackButtons
         *         True, if the buttons of the dialog, which is created by the builder, should be
         *         aligned vertically, false otherwise
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType stackButtons(final boolean stackButtons) {
            getDialog().stackButtons(stackButtons);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setNegativeButton(@Nullable final CharSequence text,
                                                   @Nullable final OnClickListener listener) {
            getDialog().setNegativeButton(text, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setNegativeButton(@StringRes final int resourceId,
                                                   @Nullable final OnClickListener listener) {
            getDialog().setNegativeButton(resourceId, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setPositiveButton(@Nullable final CharSequence text,
                                                   @Nullable final OnClickListener listener) {
            getDialog().setPositiveButton(text, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setPositiveButton(@StringRes final int resourceId,
                                                   @Nullable final OnClickListener listener) {
            getDialog().setPositiveButton(resourceId, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setNeutralButton(@Nullable final CharSequence text,
                                                  @Nullable final OnClickListener listener) {
            getDialog().setNeutralButton(text, listener);
            return self();
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
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setNeutralButton(@StringRes final int resourceId,
                                                  @Nullable final OnClickListener listener) {
            getDialog().setNeutralButton(resourceId, listener);
            return self();
        }

        /**
         * Sets, whether the divider, which is located above the buttons of the dialog, which is
         * created by the builder, should be shown, or not.
         *
         * @param show
         *         True, if the divider, which is located above the dialog's buttons, should be
         *         show, false otherwise
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType showButtonBarDivider(final boolean show) {
            getDialog().showButtonBarDivider(show);
            return self();
        }

        /**
         * Sets the color of the divider, which is located above the buttons of the dialog, which is
         * created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setButtonBarDividerColor(final int color) {
            getDialog().setButtonBarDividerColor(color);
            return self();
        }

        @CallSuper
        @Override
        protected void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            super.obtainStyledAttributes(themeResourceId);
            obtainButtonTextColor(themeResourceId);
            obtainShowButtonBarDivider(themeResourceId);
            obtainButtonBarDividerColor(themeResourceId);
        }

    }

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
     * The divider, which is shown above the dialog's buttons.
     */
    private View buttonBarDivider;

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
     * True, if the divider, which is located above the dialog's buttons, is shown, false
     * otherwise.
     */
    private boolean showButtonBarDivider;

    /**
     * The color of the divider, which is located above the dialog's buttons.
     */
    private int buttonBarDividerColor;

    /**
     * Inflates the layout, which is used to show the dialog's buttons.
     */
    private void inflateButtonBar() {
        ViewGroup rootView = getRootView();
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        buttonBarContainer =
                (ViewGroup) layoutInflater.inflate(R.layout.button_bar_container, rootView, false);
        rootView.addView(buttonBarContainer);
        View view = layoutInflater.inflate(
                stackButtons ? R.layout.stacked_button_bar : R.layout.horizontal_button_bar,
                buttonBarContainer, false);
        buttonBarContainer.addView(view);
        positiveButton = (Button) view.findViewById(android.R.id.button1);
        negativeButton = (Button) view.findViewById(android.R.id.button2);
        neutralButton = (Button) view.findViewById(android.R.id.button3);
        buttonBarDivider = view.findViewById(R.id.button_bar_divider);
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
            adaptButtonBarDividerVisibility();
            adaptButtonBarDividerColor();
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
        if (buttonBarContainer != null) {
            if (TextUtils.isEmpty(positiveButtonText) && TextUtils.isEmpty(neutralButtonText) &&
                    TextUtils.isEmpty(negativeButtonText)) {
                buttonBarContainer.setVisibility(View.GONE);
            } else {
                buttonBarContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Adapts the visibility of the divider, which is shown above the dialog's buttons.
     */
    private void adaptButtonBarDividerVisibility() {
        if (buttonBarDivider != null) {
            buttonBarDivider.setVisibility(showButtonBarDivider ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * Adapts the color of the divider, which is shown above the dialog's buttons.
     */
    private void adaptButtonBarDividerColor() {
        if (buttonBarDivider != null) {
            buttonBarDivider.setBackgroundColor(buttonBarDividerColor);
        }
    }

    /**
     * Returns the parent view of the layout, which is used to show the dialog's buttons.
     *
     * @return The parent view of the layout, which is used to show the dialog's buttons, as an
     * instance of the class {@link ViewGroup}
     */
    protected ViewGroup getButtonBarContainer() {
        return buttonBarContainer;
    }

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and may contain up to three buttons.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractButtonBarDialog(@NonNull final Context context,
                                      @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
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
     * Returns, whether the divider, which is located above the dialog's buttons, is shown, or not.
     *
     * @return True, if the divider, which is located above the dialog's buttons, is shown, false
     * otherwise
     */
    public final boolean isButtonBarDividerShown() {
        return showButtonBarDivider;
    }

    /**
     * Sets, whether the divider, which is located above the dialog's buttons, should be shown, or
     * not.
     *
     * @param show
     *         True, if the divider, which is located above the dialog's buttons, should be show,
     *         false otherwise
     */
    public final void showButtonBarDivider(final boolean show) {
        this.showButtonBarDivider = show;
        adaptButtonBarDividerVisibility();
    }

    /**
     * Returns the color of the divider, which is located above the dialog's buttons.
     *
     * @return The color of the divider, which is located above the dialog's buttons, as an {@link
     * Integer} value
     */
    public final int getButtonBarDividerColor() {
        return buttonBarDividerColor;
    }

    /**
     * Sets the color of the divider, which is located above the dialog's buttons.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setButtonBarDividerColor(final int color) {
        this.buttonBarDividerColor = color;
        adaptButtonBarDividerColor();
    }

    @CallSuper
    @Override
    public void onStart() {
        super.onStart();
        inflateButtonBar();
        adaptButtonTextColor();
        adaptPositiveButton();
        adaptNeutralButton();
        adaptNegativeButton();
        adaptButtonBarDividerVisibility();
        adaptButtonBarDividerColor();
    }

    @CallSuper
    @Override
    public void onStop() {
        super.onStop();
        buttonBarContainer = null;
        positiveButton = null;
        negativeButton = null;
        neutralButton = null;
        buttonBarDivider = null;
    }

}