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
package de.mrapp.android.dialog.builder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

import java.util.Collection;

import de.mrapp.android.dialog.DialogValidator;
import de.mrapp.android.dialog.model.ValidateableDialog;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all builders, which allow to create and show dialogs, which are
 * designed according to Android Material Design guidelines even on pre-Lollipop devices and can be
 * validated.
 *
 * @param <DialogType>
 *         The type of the dialog, which is created by the builder
 * @param <BuilderType>
 *         The type of the builder
 * @author Michael Rapp
 * @since 3.3.0
 */
public abstract class AbstractValidateableDialogBuilder<DialogType extends ValidateableDialog, BuilderType extends AbstractValidateableDialogBuilder<DialogType, ?>>
        extends AbstractAnimateableDialogBuilder<DialogType, BuilderType> {

    /**
     * Creates a new builder, which allows to create dialogs, which allow to create and show
     * dialogs, which are designed according to Android 5's Material Design guidelines even on
     * pre-Lollipop devices and can be validated.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public AbstractValidateableDialogBuilder(@NonNull final Context context) {
        super(context);
    }

    /**
     * Creates a new builder, which allows to create dialogs, which allow to create and show
     * dialogs, which are designed according to Android 5's Material Design guidelines even on
     * pre-Lollipop devices and can be validated.
     *
     * @param context
     *         The context, which should be used by the builder, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    public AbstractValidateableDialogBuilder(@NonNull final Context context,
                                             @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
    }

    /**
     * Adds a new validator, which should be executed when the positive button of the dialog, which
     * is created by the builder, is clicked.
     *
     * @param validator
     *         The validator, which should be added, as an instance of the type {@link
     *         DialogValidator}. The validator may not be null
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType addValidator(@NonNull final DialogValidator validator) {
        ensureNotNull(validator, "The validator may not be null");
        getProduct().addValidator(validator);
        return self();
    }

    /**
     * Adds all validators, which are contained by a specific collection and should be executed when
     * the positive button of the dialog, which is created by the builder, is clicked.
     *
     * @param validators
     *         A collection, which contains all validators, which should be added, as an instance of
     *         the type {@link Collection} or an empty collection, if no validators should be added
     * @return The builder, the method has been called upon, as an instance of the generic type
     * BuilderType
     */
    public final BuilderType addAllValidators(
            @NonNull final Collection<DialogValidator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        getProduct().addAllValidators(validators);
        return self();
    }

}
