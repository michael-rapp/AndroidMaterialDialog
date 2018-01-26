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

import android.support.annotation.NonNull;

import de.mrapp.android.dialog.model.ValidateableDialog;

/**
 * Defines the interface, a class, which should be able to validate the content of a dialog, must
 * implement.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public interface DialogValidator {

    /**
     * Validates the content of a dialog.
     *
     * @param dialog
     *         The dialog, whose content should be validated, as an instance of the type {@link
     *         ValidateableDialog}. The dialog may not be null
     * @return True, if the content of the dialog is valid, false otherwise
     */
    boolean validate(@NonNull ValidateableDialog dialog);

}