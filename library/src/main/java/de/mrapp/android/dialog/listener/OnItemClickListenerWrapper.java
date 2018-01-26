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
package de.mrapp.android.dialog.listener;

import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import de.mrapp.android.dialog.model.ValidateableDialog;

/**
 * A wrapper, which implements the interface {@link OnItemClickListener} in order to delegate the
 * method calls to encapsulated listener of the type {@link OnClickListener}.
 *
 * @author Michael Rapp
 * @since 1.0.0
 */
public class OnItemClickListenerWrapper extends AbstractListenerWrapper
        implements OnItemClickListener {

    /**
     * The listener, which is encapsulated by the wrapper.
     */
    private final OnClickListener wrappedListener;

    /**
     * Creates a new wrapper, which implements the interface {@link OnItemClickListener} in order to
     * delegate the method calls to encapsulated listener of the type {@link OnClickListener}.
     *
     * @param listener
     *         The listener, which should be encapsulated by the wrapper, as an instance of the type
     *         {@link OnClickListener} or null, if no listener should be encapsulated
     * @param dialog
     *         The dialog, the listener should belong to, as an instance of the type {@link
     *         ValidateableDialog}. The dialog may not be null
     * @param buttonType
     *         The type of the button or list item, the listener belongs to, as an {@link Integer}
     *         value
     */
    public OnItemClickListenerWrapper(@Nullable final OnClickListener listener,
                                      @NonNull final ValidateableDialog dialog,
                                      final int buttonType) {
        super(dialog, buttonType);
        this.wrappedListener = listener;
    }

    @Override
    public final void onItemClick(final AdapterView<?> parent, final View view, final int position,
                                  final long id) {
        if (wrappedListener != null) {
            wrappedListener.onClick(getDialog(), position);
        }

        attemptCloseDialog();
    }

}