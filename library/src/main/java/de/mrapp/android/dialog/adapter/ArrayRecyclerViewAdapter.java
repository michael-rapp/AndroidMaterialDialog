/*
 * Copyright 2014 - 2019 Michael Rapp
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
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
         * The text view, which is used to display the list item's text.
         */
        private final TextView textView;

        /**
         * The image view, which is used to display the list item's icon.
         */
        private final ImageView imageView;

        /**
         * Creates a new view holder.
         *
         * @param itemView The view, the view holder corresponds to, as an instance of the class {@link
         *                 View}. The view may not be null
         */
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            View textView = itemView.findViewById(android.R.id.text1);
            this.textView = textView instanceof TextView ? (TextView) textView :
                    (itemView instanceof TextView ? (TextView) itemView : null);
            View imageView = itemView.findViewById(android.R.id.icon);
            this.imageView = imageView instanceof ImageView ? (ImageView) imageView : null;
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
     * An array, which contains the resource ids of the list items' icons.
     */
    private final int[] iconResourceIds;

    /**
     * The text color of the adapter's list items.
     */
    private ColorStateList itemColor;

    /**
     * The typeface of the adapter's list items.
     */
    private Typeface itemTypeface;

    /**
     * Adapts the padding of a list item.
     *
     * @param holder The view holder as an instance of the class {@link ViewHolder}. The view holder
     *               may not be null
     */
    private void adaptPadding(@NonNull final ViewHolder holder) {
        View view = holder.itemView;
        Context context = view.getContext();
        Resources resources = context.getResources();
        int leftPadding = resources.getDimensionPixelSize(R.dimen.dialog_left_padding);
        int rightPadding = resources.getDimensionPixelSize(R.dimen.dialog_right_padding);
        view.setPadding(leftPadding, view.getPaddingTop(), rightPadding, view.getPaddingBottom());
    }

    /**
     * Adapts the text of a list item.
     *
     * @param holder   The view holder as an instance of the class {@link ViewHolder}. The view
     *                 holder may not be null
     * @param position The position of the list item as an {@link Integer} value
     */
    private void adaptText(@NonNull final ViewHolder holder, final int position) {
        TextView textView = holder.textView;

        if (textView != null) {
            textView.setText(items[position]);

            if (getItemColor() != null) {
                textView.setTextColor(getItemColor());
            }

            if (getItemTypeface() != null) {
                textView.setTypeface(getItemTypeface());
            }
        }
    }

    /**
     * Adapts the icon of a list item.
     *
     * @param holder   The view holder as an instance of the class {@link ViewHolder}. The view
     *                 holder may not be null
     * @param position The position of the list item as an {@link Integer} value
     */
    private void adaptIcon(@NonNull final ViewHolder holder, final int position) {
        if (iconResourceIds != null) {
            ImageView imageView = holder.imageView;

            if (imageView != null) {
                Context context = imageView.getContext();
                Drawable icon = ActivityCompat.getDrawable(context, iconResourceIds[position]);
                imageView.setImageDrawable(icon);
            }
        }
    }

    /**
     * Creates a new recycler view adapter, which displays list items that correspond to the texts
     * that are contained in an array.
     *
     * @param layoutResourceId The id of the layout resource, which should be used to display the list items, as an
     *                         {@link Integer} value. The id must correspond to a valid layout resource
     * @param items            An array, which contains the texts of the list adapter's items, as a {@link
     *                         CharSequence} array. The array may not be null
     * @param iconResourceIds  An array, which contains the resource ids of the items' icons, as an {@link Integer}
     *                         array or null, if no icons should be displayed
     */
    public ArrayRecyclerViewAdapter(@LayoutRes final int layoutResourceId,
                                    @NonNull final CharSequence[] items,
                                    @Nullable final int[] iconResourceIds) {
        Condition.INSTANCE.ensureNotNull(items, "The array may not be null");
        Condition.INSTANCE.ensureTrue(iconResourceIds == null ||
                        items.length == iconResourceIds.length,
                "Invalid number of icon resource ids given");
        this.layoutResourceId = layoutResourceId;
        this.items = items;
        this.iconResourceIds = iconResourceIds;
        this.itemColor = null;
        this.itemTypeface = null;
    }

    /**
     * Returns the text color of the adapter's items.
     *
     * @return The text color of the adapter's items as an instance of the class
     * {@link ColorStateList} or null, if no custom color has been set
     */
    @Nullable
    public final ColorStateList getItemColor() {
        return itemColor;
    }

    /**
     * Sets the text color of the adapter's items.
     *
     * @param colorStateList The text color, which should be set, as an instance of the class
     *                       {@link ColorStateList} or null, if no custom color should be set
     */
    public final void setItemColor(@Nullable final ColorStateList colorStateList) {
        this.itemColor = colorStateList;
        notifyDataSetChanged();
    }

    /**
     * Returns the typeface of the adapter's items.
     *
     * @return The typeface of the adapter's items, as an instance of the class {@link Typeface} or
     * null, if the default typeface is used
     */
    @Nullable
    public final Typeface getItemTypeface() {
        return itemTypeface;
    }

    /**
     * Sets the typeface of the adapter's items.
     *
     * @param typeface The typeface, which should be set, as an instance of the class {@link Typeface}. The
     *                 typeface may not be null
     */
    public void setItemTypeface(@NonNull final Typeface typeface) {
        Condition.INSTANCE.ensureNotNull(typeface, "The typeface may not be null");
        this.itemTypeface = typeface;
        notifyDataSetChanged();
    }

    @Override
    public final int getItemCount() {
        return items.length;
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
        adaptPadding(holder);
        adaptText(holder, position);
        adaptIcon(holder, position);
    }

}
