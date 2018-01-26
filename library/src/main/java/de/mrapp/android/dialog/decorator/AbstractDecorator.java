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
package de.mrapp.android.dialog.decorator;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;

import java.util.Map;

import de.mrapp.android.dialog.R;
import de.mrapp.android.dialog.ScrollableArea.Area;
import de.mrapp.android.dialog.listener.AreaListener;
import de.mrapp.android.dialog.model.Dialog;
import de.mrapp.android.dialog.model.DialogDecorator;
import de.mrapp.android.dialog.view.DialogRootView;
import de.mrapp.android.dialog.view.DialogRootView.ViewType;
import de.mrapp.android.util.datastructure.ListenerList;

import static de.mrapp.android.util.Condition.ensureNotNull;

/**
 * An abstract base class for all decorators, which allow to modify the view hierarchy of a dialog.
 *
 * @author Michael Rapp
 * @since 4.2.0
 */
public abstract class AbstractDecorator<DialogType extends Dialog, ParamType>
        implements DialogDecorator {

    /**
     * The dialog, whose view hierarchy is modified by the decorator.
     */
    private final DialogType dialog;

    /**
     * The listeners, which are notified, when an area has been modified by the decorator.
     */
    private final ListenerList<AreaListener> areaListeners;

    /**
     * The window of the dialog, whose view hierarchy is modified by the decorator.
     */
    private Window window;

    /**
     * The root view of the view hierarchy, which is modified by the decorator.
     */
    private View view;

    /**
     * The root view of the dialog, whose view hierarchy is modified by the decorator.
     */
    private DialogRootView dialogRootView;

    /**
     * The method, which is invoked, when the decorator is attached to the view hierarchy.
     *
     * @param window
     *         The window of the dialog, whose view hierarchy is modified by the decorator, as an
     *         instance of the class {@link Window}. The window may not be null
     * @param view
     *         The root view of the dialog, which is modified by the decorator, as an instance of
     *         the class {@link View}. The view may not be null
     * @param areas
     *         A map, which contains the areas, which have already been added by previously attached
     *         decorators, as well as the corresponding views, as an instance of the type {@link
     *         Map} or an empty map, if no areas have been added
     * @param param
     *         The parameter, which should be passed to the decorator, as an instance of the generic
     *         type ParamType or null, if no parameter should be passed
     * @return A map, which contains the views, which have been inflated by the decorator, mapped to
     * their view types, as an instance of the type {@link Map} or null, if the decorator has not
     * inflated any views
     */
    @NonNull
    protected abstract Map<ViewType, View> onAttach(@NonNull final Window window,
                                                    @NonNull final View view,
                                                    @NonNull final Map<ViewType, View> areas,
                                                    final ParamType param);

    /**
     * The method, which is invoked, when the decorator is detached from the view hierarchy.
     */
    protected abstract void onDetach();

    /**
     * Creates a new decorator, which allows to modify the view hierarchy of a dialog.
     *
     * @param dialog
     *         The dialog, whose view hierarchy should be modified by the decorator, as an instance
     *         of the generic type DialogType. The dialog may not be null
     */
    public AbstractDecorator(@NonNull final DialogType dialog) {
        ensureNotNull(dialog, "The dialog may not be null");
        this.dialog = dialog;
        this.areaListeners = new ListenerList<>();
        this.window = null;
        this.view = null;
        this.dialogRootView = null;
    }

    /**
     * Attaches the decorator to the view hierarchy. This enables the decorator to modify the view
     * hierarchy until it is detached.
     *
     * @param window
     *         The window of the dialog, whose view hierarchy should be modified by the decorator,
     *         as an instance of the class {@link Window}. The window may not be null
     * @param view
     *         The root view of the view hierarchy, which should be modified by the decorator, as an
     *         instance of the class {@link View}. The view may not be null
     * @param areas
     *         A map, which contains the areas, which have already been added by previously attached
     *         decorators, as well as the corresponding views, as an instance of the type {@link
     *         Map} or an empty map, if no areas have been added
     * @param param
     *         The parameter, which should be passed to the decorator, as an instance of the generic
     *         type ParamType or null, if no parameter should be passed
     * @return A map, which contains the views, which have been inflated by the decorator, mapped to
     * their view types, as an instance of the type {@link Map} or null, if the decorator has not
     * inflated any views
     */
    @NonNull
    public final Map<ViewType, View> attach(@NonNull final Window window, @NonNull final View view,
                                            @NonNull final Map<ViewType, View> areas,
                                            final ParamType param) {
        ensureNotNull(window, "The window may not be null");
        ensureNotNull(view, "The view may not be null");
        this.window = window;
        this.view = view;
        this.dialogRootView = view.findViewById(R.id.dialog_root_view);
        return onAttach(window, view, areas, param);
    }

    /**
     * Detaches the decorator from the view hierarchy. This prevents the decorator from modifying
     * the view hierarchy until it is attached again.
     */
    public final void detach() {
        this.window = null;
        this.view = null;
        this.dialogRootView = null;
        onDetach();
    }

    /**
     * Adds a new listener, which should be notified, when an area is modified by the dialog.
     *
     * @param listener
     *         The listener, which should be added, as an instance of the type {@link AreaListener}.
     *         The listener may not be null
     */
    public final void addAreaListener(@NonNull final AreaListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        this.areaListeners.add(listener);
    }

    /**
     * Removes a specific listener, which should not be notified, when an area is modified by the
     * decorator, anymore.
     *
     * @param listener
     *         The listener, which should be added, as an instance of the type {@link AreaListener}.
     *         The listener may not be null
     */
    public final void removeAreaListener(@NonNull final AreaListener listener) {
        ensureNotNull(listener, "The listener may not be null");
        this.areaListeners.remove(listener);
    }

    /**
     * Notifies the listeners, which have been registered to be notified, when the visibility of the
     * dialog's areas has been changed, about an area being shown.
     *
     * @param area
     *         The area, which has been shown, as a value of the enum {@link Area}. The area may not
     *         be null
     */
    protected final void notifyOnAreaShown(@NonNull final Area area) {
        for (AreaListener listener : areaListeners) {
            listener.onAreaShown(area);
        }
    }

    /**
     * Notifies the listeners, which have been registered to be notified, when the visibility of the
     * dialog's areas has been changed, about an area being hidden.
     *
     * @param area
     *         The area, which has been hidden, as a value of the enum {@link Area}. The area may
     *         not be null
     */
    protected final void notifyOnAreaHidden(@NonNull final Area area) {
        for (AreaListener listener : areaListeners) {
            listener.onAreaHidden(area);
        }
    }

    /**
     * Returns the dialog, whose view hierarchy is modified by the decorator.
     *
     * @return The dialog, whose view hierarchy is modified by the decorator, as an instance of the
     * generic type DialogType. The dialog may not be null
     */
    @NonNull
    protected final DialogType getDialog() {
        return dialog;
    }

    /**
     * Returns the window of the dialog, whose view hierarchy is modified by the decorator.
     *
     * @return The window of the dialog, whose view hierarchy is modified by the decorator, as an
     * instance of the class {@link Window} or null, if the decorator is not attached
     */
    @Nullable
    protected final Window getWindow() {
        return window;
    }

    /**
     * Returns the root view of the view hierarchy, which is modified by the decorator.
     *
     * @return The root view of the view hierarchy, which is modified by the decorator, as an
     * instance of the class {@link View} or null, if the decorator is not attached
     */
    @Nullable
    protected final View getView() {
        return view;
    }

    /**
     * Returns the root view of the dialog, whose view hierarchy is modified by the decorator.
     *
     * @return The root view of the dialog, whose view hierarchy is modified by the decorator, as an
     * instance of the class {@link DialogRootView} or null, if the decorator is not attached
     */
    @Nullable
    protected final DialogRootView getRootView() {
        return dialogRootView;
    }

    @Override
    public final Context getContext() {
        return dialog.getContext();
    }

}