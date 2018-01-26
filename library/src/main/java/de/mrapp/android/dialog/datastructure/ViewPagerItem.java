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
package de.mrapp.android.dialog.datastructure;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.io.Serializable;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * Represents one item of a view pager.
 *
 * @author Michael Rapp
 * @since 3.6.0
 */
public class ViewPagerItem implements Serializable, Parcelable {

    /**
     * A creator, which allows to create instances of the class {@link ViewPagerItem} from parcels.
     */
    public static final Creator<ViewPagerItem> CREATOR = new Creator<ViewPagerItem>() {

        @Override
        public ViewPagerItem createFromParcel(final Parcel in) {
            return new ViewPagerItem(in);
        }

        @Override
        public ViewPagerItem[] newArray(final int size) {
            return new ViewPagerItem[size];
        }

    };

    /**
     * The constant serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The item's title.
     */
    private final CharSequence title;

    /**
     * The class of the item's fragment.
     */
    private final Class<? extends Fragment> fragmentClass;

    /**
     * Optional arguments, which are passed to the item's fragmentClass.
     */
    private final Bundle arguments;

    /**
     * Creates a new representation of one item of a view pager.
     *
     * @param source
     *         The source, the representation should be created from, as an instance of the class
     *         {@link Parcel}. The source may not be null
     */
    @SuppressWarnings("unchecked")
    private ViewPagerItem(@NonNull final Parcel source) {
        this.title = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        this.fragmentClass = (Class<? extends Fragment>) source.readSerializable();
        this.arguments = source.readBundle(getClass().getClassLoader());
    }

    /**
     * Creates a new representation of one item of a view pager.
     *
     * @param title
     *         The item's title as an instance of the type {@link CharSequence} or null, if no title
     *         should be shown
     * @param fragmentClass
     *         The class of the item's fragment as an instance of the class {@link Class}. The class
     *         may not be null
     * @param arguments
     *         Optional arguments, which may be passed to the item's fragment, as an instance of the
     *         class {@link Bundle} or null, if no arguments should be passed to the fragmentClass
     */
    public ViewPagerItem(@Nullable final CharSequence title,
                         @NonNull final Class<? extends Fragment> fragmentClass,
                         @Nullable final Bundle arguments) {
        ensureNotNull(fragmentClass, "The fragmentClass class may not be null");
        this.title = title;
        this.fragmentClass = fragmentClass;
        this.arguments = arguments;
    }

    /**
     * Returns the item's title.
     *
     * @return The item's title as an instance of the type {@link CharSequence} or null, if no title
     * is shown
     */
    @Nullable
    public final CharSequence getTitle() {
        return title;
    }

    /**
     * Returns the class of the item's fragment.
     *
     * @return The class of the item's fragment as an instance of the class {@link Class}. The class
     * may not be null
     */
    @NonNull
    public final Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    /**
     * Returns the optional arguments, which are passed to the item's fragment.
     *
     * @return The optional arguments, which are passed to the item's fragment, as an instance of
     * the class {@link Bundle} or null, if no arguments are passed to the fragment
     */
    @Nullable
    public final Bundle getArguments() {
        return arguments;
    }

    @Override
    public final int describeContents() {
        return 0;
    }

    @Override
    public final void writeToParcel(final Parcel dest, final int flags) {
        TextUtils.writeToParcel(getTitle(), dest, flags);
        dest.writeSerializable(getFragmentClass());
        dest.writeBundle(getArguments());
    }

}