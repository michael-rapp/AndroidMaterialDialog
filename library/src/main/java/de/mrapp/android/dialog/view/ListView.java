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

import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.model.MaterialDialog;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A custom list view, which allows to expand its height to its content.
 *
 * @author Michael Rapp
 * @since 4.2.0
 */
public class ListView extends android.widget.ListView {

    /**
     * The dialog, which contains the list view.
     */
    private MaterialDialog dialog;

    /**
     * Creates a new custom list view, which allows to expand its height to its content.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public ListView(@NonNull final Context context) {
        super(context);
    }

    /**
     * Creates a new custom list view, which allows to expand its height to its content.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    public ListView(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /**
     * Creates a new custom list view, which allows to expand its height to its content.
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
    public ListView(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                    @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
    }

    /**
     * Creates a new custom list view, which allows to expand its height to its content.
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
    public ListView(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                    @AttrRes final int defaultStyle, @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
    }

    /**
     * Sets the dialog, which contains the list view.
     *
     * @param dialog
     *         The dialog, which should be set, as an instance of the type {@link MaterialDialog}.
     *         The dialog may not be null
     */
    public final void setDialog(@NonNull final MaterialDialog dialog) {
        ensureNotNull(dialog, "The dialog may not be null");
        this.dialog = dialog;
    }

    @Override
    public final void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (dialog != null && dialog.getScrollableArea().isScrollable(Area.CONTENT)) {
            int expandSpec =
                    MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, expandSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}