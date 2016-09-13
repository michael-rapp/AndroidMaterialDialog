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

import android.graphics.Typeface;

/**
 * Global dialog configurations. For now the title and button fonts can be set using
 */
public class MaterialDialogConfig {
    private static Typeface titleFont = null;
    private static Typeface buttonFont = null;

    /**
     * Set the title font for all dialogs created in the future.
     *
     * @param typeface
     *         This font will be used for all titles in dialogs created in the future. Set to null
     *         to use the default font.
     */
    public static void setTitleFont(Typeface typeface) {
        titleFont = typeface;
    }

    /**
     * Set the button font for all dialogs created in the future.
     *
     * @param typeface
     *         This font will be used for buttons in dialogs created in the future. Set to null to
     *         use the default font.
     */
    public static void setButtonFont(Typeface typeface) {
        buttonFont = typeface;
    }

    /**
     * Get the globally used title font for dialogs.
     * 
     * @return
     *        globally used title font, null if set to use default
     */
    public static Typeface getTitleFont() {
        return titleFont;
    }

    /**
     * Get the globally used button font for dialogs.
     *
     * @return
     *        globally used button font, null if set to use default
     */
    public static Typeface getButtonFont() {
        return buttonFont;
    }
}
