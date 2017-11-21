/*
 * Copyright 2014 - 2017 Michael Rapp
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
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import java.util.Collections;
import java.util.Map;

import de.mrapp.android.dialog.ProgressDialog.ProgressBarPosition;
import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.model.ButtonBarDialog;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;
import de.mrapp.android.view.CircularProgressBar;

import static de.mrapp.android.util.Condition.ensureAtLeast;
import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * A decorator, which allows to modify the view hierarchy of a dialog, which is designed according
 * to Android 5's Material Design guidelines even on pre-Lollipop devices and allows to indicate a
 * running progress.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public class ProgressDialogDecorator extends AbstractDialogDecorator<ButtonBarDialog>
        implements de.mrapp.android.dialog.model.ProgressDialogDecorator {

    /**
     * The name of the extra, which is used to store the color of the dialog's progress bar within a
     * bundle.
     */
    private static final String PROGRESS_BAR_COLOR_EXTRA =
            ProgressDialogDecorator.class.getSimpleName() + "::progressBarColor";

    /**
     * The name of the extra, which is used to store the size of the dialog's progress bar within a
     * bundle.
     */
    private static final String PROGRESS_BAR_SIZE_EXTRA =
            ProgressDialogDecorator.class.getSimpleName() + "::progressBarSize";

    /**
     * The name of the extra, which is used to store the thickness of the dialog's progress bar
     * within a bundle.
     */
    private static final String PROGRESS_BAR_THICKNESS_EXTRA =
            ProgressDialogDecorator.class.getSimpleName() + "::progressBarThickness";

    /**
     * The name of the extra, which is used to store the position of the dialog's progress bar
     * within a bundle.
     */
    private static final String PROGRESS_BAR_POSITION_EXTRA =
            ProgressDialogDecorator.class.getSimpleName() + "::progressBarPosition";

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
     * The position of the dialog's progress bar.
     */
    private ProgressBarPosition progressBarPosition;

    /**
     * Returns the resource id of the layout, which should be used as the dialog's custom message,
     * depending on the position of the dialog's progress bar.
     *
     * @return The resource id of the layout, which should be used as the diaog's custom message, as
     * an {@link Integer} value
     */
    private int getCustomMessageId() {
        switch (progressBarPosition) {
            case LEFT:
                return R.layout.progress_dialog_left;
            case TOP:
                return R.layout.progress_dialog_top;
            case RIGHT:
                return R.layout.progress_dialog_right;
            case BOTTOM:
                return R.layout.progress_dialog_bottom;
            default:
                return R.layout.progress_dialog_left;
        }
    }

    /**
     * Adapts the dialog's progress bar.
     */
    private void adaptProgressBar() {
        getDialog().setCustomMessage(getCustomMessageId());

        if (getRootView() != null) {
            View progressView = getRootView().findViewById(R.id.progress_bar);
            progressBar = progressView instanceof CircularProgressBar ?
                    (CircularProgressBar) progressView : null;
            adaptProgressBarColor();
            adaptProgressBarSize();
            adaptProgressBarThickness();
            adaptMessageTextSize();
        }
    }

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
     * Adapts the text size of the dialog's message, depending on whether a title is shown.
     */
    private void adaptMessageTextSize() {
        if (getRootView() != null) {
            View messageView = getRootView().findViewById(android.R.id.message);

            if (messageView instanceof TextView) {
                TextView messageTextView = (TextView) messageView;

                if (TextUtils.isEmpty(getDialog().getTitle())) {
                    messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getContext().getResources()
                                    .getDimensionPixelSize(R.dimen.dialog_message_text_size_large));
                } else {
                    messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            getContext().getResources().getDimensionPixelSize(
                                    R.dimen.dialog_message_text_size_normal));
                }
            }
        }
    }

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog, which is
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices and
     * allows to indicate a running progress.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the type {@link ButtonBarDialog}. The dialog may not be null
     */
    public ProgressDialogDecorator(@NonNull final ButtonBarDialog dialog) {
        super(dialog);
    }

    @Override
    public final int getProgressBarColor() {
        return progressBarColor;
    }

    @Override
    public final void setProgressBarColor(@ColorInt final int color) {
        this.progressBarColor = color;
        adaptProgressBarColor();
    }

    @Override
    public final int getProgressBarSize() {
        return progressBarSize;
    }

    @Override
    public final void setProgressBarSize(final int size) {
        ensureAtLeast(size, 0, "The size must be at least 0");
        this.progressBarSize = size;
        adaptProgressBarSize();
    }

    @Override
    public final int getProgressBarThickness() {
        return progressBarThickness;
    }

    @Override
    public final void setProgressBarThickness(final int thickness) {
        ensureAtLeast(thickness, 1, "The thickness must be at least 1");
        this.progressBarThickness = thickness;
        adaptProgressBarThickness();
    }

    @Override
    public final ProgressBarPosition getProgressBarPosition() {
        return progressBarPosition;
    }

    @Override
    public final void setProgressBarPosition(@NonNull final ProgressBarPosition position) {
        ensureNotNull(position, "The position may not be null");
        this.progressBarPosition = position;
        adaptProgressBar();
    }

    @Override
    public final void onSaveInstanceState(@NonNull final Bundle outState) {
        outState.putInt(PROGRESS_BAR_COLOR_EXTRA, getProgressBarColor());
        outState.putInt(PROGRESS_BAR_SIZE_EXTRA, getProgressBarSize());
        outState.putInt(PROGRESS_BAR_THICKNESS_EXTRA, getProgressBarThickness());
        outState.putInt(PROGRESS_BAR_POSITION_EXTRA, getProgressBarPosition().getValue());
    }

    @Override
    public final void onRestoreInstanceState(@NonNull final Bundle savedInstanceState) {
        setProgressBarColor(savedInstanceState.getInt(PROGRESS_BAR_COLOR_EXTRA));
        setProgressBarSize(savedInstanceState.getInt(PROGRESS_BAR_SIZE_EXTRA));
        setProgressBarThickness(savedInstanceState.getInt(PROGRESS_BAR_THICKNESS_EXTRA));
        setProgressBarPosition(ProgressBarPosition
                .fromValue(savedInstanceState.getInt(PROGRESS_BAR_POSITION_EXTRA)));
    }

    @NonNull
    @Override
    protected final Map<ViewType, View> onAttach(@NonNull final Window window,
                                                 @NonNull final View view,
                                                 @NonNull final Map<ViewType, View> areas,
                                                 final Void param) {
        adaptProgressBar();
        return Collections.emptyMap();
    }

    @Override
    protected final void onDetach() {
        progressBar = null;
    }

}