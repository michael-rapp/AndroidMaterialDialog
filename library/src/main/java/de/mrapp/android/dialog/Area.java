/*
 * Copyright 2014 - 2017 Michael Rapp
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

/**
 * Contains all areas of a dialog.
 *
 * @author Michael Rapp
 * @since 4.2.0
 */
public enum Area {

    /**
     * The header of a dialog.
     */
    HEADER(0),

    /**
     * The title of a dialog.
     */
    TITLE(1),

    /**
     * The message of a dialog.
     */
    MESSAGE(2),

    /**
     * The content of a dialog.
     */
    CONTENT(3),

    /**
     * The button bar of a dialog.
     */
    BUTTON_BAR(4);

    /**
     * The index of the area.
     */
    private final int index;

    Area(final int index) {
        this.index = index;
    }

    /**
     * Returns the index of the area.
     *
     * @return The index of the area as an {@link Integer} value
     */
    public final int getIndex() {
        return index;
    }

    /**
     * Returns the area, which corresponds to a specific index. If the given index is invalid, an
     * {@link IllegalArgumentException} will be thrown.
     *
     * @param index
     *         The index of the area, which should be returned, as an {@link Integer} value.
     * @return The area, which corresponds to the given index, as a value of the enum {@link Area}
     */
    @NonNull
    public static Area fromIndex(final int index) {
        for (Area area : values()) {
            if (area.getIndex() == index) {
                return area;
            }
        }

        throw new IllegalArgumentException("Invalid enum value: " + index);
    }

}