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

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * A layer drawable, which allows to cross-fade between the first and second layer.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public class CrossFadeTransitionDrawable extends AbstractTransitionDrawable {

    /**
     * True, if the first drawable is faded out, false otherwise.
     */
    private boolean crossFade;

    /**
     * True, if cross fading is used by a running transition, false otherwise.
     */
    private boolean useCrossFade;

    /**
     * Creates a new layer drawable, which allows to cross-fade between the first and second layer.
     *
     * @param layers
     *         The layers of this drawable as a {@link Drawable} array. The array may not be null
     */
    public CrossFadeTransitionDrawable(@NonNull final Drawable[] layers) {
        super(layers);
        this.crossFade = false;
        this.useCrossFade = false;
    }

    /**
     * Returns, whether the first drawable is faded out, or not.
     *
     * @return True, if the first drawable is faded out, false otherwise
     */
    public final boolean isCrossFade() {
        return crossFade;
    }

    /**
     * Sets, whether the first drawable should be faded out, or not.
     *
     * @param crossFade
     *         True, if the first drawable should be faded out, false otherwise
     */
    public final void setCrossFade(final boolean crossFade) {
        this.crossFade = crossFade;
    }

    @Override
    protected final void onStartTransition() {
        this.useCrossFade = isCrossFade();
    }

    @Override
    protected final void onDraw(final float interpolatedTime, @NonNull final Canvas canvas) {
        int currentAlpha = Math.round(255 * interpolatedTime);
        Drawable first = getDrawable(0);

        if (useCrossFade) {
            first.setAlpha(255 - currentAlpha);
        }

        first.draw(canvas);
        Drawable second = getDrawable(1);
        second.setAlpha(currentAlpha);
        second.draw(canvas);
    }

    @Override
    protected final void onEndTransition() {
        getDrawable(0).setAlpha(255);
        getDrawable(1).setAlpha(255);
    }

}