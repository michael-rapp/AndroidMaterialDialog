package de.mrapp.android.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.ViewGroup;

import de.mrapp.android.util.ThemeUtil;
import de.mrapp.android.view.CircularProgressBar;

import static de.mrapp.android.util.Condition.ensureAtLeast;

/**
 * A dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices and allows to indicate a running progress. Such a dialog consists of a
 * title, a message and a circular progress bar. Optionally, up to three buttons can be shown.
 *
 * For creating or showing such dialogs, the methods {@link ProgressDialog#create()} or {@link
 * ProgressDialog#show()} of the builder {@link ProgressDialog.Builder} can be used.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ProgressDialog extends AbstractButtonBarDialog {

    /**
     * A builder, which allows to create and show dialogs, which are designed according to Android
     * 5's Material Design guidelines even on pre-Lollipop devices and allow to indicate a running
     * progress. Such a dialog consists of a title, a message and a circular progress bar.
     * Optionally, up to three buttons can be shown.
     */
    public static class Builder
            extends AbstractButtonBarDialog.AbstractBuilder<ProgressDialog, Builder> {

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
            getDialog().setProgressBarColor(color);
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
            getDialog().setProgressBarSize(size);
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
            getDialog().setProgressBarThickness(thickness);
            return self();
        }

        @Override
        protected final ProgressDialog onCreateDialog(@NonNull final Context context,
                                                      @StyleRes final int themeResourceId) {
            return new ProgressDialog(context, themeResourceId);
        }

        @Override
        protected final void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            super.obtainStyledAttributes(themeResourceId);
            obtainProgressBarColor(themeResourceId);
            obtainProgressBarSize(themeResourceId);
            obtainProgressBarThickness(themeResourceId);
        }

    }

    /**
     * The dialog's circular progress bar.
     */
    private CircularProgressBar progressBar;

    /**
     * The color of the dialog's progress bar.
     */
    private int progressBarColor;

    /**
     * The size of the dialog's progress bar.
     */
    private int progressBarSize;

    /**
     * The thickness of the dialog's progress bar.
     */
    private int progressBarThickness;

    /**
     * Adapts the color of the dialog's circular progress bar.
     */
    private void adaptProgressBarColor() {
        if (progressBar != null) {
            progressBar.setColor(progressBarColor);
        }
    }

    /**
     * Adapts the size of the dialog's circular progress bar.
     */
    private void adaptProgressBarSize() {
        if (progressBar != null) {
            progressBar.setVisibility(progressBarSize > 0 ? View.VISIBLE : View.GONE);
            ViewGroup.LayoutParams layoutParams = progressBar.getLayoutParams();
            layoutParams.width = progressBarSize;
            layoutParams.height = progressBarSize;
        }
    }

    /**
     * Adapts the thickness of the dialog's circular progress bar.
     */
    private void adaptProgressBarThickness() {
        if (progressBar != null) {
            progressBar.setThickness(progressBarThickness);
        }
    }

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
        setCustomMessage(R.layout.progress_dialog);
    }

    @Override
    public final void onStart() {
        super.onStart();
        progressBar = (CircularProgressBar) findViewById(R.id.progress_bar);
        adaptProgressBarColor();
        adaptProgressBarSize();
        adaptProgressBarThickness();
    }

    /**
     * Returns the color of the dialog's progress bar.
     *
     * @return The color of the dialog's progress bar, as an {@link Integer} value
     */
    public final int getProgressBarColor() {
        return progressBarColor;
    }

    /**
     * Sets the color of the dialog's progress bar.
     *
     * @param color
     *         The color, which should be set, as an {@link Integer} value
     */
    public final void setProgressBarColor(@ColorInt final int color) {
        this.progressBarColor = color;
        adaptProgressBarColor();
    }

    /**
     * Returns the size of the dialog's progress bar.
     *
     * @return The size of the dialog's progress bar in pixels as an {@link Integer} value
     */
    public final int getProgressBarSize() {
        return progressBarSize;
    }

    /**
     * Sets the size of the dialog's progress bar.
     *
     * @param size
     *         The size, which should be set, in pixels as an {@link Integer} value. The size must
     *         be at least 0
     */
    public final void setProgressBarSize(final int size) {
        ensureAtLeast(size, 0, "The size must be at least 0");
        this.progressBarSize = size;
        adaptProgressBarSize();
    }

    /**
     * Returns the thickness of the dialog's progress bar.
     *
     * @return The thickness of the dialog's progress bar in pixels as an {@link Integer} value
     */
    public final int getProgressBarThickness() {
        return progressBarThickness;
    }

    /**
     * Sets the thickness of the dialog's progress bar.
     *
     * @param thickness
     *         The thickness, which should be set, in pixels as an {@link Integer} value. The
     *         thickness must be at least 1
     */
    public final void setProgressBarThickness(final int thickness) {
        ensureAtLeast(thickness, 1, "The thickness must be at least 1");
        this.progressBarThickness = thickness;
        adaptProgressBarThickness();
    }

    @Override
    public final void onStop() {
        super.onStop();
        progressBar = null;
    }

    @Nullable
    @Override
    protected final View onCreateContentView(@NonNull final ViewGroup parent) {
        return null;
    }

}