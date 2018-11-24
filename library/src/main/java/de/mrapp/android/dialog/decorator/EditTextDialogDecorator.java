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

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Collections;
import java.util.Map;

import androidx.annotation.NonNull;
import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.ButtonBarDialog;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;

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
        getDialog().setView(R.layout.edit_text_dialog);

        if (getRootView() != null) {
            View textInputLayoutView = getRootView().findViewById(R.id.text_input_layout);
            textInputLayout = textInputLayoutView instanceof TextInputLayout ?
                    (TextInputLayout) textInputLayoutView : null;
            View editTextView = getRootView().findViewById(R.id.edit_text);
            editText = editTextView instanceof EditText ? (EditText) editTextView : null;
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
    public final void onSaveInstanceState(@NonNull final Bundle outState) {

    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {

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