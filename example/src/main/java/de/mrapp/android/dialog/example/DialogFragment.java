/*
 * Copyright 2014 - 2017 Michael Rapp
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
package de.mrapp.android.dialog.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.mrapp.android.dialog.WizardDialog;

/**
 * An example fragment for use in a {@link WizardDialog}.
 *
 * @author Michael Rapp
 */
public class DialogFragment extends Fragment {

    /**
     * The name of the extra, which may be passed to the fragment in order to specify its index in
     * the {@link WizardDialog}.
     */
    public static final String INDEX_EXTRA = DialogFragment.class.getSimpleName() + "::IndexExtra";

    @Override
    public final View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                   final Bundle savedInstanceState) {
        TextView textView = (TextView) inflater.inflate(R.layout.dialog_fragment, container, false);
        int index = getArguments() != null ? getArguments().getInt(INDEX_EXTRA, 1) : 1;
        CharSequence text = String.format(getString(R.string.dialog_fragment_text), index);
        textView.setText(text);
        return textView;
    }

}