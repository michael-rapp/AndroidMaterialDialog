/*
 * AndroidMaterialDialog Copyright 2014 - 2016 Michael Rapp
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package de.mrapp.android.dialog.adapter;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

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
     * The control color of the adapter's items.
     */
    private int itemControlColor;

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
        this.itemControlColor = -1;
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
        this.itemControlColor = -1;
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
        this.itemControlColor = -1;
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
        this.itemControlColor = -1;
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
        this.itemControlColor = -1;
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
        this.itemControlColor = -1;
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
    }

    /**
     * Returns the control color of the adapter's items.
     *
     * @return The control color of the adapter's items as an {@link Integer} value or -1, if no
     * custom color has been set
     */
    public final int getItemControlColor() {
        return itemControlColor;
    }

    /**
     * Sets the control color of the adapter's items.
     *
     * @param color
     *         The control color, which should be set, as an {@link Integer} value or -1, if no
     *         custom color should be set
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public final void setItemControlColor(@ColorInt final int color) {
        this.itemControlColor = color;
    }

    @SuppressLint("NewApi")
    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

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

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                        getItemControlColor() != -1 && textView instanceof CheckedTextView) {
                    CheckedTextView checkedTextView = (CheckedTextView) textView;
                    ColorFilter colorFilter =
                            new PorterDuffColorFilter(getItemControlColor(), Mode.SRC_IN);
                    checkedTextView.getCheckMarkDrawable().setColorFilter(colorFilter);
                }
            }
        }

        return view;
    }

}