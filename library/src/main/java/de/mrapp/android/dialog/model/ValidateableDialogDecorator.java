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
package de.mrapp.android.dialog.model;

import android.support.annotation.NonNull;

import java.util.Collection;
import java.util.Set;

import de.mrapp.android.dialog.DialogValidator;

/**
 * Defines the interface, a decorator, which allows to modify the view hierarchy of a dialog, which
 * is designed according to Android 5's Material design guidelines even on pre-Lollipop devices and
 * can be validated, must implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface ValidateableDialogDecorator extends Dialog {

    /**
     * Returns a set, which contains the validators, which are executed when the positive button of
     * the dialog is clicked.
     *
     * @return A set, which contains the validators, which are executed when the positive button of
     * the dialog is clicked, as an instance of the type {@link Set} or an empty set, if no
     * validators are executed
     */
    Set<DialogValidator> getValidators();

    /**
     * Adds a new validator, which should be executed when the positive button of the dialog is
     * clicked.
     *
     * @param validator
     *         The validator, which should be added, as an instance of the type {@link
     *         DialogValidator}. The validator may not be null
     */
    void addValidator(@NonNull DialogValidator validator);

    /**
     * Adds all validators, which are contained by a specific collection and should be executed when
     * the positive button of the dialog is clicked.
     *
     * @param validators
     *         A collection, which contains all validators, which should be added, as an instance of
     *         the type {@link Collection} or an empty collection, if no validators should be added
     */
    void addAllValidators(@NonNull Collection<DialogValidator> validators);

    /**
     * Removes a specific validator, which should not be executed, when the positive button of the
     * dialog is clicked, anymore.
     *
     * @param validator
     *         The validator, which should be removed, as an instance of the type {@link
     *         DialogValidator}. The validator may not be null
     */
    void removeValidator(@NonNull DialogValidator validator);

    /**
     * Removes all validators, which are contained by a specific collection and should not be
     * executed, when the positive button of the dialog is clicked, anymore.
     *
     * @param validators
     *         A collection, which contains the validators, which should be removed, as an instance
     *         of the type {@link Collection} or an empty collection, if no validators should be
     *         removed
     */
    void removeAllValidators(@NonNull Collection<DialogValidator> validators);

}