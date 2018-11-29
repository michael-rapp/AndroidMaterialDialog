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
package de.mrapp.android.dialog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Collection;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.annotation.StyleRes;
import de.mrapp.android.dialog.builder.AbstractButtonBarDialogBuilder;
import de.mrapp.android.dialog.model.EditTextDialogDecorator;
import de.mrapp.android.util.ThemeUtil;
import de.mrapp.android.validation.ValidationListener;
import de.mrapp.android.validation.Validator;

/**
 * A dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices and contains an {@link EditText} widget. Such a dialog consists of a title,
 * a message and said {@link EditText} widget. Optionally, up to three buttons can be shown.
 * <p>
 * For creating or showing such dialogs, the methods <code>EditTextDialog#create()</code> or
 * <code>EditTextDialog#show()</code> of the builder {@link EditTextDialog.Builder} can be used.
 *
 * @author Michael Rapp
 * @since 5.1.0
 */
public class EditTextDialog extends AbstractButtonBarDialog implements EditTextDialogDecorator {

    /**
     * A builder, which allows to create and show dialogs, which are designed according to Android
     * 5's Material Design guidelines even on pre-Lollipop devices and allow to indicate a running
     * progress. Such a dialog consists of a title, a message and a circular progress bar.
     * Optionally, up to three buttons can be shown.
     */
    public static class Builder extends AbstractButtonBarDialogBuilder<EditTextDialog, Builder> {

        /**
         * Obtains the error color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the color should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainErrorColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogErrorColor});
            ColorStateList colorStateList = typedArray.getColorStateList(0);

            if (colorStateList != null) {
                setErrorColor(colorStateList);
            }
        }

        /**
         * Obtains the helper text color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the color should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainHelperTextColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogHelperTextColor});
            ColorStateList colorStateList = typedArray.getColorStateList(0);

            if (colorStateList != null) {
                setHelperTextColor(colorStateList);
            }
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices and contains an
         * {@link EditText} widget.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices and contains an
         * {@link EditText} widget.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public Builder(@NonNull final Context context, @StyleRes final int themeResourceId) {
            super(context, themeResourceId);
        }

        /**
         * Adds a new validator to the dialog, which is created by the builder.
         *
         * @param validator
         *         The validator, which should be added, as an instance of the type Validator. The
         *         validator may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder addValidator(@NonNull final Validator<CharSequence> validator) {
            getProduct().addValidator(validator);
            return this;
        }

        /**
         * Adds all validators, which are contained by a specific collection, to the dialog, which
         * is created by the builder.
         *
         * @param validators
         *         A collection, which contains the validators, which should be added, as an
         *         instance of the type {@link Collection} or an empty collection, if no validators
         *         should be added
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder addAllValidators(
                @NonNull final Collection<Validator<CharSequence>> validators) {
            getProduct().addAllValidators(validators);
            return this;
        }

        /**
         * Adds all validators, which are contained by a specific array to the dialog, which is
         * created by the builder.
         *
         * @param validators
         *         An array, which contains the validators, which should be added, as an array of
         *         the type Validator or an empty array, if no validators should be added
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        @SuppressWarnings("unchecked")
        public final Builder addAllValidators(
                @NonNull final Validator<CharSequence>... validators) {
            getProduct().addAllValidators(validators);
            return this;
        }

        /**
         * Sets, whether the dialog, which is created by the builder, should automatically be
         * validated, when its text has been changed, or not.
         *
         * @param validateOnValueChange
         *         True, if the value of the view should automatically be validated, when its value
         *         has been changed, false otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder validateOnValueChange(boolean validateOnValueChange) {
            getProduct().validateOnValueChange(validateOnValueChange);
            return this;
        }

        /**
         * Sets, whether the dialog, which is created by the builder, should automatically be
         * validated, when its edit text widget lost its focus, or not.
         *
         * @param validateOnFocusLost
         *         True, if the value of the view should automatically be validated, when the view
         *         loses its focus, false otherwise
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder validateOnFocusLost(final boolean validateOnFocusLost) {
            getProduct().validateOnFocusLost(validateOnFocusLost);
            return this;
        }

        /**
         * Adds a new listener, which should be notified, when the dialog, which is created by the
         * builder, has been validated.
         *
         * @param listener
         *         The listener, which should be added, as an instance of the type
         *         ValidationListener. The listener may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder addValidationListener(
                @NonNull final ValidationListener<CharSequence> listener) {
            getProduct().addValidationListener(listener);
            return this;
        }

        /**
         * Sets the text of the dialog, which is created by the builder.
         *
         * @param text
         *         The text, which should be set, as an instance of the type {@link CharSequence} or
         *         null, if no text should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setText(@Nullable final CharSequence text) {
            getProduct().setText(text);
            return this;
        }

        /**
         * Sets the hint of the dialog, which is created by the builder.
         *
         * @param hint
         *         The hint, which should be set, as an instance of the type {@link CharSequence} or
         *         null, if no hint should be set
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setHint(@Nullable final CharSequence hint) {
            getProduct().setHint(hint);
            return this;
        }

        /**
         * Sets the hint of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the hint, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setHint(@StringRes int resourceId) {
            getProduct().setHint(resourceId);
            return this;
        }

        /**
         * Sets the helper text of the dialog, which is created by the builder.
         *
         * @param helperText
         *         The helper text, which should be set, as an instance of the type {@link
         *         CharSequence} or null, if no helper text should be shown
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setHelperText(@Nullable final CharSequence helperText) {
            getProduct().setHelperText(helperText);
            return this;
        }

        /**
         * Sets the helper text of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource ID of the string resource, which contains the helper text, which
         *         should be set, as an {@link Integer} value. The resource ID must correspond to a
         *         valid string resource
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setHelperText(@StringRes final int resourceId) {
            getProduct().setHelperText(resourceId);
            return this;
        }

        /**
         * Sets the color, which should be used to indicate validation errors by the dialog, which
         * is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setErrorColor(@ColorInt final int color) {
            getProduct().setErrorColor(color);
            return this;
        }

        /**
         * Sets the color, which should be used to indicate validation errors by the dialog, which
         * is created by the builder.
         *
         * @param colorStateList
         *         The color, which should be set, as an instance of the class {@link
         *         ColorStateList}. The color state list may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setErrorColor(@NonNull final ColorStateList colorStateList) {
            getProduct().setErrorColor(colorStateList);
            return this;
        }

        /**
         * Sets the color of the helper text of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setHelperTextColor(@ColorInt final int color) {
            getProduct().setHelperTextColor(color);
            return this;
        }

        /**
         * Sets the color of the helper text of the dialog, which is created by the builder.
         *
         * @param colorStateList
         *         The color, which should be set, as an instance of the class {@link
         *         ColorStateList}. The color state list may not be null
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}. The builder may not be null
         */
        public final Builder setHelperTextColor(@NonNull final ColorStateList colorStateList) {
            getProduct().setHelperTextColor(colorStateList);
            return this;
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @return The dialog, which has been shown, as an instance of the class {@link
         * EditTextDialog}
         */
        public final EditTextDialog show() {
            EditTextDialog dialog = create();
            dialog.show();
            return dialog;
        }

        @NonNull
        @Override
        protected final EditTextDialog onCreateProduct() {
            return new EditTextDialog(getContext(), getThemeResourceId());
        }

        @Override
        protected final void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            super.obtainStyledAttributes(themeResourceId);
            obtainErrorColor(themeResourceId);
            obtainHelperTextColor(themeResourceId);
        }

    }

    /**
     * The decorator, which is used by the dialog.
     */
    private final de.mrapp.android.dialog.decorator.EditTextDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and contains an {@link EditText} widget.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected EditTextDialog(@NonNull final Context context, @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        decorator = new de.mrapp.android.dialog.decorator.EditTextDialogDecorator(this);
        addDecorator(decorator);
    }

    @Override
    public final TextInputLayout getTextInputLayout() {
        return decorator.getTextInputLayout();
    }

    @Override
    public final EditText getEditText() {
        return decorator.getEditText();
    }

    @Override
    public final void setText(@Nullable final CharSequence text) {
        decorator.setText(text);
    }

    @Override
    public final CharSequence getText() {
        return decorator.getText();
    }

    @Override
    public final CharSequence getHint() {
        return decorator.getHint();
    }

    @Override
    public final void setHint(@Nullable final CharSequence hint) {
        decorator.setHint(hint);
    }

    @Override
    public final void setHint(@StringRes final int resourceId) {
        decorator.setHint(resourceId);
    }

    @Override
    public final CharSequence getHelperText() {
        return decorator.getHelperText();
    }

    @Override
    public final void setHelperText(@Nullable final CharSequence helperText) {
        decorator.setHelperText(helperText);
    }

    @Override
    public final void setHelperText(@StringRes final int resourceId) {
        decorator.setHelperText(resourceId);
    }

    @Override
    public final ColorStateList getErrorColor() {
        return decorator.getErrorColor();
    }

    @Override
    public final void setErrorColor(@ColorInt final int color) {
        decorator.setErrorColor(color);
    }

    @Override
    public final void setErrorColor(@NonNull final ColorStateList colorStateList) {
        decorator.setErrorColor(colorStateList);
    }

    @Override
    public final ColorStateList getHelperTextColor() {
        return decorator.getHelperTextColor();
    }

    @Override
    public final void setHelperTextColor(@ColorInt final int color) {
        decorator.setHelperTextColor(color);
    }

    @Override
    public final void setHelperTextColor(@NonNull final ColorStateList colorStateList) {
        decorator.setHelperTextColor(colorStateList);
    }

    @Override
    public final Collection<Validator<CharSequence>> getValidators() {
        return decorator.getValidators();
    }

    @Override
    public final void addValidator(@NonNull final Validator<CharSequence> validator) {
        decorator.addValidator(validator);
    }

    @Override
    public final void addAllValidators(
            @NonNull final Collection<Validator<CharSequence>> validators) {
        decorator.addAllValidators(validators);
    }

    @SafeVarargs
    @Override
    public final void addAllValidators(@NonNull final Validator<CharSequence>... validators) {
        decorator.addAllValidators(validators);
    }

    @Override
    public final void removeValidator(@NonNull final Validator<CharSequence> validator) {
        decorator.removeValidator(validator);
    }

    @Override
    public final void removeAllValidators(
            @NonNull final Collection<Validator<CharSequence>> validators) {
        decorator.removeAllValidators(validators);
    }

    @SafeVarargs
    @Override
    public final void removeAllValidators(@NonNull final Validator<CharSequence>... validators) {
        decorator.removeAllValidators();
    }

    @Override
    public final void removeAllValidators() {
        decorator.removeAllValidators();
    }

    @Override
    public final boolean validate() {
        return decorator.validate();
    }

    @Override
    public final boolean isValidatedOnValueChange() {
        return decorator.isValidatedOnValueChange();
    }

    @Override
    public final void validateOnValueChange(final boolean validateOnValueChange) {
        decorator.validateOnValueChange(validateOnValueChange);
    }

    @Override
    public final boolean isValidatedOnFocusLost() {
        return decorator.isValidatedOnFocusLost();
    }

    @Override
    public final void validateOnFocusLost(final boolean validateOnFocusLost) {
        decorator.validateOnFocusLost(validateOnFocusLost);
    }

    @Override
    public final void addValidationListener(
            @NonNull final ValidationListener<CharSequence> listener) {
        decorator.addValidationListener(listener);
    }

    @Override
    public final void removeValidationListener(
            @NonNull final ValidationListener<CharSequence> listener) {
        decorator.removeValidationListener(listener);
    }

    @NonNull
    @Override
    public final Bundle onSaveInstanceState() {
        Bundle outState = super.onSaveInstanceState();
        decorator.onSaveInstanceState(outState);
        return outState;
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        decorator.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

}