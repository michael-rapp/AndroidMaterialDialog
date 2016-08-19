/*
 * Copyright 2014 - 2016 Michael Rapp
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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.ButtonBarDialog;
import de.mrapp.android.dialog.model.ListDialog;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material Design guidelines even on pre-Lollipop devices and may contain a
 * scrollable content.
 *
 * @author Michael Rapp
 * @since 3.6.0
 */
public class ScrollableDialogDecorator extends AbstractDialogDecorator<ListDialog>
        implements de.mrapp.android.dialog.model.ScrollableDialogDecorator, OnScrollListener {

    /**
     * The name of the extra, which is used to store, whether the dividers, which are located above
     * and below the dialog's content, should be shown, when the content is scrolled, or not, within
     * a bundle.
     */
    private static final String SHOW_DIVIDERS_ON_SCROLL_EXTRA =
            ScrollableDialogDecorator.class.getSimpleName() + "::showDividersOnScroll";

    /**
     * The divider, which is located above the dialog's content.
     */
    private View contentDivider;

    /**
     * The divider, which is located above the dialog's buttons.
     */
    private View buttonBarDivider;

    /**
     * The dialog's scrollable view.
     */
    private AbsListView scrollView;

    /**
     * True, if the dividers, which are located above and below the dialog's content, are shown,
     * when the content is scrolled, false otherwise
     */
    private boolean showDividersOnScroll;

    /**
     * Searches for a scrollable view in the dialog's view hierarchy and registers a scroll
     * listener, which allows to show the dividers, which are located above and below the dialog's
     * content, if possible.
     *
     * @param view
     *         The root view of the view hierarchy, the scrollable view should be searched in, as an
     *         instance of the class {@link View}. The view may not be null
     * @return True, if a scroll listener has been registered, false otherwise
     */
    private boolean registerScrollListener(@NonNull final View view) {
        if (view instanceof AbsListView) {
            scrollView = (AbsListView) view;
            scrollView.setOnScrollListener(this);
            return true;
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;

            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (registerScrollListener(viewGroup.getChildAt(i))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns, whether the last item of a scrollable view is fully visible, or not.
     *
     * @param scrollView
     *         The scrollable view as an instance of the class {@link AbsListView}. The view may not
     *         be null
     * @return True, if the last item is fully visible, false otherwise
     */
    private boolean isLastItemFullyVisible(@NonNull final AbsListView scrollView) {
        if (scrollView.getCount() > 0 && scrollView.getChildCount() > 0) {
            if (scrollView.getLastVisiblePosition() == scrollView.getCount() - 1) {
                View child = scrollView.getChildAt(scrollView.getChildCount() - 1);
                return child == null || child.getBottom() <= scrollView.getHeight();
            }
        } else {
            return true;
        }

        return false;
    }

    /**
     * Returns, whether the first item of a scrollable view is fully visible, or not.
     *
     * @param scrollView
     *         The scrollable view as an instance of the class {@link AbsListView}. The view may not
     *         be null
     * @return True, if the first item is fully visible, false otherwise
     */
    private boolean isFirstItemFullyVisible(@NonNull final AbsListView scrollView) {
        if (scrollView.getFirstVisiblePosition() == 0) {
            if (scrollView.getChildCount() == 0) {
                return true;
            } else {
                View child = scrollView.getChildAt(0);
                return child == null || child.getTop() == 0;
            }
        }

        return false;
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
     * may contain a scrollable content.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the dialog, as an instance of
     *         the type {@link ButtonBarDialog}. The dialog may not be null
     */
    public ScrollableDialogDecorator(@NonNull final ListDialog dialog) {
        super(dialog);
    }

    @Override
    public final boolean areDividersShownOnScroll() {
        return showDividersOnScroll;
    }

    @Override
    public final void showDividersOnScroll(final boolean show) {
        this.showDividersOnScroll = show;

        if (!show && buttonBarDivider != null && contentDivider != null) {
            buttonBarDivider.setVisibility(
                    getDialog().isButtonBarDividerShown() ? View.VISIBLE : View.GONE);
            contentDivider.setVisibility(View.GONE);
        }
    }

    @Override
    public final void onScrollStateChanged(final AbsListView view, final int scrollState) {

    }

    @Override
    public final void onScroll(final AbsListView view, final int firstVisibleItem,
                               final int visibleItemCount, final int totalItemCount) {
        if (showDividersOnScroll) {
            contentDivider.setVisibility(isFirstItemFullyVisible(view) ? View.GONE : View.VISIBLE);
            buttonBarDivider.setVisibility(getDialog().isButtonBarDividerShown() ? View.VISIBLE :
                    (isLastItemFullyVisible(view) ? View.GONE : View.VISIBLE));
        }
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putBoolean(SHOW_DIVIDERS_ON_SCROLL_EXTRA, areDividersShownOnScroll());
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        showDividersOnScroll(savedInstanceState.getBoolean(SHOW_DIVIDERS_ON_SCROLL_EXTRA));
    }

    @Override
    protected final void onAttach(@NonNull final View view) {
        contentDivider = view.findViewById(R.id.content_divider);
        buttonBarDivider = view.findViewById(R.id.button_bar_divider);
        View contentContainer = view.findViewById(R.id.content_container);
        registerScrollListener(contentContainer);
    }

    @Override
    protected final void onDetach() {
        contentDivider = null;
        buttonBarDivider = null;
        scrollView = null;
    }

}