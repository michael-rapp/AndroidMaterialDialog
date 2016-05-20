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
package de.mrapp.android.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

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
public abstract class AbstractValidateableDialog extends AbstractHeaderDialog
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
        this.decorator = new ValidateableDialogDecorator(this);
    }

    @Override
    public final Set<DialogValidator> getValidators() {
        return decorator.getValidators();
    }

    @Override
    public final void addValidator(@NonNull final DialogValidator validator) {
        decorator.addValidator(validator);
    }

    @Override
    public final void addAllValidators(@NonNull final Collection<DialogValidator> validators) {
        decorator.addAllValidators(validators);
    }

    @Override
    public final void removeValidator(@NonNull final DialogValidator validator) {
        decorator.removeValidator(validator);
    }

    @Override
    public final void removeAllValidators(@NonNull final Collection<DialogValidator> validators) {
        decorator.removeAllValidators(validators);
    }

}