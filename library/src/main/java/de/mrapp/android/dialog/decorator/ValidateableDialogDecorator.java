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

import android.support.annotation.NonNull;
import android.view.View;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import de.mrapp.android.dialog.DialogValidator;
import de.mrapp.android.dialog.model.HeaderDialog;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material design guidelines even on pre-Lollipop devices and can be validated.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ValidateableDialogDecorator extends AbstractDialogDecorator<HeaderDialog>
        implements de.mrapp.android.dialog.model.ValidateableDialogDecorator {

    /**
     * A set, which contains the validators of the dialog.
     */
    private final Set<DialogValidator> validators;

    /**
     * Creates  a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material design guidelines even on pre-Lollipop devices and
     * can be validated.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the type {@link HeaderDialog}. The dialog may not be null
     */
    public ValidateableDialogDecorator(@NonNull final HeaderDialog dialog) {
        super(dialog);
        this.validators = new LinkedHashSet<>();
    }

    @Override
    public final Set<DialogValidator> getValidators() {
        return validators;
    }

    @Override
    public final void addValidator(@NonNull final DialogValidator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.add(validator);
    }

    @Override
    public final void addAllValidators(@NonNull final Collection<DialogValidator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        this.validators.addAll(validators);
    }

    @Override
    public final void removeValidator(@NonNull final DialogValidator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.remove(validator);
    }

    @Override
    public final void removeAllValidators(@NonNull final Collection<DialogValidator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        this.validators.removeAll(validators);
    }

    @Override
    protected final void onAttach(@NonNull final View view) {

    }

    @Override
    protected final void onDetach() {

    }

}