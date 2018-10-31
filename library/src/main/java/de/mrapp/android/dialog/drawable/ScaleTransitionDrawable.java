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
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;

/**
 * A layer drawable, which allows to translate between the first and second layer by downscaling the
 * first layer and subsequently upscaling the second layer. Both layers should have the same size.
 *
 * @author Michael Rapp
 * @since 4.0.0
 */
public class ScaleTransitionDrawable extends AbstractTransitionDrawable {

    /**
     * The bounds of the first drawable, when the transition was started.
     */
    private Rect bounds;

    /**
     * Creates a new layer drawable, which allows to translate between the first and second layer by
     * downscaling the first layer and subsequently upscaling the second layer.
     *
     * @param layers
     *         The layers of this drawable as a {@link Drawable} array. The array may not be null
     */
    public ScaleTransitionDrawable(@NonNull final Drawable[] layers) {
        super(layers);
        this.bounds = null;
    }

    @Override
    protected final void onStartTransition() {
        this.bounds = new Rect(getDrawable(0).getBounds());
    }

    @Override
    protected final void onDraw(final float interpolatedTime, @NonNull final Canvas canvas) {
        int width = bounds.width();
        int height = bounds.height();
        Drawable drawable;
        int newWidth;
        int newHeight;

        if (interpolatedTime <= 0.5f) {
            drawable = getDrawable(0);
            newWidth = Math.round((1 - interpolatedTime * 2) * width);
            newHeight = Math.round((1 - interpolatedTime * 2) * height);
        } else {
            drawable = getDrawable(1);
            newWidth = Math.round((interpolatedTime - 0.5f) * 2 * width);
            newHeight = Math.round((interpolatedTime - 0.5f) * 2 * height);
        }

        int newLeft = Math.round(bounds.left + (width - newWidth) / 2f);
        int newTop = Math.round(bounds.top + (height - newHeight) / 2f);
        drawable.setBounds(newLeft, newTop, newLeft + newWidth, newTop + newHeight);
        drawable.draw(canvas);
    }

    @Override
    protected final void onEndTransition() {
        getDrawable(0).setBounds(bounds);
        getDrawable(1).setBounds(bounds);
    }

}
