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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import de.mrapp.android.dialog.DialogValidator;
import de.mrapp.android.dialog.model.AnimateableDialog;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;
import de.mrapp.util.Condition;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material design guidelines even on pre-Lollipop devices and can be validated.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ValidateableDialogDecorator extends AbstractDialogDecorator<AnimateableDialog>
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
     *         of the type {@link AnimateableDialog}. The dialog may not be null
     */
    public ValidateableDialogDecorator(@NonNull final AnimateableDialog dialog) {
        super(dialog);
        this.validators = new LinkedHashSet<>();
    }

    @Override
    public final Set<DialogValidator> getDialogValidators() {
        return validators;
    }

    @Override
    public final void addDialogValidator(@NonNull final DialogValidator validator) {
        Condition.INSTANCE.ensureNotNull(validator, "The validator may not be null");
        validators.add(validator);
    }

    @Override
    public final void addAllDialogValidators(@NonNull final Collection<DialogValidator> validators) {
        Condition.INSTANCE.ensureNotNull(validators, "The collection may not be null");
        this.validators.addAll(validators);
    }

    @Override
    public final void removeDialogValidator(@NonNull final DialogValidator validator) {
        Condition.INSTANCE.ensureNotNull(validator, "The validator may not be null");
        validators.remove(validator);
    }

    @Override
    public final void removeAllDialogValidators(@NonNull final Collection<DialogValidator> validators) {
        Condition.INSTANCE.ensureNotNull(validators, "The collection may not be null");
        this.validators.removeAll(validators);
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
        return Collections.emptyMap();
    }

    @Override
    protected final void onDetach() {

    }

}