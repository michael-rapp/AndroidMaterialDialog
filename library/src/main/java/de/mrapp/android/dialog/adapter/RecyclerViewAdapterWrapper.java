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
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.FrameLayout;

import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.ListDialog.OnItemSelectedListener;
import de.mrapp.android.util.ThemeUtil;
import de.mrapp.util.Condition;

/**
 * A wrapper that encapsulates a recycler view adapter in order to manage the selection states of
 * the adapter's list items.
 *
 * @param <VH>
 *         The type of the view holder of the encapsulated adapter
 * @author Michael Rapp
 * @since 5.0.0
 */
public class RecyclerViewAdapterWrapper<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<RecyclerViewAdapterWrapper.ViewHolderWrapper<VH>> {

    /**
     * Defines the interface, a class, which should be notified when a list item has been clicked,
     * must implement.
     */
    public interface OnItemClickListener {

        /**
         * The method, which is invoked, when a list item has been clicked.
         *
         * @param adapter
         *         The adapter, the list item belongs to, as an instance of the class {@link
         *         RecyclerViewAdapterWrapper}. The adapter may not be null
         * @param position
         *         The position of the list item, which has been clicked, as an {@link Integer}
         *         value
         */
        void onItemClick(@NonNull RecyclerViewAdapterWrapper<?> adapter, int position);

    }

    /**
     * Defines the interface, a choice mode, which manages the selection state of list items, must
     * implement.
     */
    public interface ChoiceMode {

        /**
         * Returns, whether the list item at a specific position is currently selected, or not.
         *
         * @param position
         *         The position of the list item, whose selection state should be returned, as an
         *         {@link Integer} value
         * @return True, if the list item is selected, false otherwise
         */
        boolean isItemChecked(int position);

        /**
         * Sets whether the list item at a specific position should be selected, or not.
         *
         * @param position
         *         The position of the list item, whose selection state should be changed, as an
         *         {@link Integer} value
         * @param checked
         *         True, if the list item should be selected, false otherwise
         * @return True, if the selection of the list item has been changed, false otherwise
         */
        boolean setItemChecked(int position, boolean checked);

    }

    /**
     * A choice mode, which allows only a single list item to be selected at once.
     */
    public static class SingleChoiceMode implements ChoiceMode {

        /**
         * The position of the currently selected list item.
         */
        private int checkedItem = 0;

        @Override
        public final boolean isItemChecked(final int position) {
            return checkedItem == position;
        }

        @Override
        public final boolean setItemChecked(final int position, final boolean checked) {
            if (checked && checkedItem != position) {
                checkedItem = position;
                return true;
            }

            return false;
        }

    }

    /**
     * A choice mode, which allows multiple list items to be selected at once.
     */
    public static class MultipleChoiceMode implements ChoiceMode {

        /**
         * A set, which contains the positions of all currently selected list items.
         */
        private final Set<Integer> checkedItems = new HashSet<>();

        @Override
        public final boolean isItemChecked(final int position) {
            return checkedItems.contains(position);
        }

        @Override
        public final boolean setItemChecked(final int position, final boolean checked) {
            if (checked) {
                return checkedItems.add(position);
            } else {
                return checkedItems.remove(position);
            }
        }

    }

    /**
     * A choice mode, which allows no list items to be selected at all.
     */
    public static class NoChoiceMode implements ChoiceMode {

        @Override
        public final boolean isItemChecked(final int position) {
            return false;
        }

        @Override
        public final boolean setItemChecked(final int position, final boolean checked) {
            return false;
        }

    }

    /**
     * A wrapper, which encapsulates a view holder.
     *
     * @param <T>
     *         The type of the encapsulated view holder
     */
    public static class ViewHolderWrapper<T extends RecyclerView.ViewHolder>
            extends RecyclerView.ViewHolder {

        /**
         * The encapsulated view holder.
         */
        private final T wrappedViewHolder;

        /**
         * Creates a new wrapper, which encapsulates a specific view holder.
         *
         * @param itemView
         *         The view, the wrapper corresponds to, as an instance of the class {@link View}.
         *         The view may not be null
         * @param wrappedViewHolder
         *         The encapsulated view holder as an instance of the generic type T. The view
         *         holder may not be null
         */
        public ViewHolderWrapper(@NonNull final View itemView, @NonNull final T wrappedViewHolder) {
            super(itemView);
            Condition.INSTANCE.ensureNotNull(wrappedViewHolder, "The view holder may not be null");
            this.wrappedViewHolder = wrappedViewHolder;
        }

        /**
         * Returns the encapsulated view holder.
         *
         * @return The encapsulated view holder as an instance of the generic type T. The view
         * holder may not be null
         */
        @NonNull
        public T getWrappedViewHolder() {
            return wrappedViewHolder;
        }

    }

    /**
     * The encapsulated recycler view adapter.
     */
    private final RecyclerView.Adapter<VH> wrappedAdapter;

    /**
     * The choice mode, which is used by the adapter.
     */
    private final ChoiceMode choiceMode;

    /**
     * The handler, which is used to adapt the selection state of list items.
     */
    private final Handler handler;

    /**
     * The listener, which is notified, when a list item has been clicked.
     */
    private OnItemClickListener itemClickListener;

    /**
     * The listener, which is notified, when a list item has been selected.
     */
    private OnItemSelectedListener itemSelectedListener;

    /**
     * Creates a listener, which allows to select a list item when it has been clicked.
     *
     * @param position
     *         The position of the list item as an {@link Integer} value
     * @return The listener, which has been created, as an instance of the type {@link
     * View.OnClickListener}. The listener may not be null
     */
    @NonNull
    private View.OnClickListener createItemClickListener(final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setItemChecked(position, !isItemChecked(position));

                if (itemClickListener != null) {
                    itemClickListener.onItemClick(RecyclerViewAdapterWrapper.this, position);
                }
            }

        };
    }

    /**
     * Creates and returns a runnable, which allows to change the selection state of a specific
     * {@link Checkable}.
     *
     * @param checkable
     *         The checkable, whose selection state should be changed, as an instance of the type
     *         {@link Checkable}. The checkable may not be null.
     * @param checked
     *         True, if the checkable should be selected, false otherwise
     * @return The runnable, which has been created, as an instance of the type {@link Runnable}.
     * The runnable may not be null
     */
    @NonNull
    private Runnable createCheckableRunnable(@NonNull final Checkable checkable,
                                             final boolean checked) {
        return new Runnable() {

            @Override
            public void run() {
                checkable.setChecked(checked);
            }

        };
    }

    /**
     * Creates a new wrapper that encapsulates a recycler view adapter in order to manage the
     * selection states of the adapter's list items.
     *
     * @param context
     *         The context, which should be used by the wrapper, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param wrappedAdapter
     *         The encapsulated recycler view adapter as an instance of the class
     *         RecyclerView.Adapter. The adapter may not be null
     * @param choiceMode
     *         The choice mode, which should be used by the adapter, as an instance of the type
     *         {@link ChoiceMode}. The choice mode may not be null
     */
    public RecyclerViewAdapterWrapper(@NonNull final Context context,
                                      @NonNull final RecyclerView.Adapter<VH> wrappedAdapter,
                                      @NonNull final ChoiceMode choiceMode) {
        Condition.INSTANCE.ensureNotNull(context, "The context may not be null");
        Condition.INSTANCE.ensureNotNull(wrappedAdapter, "The wrapped adapter may not be null");
        Condition.INSTANCE.ensureNotNull(choiceMode, "The choice mode may not be null");
        this.wrappedAdapter = wrappedAdapter;
        this.choiceMode = choiceMode;
        this.handler = new Handler(context.getMainLooper());
    }

    /**
     * Sets the listener, which should be notified, when a list item has been clicked.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         OnItemClickListener} or null, if no listener should be notified
     */
    public final void setOnItemClickListener(@Nullable final OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    /**
     * Sets the listener, which should be notified, when a list item has been selected.
     *
     * @param listener
     *         The listener, which should be set, as an instance of the type {@link
     *         OnItemSelectedListener} or null, if no listener should be notified
     */
    public final void setOnItemSelectedListener(@Nullable final OnItemSelectedListener listener) {
        this.itemSelectedListener = listener;
    }

    /**
     * Returns the choice mode, which is used by the adapter.
     *
     * @return The choice mode, which is used by the adapter, as an instance of the type {@link
     * ChoiceMode}. The choice mode may not be null
     */
    @NonNull
    public final ChoiceMode getChoiceMode() {
        return choiceMode;
    }

    /**
     * Returns the encapsulated adapter.
     *
     * @return The encapsulated adapter as an instance of the class RecyclerView.Adapter. The
     * adapter may not be null
     */
    @NonNull
    public final RecyclerView.Adapter<VH> getWrappedAdapter() {
        return wrappedAdapter;
    }

    /**
     * Returns, whether the list item at a specific position is currently selected, or not.
     *
     * @param position
     *         The position of the list item, whose selection state should be returned, as an {@link
     *         Integer} value
     * @return True, if the list item is selected, false otherwise
     */
    public final boolean isItemChecked(final int position) {
        return choiceMode.isItemChecked(position);
    }

    /**
     * Sets, whether the list item at a specific position should be selected, or not.
     *
     * @param position
     *         The position of the list item, whose selection state should be changed, as an {@link
     *         Integer} value
     * @param checked
     *         True, if the list item should be selected, false otherwise
     */
    public final void setItemChecked(final int position, final boolean checked) {
        if (choiceMode.setItemChecked(position, checked)) {
            notifyDataSetChanged();

            if (checked && itemSelectedListener != null) {
                itemSelectedListener.onItemSelected(position);
            }
        }
    }

    @NonNull
    @Override
    public final ViewHolderWrapper<VH> onCreateViewHolder(@NonNull final ViewGroup parent,
                                                          final int viewType) {
        VH viewHolder = wrappedAdapter.createViewHolder(parent, viewType);
        Context context = parent.getContext();
        FrameLayout frameLayout = new FrameLayout(context);
        frameLayout.setForeground(ThemeUtil.getDrawable(context, R.attr.selectableItemBackground));
        frameLayout.addView(viewHolder.itemView);
        frameLayout.setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT));
        return new ViewHolderWrapper<>(frameLayout, viewHolder);
    }

    @Override
    public final void onBindViewHolder(@NonNull final ViewHolderWrapper<VH> holder,
                                       final int position) {
        final VH wrappedViewHolder = holder.getWrappedViewHolder();
        wrappedAdapter.onBindViewHolder(wrappedViewHolder, position);
        View view = holder.itemView;
        view.setOnClickListener(createItemClickListener(position));

        if (view instanceof Checkable) {
            handler.post(createCheckableRunnable((Checkable) view, isItemChecked(position)));
        }
    }

    @Override
    public final int getItemCount() {
        return wrappedAdapter.getItemCount();
    }

    @Override
    public final long getItemId(final int position) {
        return wrappedAdapter.getItemId(position);
    }

    @Override
    public final int getItemViewType(final int position) {
        return wrappedAdapter.getItemViewType(position);
    }

}
