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
package de.mrapp.android.dialog.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import de.mrapp.android.dialog.datastructure.ViewPagerItem;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An adapter, which allows to manage the fragments of a view pager.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    /**
     * The context, which is used by the adapter.
     */
    private final Context context;

    /**
     * A list, which contains the adapter's items.
     */
    private final List<ViewPagerItem> items;

    /**
     * Creates a new adapter, which allows to manage the fragments of a view pager.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param fragmentManager
     *         The fragment manager, which should be used by the adapter, as an instance of the
     *         class FragmentManager. The fragment manager may not be null
     * @param items
     *         A list, which contains the adapter's items, as an instance of the type {@link List}.
     *         The list may not be null
     */
    public ViewPagerAdapter(@NonNull final Context context,
                            @NonNull final FragmentManager fragmentManager,
                            @NonNull final List<ViewPagerItem> items) {
        super(fragmentManager);
        ensureNotNull(context, "The context may not be null");
        ensureNotNull(fragmentManager, "The fragment manager may not be null");
        ensureNotNull(items, "The list may not be null");
        this.context = context;
        this.items = items;
    }

    /**
     * Adds a new fragment to the adapter.
     *
     * @param title
     *         The title of the fragment, which should be added, as an instance of the type {@link
     *         CharSequence} or null, if no title should be set
     * @param fragmentClass
     *         The class of the fragment, which should be added, as an instance of the class {@link
     *         Class}. The class may not be null
     * @param arguments
     *         A bundle, which should be passed to the fragment, when it is shown, as an instance of
     *         the class {@link Bundle} or null, if no arguments should be passed to the fragment
     */
    public final void addItem(@Nullable final CharSequence title,
                              @NonNull final Class<? extends Fragment> fragmentClass,
                              @Nullable final Bundle arguments) {
        ensureNotNull(fragmentClass, "The fragment class may not be null");
        items.add(new ViewPagerItem(title, fragmentClass, arguments));
        notifyDataSetChanged();
    }

    /**
     * Removes the fragment at a specific index from the adapter.
     *
     * @param index
     *         The index of the fragment, which should be removed, as an {@link Integer value}
     */
    public final void removeItem(final int index) {
        items.remove(index);
        notifyDataSetChanged();
    }

    /**
     * Removes all fragments from the adapter.
     */
    public final void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    @Override
    public final Fragment getItem(final int index) {
        ViewPagerItem item = items.get(index);
        Class<? extends Fragment> fragmentClass = item.getFragmentClass();
        Bundle arguments = item.getArguments();
        return Fragment.instantiate(context, fragmentClass.getName(), arguments);
    }

    @Override
    public final int getCount() {
        return items.size();
    }

    @Override
    public final CharSequence getPageTitle(final int position) {
        return items.get(position).getTitle();
    }

}