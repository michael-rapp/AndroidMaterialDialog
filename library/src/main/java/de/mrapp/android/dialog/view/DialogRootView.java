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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import de.mrapp.android.dialog.R;

import static de.mrapp.android.util.Condition.ensureAtLeast;

/**
 * The root view of a dialog, which is designed according to Android 5's Material Design guidelines
 * even on pre-Lollipop devices. It is used to restrict the maximum height of the dialog on tablets
 * in landscape mode.
 *
 * @author Michael Rapp
 * @since 3.4.3
 */
public class DialogRootView extends LinearLayout {

    /**
     * True, if the view's shadow is shown, false otherwise.
     */
    private boolean showShadow = true;

    /**
     * The maximum width of the view.
     */
    private int maxWidth = -1;

    /**
     * The maximum height of the view.
     */
    private int maxHeight = -1;

    /**
     * The drawable, which is shown as the view's background.
     */
    private Drawable background;

    /**
     * The paint, which is used to draw the view's background.
     */
    private Paint paint;

    /**
     * The bitmap, which is used to draw the view's background.
     */
    private Bitmap backingBitmap;

    /**
     * The canvas, whic his used to draw the view's background.
     */
    private Canvas backingCanvas;

    /**
     * Initializes the view.
     */
    private void initialize() {
        background =
                ContextCompat.getDrawable(getContext(), android.R.drawable.dialog_holo_light_frame);
        paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
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
     * Returns, whether the view's shadow is shown, or not.
     *
     * @return True, if the view's shadow is shown, false otherwise
     */
    public final boolean isShadowShown() {
        return showShadow;
    }

    /**
     * Sets, whether the view's shadow should be shown, or not.
     *
     * @param showShadow
     *         True, if the shadow should be shown, false otherwise
     */
    public final void showShadow(final boolean showShadow) {
        this.showShadow = showShadow;
        invalidate();
    }

    /**
     * Returns the maximum width of the view.
     *
     * @return The maximum width of the view in pixels as an {@link Integer} value or -1, if no
     * maximum width is set
     */
    public final int getMaxWidth() {
        return maxWidth;
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
            ensureAtLeast(maxWidth, 1, "The maximum width must be at least 1");
        }

        this.maxWidth = maxWidth;
        requestLayout();
    }

    /**
     * Returns the maximum height of the view.
     *
     * @return The maximum height of the view in pixels as an {@link Integer} value or -1, if no
     * maximum height is set
     */
    public final int getMaxHeight() {
        return maxHeight;
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
            ensureAtLeast(maxHeight, 1, "The maximum height must be at least 1");
        }

        this.maxHeight = maxHeight;
        requestLayout();
    }

    @Override
    protected final void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        int shadowWidth = isShadowShown() ?
                getResources().getDimensionPixelSize(R.dimen.dialog_shadow_width) : 0;
        int maxWidthMeasureSpec = getMaxWidth() != -1 ? MeasureSpec
                .makeMeasureSpec(getMaxWidth() + (shadowWidth * 2), MeasureSpec.AT_MOST) : -1;
        int maxHeightMeasureSpec = getMaxHeight() != -1 ? MeasureSpec
                .makeMeasureSpec(getMaxHeight() + (shadowWidth * 2), MeasureSpec.AT_MOST) : -1;
        super.onMeasure(maxWidthMeasureSpec != -1 ? maxWidthMeasureSpec : widthMeasureSpec,
                maxHeightMeasureSpec != -1 ? maxHeightMeasureSpec : heightMeasureSpec);
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

        if (isShadowShown()) {
            int width = getWidth();
            int height = getHeight();
            background.setBounds(0, 0, width, height);
            backingBitmap.eraseColor(Color.TRANSPARENT);
            background.draw(backingCanvas);
            canvas.drawBitmap(backingBitmap, 0, 0, paint);
        }
    }

}