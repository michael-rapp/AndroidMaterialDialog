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
package de.mrapp.android.dialog.listener;

import androidx.annotation.NonNull;

import de.mrapp.android.dialog.ScrollableArea.Area;

/**
 * A listener, a class, which should be notified, when the visibility of the areas of a dialog have
 * been changed, must implement.
 *
 * @author Michael Rapp
 * @since 4.2.0
 */
public interface AreaListener {

    /**
     * The method, which is invoked, when an area has been shown.
     *
     * @param area
     *         The area, which has been shown, as a value of the enum {@link Area}. The area may not
     *         be null
     */
    void onAreaShown(@NonNull Area area);

    /**
     * The method, which is invoked, when an area has been hidden.
     *
     * @param area
     *         The area, which has been hidden, as a value of the enum {@link Area}. The area may
     *         not be null
     */
    void onAreaHidden(@NonNull Area area);

}