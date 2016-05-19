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

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import de.mrapp.android.dialog.decorator.MaterialDialogDecorator;
import de.mrapp.android.dialog.model.MaterialDialog;
import de.mrapp.android.util.DisplayUtil;

import static de.mrapp.android.util.DisplayUtil.getDeviceType;
import static de.mrapp.android.util.DisplayUtil.getOrientation;

/**
 * An abstract base class for all dialogs, which are designed according to Android 5's Material
 * design guidelines even on pre-Lollipop devices.
 *
 * @author Michael Rapp
 * @since 3.2.0
 */
public abstract class AbstractMaterialDialog extends Dialog implements MaterialDialog {

    /**
     * An abstract base class for all builders, which allow to create and show dialogs, which are
     * designed according to Android 5's Material Design guidelines even on pre-Lollipop devices.
     *
     * @param <DialogType>
     *         The type of the dialog, which is created by the builder
     * @param <BuilderType>
     *         The type of the builder
     */
    public abstract static class AbstractBuilder<DialogType extends AbstractMaterialDialog, BuilderType extends AbstractBuilder<DialogType, ?>> {

        /**
         * The dialog, which is configured by the builder.
         */
        private DialogType dialog;

        /**
         * Initializes the builder.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value, or -1, if the default theme should be used
         */
        private void initialize(@NonNull final Context context,
                                @StyleRes final int themeResourceId) {
            int themeId = themeResourceId != -1 ? themeResourceId : R.style.MaterialDialog_Light;
            dialog = onCreateDialog(context, themeId);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setAttributes(createLayoutParams());
            obtainStyledAttributes(themeId);
        }

        /**
         * Creates and returns the layout params, which should be used by the dialog.
         *
         * @return The layout params, which should be used by the dialog, as an instance of the
         * class {@link WindowManager.LayoutParams}
         */
        private WindowManager.LayoutParams createLayoutParams() {
            WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();

            if (getDeviceType(getContext()) == DisplayUtil.DeviceType.PHONE &&
                    getOrientation(getContext()) == DisplayUtil.Orientation.PORTRAIT) {
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            }

            return layoutParams;
        }

        /**
         * Obtains the background from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the background should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainBackground(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogBackground});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setBackgroundColor(color);
            } else {
                int resourceId = typedArray.getResourceId(0, 0);

                if (resourceId != 0) {
                    setBackground(resourceId);
                }
            }
        }

        /**
         * Obtains the message color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the message color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainMessageColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogMessageColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setMessageColor(color);
            }
        }

        /**
         * Obtains the title color from a specific theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, the title color should be obtained from, as an
         *         {@link Integer} value
         */
        private void obtainTitleColor(@StyleRes final int themeResourceId) {
            TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(themeResourceId,
                    new int[]{R.attr.materialDialogTitleColor});
            int color = typedArray.getColor(0, -1);

            if (color != -1) {
                setTitleColor(color);
            }
        }

        /**
         * Returns the builder itself, casted to the generic type BuilderType.
         *
         * @return The builder itself as an instance of the generic type BuilderType
         */
        @SuppressWarnings("unchecked")
        protected final BuilderType self() {
            return (BuilderType) this;
        }

        /**
         * Returns the dialog, which is configured by the builder.
         *
         * @return The dialog, which is configured by the builder, as an instance of the generic
         * type BuilderType
         */
        protected final DialogType getDialog() {
            return dialog;
        }

        /**
         * Obtains all relevant attributes from the current theme.
         *
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid style resource
         */
        @CallSuper
        protected void obtainStyledAttributes(@StyleRes final int themeResourceId) {
            obtainBackground(themeResourceId);
            obtainMessageColor(themeResourceId);
            obtainTitleColor(themeResourceId);
        }

        /**
         * The method, which is invoked on subclasses in order to create the dialog, which is
         * configured by the builder.
         *
         * @param context
         *         The context, which should be used by the dialog, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value
         * @return The dialog, which has been created, as an instance of the generic type DialogType
         */
        protected abstract DialogType onCreateDialog(@NonNull final Context context,
                                                     @StyleRes final int themeResourceId);

        /**
         * Creates a new builder, which allows to create and show dialogs, which are designed
         * according to Android 5's Material Design guidelines even on pre-Lollipop devices.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         */
        public AbstractBuilder(@NonNull final Context context) {
            this(context, -1);
        }

        /**
         * Creates a new builder, which allows to create and show dialogs, which are designed
         * according to Android 5's Material Design guidelines even on pre-Lollipop devices.
         *
         * @param context
         *         The context, which should be used by the builder, as an instance of the class
         *         {@link Context}. The context may not be null
         * @param themeResourceId
         *         The resource id of the theme, which should be used by the dialog, as an {@link
         *         Integer} value. The resource id must correspond to a valid theme
         */
        public AbstractBuilder(@NonNull final Context context,
                               @StyleRes final int themeResourceId) {
            initialize(context, themeResourceId);
        }

        /**
         * Returns the context, which is used by the builder.
         *
         * @return The context, which is used by the builder, as an instance of the class {@link
         * Context}
         */
        public final Context getContext() {
            return getDialog().getContext();
        }

        /**
         * Sets, whether the dialog, which is created by the builder, should be cancelable, or not.
         *
         * @param cancelable
         *         True, if the dialog, which is created by the builder, should be cancelable, false
         *         otherwise
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCancelable(final boolean cancelable) {
            getDialog().setCancelable(cancelable);
            return self();
        }

        /**
         * Sets the listener, which should be notified, when the dialog, which is created by the
         * builder, is canceled.
         *
         * Even in a cancelable dialog, the dialog may be dismissed for reasons other than being
         * canceled or one of the supplied choices being selected. If you are interested in
         * listening for all cases where the dialog is dismissed and not just when it is canceled,
         * see {@link #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         * setOnDismissListener}.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnCancelListener}, or null, if no listener should be set
         * @return The builder, the method has been called upon, as an instance of the class
         * BuilderType
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(android.content.DialogInterface.OnDismissListener)
         */
        public BuilderType setOnCancelListener(@Nullable final OnCancelListener listener) {
            getDialog().setOnCancelListener(listener);
            return self();
        }

        /**
         * Sets the listener, which should be notified, when the dialog, which is created by the
         * builder, is dismissed for any reason.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnDismissListener}, or null, if no listener should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setOnDismissListener(@Nullable final OnDismissListener listener) {
            getDialog().setOnDismissListener(listener);
            return self();
        }

        /**
         * Sets the listener, which should be notified, if a key is dispatched to the dialog, which
         * is created by the builder.
         *
         * @param listener
         *         The listener, which should be set, as an instance of the type {@link
         *         OnKeyListener}, or null, if no listener should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setOnKeyListener(@Nullable final OnKeyListener listener) {
            getDialog().setOnKeyListener(listener);
            return self();
        }

        /**
         * Sets the color of the title of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setTitleColor(@ColorInt final int color) {
            getDialog().setTitleColor(color);
            return self();
        }

        /**
         * Sets the color of the message of the dialog, which is created by the builder.
         *
         * @param color
         *         The color, which should be set, as an {@link Integer} value or -1, if no custom
         *         color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMessageColor(@ColorInt final int color) {
            getDialog().setMessageColor(color);
            return self();
        }

        /**
         * Sets the background of the dialog, which is created by the builder.
         *
         * @param background
         *         The background, which should be set, as an instance of the class {@link Drawable}
         *         or null, if no custom background should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setBackground(@Nullable final Drawable background) {
            getDialog().setBackground(background);
            return self();
        }

        /**
         * Sets the background of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the background, which should be set, as an {@link Integer}
         *         value. The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setBackground(@DrawableRes final int resourceId) {
            getDialog().setBackground(resourceId);
            return self();
        }

        /**
         * Sets the background color of the dialog, which is created by the builder.
         *
         * @param color
         *         The background color, which should be set, as an {@link Integer} value or -1, if
         *         no custom background color should be set
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setBackgroundColor(@ColorInt final int color) {
            getDialog().setBackgroundColor(color);
            return self();
        }

        /**
         * Sets the title of the dialog, which is created by the builder.
         *
         * @param title
         *         The title, which should be set, as an instance of the type {@link CharSequence}
         *         or null, if no title should be shown
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setTitle(@Nullable final CharSequence title) {
            getDialog().setTitle(title);
            return self();
        }

        /**
         * Sets the title of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the title, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setTitle(@StringRes final int resourceId) {
            getDialog().setTitle(resourceId);
            return self();
        }

        /**
         * Sets the message of the dialog, which is created by the builder.
         *
         * @param message
         *         The message, which should be set, as an instance of the type {@link CharSequence}
         *         or null, if no message should be shown
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMessage(@Nullable final CharSequence message) {
            getDialog().setMessage(message);
            return self();
        }

        /**
         * Sets the message of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the message, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid string resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setMessage(@StringRes final int resourceId) {
            getDialog().setMessage(resourceId);
            return self();
        }

        /**
         * Sets the icon of the dialog, which is created by the builder.
         *
         * @param icon
         *         The icon, which should be set, as an instance of the class {@link Drawable} or
         *         null, if no icon should be shown
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setIcon(@Nullable final Drawable icon) {
            getDialog().setIcon(icon);
            return self();
        }

        /**
         * Sets the icon of the dialog, which is created by the builder.
         *
         * @param resourceId
         *         The resource id of the icon, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setIcon(@DrawableRes final int resourceId) {
            getDialog().setIcon(resourceId);
            return self();
        }

        /**
         * Set the icon of the dialog, which is created by the builder.
         *
         * @param attributeId
         *         The id of the theme attribute, which supplies the icon, which should be set, as
         *         an {@link Integer} value. The id must point to a valid drawable resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setIconAttribute(@AttrRes final int attributeId) {
            getDialog().setIconAttribute(attributeId);
            return self();
        }

        /**
         * Sets the custom view, which should be shown by the dialog, which is created by the
         * builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be shown
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setView(@Nullable final View view) {
            getDialog().setView(view);
            return self();
        }

        /**
         * Sets the custom view, which should be shown by the dialog, which is created by the
         * builder.
         *
         * @param resourceId
         *         The resource id of the view, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setView(@LayoutRes final int resourceId) {
            getDialog().setView(resourceId);
            return self();
        }

        /**
         * Sets the custom view, which should be used to show the title of the dialog, which is
         * created by the builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be used to show the title
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCustomTitle(@Nullable final View view) {
            getDialog().setCustomTitle(view);
            return self();
        }

        /**
         * Sets the custom view, which should be used to show the title of the dialog, which is
         * created by the builder.
         *
         * @param resourceId
         *         The resource id of the view, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCustomTitle(@LayoutRes final int resourceId) {
            getDialog().setCustomTitle(resourceId);
            return self();
        }

        /**
         * Sets the custom view, which should be used to show the message of the dialog, which is
         * created by the builder.
         *
         * @param view
         *         The view, which should be set, as an instance of the class {@link View} or null,
         *         if no custom view should be used to show the message
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCustomMessage(@Nullable final View view) {
            getDialog().setCustomMessage(view);
            return self();
        }

        /**
         * Sets the custom view, which should be used to show the message of the dialog, which is
         * created by the builder.
         *
         * @param resourceId
         *         The resource id of the view, which should be set, as an {@link Integer} value.
         *         The resource id must correspond to a valid layout resource
         * @return The builder, the method has been called upon, as an instance of the generic type
         * BuilderType
         */
        public final BuilderType setCustomMessage(@LayoutRes final int resourceId) {
            getDialog().setCustomMessage(resourceId);
            return self();
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder. Calling
         * this method does not display the dialog.
         *
         * @return The dialog, which has been created as an instance of the generic type DialogType
         */
        public final DialogType create() {
            return getDialog();
        }

        /**
         * Creates a dialog with the arguments, which have been supplied to the builder and
         * immediately displays it.
         *
         * @return The dialog, which has been shown, as an instance of the generic type DialogType
         */
        public final DialogType show() {
            getDialog().show();
            return getDialog();
        }

    }

    /**
     * The decorator, which is used by the dialog.
     */
    private final MaterialDialogDecorator decorator;

    /**
     * Inflates the dialog's root view.
     *
     * @return The view, which has been inflated, as an instance of the class {@link View}. The view
     * may not be null
     */
    private View inflateLayout() {
        return View.inflate(getContext(), R.layout.material_dialog, null);
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
    protected AbstractMaterialDialog(@NonNull final Context context,
                                     @StyleRes final int themeResourceId) {
        super(context, themeResourceId);
        this.decorator = new MaterialDialogDecorator(this);
    }

    /**
     * The method, which is invoked when the dialog's decorators should be attached. This method may
     * be overridden by subclasses in order to attach additional decorators.
     *
     * @param view
     *         The root view of the view hierarchy, which should be modified by the decorators, as
     *         an instance of the class {@link View}. The view may not be null
     */
    @CallSuper
    protected void onAttachDecorators(@NonNull final View view) {
        decorator.attach(view);
    }

    /**
     * The method, which is invoked when the dialog's decorators should be detached. This method
     * must be overridden by subclasses if they attach additional decorators.
     */
    @CallSuper
    protected void onDetachDecorators() {
        decorator.detach();
    }

    @Override
    public final Drawable getIcon() {
        return decorator.getIcon();
    }

    @Override
    public final void setIcon(final Drawable icon) {
        decorator.setIcon(icon);
    }

    @Override
    public final void setIcon(@DrawableRes final int resourceId) {
        decorator.setIcon(resourceId);
    }

    @Override
    public final void setIconAttribute(@AttrRes final int attributeId) {
        decorator.setIconAttribute(attributeId);
    }

    @Override
    public final int getTitleColor() {
        return decorator.getTitleColor();
    }

    @Override
    public final void setTitleColor(@ColorInt final int color) {
        decorator.setTitleColor(color);
    }

    @Override
    public final int getMessageColor() {
        return decorator.getMessageColor();
    }

    @Override
    public final void setMessageColor(@ColorInt final int color) {
        decorator.setMessageColor(color);
    }

    @Override
    public final Drawable getBackground() {
        return decorator.getBackground();
    }

    @Override
    public final void setBackground(@Nullable final Drawable background) {
        decorator.setBackground(background);
    }

    @Override
    public final void setBackground(@DrawableRes final int resourceId) {
        decorator.setBackground(resourceId);
    }

    @Override
    public final void setBackgroundColor(@ColorInt final int color) {
        decorator.setBackgroundColor(color);
    }

    @Override
    public final void setCustomTitle(@Nullable final View view) {
        decorator.setCustomTitle(view);
    }

    @Override
    public final void setCustomTitle(@LayoutRes final int resourceId) {
        decorator.setCustomTitle(resourceId);
    }

    @Override
    public final void setCustomMessage(@Nullable final View view) {
        decorator.setCustomMessage(view);
    }

    @Override
    public final void setCustomMessage(@LayoutRes final int resourceId) {
        decorator.setCustomMessage(resourceId);
    }

    @Override
    public final void setView(@Nullable final View view) {
        decorator.setView(view);
    }

    @Override
    public final void setView(@LayoutRes final int resourceId) {
        decorator.setView(resourceId);
    }

    @Override
    public final CharSequence getMessage() {
        return decorator.getMessage();
    }

    @Override
    public final void setMessage(@Nullable final CharSequence message) {
        decorator.setMessage(message);
    }

    @Override
    public final void setMessage(@StringRes final int resourceId) {
        decorator.setMessage(resourceId);
    }

    @Override
    public final CharSequence getTitle() {
        return decorator.getTitle();
    }

    @Override
    public final void setTitle(@Nullable final CharSequence title) {
        super.setTitle(title);
        decorator.setTitle(title);
    }

    @Override
    public final void onStart() {
        super.onStart();
        View view = inflateLayout();
        setContentView(view);
        onAttachDecorators(view);
    }

    @Override
    public final void onStop() {
        super.onStop();
        onDetachDecorators();
    }

}