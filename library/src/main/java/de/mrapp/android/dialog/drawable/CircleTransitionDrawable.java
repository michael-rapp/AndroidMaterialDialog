package de.mrapp.android.dialog.drawable;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.SystemClock;
import android.support.annotation.NonNull;

import static de.mrapp.android.util.Condition.ensureAtLeast;

/**
 * A layer drawable, which allows to translate between the first and second layer by animating the
 * second layer as a circle, which grows from a specific position.
 *
 * @author Michael Rapp
 */
public class CircleTransitionDrawable extends LayerDrawable {

    /**
     * The state when a transition is about to start.
     */
    private static final int TRANSITION_STARTING = 0;

    /**
     * The state when a transition is in progress.
     */
    private static final int TRANSITION_RUNNING = 1;

    /**
     * The state when no transition is running.
     */
    private static final int TRANSITION_NONE = 2;

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
     * The duration, which has been used by the last transition.
     */
    private int duration;

    /**
     * The time, the last transition has been started.
     */
    private long startTime;

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
     * The current radius of the second layer.
     */
    private double currentRadius;

    /**
     * The current state of the drawable.
     */
    private int state;

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
     * Initializes the drawable.
     */
    private void initialize() {
        this.x = null;
        this.y = null;
        this.radius = 0;
        this.fromX = -1;
        this.fromY = -1;
        this.fromRadius = -1;
        this.currentRadius = -1;
        this.state = TRANSITION_NONE;
    }

    /**
     * Creates a new layer drawable with the list of specified layers.
     *
     * @param layers
     *         a list of drawables to use as layers in this new drawable, must be non-null
     */
    public CircleTransitionDrawable(@NonNull final Drawable[] layers) {
        super(layers);
        initialize();
        paint = new Paint();
        paint.setAntiAlias(true);
        porterDuffPaint = new Paint();
        porterDuffPaint.setAntiAlias(true);
        porterDuffPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
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

    /**
     * Starts the transition, which shows the second layer in front of the first layer.
     *
     * @param duration
     *         The duration of the transition in milliseconds as an {@link Integer} value
     */
    public final void startTransition(final int duration) {
        this.duration = duration;
        this.fromX = getX() != null ? getX() : -1;
        this.fromY = getY() != null ? getY() : -1;
        this.fromRadius = getRadius();
        this.state = TRANSITION_STARTING;
        invalidateSelf();
    }

    /**
     * Shows only the first layer.
     */
    public final void resetTransition() {
        initialize();
        invalidateSelf();
    }

    @Override
    public final void setBounds(final int left, final int top, final int right, final int bottom) {
        super.setBounds(left, top, right, bottom);
        backingBitmap = Bitmap.createBitmap(right - left, bottom - top, Bitmap.Config.ARGB_8888);
        backingCanvas = new Canvas(backingBitmap);
    }

    @Override
    public final void draw(final Canvas canvas) {
        boolean done = true;

        if (state == TRANSITION_STARTING) {
            startTime = SystemClock.uptimeMillis();
            done = false;
            state = TRANSITION_RUNNING;
        } else if (state == TRANSITION_RUNNING) {
            if (startTime >= 0) {
                float normalized = (float) (SystemClock.uptimeMillis() - startTime) / duration;
                done = normalized >= 1f;
                normalized = Math.min(normalized, 1f);
                float x = fromX != -1 ? fromX : getBounds().width() / 2f;
                float y = fromY != -1 ? fromY : getBounds().height() / 2f;
                float width = Math.max(x, getBounds().width() - x);
                float height = Math.max(y, getBounds().height() - y);
                currentRadius = fromRadius +
                        (Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2)) - fromRadius) *
                                normalized;
            }
        }

        Drawable first = getDrawable(0);
        Drawable second = getDrawable(1);

        if (done) {
            if (fromRadius != -1 && currentRadius != fromRadius) {
                second.draw(canvas);
            } else {
                first.draw(canvas);
            }

            return;
        }

        if (currentRadius > 0) {
            second.draw(canvas);
            backingBitmap.eraseColor(Color.TRANSPARENT);
            first.draw(backingCanvas);
            backingCanvas.drawCircle(fromX != -1 ? fromX : getBounds().width() / 2f,
                    fromY != -1 ? fromY : getBounds().height() / 2f, (float) currentRadius,
                    porterDuffPaint);
            canvas.drawBitmap(backingBitmap, 0, 0, paint);
        } else {
            first.draw(canvas);
        }

        invalidateSelf();
    }

}