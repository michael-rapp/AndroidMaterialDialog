/*
 * Copyright 2014 - 2019 Michael Rapp
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.listener.OnClickListenerWrapper;
import de.mrapp.android.dialog.model.ValidateableDialog;
import de.mrapp.android.dialog.view.DialogRootView.AreaViewType;
import de.mrapp.android.dialog.view.DialogRootView.DividerLocation;
import de.mrapp.android.dialog.view.DialogRootView.DividerViewType;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;
import de.mrapp.android.dialog.view.Divider;
import de.mrapp.util.Condition;

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
     * The name of the extra, which is used to store, whether the divider, which is shown above the
     * dialog's buttons, should be shown, or not, within a bundle.
     */
    private static final String SHOW_BUTTON_BAR_DIVIDER_EXTRA =
            ButtonBarDialogDecorator.class.getSimpleName() + "::showButtonBarDivider";

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
    private Divider buttonBarDivider;

    /**
     * The text color of the dialog's buttons.
     */
    private ColorStateList buttonTextColor;

    /**
     * The typeface of the dialog's buttons.
     */
    private Typeface buttonTypeface;

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
     * The resource id of the custom button bar of the dialog.
     */
    private int customButtonBarViewId = -1;

    /**
     * The custom button bar of the dialog.
     */
    private View customButtonBarView;

    /**
     * Inflates the layout, which is used to show the dialog's buttons.
     *
     * @return The view, which has been inflated, as an instance of the class {@link View} or null,
     * if no view has been inflated
     */
    private View inflateButtonBar() {
        if (getRootView() != null) {
            if (buttonBarContainer == null) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                buttonBarContainer = (ViewGroup) layoutInflater
                        .inflate(R.layout.button_bar_container, getRootView(), false);
                buttonBarDivider = buttonBarContainer.findViewById(R.id.button_bar_divider);
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
            return buttonBarContainer;
        }

        return null;
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
            adaptButtonTypeface();
            adaptButtonBarContainerVisibility();
            adaptButtonBarDividerVisibility();
        }
    }

    /**
     * Adapts the text color of the dialog's buttons.
     */
    private void adaptButtonTextColor() {
        if (buttonTextColor != null) {
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
     * Adapts the typeface of the dialog's buttons.
     */
    private void adaptButtonTypeface() {
        if (buttonTypeface != null) {
            if (positiveButton != null) {
                positiveButton.setTypeface(buttonTypeface);
            }

            if (neutralButton != null) {
                neutralButton.setTypeface(buttonTypeface);
            }

            if (negativeButton != null) {
                negativeButton.setTypeface(buttonTypeface);
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
                    new OnClickListenerWrapper(positiveButtonListener, true, getDialog(),
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
                    new OnClickListenerWrapper(neutralButtonListener, false, getDialog(),
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
                    new OnClickListenerWrapper(negativeButtonListener, false, getDialog(),
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
            buttonBarDivider.setVisibleByDefault(showButtonBarDivider);
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

    @Nullable
    @Override
    public final ColorStateList getButtonTextColor() {
        return buttonTextColor;
    }

    @Override
    public final void setButtonTextColor(@ColorInt final int color) {
        setButtonTextColor(ColorStateList.valueOf(color));
    }

    @Override
    public final void setButtonTextColor(@NonNull final ColorStateList colorStateList) {
        Condition.INSTANCE.ensureNotNull(colorStateList, "The color state list may not be null");
        buttonTextColor = colorStateList;
        adaptButtonTextColor();
    }

    @Nullable
    @Override
    public final Typeface getButtonTypeface() {
        return buttonTypeface;
    }

    @Override
    public final void setButtonTypeface(@NonNull final Typeface typeface) {
        Condition.INSTANCE.ensureNotNull(typeface, "The typeface may not be null");
        buttonTypeface = typeface;
        adaptButtonTypeface();
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
    public final boolean isCustomButtonBarUsed() {
        return customButtonBarView != null || customButtonBarViewId != -1;
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
        outState.putParcelable(BUTTON_TEXT_COLOR_EXTRA, getButtonTextColor());
        outState.putBoolean(SHOW_BUTTON_BAR_DIVIDER_EXTRA, isButtonBarDividerShown());
        outState.putCharSequence(POSITIVE_BUTTON_TEXT_EXTRA, positiveButtonText);
        outState.putCharSequence(NEUTRAL_BUTTON_TEXT_EXTRA, neutralButtonText);
        outState.putCharSequence(NEGATIVE_BUTTON_TEXT_EXTRA, negativeButtonText);
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        stackButtons(savedInstanceState.getBoolean(STACK_BUTTONS_EXTRA));
        showButtonBarDivider(savedInstanceState.getBoolean(SHOW_BUTTON_BAR_DIVIDER_EXTRA));
        setPositiveButton(savedInstanceState.getCharSequence(POSITIVE_BUTTON_TEXT_EXTRA),
                positiveButtonListener);
        setNeutralButton(savedInstanceState.getCharSequence(NEUTRAL_BUTTON_TEXT_EXTRA),
                neutralButtonListener);
        setNegativeButton(savedInstanceState.getCharSequence(NEGATIVE_BUTTON_TEXT_EXTRA),
                negativeButtonListener);
        ColorStateList buttonTextColor = savedInstanceState.getParcelable(BUTTON_TEXT_COLOR_EXTRA);

        if (buttonTextColor != null) {
            setButtonTextColor(buttonTextColor);
        }
    }

    @NonNull
    @Override
    protected final Map<ViewType, View> onAttach(@NonNull final Window window,
                                                 @NonNull final View view,
                                                 @NonNull final Map<ViewType, View> areas,
                                                 final Void param) {
        View inflatedView = inflateButtonBar();

        if (inflatedView != null) {
            adaptButtonTextColor();
            adaptButtonTypeface();
            adaptPositiveButton();
            adaptNeutralButton();
            adaptNegativeButton();
            adaptButtonBarDividerVisibility();
            Map<ViewType, View> result = new HashMap<>();
            result.put(new AreaViewType(Area.BUTTON_BAR), buttonBarContainer);
            result.put(new DividerViewType(DividerLocation.BOTTOM), buttonBarDivider);
            return result;
        }

        return Collections.emptyMap();
    }

    @Override
    protected final void onDetach() {
        if (buttonBarContainer != null) {
            buttonBarContainer.removeAllViews();
            buttonBarContainer = null;
        }

        positiveButton = null;
        negativeButton = null;
        neutralButton = null;
        buttonBarDivider = null;
    }

}