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
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.mrapp.android.dialog.R;

/**
 * An adapter, which is extended from the class {@link android.widget.ArrayAdapter} in order to be
 * able to customize the text and control color of the adapter's items.
 *
 * @param <Type>
 *         The type of the adapter's underlying data
 * @author Michael Rapp
 * @since 1.2.0
 */
public class ArrayAdapter<Type> extends android.widget.ArrayAdapter<Type> {

    /**
     * The id of the text view within the adapter's layout resource to be populated.
     */
    private final int textViewResourceId;

    /**
     * The text color of the adapter's items.
     */
    private int itemColor;

    /**
     * Creates a new array adapter.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param resourceId
     *         The resource id of the layout, which should be used by the adapter, as an {@link
     *         Integer} value. The resource id must correspond to a valid layout resource
     */
    public ArrayAdapter(@NonNull final Context context, final int resourceId) {
        super(context, resourceId);
        this.textViewResourceId = -1;
        this.itemColor = -1;
    }

    /**
     * Creates a new array adapter.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param resourceId
     *         The resource id of the layout, which should be used by the adapter, as an {@link
     *         Integer} value. The resource id must correspond to a valid layout resource
     * @param textViewResourceId
     *         The id of the text view within the layout resource to be populated, as an {@link
     *         Integer} value
     */
    public ArrayAdapter(@NonNull final Context context, final int resourceId,
                        final int textViewResourceId) {
        super(context, resourceId, textViewResourceId);
        this.textViewResourceId = textViewResourceId;
        this.itemColor = -1;
    }

    /**
     * Creates a new array adapter.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param resourceId
     *         The resource id of the layout, which should be used by the adapter, as an {@link
     *         Integer} value. The resource id must correspond to a valid layout resource
     * @param items
     *         The items of the adapter as an array of the generic type Type. The items may not be
     *         null
     */
    public ArrayAdapter(@NonNull final Context context, final int resourceId,
                        @NonNull final Type[] items) {
        super(context, resourceId, items);
        this.textViewResourceId = -1;
        this.itemColor = -1;
    }

    /**
     * Creates a new array adapter.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param resourceId
     *         The resource id of the layout, which should be used by the adapter, as an {@link
     *         Integer} value. The resource id must correspond to a valid layout resource
     * @param textViewResourceId
     *         The id of the text view within the layout resource to be populated, as an {@link
     *         Integer} value
     * @param items
     *         The items of the adapter as an array of the generic type Type. The items may not be
     *         null
     */
    public ArrayAdapter(@NonNull final Context context, final int resourceId,
                        final int textViewResourceId, @NonNull final Type[] items) {
        super(context, resourceId, textViewResourceId, items);
        this.textViewResourceId = textViewResourceId;
        this.itemColor = -1;
    }

    /**
     * Creates a new array adapter.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param resourceId
     *         The resource id of the layout, which should be used by the adapter, as an {@link
     *         Integer} value. The resource id must correspond to a valid layout resource
     * @param items
     *         A list, which contains the items of the adapter, as an instance of the type {@link
     *         List}. The list may not be null
     */
    public ArrayAdapter(@NonNull final Context context, final int resourceId,
                        @NonNull final List<Type> items) {
        super(context, resourceId, items);
        this.textViewResourceId = -1;
        this.itemColor = -1;
    }

    /**
     * Creates a new array adapter.
     *
     * @param context
     *         The context, which should be used by the adapter, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param resourceId
     *         The resource id of the layout, which should be used by the adapter, as an {@link
     *         Integer} value. The resource id must correspond to a valid layout resource
     * @param textViewResourceId
     *         The id of the text view within the layout resource to be populated, as an {@link
     *         Integer} value
     * @param items
     *         A list, which contains the items of the adapter, as an instance of the type {@link
     *         List}. The list may not be null
     */
    public ArrayAdapter(@NonNull final Context context, final int resourceId,
                        final int textViewResourceId, @NonNull final List<Type> items) {
        super(context, resourceId, textViewResourceId, items);
        this.textViewResourceId = textViewResourceId;
        this.itemColor = -1;
    }

    /**
     * Returns the text color of the adapter's items.
     *
     * @return The text color of the adapter's items as an {@link Integer} value or -1, if no custom
     * color has been set
     */
    public final int getItemColor() {
        return itemColor;
    }

    /**
     * Sets the text color of the adapter's items.
     *
     * @param color
     *         The text color, which should be set, as an {@link Integer} value or -1, if no custom
     *         color should be set
     */
    public final void setItemColor(@ColorInt final int color) {
        this.itemColor = color;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public final View getView(final int position, final View convertView,
                              @NonNull final ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        int leftPadding =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_left_padding);
        int rightPadding =
                getContext().getResources().getDimensionPixelSize(R.dimen.dialog_right_padding);
        view.setPadding(leftPadding, view.getPaddingTop(), rightPadding, view.getPaddingBottom());

        if (getItemColor() != -1) {
            TextView textView = null;

            if (textViewResourceId != -1) {
                View childView = view.findViewById(textViewResourceId);

                if (childView instanceof TextView) {
                    textView = (TextView) childView;
                }
            }

            if (textView == null && view instanceof TextView) {
                textView = (TextView) view;
            }

            if (textView != null) {
                textView.setTextColor(getItemColor());
            }
        }

        return view;
    }

}