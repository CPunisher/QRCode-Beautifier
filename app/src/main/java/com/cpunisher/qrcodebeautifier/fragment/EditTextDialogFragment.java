package com.cpunisher.qrcodebeautifier.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.model.ParamModel;

public class EditTextDialogFragment extends DialogFragment {

    private ParamModel paramModel;
    private EditText editText;

    private EditTextDialogListener listener;

    public EditTextDialogFragment(ParamModel paramModel, EditTextDialogListener listener) {
        this.paramModel = paramModel;
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_text, null);

        builder.setView(view);
        builder.setTitle(this.paramModel.name);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> listener.onEditTextChange(getId(), editText.getText().toString()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> EditTextDialogFragment.this.getDialog().cancel());

        String initText = getArguments().getString("text");
        editText = view.findViewById(R.id.text_input);
        editText.setMaxLines(1);
        editText.setText(initText);
        editText.setHint(paramModel.def);
        if ("number".equals(paramModel.type)) editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        editText.requestFocus();
        editText.post(() -> {
            InputMethodManager imn = (InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imn.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        });
        return builder.create();
    }

    public interface EditTextDialogListener {
        void onEditTextChange(int dialogId, String text);
    }
}
