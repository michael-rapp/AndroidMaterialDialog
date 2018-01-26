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
package de.mrapp.android.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;

import java.util.Map;

import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.builder.AbstractButtonBarDialogBuilder;
import de.mrapp.android.dialog.model.ProgressDialogDecorator;
import de.mrapp.android.util.ThemeUtil;

/**
 * A dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices and allows to indicate a running progress. Such a dialog consists of a
 * title, a message and a circular progress bar. Optionally, up to three buttons can be shown.
 *
 * For creating or showing such dialogs, the methods <code>ProgressDialog#create()</code> or
 * <code>ProgressDialog#show()</code> of the builder {@link ProgressDialog.Builder} can be used.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ProgressDialog extends AbstractButtonBarDialog implements ProgressDialogDecorator {

    /**
     * Contains all possible positions of the dialog's progress bar.
     */
    public enum ProgressBarPosition {

        /**
         * When the progress bar is located to the left of the dialog's message.
         */
        LEFT(0),

        /**
         * When the progress bar is located above the dialog's message.
         */
        TOP(1),

        /**
         * When the progress bar is located to the right of the dialog's message.
         */
        RIGHT(2),

        /**
         * When the progress bar is located below the dialog's message.
         */
        BOTTOM(3);

        /**
         * The position's value.
         */
        private int value;

        /**
         * Creates a new position.
         *
         * @param value
         *         The position's value
         */
        ProgressBarPosition(final int value) {
            this.value = value;
        }

        /**
         * Returns the position's value.
         *
         * @return The position's value as an {@link Integer} value
         */
        public final int getValue() {
            return value;
        }

        /**
         * Creates and returns the position, which corresponds to a specific value.
         *
         * @param value
         *         The value of the position, which should be created
         * @return The position, which corresponds to the given value, as a value of the enum {@link
         * ProgressBarPosition}
         */
        public static ProgressBarPosition fromValue(final int value) {
            for (ProgressBarPosition position : values()) {
                if (position.getValue() == value) {
                    return position;
                }
            }

            throw new IllegalArgumentException("Invalid enum value: " + value);
        }

    }

    /**
     * A builder, which allows to create and show dialogs, which are designed according to Android
     * 5's Material Design guidelines even on pre-Lollipop devices and allow to indicate a running
     * progress. Such a dialog consists of a title, a message and a circular progress bar.
     * Optionally, up to three buttons can be shown.
     */
    public static class Builder extends AbstractButtonBarDialogBuilder<ProgressDialog, Builder> {

        /**
         * Obtains the color of the dialog's progress bar from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the color should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainProgressBarColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogProgressBarColor});
            int defaultColor =
                    ThemeUtil.getColor(getContext(), themeResourceId, R.attr.colorAccent);
            setProgressBarColor(typedArray.getColor(0, defaultColor));
        }

        /**
         * Obtains the size of the dialog's progress bar from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the size should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainProgressBarSize(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogProgressBarSize});
            int defaultHeight = getContext().getResources()
                    .getDimensionPixelSize(R.dimen.circular_progress_bar_size_normal);
            setProgressBarSize(typedArray.getDimensionPixelSize(0, defaultHeight));
        }

        /**
         * Obtains the thickness of the dialog's progress bar from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the thickness should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainProgressBarThickness(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogProgressBarThickness});
            int defaultHeight = getContext().getResources()
                    .getDimensionPixelSize(R.dimen.circular_progress_bar_thickness_normal);
            setProgressBarThickness(typedArray.getDimensionPixelSize(0, defaultHeight));
        }

        /**
         * Obtains the position of the dialog's progress bar from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the position should be obtained from, as an {@link
         *         Integer} value
         */
        private void obtainProgressBarPosition(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogProgressBarPosition});
            ProgressBarPosition defaultPosition = ProgressBarPosition.LEFT;
            setProgressBarPosition(ProgressBarPosition
                    .fromValue(typedArray.getInt(0, defaultPosition.getValue())));
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices and allow to indicate
         * a running progress.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public Builder(@NonNull final Context context) {
            super(context);
        }

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices and allow to indicate
         * a running progress.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public Builder(@NonNull final Context context, @StyleRes final int themeResourceId) {
            super(context, themeResourceId);
        }

        /**
         * Sets the color of the progress bar of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setProgressBarColor(@ColorInt final int color) {
            getProduct().setProgressBarColor(color);
            return self();
        }

        /**
         * Sets the size of the progress bar of the dialog, which is created by the builder.
         *
         * @param size
         *         The size, which should be set, in pixels as an {@link Integer} value. The size
         *         must be at least 0
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setProgressBarSize(final int size) {
            getProduct().setProgressBarSize(size);
            return self();
        }

        /**
         * Sets the thickness of the progress bar of the dialog, which is created by the builder.
         *
         * @param thickness
         *         The thickness, which should be set, in pixels as an {@link Integer} value. The
         *         thickness must be at least 1
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setProgressBarThickness(final int thickness) {
            getProduct().setProgressBarThickness(thickness);
            return self();
        }

        /**
         * Sets the position of the progress bar of the dialog, which is created by the builder.
         *
         * @param position
         *         The position, which should be set, as a value of the enum {@link
         *         ProgressBarPosition}. The position may either be <code>LEFT</code>,
         *         <code>TOP</code>, <code>RIGHT</code> or <code>BOTTOM</code>
         * @return The builder, the method has been called upon, as an instance of the class {@link
         * Builder}
         */
        public final Builder setProgressBarPosition(@NonNull final ProgressBarPosition position) {
            getProduct().setProgressBarPosition(position);
            return self();
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @return The dialog, which has been shown, as an instance of the class {@link
         * ProgressDialog}
         */
        public final ProgressDialog show() {
            ProgressDialog dialog = create();
            dialog.show();
            return dialog;
        }

        @NonNull
        @Override
        protected final ProgressDialog onCreateProduct() {
            return new ProgressDialog(getContext(), getThemeResourceId());
        }

        @Override
        protected final void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            super.obtainStyledAttributes(themeResourceId);
            obtainProgressBarColor(themeResourceId);
            obtainProgressBarSize(themeResourceId);
            obtainProgressBarThickness(themeResourceId);
            obtainProgressBarPosition(themeResourceId);
        }

    }

    /**
     * The decorator, which is used by the dialog.
     */
    private final de.mrapp.android.dialog.decorator.ProgressDialogDecorator decorator;

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices and allows to indicate a running progress.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected ProgressDialog(@NonNull final Context context, @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        decorator = new de.mrapp.android.dialog.decorator.ProgressDialogDecorator(this);
        addDecorator(decorator);
        setCancelable(false);
    }

    @Override
    public final int getProgressBarColor() {
        return decorator.getProgressBarColor();
    }

    @Override
    public final void setProgressBarColor(@ColorInt final int color) {
        decorator.setProgressBarColor(color);
    }

    @Override
    public final int getProgressBarSize() {
        return decorator.getProgressBarSize();
    }

    @Override
    public final void setProgressBarSize(final int size) {
        decorator.setProgressBarSize(size);
    }

    @Override
    public final int getProgressBarThickness() {
        return decorator.getProgressBarThickness();
    }

    @Override
    public final void setProgressBarThickness(final int thickness) {
        decorator.setProgressBarThickness(thickness);
    }

    @Override
    public final ProgressBarPosition getProgressBarPosition() {
        return decorator.getProgressBarPosition();
    }

    @Override
    public final void setProgressBarPosition(@NonNull final ProgressBarPosition position) {
        decorator.setProgressBarPosition(position);
    }

    @NonNull
    @Override
    public final Bundle onSaveInstanceState() {
        Bundle outState = super.onSaveInstanceState();
        decorator.onSaveInstanceState(outState);
        return outState;
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        decorator.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

}