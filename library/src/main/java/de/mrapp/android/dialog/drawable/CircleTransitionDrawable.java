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
package de.mrapp.android.dialog.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import static de.mrapp.android.util.Condition.ensureAtLeast;

/**
 * A layer drawable, which allows to translate between the first and second layer by animating the
 * second layer as a circle, which grows from a specific position.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public class CircleTransitionDrawable extends AbstractTransitionDrawable {

    /**
     * The horizontal position, which is used by the transition.
     */
    private Integer x;

    /**
     * The vertical position, which is used by the transition.
     */
    private Integer y;

    /**
     * The radius, which is used by the transition.
     */
    private int radius;

    /**
     * The horizontal position, which has been used to start the currently running transition.
     */
    private float fromX;

    /**
     * The vertical position, which has been used to start the currently running transition.
     */
    private float fromY;

    /**
     * The radius, which has been used to start the currently running transition.
     */
    private int fromRadius;

    /**
     * The bitmap, which is used to draw the drawable.
     */
    private Bitmap backingBitmap;

    /**
     * The canvas, which is used to draw the drawable.
     */
    private Canvas backingCanvas;

    /**
     * The paint, which is used to draw the drawable.
     */
    private final Paint paint;

    /**
     * The paint, which is used to draw the drawable using a Porter Duff filter.
     */
    private final Paint porterDuffPaint;

    /**
     * Creates a new layer drawable with the list of specified layers.
     *
     * @param layers
     *         a list of drawables to use as layers in this new drawable, must be non-null
     */
    public CircleTransitionDrawable(@NonNull final Drawable[] layers) {
        super(layers);
        this.x = null;
        this.y = null;
        this.radius = 0;
        this.fromX = -1;
        this.fromY = -1;
        this.fromRadius = -1;
        this.paint = new Paint();
        this.paint.setAntiAlias(true);
        this.porterDuffPaint = new Paint();
        this.porterDuffPaint.setAntiAlias(true);
        this.porterDuffPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    /**
     * Sets the horizontal position, which is used by the transition.
     *
     * @return The horizontal position, which is used by the transition, in pixels as an {@link
     * Integer} value or null, if the transition is started at the center
     */
    public final Integer getX() {
        return x;
    }

    /**
     * Sets the horizontal position, which should be used by the transition.
     *
     * @param x
     *         The horizontal position, which should be set, in pixels as an {@link Integer} value
     */
    public final void setX(final int x) {
        ensureAtLeast(x, 0, "The horizontal position must be at least 0");
        this.x = x;
    }

    /**
     * Sets the vertical position, which is used by the transition.
     *
     * @return The vertical position, which is used by the transition, in pixels as an {@link
     * Integer} value or null, if the transition is started at the center
     */
    public final Integer getY() {
        return y;
    }

    /**
     * Sets the vertical position, which should be used by the transition.
     *
     * @param y
     *         The vertical position, which should be set, in pixels as an {@link Integer} value
     */
    public final void setY(final int y) {
        ensureAtLeast(y, 0, "The vertical position must be at least 0");
        this.y = y;
    }

    /**
     * Returns the radius, which is used by the transition.
     *
     * @return The radius, which is used by the transition, in pixels as an {@link Integer} value
     */
    public final int getRadius() {
        return radius;
    }

    /**
     * Sets the radius, which should be used by the transition.
     *
     * @param radius
     *         The radius, which should be set, in pixels as an {@link Integer} value
     */
    public final void setRadius(final int radius) {
        ensureAtLeast(radius, 0, "The radius must be at least 0");
        this.radius = radius;
    }

    @Override
    public final void setBounds(final int left, final int top, final int right, final int bottom) {
        super.setBounds(left, top, right, bottom);
        backingBitmap = Bitmap.createBitmap(right - left, bottom - top, Bitmap.Config.ARGB_8888);
        backingCanvas = new Canvas(backingBitmap);
    }

    @Override
    protected final void onStartTransition() {
        this.fromX = getX() != null ? getX() : -1;
        this.fromY = getY() != null ? getY() : -1;
        this.fromRadius = getRadius();
    }

    @Override
    protected final void onDraw(final float interpolatedTime, @NonNull final Canvas canvas) {
        float x = fromX != -1 ? fromX : getBounds().width() / 2f;
        float y = fromY != -1 ? fromY : getBounds().height() / 2f;
        float width = Math.max(x, getBounds().width() - x);
        float height = Math.max(y, getBounds().height() - y);
        double currentRadius = fromRadius +
                (Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) - fromRadius) *
                        interpolatedTime;

        if (currentRadius > 0) {
            getDrawable(1).draw(canvas);
            backingBitmap.eraseColor(Color.TRANSPARENT);
            getDrawable(0).draw(backingCanvas);
            backingCanvas.drawCircle(fromX != -1 ? fromX : getBounds().width() / 2f,
                    fromY != -1 ? fromY : getBounds().height() / 2f, (float) currentRadius,
                    porterDuffPaint);
            canvas.drawBitmap(backingBitmap, 0, 0, paint);
        } else {
            getDrawable(0).draw(canvas);
        }
    }

    @Override
    protected final void onEndTransition() {

    }

}