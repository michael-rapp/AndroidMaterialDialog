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
            // TODO
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
        setCancelable(false);
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
    public final void setText(@Nullable final String text) {
        decorator.setText(text);
    }

    @Override
    public final String getText() {
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
    public final int getErrorColor() {
        return decorator.getErrorColor();
    }

    @Override
    public final void setErrorColor(@ColorInt final int color) {
        decorator.setErrorColor(color);
    }

    @ColorInt
    @Override
    public final int getHelperTextColor() {
        return decorator.getHelperTextColor();
    }

    @Override
    public final void setHelperTextColor(@ColorInt final int color) {
        decorator.setHelperTextColor(color);
    }

    @Override
    public final Collection<Validator<String>> getValidators() {
        return decorator.getValidators();
    }

    @Override
    public final void addValidator(@NonNull final Validator<String> validator) {
        decorator.addValidator(validator);
    }

    @Override
    public final void addAllValidators(@NonNull final Collection<Validator<String>> validators) {
        decorator.addAllValidators(validators);
    }

    @SafeVarargs
    @Override
    public final void addAllValidators(@NonNull final Validator<String>... validators) {
        decorator.addAllValidators(validators);
    }

    @Override
    public final void removeValidator(@NonNull final Validator<String> validator) {
        decorator.removeValidator(validator);
    }

    @Override
    public final void removeAllValidators(@NonNull final Collection<Validator<String>> validators) {
        decorator.removeAllValidators(validators);
    }

    @SafeVarargs
    @Override
    public final void removeAllValidators(@NonNull final Validator<String>... validators) {
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
    public final void addValidationListener(@NonNull final ValidationListener<String> listener) {
        decorator.addValidationListener(listener);
    }

    @Override
    public final void removeValidationListener(@NonNull final ValidationListener<String> listener) {
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