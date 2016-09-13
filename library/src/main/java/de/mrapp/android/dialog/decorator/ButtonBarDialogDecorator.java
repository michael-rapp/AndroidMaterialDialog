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

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Locale;

import de.mrapp.android.dialog.MaterialDialogConfig;
import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.listener.OnClickListenerWrapper;
import de.mrapp.android.dialog.model.ValidateableDialog;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material design guidelines even on pre-Lollipop devices and may contain up to
 * three buttons.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ButtonBarDialogDecorator extends AbstractDialogDecorator<ValidateableDialog>
        implements de.mrapp.android.dialog.model.ButtonBarDialogDecorator {

    /**
     * The name of the extra, which is used to store, whether the dialog's buttons should be
     * stacked, or not, within a bundle.
     */
    private static final String STACK_BUTTONS_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::stackButtons";

    /**
     * The name of the extra, which is used to store the text color of the dialog's buttons within a
     * bundle.
     */
    private static final String BUTTON_TEXT_COLOR_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::buttonTextColor";

    /**
     * The name of the extra, which is used to store the disabled text color of the dialog's buttons
     * within a bundle.
     */
    private static final String DISABLED_BUTTON_TEXT_COLOR_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::disabledButtonTextColor";

    /**
     * The name of the extra, which is used to store, whether the divider, which is shown above the
     * dialog's buttons, should be shown, or not, within a bundle.
     */
    private static final String SHOW_BUTTON_BAR_DIVIDER_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::showButtonBarDivider";

    /**
     * The name of the extra, which is used to store the color of the divider, which is shown above
     * the dialog's buttons, within a bundle.
     */
    private static final String BUTTON_BAR_DIVIDER_COLOR_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::buttonBarDividerColor";

    /**
     * The name of the extra, which is used to store the text of the positive button within a
     * bundle.
     */
    private static final String POSITIVE_BUTTON_TEXT_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::positiveButtonText";

    /**
     * The name of the extra, which is used to store the text of the neutral button within a
     * bundle.
     */
    private static final String NEUTRAL_BUTTON_TEXT_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::neutralButtonText";

    /**
     * The name of the extra, which is used to store the text of the negative button within a
     * bundle.
     */
    private static final String NEGATIVE_BUTTON_TEXT_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::negativeButtonText";

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
     * The divider, which is shown above the dialog's custom view.
     */
    private View contentDivider;

    /**
     * The text color of the dialog's buttons.
     */
    private int buttonTextColor;

    /**
     * The text color of the dialog's buttons when disabled.
     */
    private int disabledButtonTextColor;

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
    private DialogInterface.OnClickListener negativeButtonListener;

    /**
     * The listener, which is notified, when the neutral button of the dialog, is clicked.
     */
    private DialogInterface.OnClickListener neutralButtonListener;

    /**
     * The listener, which is notified, when the positive button of the dialog, is clicked.
     */
    private DialogInterface.OnClickListener positiveButtonListener;

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
     * The resource id of the custom button bar of the dialog.
     */
    private int customButtonBarViewId = -1;

    /**
     * The custom button bar of the dialog.
     */
    private View customButtonBarView;

    /**
     * Inflates the layout, which is used to show the dialog's buttons.
     */
    private void inflateButtonBar() {
        ViewGroup rootView = (ViewGroup) getView();

        if (rootView != null) {
            if (buttonBarContainer == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                buttonBarContainer = (ViewGroup) layoutInflater
                        .inflate(R.layout.button_bar_container, rootView, false);
                buttonBarDivider = buttonBarContainer.findViewById(R.id.button_bar_divider);
                rootView.addView(buttonBarContainer);
            }

            if (buttonBarContainer.getChildCount() > 1) {
                buttonBarContainer.removeViewAt(1);
            }

            if (customButtonBarView != null) {
                buttonBarContainer.addView(customButtonBarView);
            } else if (customButtonBarViewId != -1) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view =
                        layoutInflater.inflate(customButtonBarViewId, buttonBarContainer, false);
                buttonBarContainer.addView(view);
            } else {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View view = layoutInflater.inflate(
                        stackButtons ? R.layout.stacked_button_bar : R.layout.horizontal_button_bar,
                        buttonBarContainer, false);
                buttonBarContainer.addView(view);
            }

            View positiveButtonView = buttonBarContainer.findViewById(android.R.id.button1);
            View negativeButtonView = buttonBarContainer.findViewById(android.R.id.button2);
            View neutralButtonView = buttonBarContainer.findViewById(android.R.id.button3);
            positiveButton =
                    positiveButtonView instanceof Button ? (Button) positiveButtonView : null;
            negativeButton =
                    negativeButtonView instanceof Button ? (Button) negativeButtonView : null;
            neutralButton = neutralButtonView instanceof Button ? (Button) neutralButtonView : null;
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
            adaptButtonBarDividerVisibility();
            adaptButtonBarDividerColor();
        }
    }

    /**
     * Adapts the text color of the dialog's buttons.
     */
    private void adaptButtonTextColor() {
        int[][] states = new int[][]{new int[]{-android.R.attr.state_enabled}, new int[]{}};
        int[] colors = new int[]{disabledButtonTextColor, buttonTextColor};
        ColorStateList colorStateList = new ColorStateList(states, colors);

        if (positiveButton != null) {
            positiveButton.setTextColor(colorStateList);
        }

        if (neutralButton != null) {
            neutralButton.setTextColor(colorStateList);
        }

        if (negativeButton != null) {
            negativeButton.setTextColor(colorStateList);
        }
    }

    /**
     * Adapt a specific button
     *
     * @param button
     *         the button to adapt
     * @param buttonText
     *         text displayed on the button
     * @param listener
     *         listener of the button
     * @param identifier
     *         type of button e.g. {@link DialogInterface#BUTTON_POSITIVE},
     *         {@link DialogInterface#BUTTON_NEGATIVE}, or {@link DialogInterface#BUTTON_NEUTRAL}.
     */
    private void adaptButton(Button button, CharSequence buttonText,
                             DialogInterface.OnClickListener listener, final int identifier) {
        if (button != null) {
            Typeface buttonFont = MaterialDialogConfig.getButtonFont();
            if (buttonFont != null) {
                button.setTypeface(buttonFont);
            }

            button.setText(buttonText != null ?
                    buttonText.toString().toUpperCase(Locale.getDefault()) : null);
            OnClickListenerWrapper onClickListener =
                    new OnClickListenerWrapper(listener, true, getDialog(),
                            DialogInterface.BUTTON_POSITIVE);
            button.setOnClickListener(onClickListener);
            button.setVisibility(!TextUtils.isEmpty(buttonText) ? View.VISIBLE : View.GONE);

            adaptButtonBarContainerVisibility();
        }
    }

    /**
     * Adapts the dialog's positive button.
     */
    private void adaptPositiveButton() {
        adaptButton(positiveButton, positiveButtonText, positiveButtonListener,
                DialogInterface.BUTTON_POSITIVE);
    }

    /**
     * Adapts the dialog's neutral button.
     */
    private void adaptNeutralButton() {
        adaptButton(neutralButton, neutralButtonText, neutralButtonListener,
                DialogInterface.BUTTON_NEUTRAL);
    }

    /**
     * Adapts the dialog's negative button.
     */
    private void adaptNegativeButton() {
        adaptButton(negativeButton, negativeButtonText, negativeButtonListener,
                DialogInterface.BUTTON_NEGATIVE);
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

        if (contentDivider != null) {
            contentDivider.setBackgroundColor(buttonBarDividerColor);
        }
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material design guidelines even on pre-Lollipop devices and
     * may contain up to three buttons.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the type {@link ValidateableDialog}. The dialog may not be null
     */
    public ButtonBarDialogDecorator(@NonNull final ValidateableDialog dialog) {
        super(dialog);
    }

    @Override
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

    @Override
    public final void setPositiveButton(@Nullable final CharSequence text,
                                        @Nullable final DialogInterface.OnClickListener listener) {
        positiveButtonText = text;
        positiveButtonListener = listener;
        adaptPositiveButton();
    }

    @Override
    public final void setPositiveButton(@StringRes final int resourceId,
                                        @Nullable final DialogInterface.OnClickListener listener) {
        setPositiveButton(getContext().getText(resourceId), listener);
    }

    @Override
    public final void setNegativeButton(@Nullable final CharSequence text,
                                        @Nullable final DialogInterface.OnClickListener listener) {
        negativeButtonText = text;
        negativeButtonListener = listener;
        adaptNegativeButton();
    }

    @Override
    public final void setNegativeButton(@StringRes final int resourceId,
                                        @Nullable final DialogInterface.OnClickListener listener) {
        setNegativeButton(getContext().getText(resourceId), listener);
    }

    @Override
    public final void setNeutralButton(@Nullable final CharSequence text,
                                       @Nullable final DialogInterface.OnClickListener listener) {
        neutralButtonText = text;
        neutralButtonListener = listener;
        adaptNeutralButton();
    }

    @Override
    public final void setNeutralButton(@StringRes final int resourceId,
                                       @Nullable final DialogInterface.OnClickListener listener) {
        setNeutralButton(getContext().getText(resourceId), listener);
    }

    @Override
    public final boolean areButtonsStacked() {
        return stackButtons;
    }

    @Override
    public final void stackButtons(final boolean stackButtons) {
        this.stackButtons = stackButtons;
        adaptButtonBar();
    }

    @Override
    public final int getButtonTextColor() {
        return buttonTextColor;
    }

    @Override
    public final void setButtonTextColor(@ColorInt final int color) {
        buttonTextColor = color;
        adaptButtonTextColor();
    }

    @Override
    public final int getDisabledButtonTextColor() {
        return disabledButtonTextColor;
    }

    @Override
    public final void setDisabledButtonTextColor(@ColorInt final int color) {
        disabledButtonTextColor = color;
        adaptButtonTextColor();
    }

    @Override
    public final boolean isButtonBarDividerShown() {
        return showButtonBarDivider;
    }

    @Override
    public final void showButtonBarDivider(final boolean show) {
        this.showButtonBarDivider = show;
        adaptButtonBarDividerVisibility();
    }

    @Override
    public final int getButtonBarDividerColor() {
        return buttonBarDividerColor;
    }

    @Override
    public final void setButtonBarDividerColor(final int color) {
        this.buttonBarDividerColor = color;
        adaptButtonBarDividerColor();
    }

    @Override
    public final void setCustomButtonBar(@LayoutRes final int resourceId) {
        customButtonBarView = null;
        customButtonBarViewId = resourceId;
        adaptButtonBar();
    }

    @Override
    public final void setCustomButtonBar(@Nullable final View view) {
        customButtonBarView = view;
        customButtonBarViewId = -1;
        adaptButtonBar();
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putBoolean(STACK_BUTTONS_EXTRA, areButtonsStacked());
        outState.putInt(BUTTON_TEXT_COLOR_EXTRA, getButtonTextColor());
        outState.putInt(DISABLED_BUTTON_TEXT_COLOR_EXTRA, getDisabledButtonTextColor());
        outState.putBoolean(SHOW_BUTTON_BAR_DIVIDER_EXTRA, isButtonBarDividerShown());
        outState.putInt(BUTTON_BAR_DIVIDER_COLOR_EXTRA, getButtonBarDividerColor());
        outState.putCharSequence(POSITIVE_BUTTON_TEXT_EXTRA, positiveButtonText);
        outState.putCharSequence(NEUTRAL_BUTTON_TEXT_EXTRA, neutralButtonText);
        outState.putCharSequence(NEGATIVE_BUTTON_TEXT_EXTRA, negativeButtonText);
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        stackButtons(savedInstanceState.getBoolean(STACK_BUTTONS_EXTRA));
        setButtonTextColor(savedInstanceState.getInt(BUTTON_TEXT_COLOR_EXTRA));
        setDisabledButtonTextColor(savedInstanceState.getInt(DISABLED_BUTTON_TEXT_COLOR_EXTRA));
        showButtonBarDivider(savedInstanceState.getBoolean(SHOW_BUTTON_BAR_DIVIDER_EXTRA));
        setButtonBarDividerColor(savedInstanceState.getInt(BUTTON_BAR_DIVIDER_COLOR_EXTRA));
        setPositiveButton(savedInstanceState.getCharSequence(POSITIVE_BUTTON_TEXT_EXTRA), null);
        setNeutralButton(savedInstanceState.getCharSequence(NEUTRAL_BUTTON_TEXT_EXTRA), null);
        setNegativeButton(savedInstanceState.getCharSequence(NEGATIVE_BUTTON_TEXT_EXTRA), null);
    }

    @Override
    protected final void onAttach(@NonNull final View view) {
        contentDivider = view.findViewById(R.id.content_divider);
        inflateButtonBar();
        adaptButtonTextColor();
        adaptPositiveButton();
        adaptNeutralButton();
        adaptNegativeButton();
        adaptButtonBarDividerVisibility();
        adaptButtonBarDividerColor();
    }

    @Override
    protected final void onDetach() {
        contentDivider = null;
        buttonBarContainer = null;
        positiveButton = null;
        negativeButton = null;
        neutralButton = null;
        buttonBarDivider = null;
    }

}