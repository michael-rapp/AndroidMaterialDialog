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
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.mrapp.android.dialog.R;
import de.mrapp.util.Condition;

/**
 * A recycler view adapter, which displays list items that correspond to the texts that are
 * contained in an array.
 *
 * @author Michael Rapp
 * @since 5.0.0
 */
public class ArrayRecyclerViewAdapter
        extends RecyclerView.Adapter<ArrayRecyclerViewAdapter.ViewHolder> {

    /**
     * The view holder that is used by the adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * The text view, which is used to display the list item's texts.
         */
        private final TextView textView;

        /**
         * Creates a new view holder.
         *
         * @param itemView
         *         The view, the view holder corresponds to, as an instance of the class {@link
         *         View}. The view may not be null
         */
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            View childView = itemView.findViewById(android.R.id.text1);
            textView = childView instanceof TextView ? (TextView) childView :
                    (itemView instanceof TextView ? (TextView) itemView : null);
        }

    }

    /**
     * The id of the layout resource, which is used to display the list items.
     */
    private final int layoutResourceId;

    /**
     * An array, which contains the texts of the adapter's list items.
     */
    private final CharSequence[] items;

    /**
     * The text color of the adapter's list items.
     */
    private int itemColor;

    /**
     * Creates a new recycler view adapter, which displays list items that correspond to the texts
     * that are contained in an array.
     *
     * @param layoutResourceId
     *         The id of the layout resource, which should be used to display the list items, as an
     *         {@link Integer} value. The id must correspond to a valid layout resource
     * @param items
     *         An array, which contains the texts of the list adapter's items, as a {@link
     *         CharSequence} array. The array may not be null
     */
    public ArrayRecyclerViewAdapter(@LayoutRes final int layoutResourceId,
                                    @NonNull final CharSequence[] items) {
        Condition.INSTANCE.ensureNotNull(items, "The array may not be null");
        this.layoutResourceId = layoutResourceId;
        this.items = items;
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
    public final ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent,
                                               final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layoutResourceId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public final void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        View view = holder.itemView;
        Context context = view.getContext();
        Resources resources = context.getResources();
        int leftPadding = resources.getDimensionPixelSize(R.dimen.dialog_left_padding);
        int rightPadding = resources.getDimensionPixelSize(R.dimen.dialog_right_padding);
        view.setPadding(leftPadding, view.getPaddingTop(), rightPadding, view.getPaddingBottom());
        TextView textView = holder.textView;

        if (textView != null) {
            textView.setText(items[position]);

            if (getItemColor() != -1) {
                textView.setTextColor(getItemColor());
            }
        }
    }

    @Override
    public final int getItemCount() {
        return items.length;
    }

}
