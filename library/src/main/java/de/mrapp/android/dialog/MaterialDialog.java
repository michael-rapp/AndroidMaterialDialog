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
package de.mrapp.android.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;

/**
 * A dialog, which is designed according to Android 5's Material Design guidelines even on
 * pre-Lollipop devices. Such a dialog consists of a title, a message and up to three buttons.
 * Furthermore, the dialog can be used to show list items. It is possible to customize the color of
 * the dialog's title and button texts and the title as well as the dialog's content can be replaced
 * with a custom view.
 *
 * For creating or showing such dialogs, the methods {@link MaterialDialog#create()} or {@link
 * MaterialDialog#show()} of the builder {@link MaterialDialog.Builder} can be used.
 *
 * @author Michael Rapp
 * @since 3.0.0
 */
public class MaterialDialog extends AbstractListDialog {

    /**
     * A builder, which allows to create and show dialogs, which are designed according to Android
     * 5's Material Design guidelines even on pre-Lollipop devices. Such a dialog consists of a
     * title, a message and up to three buttons. Furthermore, the dialog can be used to show list
     * items. It is possible to customize the color of the dialog's title and button texts and the
     * title as well as the dialog's content can be replaced with a custom view.
     */
    public static class Builder extends AbstractListDialog.Builder<MaterialDialog, Builder> {

        /**
         * Creates a new builder, which allows to create dialogs, which are designed according to
         * Android 5's Material Design guidelines even on pre-Lollipop devices.
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
         * Android 5's Material Design guidelines even on pre-Lollipop devices.
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

        @Override
        protected final MaterialDialog onCreateDialog(@NonNull final Context context,
                                                      @StyleRes final int themeResourceId) {
            return new MaterialDialog(context, themeResourceId);
        }

    }

    /**
     * Creates a dialog, which is designed according to Android 5's Material Design guidelines even
     * on pre-Lollipop devices.
     *
     * @param context
     *         The context, which should be used by the dialog, as an instance of the class {@link
     *         Context}. The context may not be null
     * @param themeResourceId
     *         The resource id of the theme, which should be used by the dialog, as an {@link
     *         Integer} value. The resource id must correspond to a valid theme
     */
    protected MaterialDialog(@NonNull final Context context, @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
    }

}