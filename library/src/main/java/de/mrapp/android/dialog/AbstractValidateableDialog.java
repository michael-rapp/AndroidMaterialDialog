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
import java.util.LinkedHashSet;
import java.util.Set;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all dialogs, all dialogs, which are designed according to Android 5's
 * Material design guidelines even on pre-Lollipop devices and can be validated.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractValidateableDialog extends AbstractMaterialDialog {

    /**
     * An abstract base class for all builders, which allow to create and show dialogs, which are
     * designed according to Android Material Design guidelines even on pre-Lollipop devices and can
     * be validated.
     *
     * @param <DialogType>
     *         The type of the dialog, which is created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    public static abstract class AbstractBuilder<DialogType extends AbstractValidateableDialog, BuilderType extends AbstractMaterialDialog.AbstractBuilder<DialogType, ?>>
            extends AbstractMaterialDialog.AbstractBuilder<DialogType, BuilderType> {

        /**
         * Creates a new builder, which allows to create dialogs, which allow to create and show
         * dialogs, which are designed according to Android 5's Material Design guidelines even on
         * pre-Lollipop devices and can be validated.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public AbstractBuilder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which allow to create and show
         * dialogs, which are designed according to Android 5's Material Design guidelines even on
         * pre-Lollipop devices and can be validated.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public AbstractBuilder(@NonNull final Context context,
                               @StyleRes final int themeResourceId) {
            super(context, themeResourceId);
        }

        /**
         * Adds a new validator, which should be executed when the positive button of the dialog,
         * which is created by the builder, is clicked.
         *
         * @param validator
         *         The validator, which should be added, as an instance of the type {@link
         *         DialogValidator}. The validator may not be null
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType addValidator(@NonNull final DialogValidator validator) {
            ensureNotNull(validator, "The validator may not be null");
            getDialog().addValidator(validator);
            return self();
        }

        /**
         * Adds all validators, which are contained by a specific collection and should be executed
         * when the positive button of the dialog, which is created by the builder, is clicked.
         *
         * @param validators
         *         A collection, which contains all validators, which should be added, as an
         *         instance of the type {@link Collection} or an empty collection, if no validators
         *         should be added
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType addAllValidators(
                @NonNull final Collection<DialogValidator> validators) {
            ensureNotNull(validators, "The collection may not be null");
            getDialog().addAllValidators(validators);
            return self();
        }

    }

    /**
     * A set, which contains the validators of the dialog.
     */
    private final Set<DialogValidator> validators;

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
        this.validators = new LinkedHashSet<>();
    }

    /**
     * Returns a set, which contains the validators, which are executed when the positive button of
     * the dialog is clicked.
     *
     * @return A set, which contains the validators, which are executed when the positive button of
     * the dialog is clicked, as an instance of the type {@link Set} or an empty set, if no
     * validators are executed
     */
    public final Set<DialogValidator> getValidators() {
        return validators;
    }

    /**
     * Adds a new validator, which should be executed when the positive button of the dialog is
     * clicked.
     *
     * @param validator
     *         The validator, which should be added, as an instance of the type {@link
     *         DialogValidator}. The validator may not be null
     */
    public final void addValidator(@NonNull final DialogValidator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.add(validator);
    }

    /**
     * Adds all validators, which are contained by a specific collection and should be executed when
     * the positive button of the dialog is clicked.
     *
     * @param validators
     *         A collection, which contains all validators, which should be added, as an instance of
     *         the type {@link Collection} or an empty collection, if no validators should be added
     */
    public final void addAllValidators(@NonNull final Collection<DialogValidator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        this.validators.addAll(validators);
    }

    /**
     * Removes a specific validator, which should not be executed, when the positive button of the
     * dialog is clicked, anymore.
     *
     * @param validator
     *         The validator, which should be removed, as an instance of the type {@link
     *         DialogValidator}. The validator may not be null
     */
    public final void removeValidator(@NonNull final DialogValidator validator) {
        ensureNotNull(validator, "The validator may not be null");
        validators.remove(validator);
    }

    /**
     * Removes all validators, which are contained by a specific collection and should not be
     * executed, when the positive button of the dialog is clicked, anymore.
     *
     * @param validators
     *         A collection, which contains the validators, which should be removed, as an instance
     *         of the type {@link Collection} or an empty collection, if no validators should be
     *         removed
     */
    public final void removeAllValidators(@NonNull final Collection<DialogValidator> validators) {
        ensureNotNull(validators, "The collection may not be null");
        this.validators.removeAll(validators);
    }

}