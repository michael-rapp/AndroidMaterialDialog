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

import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Defines the interface, all decorators, which allow to modify the view hierarchy of a dialog, must
 * implement.
 *
 * @author Michael Rapp
 * @since 3.6.0
 */
public interface DialogDecorator extends Dialog {

    /**
     * The method, which is invoked in order to stores the decorator's state.
     *
     * @param outState
     *         The bundle, the decorator's state should be stored within, as an instance of the
     *         class {@link Bundle}. The bundle may not be null
     */
    void onSaveInstanceState(@NonNull Bundle outState);

    /**
     * The method, which is invoked in order to restore the state, which has previously stored
     * within a bundle.
     *
     * @param savedInstanceState
     *         The bundle, which has been used to store the state, as an instance of the class
     *         {@link Bundle}. The bundle may not be null
     */
    void onRestoreInstanceState(@NonNull Bundle savedInstanceState);

}