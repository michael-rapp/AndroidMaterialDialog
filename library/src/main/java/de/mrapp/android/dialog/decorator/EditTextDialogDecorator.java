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
package de.mrapp.android.dialog.decorator;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.ButtonBarDialog;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;
import de.mrapp.android.validation.ValidationListener;
import de.mrapp.android.validation.Validator;
import de.mrapp.util.Condition;
import de.mrapp.util.datastructure.ListenerList;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material Design guidelines even on pre-Lollipop devices and contains an {@link
 * EditText} widget.
 *
 * @author Michael Rapp
 * @since 5.1.0
 */
public class EditTextDialogDecorator extends AbstractDialogDecorator<ButtonBarDialog>
        implements de.mrapp.android.dialog.model.EditTextDialogDecorator {

    /**
     * The name of the extra, which is used to store the text of the dialog's edit text widget
     * within a bundle.
     */
    private static final String TEXT_EXTRA =
            EditTextDialogDecorator.class.getSimpleName() + "::text";

    /**
     * The name of the extra, which is used to store the hint of the dialog's edit text widget
     * within a bundle.
     */
    private static final String HINT_EXTRA =
            EditTextDialogDecorator.class.getSimpleName() + "::hint";

    /**
     * The name of the extra, which is used to store the helper text of the dialog's edit text
     * widget within a bundle.
     */
    private static final String HELPER_TEXT_EXTRA =
            EditTextDialogDecorator.class.getSimpleName() + "::helperText";

    /**
     * The name of the extra, which is used to store the error color of the dialog's edit text
     * widget within a bundle.
     */
    private static final String ERROR_COLOR_EXTRA =
            EditTextDialogDecorator.class.getSimpleName() + "::errorColor";

    /**
     * The name of the extra, which is used to store the helper text color of the dialog's edit text
     * widget within a bundle.
     */
    private static final String HELPER_TEXT_COLOR_EXTRA =
            EditTextDialogDecorator.class.getSimpleName() + "::helperTextColor";

    /**
     * The name of the extra, which is used to store, whether the dialog's edit text widget should
     * be validated when its value changed, within a bundle.
     */
    private static final String VALIDATE_ON_VALUE_CHANGE_EXTRA =
            EditTextDialogDecorator.class.getSimpleName() + "::validateOnValueChange";

    /**
     * The name of the extra, which is used to store, whether the dialog's edit text widget should
     * be validated when it list its focus, within a bundle.
     */
    private static final String VALIDATE_ON_FOCUS_LOST_EXTRA =
            EditTextDialogDecorator.class.getSimpleName() + "::validateOnFocusLost";

    /**
     * A set, which contains the validators of the dialog's edit text widget.
     */
    private final Set<Validator<String>> validators = new LinkedHashSet<>();

    /**
     * Contains the listeners that should be notified when the dialog's edit text widget has been
     * validated.
     */
    private final ListenerList<ValidationListener<String>> validationListeners =
            new ListenerList<>();

    /**
     * The text of the dialog's edit text widget.
     */
    private String text;

    /**
     * The hint of the dialog's edit text widget.
     */
    private CharSequence hint;

    /**
     * The helper text of the dialog's edit text widget.
     */
    private CharSequence helperText;

    /**
     * The error color of the dialog's edit text widget.
     */
    @ColorInt
    private int errorColor;

    /**
     * The helper text color of the dialog's edit text widget.
     */
    @ColorInt
    private int helperTextColor;

    /**
     * True, if the dialog's edit text widget is validated when its text has been changed, false
     * otherwise.
     */
    private boolean validateOnValueChange;

    /**
     * True, if the dialog's edit text widget is validated when its focus got lost, false
     * otherwise.
     */
    private boolean validateOnFocusLost;

    /**
     * The dialog's text input layout.
     */
    private TextInputLayout textInputLayout;

    /**
     * The dialog's edit text widget.
     */
    private EditText editText;

    /**
     * Inflates the dialog's edit text widget.
     */
    private void inflateEditText() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.edit_text_dialog, getRootView(), false);
        getDialog().setView(view);
        View textInputLayoutView = view.findViewById(R.id.text_input_layout);
        textInputLayout = textInputLayoutView instanceof TextInputLayout ?
                (TextInputLayout) textInputLayoutView : null;
        View editTextView = view.findViewById(R.id.edit_text);
        editText = editTextView instanceof EditText ? (EditText) editTextView : null;
        adaptHint();
        adaptErrorColor();
        adaptHelperTextColor();
        adaptHelperText();
        adaptText();
        validate();
        adaptTextChangedListener();
        adaptFocusChangeListener();
    }

    /**
     * Adapts the text of the edit text widget, which is contained by the dialog.
     */
    private void adaptText() {
        if (editText != null) {
            editText.setText(text);

            if (text != null) {
                editText.setSelection(text.length());
            }
        }
    }

    /**
     * Adapts the hint of the dialog's edit text widget.
     */
    private void adaptHint() {
        if (textInputLayout != null) {
            textInputLayout.setHint(hint);
        } else if (editText != null) {
            editText.setHint(hint);
        }
    }

    /**
     * Adapts the error color of the dialog's edit text widget.
     */
    private void adaptErrorColor() {
        if (textInputLayout != null) {
            textInputLayout.setErrorTextColor(ColorStateList.valueOf(errorColor));
        }
    }

    /**
     * Adapts the helper text color of the dialog's edit text widget.
     */
    private void adaptHelperTextColor() {
        if (textInputLayout != null) {
            textInputLayout.setHelperTextColor(ColorStateList.valueOf(helperTextColor));
        }
    }

    /**
     * Adapts the helper text of the dialog's edit text widget.
     */
    private void adaptHelperText() {
        if (textInputLayout != null && TextUtils.isEmpty(getText())) {
            textInputLayout.setHelperText(helperText);
            textInputLayout.setHelperTextEnabled(true);
        }
    }

    /**
     * Adapts the listener, which allows to observe when the text of the the dialog's edit text has
     * changed.
     */
    private void adaptTextChangedListener() {
        if (editText != null) {
            editText.addTextChangedListener(createTextChangedListener());
        }
    }

    /**
     * Creates and returns a listener, which allows to observe when the text of the dialog's edit
     * text has changed.
     *
     * @return The listener, which has been created, as an instance of the type {@link TextWatcher}.
     * The listener may not be null
     */
    @NonNull
    private TextWatcher createTextChangedListener() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(final CharSequence text, final int start, final int count,
                                          final int after) {

            }

            @Override
            public void afterTextChanged(final Editable text) {

            }

            @Override
            public void onTextChanged(final CharSequence text, final int start, final int before,
                                      final int count) {
                EditTextDialogDecorator.this.text = text.toString();

                if (validateOnValueChange) {
                    validate();
                }
            }

        };
    }

    /**
     * Adapts the listener, which allows to observe when the dialog's edit text has lost its focus.
     */
    private void adaptFocusChangeListener() {
        if (editText != null) {
            editText.setOnFocusChangeListener(createFocusChangeListener());
        }
    }

    /**
     * Creates and returns a listener, which allows to validate the dialog's edit text widget when
     * its focus got lost.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * View.OnFocusChangeListener}. The listener may not be null
     */
    @NonNull
    private View.OnFocusChangeListener createFocusChangeListener() {
        return new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                if (!hasFocus && validateOnFocusLost) {
                    validate();
                }
            }

        };
    }

    /**
     * Notifies the listeners that the validation of the dialog's edit text widget has failed.
     *
     * @param validator
     *         The validator, which caused the validation to fail, as an instance of the type {@link
     *         Validator}. The validator may not be null
     */
    private void notifyOnValidationFailure(@NonNull final Validator<String> validator) {
        for (ValidationListener<String> validationListener : validationListeners) {
            validationListener.onValidationFailure(this, validator);
        }
    }

    /**
     * Notifies the listeners that the validation of the dialog's edit text widget has succeeded.
     */
    private void notifyOnValidationSuccess() {
        for (ValidationListener<String> validationListener : validationListeners) {
            validationListener.onValidationSuccess(this);
        }
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
     * contains an {@link EditText} widget.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the type {@link ButtonBarDialog}. The dialog may not be null
     */
    public EditTextDialogDecorator(@NonNull final ButtonBarDialog dialog) {
        super(dialog);
    }

    @Override
    public final TextInputLayout getTextInputLayout() {
        return textInputLayout;
    }

    @Override
    public final EditText getEditText() {
        return editText;
    }

    @Override
    public final String getText() {
        return text;
    }

    @Override
    public final void setText(@Nullable final String text) {
        this.text = text;
        adaptText();
    }

    @Override
    public final CharSequence getHint() {
        return hint;
    }

    @Override
    public final void setHint(@Nullable final CharSequence hint) {
        this.hint = hint;
        adaptHint();
    }

    @Override
    public final void setHint(@StringRes final int resourceId) {
        setHint(getContext().getString(resourceId));
    }

    @Override
    public final CharSequence getHelperText() {
        return helperText;
    }

    @Override
    public final void setHelperText(@Nullable final CharSequence helperText) {
        this.helperText = helperText;
        adaptHelperText();
    }

    @Override
    public final void setHelperText(@StringRes final int resourceId) {
        setHelperText(getContext().getString(resourceId));
    }

    @ColorInt
    @Override
    public final int getErrorColor() {
        return errorColor;
    }

    @Override
    public final void setErrorColor(@ColorInt final int color) {
        this.errorColor = color;
        adaptErrorColor();
    }

    @ColorInt
    @Override
    public final int getHelperTextColor() {
        return helperTextColor;
    }

    @Override
    public final void setHelperTextColor(@ColorInt final int color) {
        this.helperTextColor = color;
        adaptHelperTextColor();
    }

    @Override
    public final Collection<Validator<String>> getValidators() {
        return Collections.unmodifiableSet(validators);
    }

    @Override
    public final void addValidator(@NonNull final Validator<String> validator) {
        Condition.INSTANCE.ensureNotNull(validator, "The validator may not be null");
        this.validators.add(validator);
    }

    @Override
    public final void addAllValidators(@NonNull final Collection<Validator<String>> validators) {
        Condition.INSTANCE.ensureNotNull(validators, "The collection may not be null");

        for (Validator<String> validator : validators) {
            addValidator(validator);
        }
    }

    @SafeVarargs
    @Override
    public final void addAllValidators(@NonNull final Validator<String>... validators) {
        Condition.INSTANCE.ensureNotNull(validators, "The array may not be null");

        for (Validator<String> validator : validators) {
            addValidator(validator);
        }
    }

    @Override
    public final void removeValidator(@NonNull final Validator<String> validator) {
        Condition.INSTANCE.ensureNotNull(validator, "The validator may not be null");
        this.validators.remove(validator);
    }

    @Override
    public final void removeAllValidators(@NonNull final Collection<Validator<String>> validators) {
        Condition.INSTANCE.ensureNotNull(validators, "The collection may not be null");

        for (Validator<String> validator : validators) {
            addValidator(validator);
        }
    }

    @SafeVarargs
    @Override
    public final void removeAllValidators(@NonNull final Validator<String>... validators) {
        Condition.INSTANCE.ensureNotNull(validators, "The array may not be null");

        for (Validator<String> validator : validators) {
            addValidator(validator);
        }
    }

    @Override
    public final void removeAllValidators() {
        this.validators.clear();
    }

    @Override
    public final boolean validate() {
        if (editText != null) {
            for (Validator<String> validator : validators) {
                if (!validator.validate(getText())) {
                    notifyOnValidationFailure(validator);
                    return false;
                }
            }

            notifyOnValidationSuccess();
        }

        return true;
    }

    @Override
    public final boolean isValidatedOnValueChange() {
        return validateOnValueChange;
    }

    @Override
    public final void validateOnValueChange(final boolean validateOnValueChange) {
        this.validateOnValueChange = validateOnValueChange;
    }

    @Override
    public final boolean isValidatedOnFocusLost() {
        return validateOnFocusLost;
    }

    @Override
    public final void validateOnFocusLost(final boolean validateOnFocusLost) {
        this.validateOnFocusLost = validateOnFocusLost;
    }

    @Override
    public final void addValidationListener(@NonNull final ValidationListener<String> listener) {
        validationListeners.add(listener);
    }

    @Override
    public final void removeValidationListener(@NonNull final ValidationListener<String> listener) {
        validationListeners.remove(listener);
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putString(TEXT_EXTRA, getText());
        outState.putCharSequence(HINT_EXTRA, getHint());
        outState.putCharSequence(HELPER_TEXT_EXTRA, getHelperText());
        outState.putInt(ERROR_COLOR_EXTRA, getErrorColor());
        outState.putInt(HELPER_TEXT_COLOR_EXTRA, getHelperTextColor());
        outState.putBoolean(VALIDATE_ON_VALUE_CHANGE_EXTRA, isValidatedOnValueChange());
        outState.putBoolean(VALIDATE_ON_FOCUS_LOST_EXTRA, isValidatedOnFocusLost());

    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        setText(savedInstanceState.getString(TEXT_EXTRA));
        setHint(savedInstanceState.getCharSequence(HINT_EXTRA));
        setHelperText(savedInstanceState.getCharSequence(HELPER_TEXT_EXTRA));
        setErrorColor(savedInstanceState.getInt(ERROR_COLOR_EXTRA));
        setHelperTextColor(savedInstanceState.getInt(HELPER_TEXT_COLOR_EXTRA));
        validateOnValueChange(savedInstanceState.getBoolean(VALIDATE_ON_VALUE_CHANGE_EXTRA));
        validateOnFocusLost(savedInstanceState.getBoolean(VALIDATE_ON_FOCUS_LOST_EXTRA));
    }

    @NonNull
    @Override
    protected final Map<ViewType, View> onAttach(@NonNull final Window window,
                                                 @NonNull final View view,
                                                 @NonNull final Map<ViewType, View> areas,
                                                 final Void param) {
        inflateEditText();
        return Collections.emptyMap();
    }

    @Override
    protected final void onDetach() {
        editText = null;
        textInputLayout = null;
    }

}