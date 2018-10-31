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
package de.mrapp.android.dialog.decorator;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.adapter.ArrayAdapter;
import de.mrapp.android.dialog.listener.OnItemClickListenerWrapper;
import de.mrapp.android.dialog.listener.OnMultiChoiceClickListenerWrapper;
import de.mrapp.android.dialog.model.ButtonBarDialog;
import de.mrapp.android.dialog.view.DialogRootView.AreaViewType;
import de.mrapp.android.dialog.view.DialogRootView.DividerLocation;
import de.mrapp.android.dialog.view.DialogRootView.DividerViewType;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;
import de.mrapp.android.dialog.view.Divider;
import de.mrapp.android.util.ArrayUtil;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material Design guidelines even on pre-Lollipop devices and may contain list
 * items.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ListDialogDecorator extends AbstractDialogDecorator<ButtonBarDialog>
        implements de.mrapp.android.dialog.model.ListDialogDecorator {

    /**
     * The name of the extra, which is used to store the color of the list items of the dialog
     * within a bundle.
     */
    private static final String ITEM_COLOR_EXTRA =
            ListDialogDecorator.class.getSimpleName() + "::itemColor";

    /**
     * The name of the extra, which is used to store the dialog's items within a bundle.
     */
    private static final String ITEMS_EXTRA = ListDialogDecorator.class.getSimpleName() + "::items";

    /**
     * The name of the extra, which is used to store the dialog's single choice items within a
     * bundle.
     */
    private static final String SINGLE_CHOICE_ITEMS_EXTRA =
            ListDialogDecorator.class.getSimpleName() + "::singleChoiceItems";

    /**
     * The name of the extra, which is used to store the dialog's mnultiple choice items within a
     * bundle.
     */
    private static final String MULTI_CHOICE_ITEMS_EXTRA =
            ListDialogDecorator.class.getSimpleName() + "::multiChoiceItems";

    /**
     * The name of the extra, which is used to store the checked items within a bundle.
     */
    private static final String CHECKED_ITEMS_EXTRA =
            ListDialogDecorator.class.getSimpleName() + "::checkedItems";

    /**
     * The list view, which is used to show the dialog's list items.
     */
    private ListView listView;

    /**
     * The divider, which is shown above the list view.
     */
    private Divider listDivider;

    /**
     * True, if the custom view of the dialog has been inflated by the decorator itself, false
     * otherwise.
     */
    private boolean inflatedCustomView;

    /**
     * The color of the list items of the dialog.
     */
    private int itemColor;

    /**
     * The adapter, which is used to manage the list items of the dialog.
     */
    private ListAdapter adapter;

    /**
     * The choice mode of the list view, which is used to show the list items of the dialog.
     */
    private int choiceMode = -1;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_SINGLE</code>.
     */
    private DialogInterface.OnClickListener singleChoiceListener;

    /**
     * The listener, which is notified, when the selection of a list item of the dialog changes and
     * the list view's choice mode is <code>ListView.CHOICE_MODE_MULTIPLE</code>.
     */
    private DialogInterface.OnMultiChoiceClickListener multiChoiceListener;

    /**
     * The listener, which is notified, when a list item of the dialog becomes selected,
     * irrespective of the list view's choice mode.
     */
    private AdapterView.OnItemSelectedListener listViewItemSelectedListener;

    /**
     * An array, which is used to identify the list items of the dialog, which is created by the
     * builder, which are checked by default.
     */
    private boolean[] checkedItems;

    /**
     * The dialog's items.
     */
    private CharSequence[] items;

    /**
     * The dialog's single choice items.
     */
    private CharSequence[] singleChoiceItems;

    /**
     * The dialog's multiple choice items.
     */
    private CharSequence[] multiChoiceItems;

    /**
     * Inflates the list view, which is used to show the dialog's list items.
     *
     * @param contentContainer
     *         The view group, which contains the dialog's content, as an instance of the class
     *         {@link ViewGroup}. The view group may not be null
     */
    private void inflateListView(@NonNull final ViewGroup contentContainer) {
        View listView = contentContainer.findViewById(android.R.id.list);
        this.listView = listView instanceof ListView ? (ListView) listView : null;

        if (this.listView == null && !getDialog().isCustomViewUsed()) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            View view = layoutInflater
                    .inflate(R.layout.material_dialog_list_view, contentContainer, false);
            de.mrapp.android.dialog.view.ListView inflatedListView =
                    view.findViewById(android.R.id.list);
            inflatedListView.setDialog(getDialog());
            this.listView = inflatedListView;
            getDialog().setView(view);
            this.inflatedCustomView = true;
        } else {
            this.inflatedCustomView = false;
        }

        View divider = contentContainer.findViewById(R.id.list_divider);
        this.listDivider = divider instanceof Divider ? (Divider) divider : null;
    }

    /**
     * Attaches the adapter to the dialog's list view.
     */
    private void attachAdapter() {
        if (listView != null) {
            if (adapter != null) {
                listView.setAdapter(adapter);
                listView.setVisibility(adapter != null ? View.VISIBLE : View.GONE);
                listView.setOnItemSelectedListener(listViewItemSelectedListener);
                listView.setChoiceMode(choiceMode);
                initializeSelectionListener();
                initializeCheckedItems();
                adaptItemColor();
            } else {
                if (inflatedCustomView) {
                    getDialog().setView(null);
                }

                listView = null;
            }
        }
    }

    /**
     * Initializes the list items, which are checked by default.
     */
    private void initializeCheckedItems() {
        if (checkedItems != null) {
            for (int i = 0; i < checkedItems.length; i++) {
                listView.setItemChecked(i, checkedItems[i]);

                if (checkedItems[i]) {
                    listView.setSelection(i);
                }
            }
        }
    }

    /**
     * Returns an array, which identifies the currently checked list items.
     *
     * @return An array, which identifies the currently checked list items, as {@link Boolean} array
     */
    @Nullable
    private boolean[] getCheckedItems() {
        if (listView != null && adapter != null) {
            boolean[] result = new boolean[adapter.getCount()];

            for (int i = 0; i < result.length; i++) {
                result[i] = listView.isItemChecked(i);
            }

            return result;
        } else {
            return checkedItems;
        }
    }

    /**
     * Initializes the listener, which should be notified, when the selection of a list item of the
     * dialog has been changed.
     */
    private void initializeSelectionListener() {
        if (choiceMode == ListView.CHOICE_MODE_NONE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(singleChoiceListener, getDialog(),
                            DialogInterface.BUTTON_POSITIVE));
        } else if (choiceMode == ListView.CHOICE_MODE_SINGLE) {
            listView.setOnItemClickListener(
                    new OnItemClickListenerWrapper(singleChoiceListener, getDialog(), 0));
        } else if (choiceMode == ListView.CHOICE_MODE_MULTIPLE) {
            listView.setOnItemClickListener(
                    new OnMultiChoiceClickListenerWrapper(multiChoiceListener, getDialog(), 0));
        }
    }

    /**
     * Adapts the color of the dialog's list items.
     */
    private void adaptItemColor() {
        ListAdapter listAdapter = getListAdapter();

        if (listAdapter instanceof ArrayAdapter<?>) {
            ((ArrayAdapter<?>) listAdapter).setItemColor(itemColor);
        }
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
     * may contain list items.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the dialog, as an instance of
     *         the type {@link ButtonBarDialog}. The dialog may not be null
     */
    public ListDialogDecorator(@NonNull final ButtonBarDialog dialog) {
        super(dialog);
    }

    @Override
    public final ListView getListView() {
        return listView != null && listView.getVisibility() == View.VISIBLE ? listView : null;
    }

    @Override
    public final ListAdapter getListAdapter() {
        return adapter;
    }

    @Override
    public final int getItemColor() {
        return itemColor;
    }

    @Override
    public final void setItemColor(@ColorInt final int color) {
        itemColor = color;
        adaptItemColor();
    }

    @Override
    public final void setItems(@Nullable final CharSequence[] items,
                               @Nullable final DialogInterface.OnClickListener listener) {
        this.items = items;
        this.singleChoiceItems = null;
        this.multiChoiceItems = null;
        this.adapter = items != null ?
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items) : null;
        this.singleChoiceListener = items != null ? listener : null;
        this.multiChoiceListener = null;
        this.choiceMode = items != null ? ListView.CHOICE_MODE_NONE : -1;
        this.checkedItems = null;
        attachAdapter();
    }

    @Override
    public final void setItems(@ArrayRes final int resourceId,
                               @Nullable final DialogInterface.OnClickListener listener) {
        setItems(getContext().getResources().getTextArray(resourceId), listener);
    }

    @Override
    public final void setAdapter(@Nullable final ListAdapter adapter,
                                 @Nullable final DialogInterface.OnClickListener listener) {
        this.items = null;
        this.singleChoiceItems = null;
        this.multiChoiceItems = null;
        this.adapter = adapter;
        this.singleChoiceListener = adapter != null ? listener : null;
        this.multiChoiceListener = null;
        this.choiceMode = adapter != null ? ListView.CHOICE_MODE_NONE : -1;
        this.checkedItems = null;
        attachAdapter();
    }

    @Override
    public final void setSingleChoiceItems(@Nullable final CharSequence[] items,
                                           final int checkedItem,
                                           @Nullable final DialogInterface.OnClickListener listener) {
        this.items = null;
        this.singleChoiceItems = items;
        this.multiChoiceItems = null;
        this.adapter = items != null ?
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice,
                        items) : null;
        this.singleChoiceListener = items != null ? listener : null;
        this.multiChoiceListener = null;
        this.choiceMode = items != null ? ListView.CHOICE_MODE_SINGLE : -1;
        this.checkedItems = items != null ? new boolean[items.length] : null;

        if (this.checkedItems != null && checkedItem >= 0) {
            this.checkedItems[checkedItem] = true;
        }

        attachAdapter();
    }

    @Override
    public final void setSingleChoiceItems(@ArrayRes final int resourceId, final int checkedItem,
                                           @Nullable final DialogInterface.OnClickListener listener) {
        setSingleChoiceItems(getContext().getResources().getTextArray(resourceId), checkedItem,
                listener);
    }

    @Override
    public final void setSingleChoiceItems(@Nullable final ListAdapter adapter,
                                           final int checkedItem,
                                           @Nullable final DialogInterface.OnClickListener listener) {
        this.items = null;
        this.singleChoiceItems = null;
        this.multiChoiceItems = null;
        this.adapter = adapter;
        this.singleChoiceListener = adapter != null ? listener : null;
        this.multiChoiceListener = null;
        this.choiceMode = adapter != null ? ListView.CHOICE_MODE_SINGLE : -1;
        this.checkedItems = adapter != null ? new boolean[adapter.getCount()] : null;

        if (this.checkedItems != null && checkedItem >= 0) {
            this.checkedItems[checkedItem] = true;
        }

        attachAdapter();
    }

    @Override
    public final void setMultiChoiceItems(@Nullable final CharSequence[] items,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        this.items = null;
        this.singleChoiceItems = null;
        this.multiChoiceItems = items;
        this.adapter = items != null ?
                new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice,
                        items) : null;
        this.singleChoiceListener = null;
        this.multiChoiceListener = items != null ? listener : null;
        this.choiceMode = items != null ? ListView.CHOICE_MODE_MULTIPLE : -1;
        this.checkedItems = items != null ? checkedItems : null;
        attachAdapter();
    }

    @Override
    public final void setMultiChoiceItems(@ArrayRes final int resourceId,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        setMultiChoiceItems(getContext().getResources().getTextArray(resourceId), checkedItems,
                listener);
    }

    @Override
    public final void setMultiChoiceItems(@Nullable final ListAdapter adapter,
                                          @Nullable final boolean[] checkedItems,
                                          @Nullable final DialogInterface.OnMultiChoiceClickListener listener) {
        this.items = null;
        this.singleChoiceItems = null;
        this.multiChoiceItems = null;
        this.adapter = adapter;
        this.singleChoiceListener = null;
        this.multiChoiceListener = adapter != null ? listener : null;
        this.choiceMode = adapter != null ? ListView.CHOICE_MODE_MULTIPLE : -1;
        this.checkedItems = adapter != null ? checkedItems : null;
        attachAdapter();
    }

    @Override
    public final void setOnItemSelectedListener(
            @Nullable final AdapterView.OnItemSelectedListener listener) {
        listViewItemSelectedListener = listener;

        if (listView != null) {
            listView.setOnItemSelectedListener(listViewItemSelectedListener);
        }
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putInt(ITEM_COLOR_EXTRA, getItemColor());

        if (items != null) {
            outState.putCharSequenceArray(ITEMS_EXTRA, items);
        } else if (singleChoiceItems != null) {
            outState.putCharSequenceArray(SINGLE_CHOICE_ITEMS_EXTRA, singleChoiceItems);
            outState.putBooleanArray(CHECKED_ITEMS_EXTRA, getCheckedItems());
        } else if (multiChoiceItems != null) {
            outState.putCharSequenceArray(MULTI_CHOICE_ITEMS_EXTRA, multiChoiceItems);
            outState.putBooleanArray(CHECKED_ITEMS_EXTRA, getCheckedItems());
        }
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        setItemColor(savedInstanceState.getInt(ITEM_COLOR_EXTRA));
        CharSequence[] items = savedInstanceState.getCharSequenceArray(ITEMS_EXTRA);

        if (items != null) {
            setItems(items, null);
        } else {
            boolean[] checkedItems = savedInstanceState.getBooleanArray(CHECKED_ITEMS_EXTRA);
            CharSequence[] singleChoiceItems =
                    savedInstanceState.getCharSequenceArray(SINGLE_CHOICE_ITEMS_EXTRA);

            if (singleChoiceItems != null) {
                int checkedItem = checkedItems != null ? ArrayUtil.indexOf(checkedItems, true) : 0;
                setSingleChoiceItems(singleChoiceItems, checkedItem, null);
            } else {
                CharSequence[] multiChoiceItems =
                        savedInstanceState.getCharSequenceArray(MULTI_CHOICE_ITEMS_EXTRA);

                if (multiChoiceItems != null) {
                    setMultiChoiceItems(multiChoiceItems, checkedItems, null);
                }
            }
        }
    }

    @NonNull
    @Override
    protected final Map<ViewType, View> onAttach(@NonNull final Window window,
                                                 @NonNull final View view,
                                                 @NonNull final Map<ViewType, View> areas,
                                                 final Void param) {
        View contentView = areas.get(new AreaViewType(Area.CONTENT));

        if (contentView instanceof ViewGroup) {
            inflateListView((ViewGroup) contentView);
            attachAdapter();
        }

        if (listDivider != null) {
            Map<ViewType, View> result = new HashMap<>();
            result.put(new DividerViewType(DividerLocation.TOP), listDivider);
            return result;
        }

        return Collections.emptyMap();
    }

    @Override
    protected final void onDetach() {
        this.listView = null;
    }

}