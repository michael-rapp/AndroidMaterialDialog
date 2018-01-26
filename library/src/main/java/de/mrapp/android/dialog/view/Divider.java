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

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.View;

/**
 * A horizontal divider, which is contained by a dialog.
 *
 * @author Michael Rapp
 * @since 4.2.0
 */
public class Divider extends View {

    /**
     * True, if the divider is visible by default, false otherwise.
     */
    private boolean visibleByDefault;

    /**
     * Initializes the view.
     */
    private void initialize() {
        this.visibleByDefault = false;
        setTag(true);
    }

    /**
     * Creates and returns a listener, which allows to observe the progress of an animation, which
     * is used to show or hide the divider.
     *
     * @param show
     *         True, if the divider is shown by the animation, false otherwise
     * @return The listener, which has been created, as an instance of the type {@link
     * AnimatorListener}. The listener may not be null
     */
    @NonNull
    private AnimatorListener createVisibilityAnimationListener(final boolean show) {
        return new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(final Animator animation) {
                super.onAnimationStart(animation);

                if (show) {
                    Divider.super.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                super.onAnimationEnd(animation);

                if (!show) {
                    Divider.super.setVisibility(View.INVISIBLE);
                }
            }

        };
    }

    /**
     * Creates a new horizontal divider, which is contained by a dialog.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     */
    public Divider(@NonNull final Context context) {
        this(context, null);
    }

    /**
     * Creates a new horizontal divider, which is contained by a dialog.
     *
     * @param context
     *         The context, which should be used by the view, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param attributeSet
     *         The attribute set, the view's attributes should be obtained from, as an instance of
     *         the type {@link AttributeSet} or null, if no attributes should be obtained
     */
    public Divider(@NonNull final Context context, @Nullable final AttributeSet attributeSet) {
        super(context, attributeSet);
        initialize();
    }

    /**
     * Creates a new horizontal divider, which is contained by a dialog.
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
    public Divider(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                   @AttrRes final int defaultStyle) {
        super(context, attributeSet, defaultStyle);
        initialize();
    }

    /**
     * Creates a new horizontal divider, which is contained by a dialog.
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
    public Divider(@NonNull final Context context, @Nullable final AttributeSet attributeSet,
                   @AttrRes final int defaultStyle, @StyleRes final int defaultStyleResource) {
        super(context, attributeSet, defaultStyle, defaultStyleResource);
        initialize();
    }

    /**
     * Returns, whether the divider is visible by default, or not.
     *
     * @return True, if the divider is visible by default, false otherwise
     */
    public final boolean isVisibleByDefault() {
        return visibleByDefault;
    }

    /**
     * Sets, whether the divider is visible by default, or not.
     */
    public final void setVisibleByDefault(final boolean visibleByDefault) {
        this.visibleByDefault = visibleByDefault;
    }

    /**
     * Sets the visibility of the divider.
     *
     * @param visibility
     *         The visibility, which should be set, as an {@link Integer} value. Must be one of
     *         {@link #VISIBLE}, {@link #INVISIBLE} or {@link #GONE}.
     * @param animate
     *         True, if the visibility should be changed in an animated manner, false otherwise
     */
    public final void setVisibility(final int visibility, final boolean animate) {
        boolean visible = visibility == View.VISIBLE;
        Boolean tag = (Boolean) getTag();
        setTag(visible);

        if (animate) {
            if (tag == null || tag != visible) {
                if (getAnimation() != null) {
                    getAnimation().cancel();

                }

                animate().alpha(visible ? 1 : 0).setDuration(
                        getResources().getInteger(android.R.integer.config_mediumAnimTime))
                        .setListener(createVisibilityAnimationListener(visible)).start();
            }
        } else {
            super.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    public final void setVisibility(final int visibility) {
        setVisibility(visibility, false);
    }

}