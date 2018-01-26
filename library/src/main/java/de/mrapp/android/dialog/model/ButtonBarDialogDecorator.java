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
package de.mrapp.android.dialog.model;

import android.content.DialogInterface;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;

/**
 * Defines the interface, a decorator, which allows to modify the view hierarchy of a dialog, which
 * is designed according to Android 5's Material design guidelines even on pre-Lollipop devices and
 * may contain up to three buttons, must implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface ButtonBarDialogDecorator extends Dialog {

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
    Button getButton(int whichButton);

    /**
     * Sets the text of the positive button of the dialog.
     *
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no positive button should be shown
     * @param listener
     *         The listener, which should be notified, when the positive button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     */
    void setPositiveButton(@Nullable CharSequence text,
                           @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the text of the positive button of the dialog.
     *
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the positive button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     */
    void setPositiveButton(@StringRes int resourceId,
                           @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the text of the negative button of the dialog.
     *
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no negative button should be shown
     * @param listener
     *         The listener, which should be notified, when the negative button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     */
    void setNegativeButton(@Nullable CharSequence text,
                           @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the text of the negative button of the dialog.
     *
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the negative button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     */
    void setNegativeButton(@StringRes int resourceId,
                           @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the text of the neutral button of the dialog.
     *
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no neutral button should be shown
     * @param listener
     *         The listener, which should be notified, when the neutral button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     */
    void setNeutralButton(@Nullable CharSequence text,
                          @Nullable DialogInterface.OnClickListener listener);

    /**
     * Sets the text of the neutral button of the dialog.
     *
     * Note, that the attached listener is not stored using a dialog's
     * <code>onSaveInstanceState</code>-method, because it is not serializable. Therefore this
     * method must be called again after configuration changes, e.g when the orientation of the
     * device has changed, in order to re-register the listener.
     *
     * @param resourceId
     *         The resource id of the text, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     * @param listener
     *         The listener, which should be notified, when the neutral button is clicked, as an
     *         instance of the type {@link DialogInterface.OnClickListener} or null, if no listener
     *         should be notified
     */
    void setNeutralButton(@StringRes int resourceId,
                          @Nullable DialogInterface.OnClickListener listener);

    /**
     * Returns, whether the buttons of the dialog are aligned vertically, or not.
     *
     * @return True, if the buttons of the dialog are aligned vertically, false otherwise
     */
    boolean areButtonsStacked();

    /**
     * Sets, whether the buttons of the dialog should be aligned vertically, or not.
     *
     * @param stackButtons
     *         True, if the buttons of the dialog should be aligned vertically, false otherwise
     */
    void stackButtons(boolean stackButtons);

    /**
     * Returns the text color of the dialog's buttons.
     *
     * @return The text color of dialog's buttons as an {@link Integer} value
     */
    int getButtonTextColor();

    /**
     * Sets the text color of the dialog's buttons.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setButtonTextColor(@ColorInt int color);

    /**
     * Returns the text color the dialog's buttons when disabled.
     *
     * @return The text color of the dialog's buttons when disabled as an {@link Integer} value
     */
    int getDisabledButtonTextColor();

    /**
     * Sets the text color of the dialog's buttons when disabled.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setDisabledButtonTextColor(@ColorInt int color);

    /**
     * Returns, whether the divider, which is located above the dialog's buttons, is shown, or not.
     *
     * @return True, if the divider, which is located above the dialog's buttons, is shown, false
     * otherwise
     */
    boolean isButtonBarDividerShown();

    /**
     * Sets, whether the divider, which is located above the dialog's buttons, should be shown, or
     * not.
     *
     * @param show
     *         True, if the divider, which is located above the dialog's buttons, should be show,
     *         false otherwise
     */
    void showButtonBarDivider(boolean show);

    /**
     * Returns, whether a custom view is set for showing the buttons of the dialog, or not.
     *
     * @return True, if a custom view is set for showing the buttons of the dialog, false otherwise
     */
    boolean isCustomButtonBarUsed();

    /**
     * Sets the custom view, which should be used to show the buttons of the dialog.
     *
     * @param resourceId
     *         The resource id of the view, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid layout resource
     */
    void setCustomButtonBar(@LayoutRes int resourceId);

    /**
     * Sets the custom view, which should be used to show the buttons of the dialog.
     *
     * @param view
     *         The view, which should be set, as an instance of the class {@link View} or null, if
     *         no custom view should be used to show the title
     */
    void setCustomButtonBar(@Nullable View view);

}