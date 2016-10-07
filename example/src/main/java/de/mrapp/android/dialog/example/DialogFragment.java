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