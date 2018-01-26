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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;
import static de.mrapp.android.util.Condition.ensureTrue;

/**
 * Represents the scrollable area(s) of a dialog.
 *
 * @author Michael Rapp
 * @since 4.2.0
 */
public final class ScrollableArea implements Serializable, Parcelable {

    /**
     * A creator, which allows to create instances of the class {@link ScrollableArea} from parcels.
     */
    public static final Creator<ScrollableArea> CREATOR = new Creator<ScrollableArea>() {

        @Override
        public ScrollableArea createFromParcel(final Parcel source) {
            return new ScrollableArea(source);
        }

        @Override
        public ScrollableArea[] newArray(final int size) {
            return new ScrollableArea[size];
        }

    };

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
     * Creates a new scrollable area.
     *
     * @param source
     *         The source, the scrollable area should be created from, as an instance of the class
     *         {@link Parcel}. The source may not be null
     */
    private ScrollableArea(@NonNull final Parcel source) {
        this.topScrollableArea = (Area) source.readSerializable();
        this.bottomScrollableArea = (Area) source.readSerializable();
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

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(final Parcel dest, final int flags) {
        dest.writeSerializable(topScrollableArea);
        dest.writeSerializable(bottomScrollableArea);
    }

    @Override
    public final String toString() {
        return "ScrollableArea{" + "topScrollableArea=" + topScrollableArea +
                ", bottomScrollableArea=" + bottomScrollableArea + '}';
    }

    @Override
    public final int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (topScrollableArea == null ? 0 : topScrollableArea.hashCode());
        result = prime * result +
                (bottomScrollableArea == null ? 0 : bottomScrollableArea.hashCode());
        return result;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScrollableArea other = (ScrollableArea) obj;
        return topScrollableArea == other.topScrollableArea &&
                bottomScrollableArea.equals(other.bottomScrollableArea);
    }

}