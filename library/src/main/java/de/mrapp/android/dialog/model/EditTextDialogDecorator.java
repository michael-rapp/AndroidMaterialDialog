/*
 * Copyright 2014 - 2020 Michael Rapp
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

import android.content.res.ColorStateList;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import de.mrapp.android.validation.Validateable;

/**
 * Defines the interface, a decorator, which allows to modify the view hierarchy of a dialog, which
 * is designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
 * contains an {@link EditText} widget.
 *
 * @author Michael Rapp
 * @since 5.1.0
 */
public interface EditTextDialogDecorator extends Dialog, Validateable<CharSequence> {

    /**
     * Returns the text input layout, which is contained by the dialog.
     *
     * @return The text input layout, which is contained by the dialog, as an instance of the class
     * TextInputLayout or null, if the dialog has not been shown yet
     */
    TextInputLayout getTextInputLayout();

    /**
     * Returns the edit text widget, which is contained by the dialog.
     *
     * @return The edit text widget, which is contained by the dialog, as an instance of the class
     * {@link EditText} or null, if the dialog has not been shown yet
     */
    EditText getEditText();

    /**
     * Returns the text of the edit text widget, which is contained by the dialog.
     *
     * @return The text of the edit text widget, which is contained by the dialog, as an instance of
     * the type {@link CharSequence}
     */
    CharSequence getText();

    /**
     * Sets the text of the edit text widget, which is contained by the dialog.
     *
     * @param text
     *         The text, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no text should be set
     */
    void setText(@Nullable CharSequence text);

    /**
     * Returns the hint of the edit text widget, which is contained by the dialog.
     *
     * @return The hint of the edit text widget, which is contained by the dialog, as an instance of
     * the type {@link CharSequence} or null, if no hint is set
     */
    CharSequence getHint();

    /**
     * Sets the hint of the edit text widget, which is contained by the dialog.
     *
     * @param hint
     *         The hint, which should be set, as an instance of the type {@link CharSequence} or
     *         null, if no hint should be set
     */
    void setHint(@Nullable CharSequence hint);

    /**
     * Sets the hint of the edit text widget, which is contained by the dialog.
     *
     * @param resourceId
     *         The resource id of the hint, which should be set, as an {@link Integer} value. The
     *         resource id must correspond to a valid string resource
     */
    void setHint(@StringRes int resourceId);

    /**
     * Returns the helper text of the edit text widget, which is contained by the dialog.
     *
     * @return The helper text of the edit text widget, which is contained by the dialog, as an
     * instance of the type {@link CharSequence} or null, if no helper text is shown
     */
    CharSequence getHelperText();

    /**
     * Sets the helper text of the edit text widget, which is contained by the dialog.
     *
     * @param helperText
     *         The helper text, which should be set, as an instance of the type {@link CharSequence}
     *         or null, if no helper text should be shown
     */
    void setHelperText(@Nullable CharSequence helperText);

    /**
     * Sets the helper text of the edit text widget, which is contained by the dialog.
     *
     * @param resourceId
     *         The resource ID of the string resource, which contains the helper text, which should
     *         be set, as an {@link Integer} value. The resource ID must correspond to a valid
     *         string resource
     */
    void setHelperText(@StringRes int resourceId);

    /**
     * Returns the color, which is used to indicate validation errors.
     *
     * @return The color, which is used to indicate validation errors, as an instance of the class
     * {@link ColorStateList} or null, if the default color is used
     */
    ColorStateList getErrorColor();

    /**
     * Sets the color, which should be used to indicate validation errors.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setErrorColor(@ColorInt int color);

    /**
     * Sets the color, which should be used to indicate validation errors.
     *
     * @param colorStateList
     *         The color, which should be set, as an instance of the class {@link ColorStateList}.
     *         The color state list may not be null
     */
    void setErrorColor(@NonNull ColorStateList colorStateList);

    /**
     * Returns the color of the helper text of the edit text widget, which is contained by the
     * dialog.
     *
     * @return The color of the helper text as an instance of the class {@link ColorStateList} or
     * null, if the default color is used
     */
    ColorStateList getHelperTextColor();

    /**
     * Sets the color of the helper text of the edit text widget, which is contained by the dialog.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    void setHelperTextColor(@ColorInt int color);

    /**
     * Sets the color of the helper text of the edit text widget, which is contained by the dialog.
     *
     * @param colorStateList
     *         The color, which should be set, as an instance of the class {@link ColorStateList}.
     *         The color state list may not be null
     */
    void setHelperTextColor(@NonNull ColorStateList colorStateList);

}