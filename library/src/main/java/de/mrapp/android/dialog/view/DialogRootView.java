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
package de.mrapp.android.dialog.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.ScrollableArea;
import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.listener.AreaListener;
import de.mrapp.android.dialog.view.ScrollView.ScrollListener;
import de.mrapp.android.util.ViewUtil;
import de.mrapp.util.Condition;

import static de.mrapp.android.util.DisplayUtil.dpToPixels;

/**
 * The root view of a dialog, which is designed according to Android 5's Material Design guidelines
 * even on pre-Lollipop devices. It is used to restrict the maximum height of the dialog on tablets
 * in landscape mode.
 *
 * @author Michael Rapp
 * @since 3.4.3
 */
public class DialogRootView extends LinearLayout implements AreaListener {

    /**
     * Defines the interface, a class, which represents a view type of a dialog, must implement.
     */
    public interface ViewType extends Serializable {

    }

    /**
     * Represents a view, which corresponds to an {@link Area}.
     */
    public static class AreaViewType implements ViewType {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = -4415484663952710929L;

        /**
         * The area, the view corresponds to.
         */
        private final Area area;

        /**
         * Creates a new view type, which represents a view, which corresponds to an {@link Area}.
         *
         * @param area
         *         The area, the view corresponds to, as a value of the enum {@link Area}. The area
         *         may not be null
         */
        public AreaViewType(final Area area) {
            Condition.INSTANCE.ensureNotNull(area, "The area may not be null");
            this.area = area;
        }

        /**
         * Returns the area, the view corresponds to.
         *
         * @return The area, the view corresponds to, as a value of the enum {@link Area}. The area
         * may not be null
         */
        @NonNull
        public final Area getArea() {
            return area;
        }

        @Override
        public final String toString() {
            return "AreaViewType [area=" + area + "]";
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + area.hashCode();
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
            AreaViewType other = (AreaViewType) obj;
            return area.equals(other.area);
        }

    }

    /**
     * Represents a divider.
     */
    public static class DividerViewType implements ViewType {

        /**
         * The constant serial version UID.
         */
        private static final long serialVersionUID = -7599447393953393719L;

        /**
         * The location of the divider.
         */
        private final DividerLocation location;

        /**
         * Creates a new view type, which represents a divider.
         *
         * @param location
         *         The location of the divider as a value of the enum {@link DividerLocation}. The
         *         location may not be null
         */
        public DividerViewType(@NonNull final DividerLocation location) {
            Condition.INSTANCE.ensureNotNull(location, "The location may not be null");
            this.location = location;
        }

        /**
         * Returns the location of the divider.
         *
         * @return The location of the divider as a value of the enum {@link DividerLocation}. The
         * location may not be null
         */
        @NonNull
        public final DividerLocation getLocation() {
            return location;
        }

        @Override
        public final String toString() {
            return "DividerViewType [location=" + location + "]";
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + location.hashCode();
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
            DividerViewType other = (DividerViewType) obj;
            return location.equals(other.location);
        }

    }

    /**
     * Contains all possible locations of a divider in relation to the scroll view, which is
     * contained by a dialog.
     */
    public enum DividerLocation {

        /**
         * If the divider is located above the scroll view.
         */
        TOP,

        /**
         * If the divider is located below the scroll view.
         */
        BOTTOM

    }

    /**
     * A comparator, which compares values of the enum {@link Area}.
     */
    private static class AreaComparator implements Comparator<Area> {

        @Override
        public int compare(final Area area1, final Area area2) {
            int index1 = area1.getIndex();
            int index2 = area2.getIndex();
            return index1 > index2 ? 1 : (index1 == index2 ? 0 : -1);
        }

    }

    /**
     * True, if the dialog is shown fullscreen, false otherwise.
     */
    private boolean fullscreen = false;

    /**
     * The maximum width of the view.
     */
    private int maxWidth = -1;

    /**
     * The maximum height of the view.
     */
    private int maxHeight = -1;

    /**
     * The drawable, which is shown as the dialog's window background.
     */
    private Drawable windowBackground;

    /**
     * The left, top, right and bottom insets of the dialog. The insets correspond to the paddings
     * of the window background drawable.
     */
    private Rect windowInsets;

    /**
     * The paint, which is used to draw the view's background.
     */
    private Paint paint;

    /**
     * The bitmap, which is used to draw the view's background.
     */
    private Bitmap backingBitmap;

    /**
     * The canvas, which his used to draw the view's background.
     */
    private Canvas backingCanvas;

    /**
     * The scroll view, which contains the scrollable areas of the dialog.
     */
    private ScrollView scrollView;

    /**
     * The list view, which is contained by the dialog.
     */
    private AbsListView listView;

    /**
     * The divider, which is shown above the scrollable areas of the dialog.
     */
    private Divider topDivider;

    /**
     * The divider, which is shown below the scrollable areas of the dialog.
     */
    private Divider bottomDivider;

    /**
     * The views, which are contained by the dialog, mapped to the corresponding types.
     */
    private SortedMap<Area, View> areas;

    /**
     * The dividers, which are contained by the dialog, mapped to the corresponding locations.
     */
    private Map<DividerLocation, Divider> dividers;

    /**
     * The scrollable area of the dialog.
     */
    private ScrollableArea scrollableArea;

    /**
     * True, if the dividers, which are located above and below the dialog's scrollable areas, are
     * shown, when scrolling, false otherwise
     */
    private boolean showDividersOnScroll;

    /**
     * The color of dividers.
     */
    private int dividerColor;

    /**
     * The left and right margin of dividers.
     */
    private int dividerMargin;

    /**
     * The padding of the dialog.
     */
    private int[] dialogPadding;

    /**
     * Initializes the view.
     */
    private void initialize() {
        scrollableArea = ScrollableArea.create(null, null);
        showDividersOnScroll = true;
        dividerColor = ContextCompat.getColor(getContext(), R.color.divider_color_light);
        dividerMargin = 0;
        dialogPadding = new int[]{0, 0, 0, 0};
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
    }

    /**
     * Adapts the background and insets of the dialog's window.
     */
    private void adaptWindowBackgroundAndInsets() {
        super.setPadding(getLeftInset(), getTopInset(), getRightInset(), getBottomInset());
        invalidate();
    }

    /**
     * Adds the areas, which are contained by the dialog, to the root view.
     */
    private void addAreas() {
        if (areas != null) {
            removeAllViews();
            scrollView = null;
            topDivider = null;
            bottomDivider = null;
            Area previousArea = null;
            boolean canAddTopDivider = false;

            for (Map.Entry<Area, View> entry : areas.entrySet()) {
                Area area = entry.getKey();
                View view = entry.getValue();

                if (scrollableArea.isScrollable(area)) {
                    if (topDivider == null && canAddTopDivider &&
                            !scrollableArea.isScrollable(previousArea)) {
                        topDivider = addDivider();
                    }

                    inflateScrollView(scrollableArea);
                    ViewGroup scrollContainer =
                            scrollView.getChildCount() > 0 ? (ViewGroup) scrollView.getChildAt(0) :
                                    scrollView;
                    view.getViewTreeObserver()
                            .addOnGlobalLayoutListener(createScrollViewChildLayoutListener());
                    scrollContainer.addView(view);
                } else {
                    if (bottomDivider == null && previousArea != null &&
                            scrollableArea.getBottomScrollableArea() != null &&
                            scrollableArea.getBottomScrollableArea().getIndex() < area.getIndex() &&
                            view.getVisibility() == View.VISIBLE && area != Area.BUTTON_BAR) {
                        bottomDivider = addDivider();
                    }

                    addView(view);
                }

                canAddTopDivider |= area != Area.HEADER && view.getVisibility() == View.VISIBLE;
                previousArea = area;
            }

            adaptAreaPadding();
            findListView();
        }
    }

    /**
     * Searches for the list view, which is contained by the dialog, in order to register a scroll
     * listener.
     */
    private void findListView() {
        if (scrollView == null) {
            View contentContainer = findViewById(R.id.content_container);

            if (contentContainer != null) {
                findListView(contentContainer);
            }
        }
    }

    /**
     * Searches for the list view, which is contained by the dialog, in order to register a scroll
     * listener.
     *
     * @param view
     *         The view, which should be searched, as an instance of the class {@link ViewGroup}.
     *         The view may not be null
     */
    private boolean findListView(@NonNull final View view) {
        if (view instanceof AbsListView) {
            this.listView = (AbsListView) view;
            this.listView.setOnScrollListener(createListViewScrollListener());
            return true;
        } else if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;

            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (findListView(viewGroup.getChildAt(i))) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Adds the dividers, which are contained by the dialog, to the root view.
     */
    private void addDividers() {
        for (Map.Entry<DividerLocation, Divider> entry : dividers.entrySet()) {
            if (entry.getKey() == DividerLocation.BOTTOM && bottomDivider == null &&
                    !scrollableArea.isScrollable(Area.BUTTON_BAR)) {
                bottomDivider = entry.getValue();
            } else if (entry.getKey() == DividerLocation.TOP && topDivider == null &&
                    !scrollableArea.isScrollable(Area.CONTENT)) {
                topDivider = entry.getValue();
            }
        }
    }

    /**
     * Adds a divider to the view.
     *
     * @return The divider, which has been added to the view, as an instance of the class {@link
     * Divider}. The divider may not be null
     */
    @NonNull
    private Divider addDivider() {
        Divider divider = new Divider(getContext());
        divider.setVisibility(View.INVISIBLE);
        divider.setBackgroundColor(dividerColor);
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, dpToPixels(getContext(), 1));
        layoutParams.leftMargin = dividerMargin;
        layoutParams.rightMargin = dividerMargin;
        addView(divider, layoutParams);
        return divider;
    }

    /**
     * Adapts the padding of the areas, which are contained by the dialog.
     */
    private void adaptAreaPadding() {
        if (areas != null) {
            boolean paddingTopApplied = false;
            Area previousArea = null;
            View previousView = null;
            int scrollViewPaddingTop = 0;
            int scrollViewMarginBottom = 0;
            Iterator<Map.Entry<Area, View>> iterator = areas.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry<Area, View> entry = iterator.next();
                Area area = entry.getKey();
                View view = entry.getValue();

                applyDialogPaddingLeft(area, view);
                applyDialogPaddingRight(area, view);

                if (!paddingTopApplied) {
                    paddingTopApplied = applyDialogPaddingTop(area, view);
                }

                if (!iterator.hasNext()) {
                    applyDialogPaddingBottom(area, view);
                }

                if (previousArea != null) {
                    if (area == Area.BUTTON_BAR) {
                        applyDialogPaddingBottom(previousArea, previousView);
                    }

                    Pair<Integer, Integer> pair = addViewSpacing(previousArea, previousView, area);
                    scrollViewPaddingTop += pair.first != null ? pair.first : 0;
                    scrollViewMarginBottom += pair.second != null ? pair.second : 0;
                }

                previousArea = area;
                previousView = view;
            }

            if (scrollView != null) {
                LinearLayout.LayoutParams layoutParams =
                        (LayoutParams) scrollView.getLayoutParams();
                layoutParams.bottomMargin = scrollViewMarginBottom;
                scrollView.setPadding(scrollView.getPaddingLeft(),
                        scrollView.getPaddingTop() + scrollViewPaddingTop,
                        scrollView.getPaddingRight(), scrollView.getPaddingBottom());
            }
        }
    }

    /**
     * Registers an {@link OnGlobalLayoutListener} at the dialog's scroll view or list view, if
     * present, in order to be able to adjust the initial visibilities of the dividers as soon as
     * the respective view has been layouted.
     */
    private void registerScrollLayoutListener() {
        if (scrollView != null) {
            scrollView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(createScrollViewLayoutListener(scrollView));
        } else if (listView != null) {
            listView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(createScrollViewLayoutListener(listView));
        }
    }

    /**
     * Creates and returns a {@link OnGlobalLayoutListener}, which allows to adjust the initial
     * visibility of the dividers, when a view has been layouted.
     *
     * @param view
     *         The observed view as an instance of the class {@link View}. The view may not be null
     * @return The listener, which has been created, as an instance of the type {@link
     * OnGlobalLayoutListener}. The listener may not be null
     */
    @NonNull
    private OnGlobalLayoutListener createScrollViewLayoutListener(@NonNull final View view) {
        return new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                ViewUtil.removeOnGlobalLayoutListener(view.getViewTreeObserver(), this);
                adaptDividerVisibilities();
            }

        };
    }

    /**
     * Adapts the visibility of the top and bottom divider, depending on the state of the dialog's
     * scroll view.
     */
    private void adaptDividerVisibilities() {
        if (scrollView != null) {
            adaptDividerVisibilities(scrollView.isScrolledToTop(), scrollView.isScrolledToBottom(),
                    false);
        } else if (listView != null) {
            adaptDividerVisibilities(isListViewScrolledToTop(listView),
                    isListViewScrolledToBottom(listView), false);
        }
    }

    /**
     * Adapts the visibility of the top and bottom divider, depending on whether the scrollable area
     * is scrolled to the top/bottom, or not.
     *
     * @param scrolledToTop
     *         True, if the scrollable area is scrolled to the top, false otherwise
     * @param scrolledToBottom
     *         True, if the scrollable area is scrolled to the bottom, false otherwise
     * @param animate
     *         True, if the visibility should be changed in an animated manner, false otherwise
     */
    private void adaptDividerVisibilities(final boolean scrolledToTop,
                                          final boolean scrolledToBottom, final boolean animate) {
        if (topDivider != null && !topDivider.isVisibleByDefault()) {
            topDivider.setVisibility(
                    scrolledToTop || !showDividersOnScroll ? View.INVISIBLE : View.VISIBLE,
                    animate);
        }

        if (bottomDivider != null && !bottomDivider.isVisibleByDefault()) {
            bottomDivider.setVisibility(
                    scrolledToBottom || !showDividersOnScroll ? View.INVISIBLE : View.VISIBLE,
                    animate);
        }
    }

    /**
     * Returns, whether a specific list view is scrolled to the bottom, or not.
     *
     * @param scrollView
     *         The list view as an instance of the class {@link AbsListView}. The list view may not
     *         be null
     * @return True, if the given list view is scrolled to the bottom, false otherwise
     */
    private boolean isListViewScrolledToBottom(@NonNull final AbsListView scrollView) {
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
     * Returns, whether a specific list view is scrolled to the top, or not.
     *
     * @param listView
     *         The list view as an instance of the class {@link AbsListView}. The list view may not
     *         be null
     * @return True, if the given list view is scrolled to the top, false otherwise
     */
    private boolean isListViewScrolledToTop(@NonNull final AbsListView listView) {
        if (listView.getFirstVisiblePosition() == 0) {
            if (listView.getChildCount() == 0) {
                return true;
            } else {
                View child = listView.getChildAt(0);
                return child == null || child.getTop() == 0;
            }
        }

        return false;
    }

    /**
     * Applies the dialog's left padding to the view of a specific area.
     *
     * @param area
     *         The area, the view, the padding should be applied to, corresponds to, as an instance
     *         of the class {@link Area}. The area may not be null
     * @param view
     *         The view, the padding should be applied to, as an instance of the class {@link View}.
     *         The view may not be null
     */
    private void applyDialogPaddingLeft(@NonNull final Area area, @NonNull final View view) {
        int padding = area != Area.HEADER && area != Area.BUTTON_BAR && area != Area.CONTENT ?
                dialogPadding[0] : 0;
        view.setPadding(padding, view.getPaddingTop(), view.getPaddingRight(),
                view.getPaddingBottom());
    }

    /**
     * Applies the dialog's top padding to the view of a specific area.
     *
     * @param area
     *         The area, the view, the padding should be applied to, corresponds to, as an instance
     *         of the class {@link Area}. The area may not be null
     * @param view
     *         The view, the padding should be applied to, as an instance of the class {@link View}.
     *         The view may not be null
     */
    private boolean applyDialogPaddingTop(@NonNull final Area area, @NonNull final View view) {
        if (area != Area.HEADER && area != Area.CONTENT && area != Area.BUTTON_BAR &&
                view.getVisibility() == View.VISIBLE) {
            view.setPadding(view.getPaddingLeft(), dialogPadding[1], view.getPaddingRight(),
                    view.getPaddingBottom());
            return true;
        }

        return false;
    }

    /**
     * Applies the dialog's right padding to the view of a specific area.
     *
     * @param area
     *         The area, the view, the padding should be applied to, corresponds to, as an instance
     *         of the class {@link Area}. The area may not be null
     * @param view
     *         The view, the padding should be applied to, as an instance of the class {@link View}.
     *         The view may not be null
     */
    private void applyDialogPaddingRight(@NonNull final Area area, @NonNull final View view) {
        int padding = area != Area.HEADER && area != Area.BUTTON_BAR && area != Area.CONTENT ?
                dialogPadding[2] : 0;
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), padding,
                view.getPaddingBottom());
    }

    /**
     * Applies the dialog's bottom padding to the view of a specific area.
     *
     * @param area
     *         The area, the view, the padding should be applied to, corresponds to, as an instance
     *         of the class {@link Area}. The area may not be null
     * @param view
     *         The view, the padding should be applied to, as an instance of the class {@link View}.
     *         The view may not be null
     */
    private void applyDialogPaddingBottom(@NonNull final Area area, @NonNull final View view) {
        if (area != Area.HEADER && area != Area.BUTTON_BAR) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(),
                    dialogPadding[3]);
        }
    }

    /**
     * Adds spacing to the view of a specific area. The spacing is added to the view's current
     * bottom padding.
     *
     * @param previousArea
     *         The area, the view, the spacing should be applied to, corresponds to, as an instance
     *         of the class {@link Area}. The area may not be null
     * @param previousView
     *         The view, the spacing should be applied to, as an instance of the class {@link View}.
     *         The view may not be null
     * @param area
     *         The current area as a value of the enum {@link Area}. The area may not be null
     * @return A pair, which contains the top and bottom padding, which should be added to the
     * dialog's scroll view, as an instance of the class {@link Pair} value
     */
    @NonNull
    private Pair<Integer, Integer> addViewSpacing(@NonNull final Area previousArea,
                                                  @NonNull final View previousView,
                                                  @NonNull final Area area) {
        int scrollViewPaddingTop = 0;
        int scrollViewMarginBottom = 0;
        int padding = -1;

        if (previousArea == Area.TITLE) {
            padding = getResources().getDimensionPixelSize(R.dimen.dialog_title_bottom_padding);
        } else if (previousArea == Area.MESSAGE) {
            padding = getResources().getDimensionPixelSize(R.dimen.dialog_message_bottom_padding);
        }

        if (previousArea != Area.HEADER && !scrollableArea.isScrollable(previousArea) &&
                scrollableArea.isScrollable(area)) {
            int originalPadding = padding;
            padding = originalPadding / 2;
            scrollViewPaddingTop = originalPadding - padding;
        } else if (area == Area.BUTTON_BAR && scrollableArea.isScrollable(previousArea) &&
                !scrollableArea.isScrollable(area)) {
            int originalPadding = padding;
            padding = originalPadding / 2;
            scrollViewMarginBottom = originalPadding - padding;
        }

        if (padding != -1) {
            previousView.setPadding(previousView.getPaddingLeft(), previousView.getPaddingTop(),
                    previousView.getPaddingRight(), previousView.getPaddingBottom() + padding);
        }

        return Pair.create(scrollViewPaddingTop, scrollViewMarginBottom);
    }

    /**
     * Inflates the scroll view, which contains the dialog's scrollable areas, if it has not been
     * inflated yet.
     *
     * @param scrollableArea
     *         The scrollable area of the dialog, as an instance of the class {@link
     *         ScrollableArea}. The scrollable area may not be null
     */
    private void inflateScrollView(@NonNull final ScrollableArea scrollableArea) {
        if (scrollView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            scrollView = (ScrollView) layoutInflater
                    .inflate(R.layout.material_dialog_scroll_view, this, false);
            scrollView.addScrollListener(createScrollViewScrollListener());
            scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            scrollView.setFocusableInTouchMode(true);

            if (scrollableArea.getBottomScrollableArea().getIndex() -
                    scrollableArea.getTopScrollableArea().getIndex() > 0) {
                LinearLayout scrollContainer = new LinearLayout(getContext());
                scrollContainer.setOrientation(LinearLayout.VERTICAL);
                scrollView.addView(scrollContainer, ScrollView.LayoutParams.MATCH_PARENT,
                        ScrollView.LayoutParams.MATCH_PARENT);
            }

            addView(scrollView);
        }
    }

    /**
     * Creates and returns a listener, which allows to observe, when the child of the dialog's
     * scroll view has been layouted. If the scroll view's height is greater than necessary, its
     * height is reduced to match the height of its child.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnGlobalLayoutListener}. The listener may not be null
     */
    @NonNull
    private OnGlobalLayoutListener createScrollViewChildLayoutListener() {
        return new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                View child = scrollView.getChildAt(0);
                int childHeight = child.getHeight();
                int containerHeight = scrollView.getHeight() - scrollView.getPaddingTop() -
                        scrollView.getPaddingBottom();

                if (containerHeight > childHeight) {
                    LinearLayout.LayoutParams layoutParams =
                            (LinearLayout.LayoutParams) scrollView.getLayoutParams();
                    layoutParams.height = childHeight;
                    layoutParams.weight = 0;
                    scrollView.requestLayout();
                }

                ViewUtil.removeOnGlobalLayoutListener(child.getViewTreeObserver(), this);
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to observe when the scroll view, which is
     * contained by the dialog, is scrolled.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * OnScrollChangedListener}. The listener may not be null
     */
    @NonNull
    private ScrollListener createScrollViewScrollListener() {
        return new ScrollListener() {

            @Override
            public void onScrolled(final boolean scrolledToTop, final boolean scrolledToBottom) {
                adaptDividerVisibilities(scrolledToTop, scrolledToBottom, true);
            }

        };
    }

    /**
     * Creates and returns a listener, which allows to observe the list view, which is contained by
     * the dialog, is scrolled.
     *
     * @return The listener, which has been created, as an instance of the type {@link
     * android.widget.AbsListView.OnScrollListener}. The listener may not be null
     */
    @NonNull
    private AbsListView.OnScrollListener createListViewScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {

            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem,
                                 final int visibleItemCount, final int totalItemCount) {
                adaptDividerVisibilities(isListViewScrolledToTop(view),
                        isListViewScrolledToBottom(view), true);
            }

        };
    }

    /**
     * Adapts the color of dividers.
     */
    private void adaptDividerColor() {
        adaptDividerColor(topDivider);
        adaptDividerColor(bottomDivider);

        if (dividers != null) {
            for (Divider divider : dividers.values()) {
                adaptDividerColor(divider);
            }
        }
    }

    /**
     * Adapts the color of a specific divider.
     *
     * @param divider
     *         The divider, whose color should be adapted, as an instance of the class {@link
     *         Divider}
     */
    private void adaptDividerColor(@Nullable final Divider divider) {
        if (divider != null) {
            divider.setBackgroundColor(dividerColor);
        }
    }

    /**
     * Adapts the left and right margin of dividers.
     */
    private void adaptDividerMargin() {
        adaptDividerMargin(topDivider);
        adaptDividerMargin(bottomDivider);

        if (dividers != null) {
            for (Divider divider : dividers.values()) {
                adaptDividerMargin(divider);
            }
        }
    }

    /**
     * Adapts the left and right margin of a specific divider.
     *
     * @param divider
     *         The divider, whose left and right margin should be adapted, as an instance of the
     *         class {@link Divider}
     */
    private void adaptDividerMargin(@Nullable final Divider divider) {
        if (divider != null) {
            ViewGroup.LayoutParams layoutParams = divider.getLayoutParams();

            if (layoutParams instanceof LayoutParams) {
                ((LayoutParams) layoutParams).leftMargin = dividerMargin;
                ((LayoutParams) layoutParams).rightMargin = dividerMargin;
            }
        }
    }

    /**
     * Returns the left inset of the dialog, depending on whether the dialog is shown fullscreen, or
     * not.
     *
     * @return The left inset of the dialog in pixels as an {@link Integer} value
     */
    private int getLeftInset() {
        return fullscreen || windowInsets == null ? 0 : windowInsets.left;
    }

    /**
     * Returns the top inset of the dialog, depending on whether the dialog is shown fullscreen, or
     * not.
     *
     * @return The top inset of the dialog in pixels as an {@link Integer} value
     */
    private int getTopInset() {
        return fullscreen || windowInsets == null ? 0 : windowInsets.top;
    }

    /**
     * Returns the right inset of the dialog, depending on whether the dialog is shown fullscreen,
     * or not.
     *
     * @return The right inset of the dialog in pixels as an {@link Integer} value
     */
    private int getRightInset() {
        return fullscreen || windowInsets == null ? 0 : windowInsets.right;
    }

    /**
     * Returns the bottom inset of the dialog, depending on whether the dialog is shown fullscreen,
     * or not.
     *
     * @return The bottom inset of the dialog in pixels as an {@link Integer} value
     */
    private int getBottomInset() {
        return fullscreen || windowInsets == null ? 0 : windowInsets.bottom;
    }

    /**
     * Creates a new root view of a dialog, which is designed according to Android 5's Material
     * Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public DialogRootView(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new root view of a dialog, which is designed according to Android 5's Material
     * Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    public DialogRootView(@NonNull final Context context,
                          @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    /**
     * Creates a new root view of a dialog, which is designed according to Android 5's Material
     * Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     */
    public DialogRootView(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                          @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize();
    }

    /**
     * Creates a new root view of a dialog, which is designed according to Android 5's Material
     * Design guidelines even on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     * @param defaultStyle
     *         The default style to apply to this view. If 0, no style will be applied (beyond what
     *         is included in the theme). This may either be an attribute resource, whose value will
     *         be retrieved from the current theme, or an explicit style resource
     * @param defaultStyleResource
     *         A resource identifier of a style resource that supplies default values for the view,
     *         used only if the default style is 0 or can not be found in the theme. Can be 0 to not
     *         look for defaults
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DialogRootView(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                          @AttrRes final int defaultStyle,
                          @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize();
    }

    /**
     * Sets the background and inset of the dialog's window.
     *
     * @param windowBackground
     *         The background, which should be set, as an instance of the class {@link Drawable} or
     *         null, if no window background is set
     * @param windowInsets
     *         The insets of the dialog's window as an instance of the class {@link Rect} or null,
     *         if no window background is set
     */
    public final void setWindowBackgroundAndInset(@Nullable final Drawable windowBackground,
                                                  @Nullable final Rect windowInsets) {
        this.windowBackground = windowBackground;
        this.windowInsets = windowInsets;
        adaptWindowBackgroundAndInsets();
    }

    /**
     * Sets, whether the dialog is shown fullscreen, or not.
     *
     * @param fullscreen
     *         True, if the dialog is shown fullscreen, false otherwise
     */
    public final void setFullscreen(final boolean fullscreen) {
        this.fullscreen = fullscreen;
        adaptWindowBackgroundAndInsets();
    }

    /**
     * Sets the maximum width of the view.
     *
     * @param maxWidth
     *         The maximum width, which should be set, in pixels as an {@link Integer} value. The
     *         maximum width must be at least 1 or -1, if no maximum width should be set
     */
    public final void setMaxWidth(final int maxWidth) {
        if (maxWidth != -1) {
            Condition.INSTANCE.ensureAtLeast(maxWidth, 1, "The maximum width must be at least 1");
        }

        this.maxWidth = maxWidth;
        requestLayout();
    }

    /**
     * Sets the maximum height of the view.
     *
     * @param maxHeight
     *         The maximum height, which should be set, in pixels as an {@link Integer} value. The
     *         maximum height must be at least 1 or -1, if no maximum height should be set
     */
    public final void setMaxHeight(final int maxHeight) {
        if (maxHeight != -1) {
            Condition.INSTANCE.ensureAtLeast(maxHeight, 1, "The maximum height must be at least 1");
        }

        this.maxHeight = maxHeight;
        requestLayout();
    }

    /**
     * Returns the scroll view, which contains the dialog's scrollable areas.
     *
     * @return The scroll view, which contains the dialog's scrollable areas, as an instance of the
     * class {@link ScrollView} or null, if the decorator is not attached or if the dialog does not
     * contain any scrollable areas
     */
    @Nullable
    public final ScrollView getScrollView() {
        return scrollView;
    }

    /**
     * Sets the scrollable area of the dialog.
     *
     * @param scrollableArea
     *         The scrollable area, which should be set, as an instance of the class {@link
     *         ScrollableArea}. The scrollable area may not be null
     */
    public final void setScrollableArea(@NonNull final ScrollableArea scrollableArea) {
        Condition.INSTANCE.ensureNotNull(scrollableArea, "The scrollable area may not be null");
        this.scrollableArea = scrollableArea;
        addAreas();
    }

    /**
     * Sets, whether dividers, which are located above and below the dialog's scrollable areas,
     * should be shown, when scrolled, or not.
     *
     * @param show
     *         True, if the dividers, which are located above and below the dialog's scrollable
     *         areas, should be shown, when scrolling, false otherwise
     */
    public final void showDividersOnScroll(final boolean show) {
        this.showDividersOnScroll = show;
        adaptDividerVisibilities();
    }

    /**
     * Sets the color of dividers.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setDividerColor(@ColorInt final int color) {
        this.dividerColor = color;
        adaptDividerColor();
    }

    /**
     * Sets the left and right margin of the divider, which is located above the dialog's button.
     *
     * @param margin
     *         The left and right margin, which should be set, in pixels as an {@link Integer}
     *         value. The margin must be at least 0
     */
    public final void setDividerMargin(final int margin) {
        Condition.INSTANCE.ensureAtLeast(margin, 0, "The margin must be at least 0");
        this.dividerMargin = margin;
        adaptDividerMargin();
    }

    /**
     * Adds the different areas of a dialog to the root view.
     *
     * @param areas
     *         A map, which contains the areas, which should be added, as keys and their
     *         corresponding views as values, as an instance of the type {@link Map}. The map may
     *         not be null
     */
    public final void addAreas(@NonNull final Map<ViewType, View> areas) {
        this.areas = new TreeMap<>(new AreaComparator());
        this.dividers = new HashMap<>();

        for (Map.Entry<ViewType, View> entry : areas.entrySet()) {
            ViewType viewType = entry.getKey();
            View view = entry.getValue();

            if (viewType instanceof AreaViewType) {
                this.areas.put(((AreaViewType) viewType).getArea(), view);
            } else if (viewType instanceof DividerViewType && view instanceof Divider) {
                this.dividers.put(((DividerViewType) viewType).getLocation(), (Divider) view);
            }
        }

        addAreas();
        addDividers();
        registerScrollLayoutListener();
    }

    @Override
    public final void onAreaShown(@NonNull final Area area) {
        adaptAreaPadding();
    }

    @Override
    public final void onAreaHidden(@NonNull final Area area) {
        adaptAreaPadding();
    }

    @Override
    public final void setPadding(final int left, final int top, final int right, final int bottom) {
        this.dialogPadding = new int[]{left, top, right, bottom};
        adaptAreaPadding();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected final void onLayout(final boolean changed, final int l, final int t, final int r,
                                  final int b) {
        super.onLayout(changed, l, t, r, b);

        if (changed) {
            backingBitmap = Bitmap.createBitmap(r - l, b - t, Bitmap.Config.ARGB_8888);
            backingCanvas = new Canvas(backingBitmap);
        }
    }

    @Override
    public final void draw(final Canvas canvas) {
        super.draw(canvas);

        if (!fullscreen && windowBackground != null) {
            int width = getWidth();
            int height = getHeight();
            windowBackground.setBounds(0, 0, width, height);
            backingBitmap.eraseColor(Color.TRANSPARENT);
            windowBackground.draw(backingCanvas);
            canvas.drawBitmap(backingBitmap, 0, 0, paint);
        }
    }

    @Override
    protected final void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int maxWidthMeasureSpec = maxWidth != -1 ? MeasureSpec
                .makeMeasureSpec(maxWidth + getLeftInset() + getRightInset(), MeasureSpec.AT_MOST) :
                -1;
        int maxHeightMeasureSpec = maxHeight != -1 ? MeasureSpec
                .makeMeasureSpec(maxHeight + getLeftInset() + getRightInset(),
                        MeasureSpec.AT_MOST) : -1;
        super.onMeasure(maxWidthMeasureSpec != -1 ? maxWidthMeasureSpec : widthMeasureSpec,
                maxHeightMeasureSpec != -1 ? maxHeightMeasureSpec : heightMeasureSpec);
    }

}