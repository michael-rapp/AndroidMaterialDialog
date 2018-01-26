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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;

import de.mrapp.android.util.datastructure.ListenerList;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A custom scroll view, which allows listeners to be notified when scrolling.
 *
 * @author Michael Rapp
 * @since 4.2.0
 */
public class ScrollView extends android.widget.ScrollView {

    /**
     * Defines the interface, a class, which should be notified, when the scroll view is scrolled,
     * must implement.
     */
    public interface ScrollListener {

        /**
         * The method, which is invoked, when the scroll view has been scrolled.
         *
         * @param scrolledToTop
         *         True, if the scroll view is scrolled to the top, false otherwise
         * @param scrolledToBottom
         *         True, if the scroll view is scrolled to the bottom, false otherwise
         */
        void onScrolled(final boolean scrolledToTop, final boolean scrolledToBottom);

    }

    /**
     * A set, which contains the listeners, which should be notified, when the scroll view is
     * scrolled.
     */
    private ListenerList<ScrollListener> scrollListeners;

    /**
     * Initializes the view.
     */
    private void initialize() {
        this.scrollListeners = new ListenerList<>();
    }

    /**
     * Notifies, when the scroll view has been scrolled.
     *
     * @param scrolledToTop
     *         True, if the scroll view is scrolled to the top, false otherwise
     * @param scrolledToBottom
     *         True, if the scroll view is scrolled to the bottom, false otherwise
     */
    private void notifyOnScrolled(final boolean scrolledToTop, final boolean scrolledToBottom) {
        for (ScrollListener listener : scrollListeners) {
            listener.onScrolled(scrolledToTop, scrolledToBottom);
        }
    }

    /**
     * Creates a custom scroll view, which allows listeners to be notified when scrolling.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public ScrollView(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a custom scroll view, which allows listeners to be notified when scrolling.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    public ScrollView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    /**
     * Creates a custom scroll view, which allows listeners to be notified when scrolling.
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
    public ScrollView(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                      @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize();
    }

    /**
     * Creates a custom scroll view, which allows listeners to be notified when scrolling.
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
    public ScrollView(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                      @AttrRes final int defaultStyle, @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize();
    }

    /**
     * Adds a listener, which should be notified, when the scroll view is scrolled.
     *
     * @param listener
     *         The listener, which should be added, as an instance of the type {@link
     *         ScrollListener}. The listener may not be null
     */
    public void addScrollListener(@NonNull final ScrollListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        this.scrollListeners.add(listener);
    }

    /**
     * Removes a specific listener, which should not be notified, when the scroll view is scrolled,
     * anymore.
     *
     * @param listener
     *         The listener, which should be removed, as an instance of the type {@link
     *         ScrollListener}. The listener may not be null
     */
    public void removeScrollListener(@NonNull final ScrollListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        this.scrollListeners.remove(listener);
    }

    /**
     * Returns, whether the scroll view is scrolled to the top, or not.
     *
     * @return True, if the scroll view is scrolled to the top, false otherwise
     */
    public final boolean isScrolledToTop() {
        return getScrollY() == 0;
    }

    /**
     * Returns, whether the scroll view is scrolled to the bottom, or not.
     *
     * @return True, if the scroll view is scrolled to the bottom, false otherwise
     */
    public final boolean isScrolledToBottom() {
        int y = getScrollY();
        View view = getChildAt(0);
        return (view.getBottom() - y) == getHeight();
    }

    @Override
    protected final void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        notifyOnScrolled(isScrolledToTop(), isScrolledToBottom());
    }

}