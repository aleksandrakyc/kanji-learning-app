package pl.polsl.kanjiapp.dialogs;

import android.content.Context;

import androidx.fragment.app.DialogFragment;

public class SetNameDialog extends DialogFragment {

    public interface SetNameDialogListener {
        public void onNameConfirm(DialogFragment dialog);
        public void onCancel(DialogFragment dialog);

    }
    SetNameDialogListener listener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = (SetNameDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface. Throw exception.
            throw new ClassCastException(e.toString()
                    + "Class must implement NoticeDialogListener");
        }
    }

}
