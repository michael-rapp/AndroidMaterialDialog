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
import android.support.annotation.Nullable;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;
import static de.mrapp.android.util.Condition.ensureTrue;

/**
 * Represents the scrollable area(s) of a dialog.
 *
 * @author Michael Rapp
 * @since 4.2.0
 */
public final class ScrollableArea {

    /**
     * Contains all area of a dialog, which may be scrollable.
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
         * Returns the area, which corresponds to a specific index. If the given index is invalid,
         * an {@link IllegalArgumentException} will be thrown.
         *
         * @param index
         *         The index of the area, which should be returned, as an {@link Integer} value.
         * @return The area, which corresponds to the given index, as a value of the enum {@link
         * Area}
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

    /**
     * The top-most scrollable area.
     */
    private final Area topScrollableArea;

    /**
     * The bottom-most scrollable area.
     */
    private final Area bottomScrollableArea;

    /**
     * Creates a new scrollable area.
     *
     * @param top
     *         The top-most area, which should be scrollable, as a value of the enum {@link Area} or
     *         null, if no area should be scrollable
     * @param bottom
     *         The bottom-most area, which should be scrollable, as a value of the enum {@link
     *         Area}. If the top-most area is null, the bottom-most are must be null as well. The
     *         index of the bottom-most area must be at least the index of the top-most area
     */
    private ScrollableArea(final Area top, final Area bottom) {
        if (top != null) {
            ensureNotNull(bottom,
                    "If the top-most area is not null, the bottom-most area may neither be null");
            ensureAtLeast(bottom.getIndex(), top.getIndex(),
                    "The index of the bottom-most area must be at least the index of the top-most area");
        } else {
            ensureTrue(bottom == null,
                    "If the top-most area is null, the bottom-most area must be null as well");
        }

        this.topScrollableArea = top;
        this.bottomScrollableArea = bottom;
    }

    /**
     * Creates and returns a new scrollable area.
     *
     * @param area
     *         The area, which should be set, as a value of the enum {@link Area} or null, if no
     *         area should be scrollable
     */
    public static ScrollableArea create(@Nullable final Area area) {
        return create(area, area);
    }

    /**
     * Creates and returns a new scrollable area.
     *
     * @param top
     *         The top-most area, which should be scrollable, as a value of the enum {@link Area} or
     *         null, if no area should be scrollable
     * @param bottom
     *         The bottom-most area, which should be scrollable, as a value of the enum {@link
     *         Area}. If the top-most area is null, the bottom-most are must be null as well. The
     *         index of the bottom-most area must be at least the index of the top-most area
     */
    public static ScrollableArea create(@Nullable final Area top, @Nullable final Area bottom) {
        return new ScrollableArea(top, bottom);
    }

    /**
     * Returns, whether a specific area is scrollable.
     *
     * @param area
     *         The area, which should be checked, as a value of the enum {@link Area}. The area may
     *         not be null
     * @return True, if the given area is scrollable, false otherwise
     */
    public final boolean isScrollable(@NonNull final Area area) {
        return topScrollableArea != null && topScrollableArea.getIndex() <= area.getIndex() &&
                bottomScrollableArea.getIndex() >= area.getIndex();
    }

    /**
     * Returns the top-most scrollable area.
     *
     * @return The top-most scrollable area as a value of the enum {@link Area} or null, if no area
     * is scrollable
     */
    public final Area getTopScrollableArea() {
        return topScrollableArea;
    }

    /**
     * Returns the bottom-most scrollable area.
     *
     * @return The bottom-most scrollable area as a value of the enum {@link Area} or null, if no
     * area is scrollable
     */
    public final Area getBottomScrollableArea() {
        return bottomScrollableArea;
    }

}