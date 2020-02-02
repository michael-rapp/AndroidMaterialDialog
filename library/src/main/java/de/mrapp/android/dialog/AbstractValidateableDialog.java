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
package de.mrapp.android.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.StyleRes;

import java.util.Collection;
import java.util.Set;

import de.mrapp.android.dialog.decorator.ValidateableDialogDecorator;
import de.mrapp.android.dialog.model.ValidateableDialog;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices and can be validated.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractValidateableDialog extends AbstractAnimateableDialog
        implements ValidateableDialog {

    /**
     * The decorator, which is used by the dialog.
     */
    private final ValidateableDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and can be validated.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected AbstractValidateableDialog(@NonNull final Context context,
                                         @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        decorator = new ValidateableDialogDecorator(this);
        addDecorator(decorator);
    }

    @Override
    public final Set<DialogValidator> getDialogValidators() {
        return decorator.getDialogValidators();
    }

    @Override
    public final void addDialogValidator(@NonNull final DialogValidator validator) {
        decorator.addDialogValidator(validator);
    }

    @Override
    public final void addAllDialogValidators(@NonNull final Collection<DialogValidator> validators) {
        decorator.addAllDialogValidators(validators);
    }

    @Override
    public final void removeDialogValidator(@NonNull final DialogValidator validator) {
        decorator.removeDialogValidator(validator);
    }

    @Override
    public final void removeAllDialogValidators(@NonNull final Collection<DialogValidator> validators) {
        decorator.removeAllDialogValidators(validators);
    }

    @NonNull
    @CallSuper
    @Override
    public Bundle onSaveInstanceState() {
        Bundle outState = super.onSaveInstanceState();
        decorator.onSaveInstanceState(outState);
        return outState;
    }

    @CallSuper
    @Override
    public void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        decorator.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

}