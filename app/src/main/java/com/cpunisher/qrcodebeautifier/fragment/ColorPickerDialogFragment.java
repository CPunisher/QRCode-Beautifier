package com.cpunisher.qrcodebeautifier.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import androidx.fragment.app.DialogFragment;
import com.cpunisher.qrcodebeautifier.R;
import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;

public class ColorPickerDialogFragment extends DialogFragment implements ColorPickerView.OnColorChangedListener {

    private ColorPickerView colorPickerView;
    private ColorPanelView newColorPanelView;

    private com.jaredrummler.android.colorpicker.ColorPickerDialogListener listener;

    public ColorPickerDialogFragment(ColorPickerDialogListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_color_picker, null);

        builder.setView(view);
        builder.setTitle(R.string.color_picker);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> listener.onColorSelected(getId(), colorPickerView.getColor()));
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> ColorPickerDialogFragment.this.getDialog().cancel());

        int initialColor = getArguments().getInt("color");
        ColorPanelView oldColorPanelView = view.findViewById(R.id.cpv_color_panel_old);
        colorPickerView = view.findViewById(R.id.cpv_color_picker_view);
        newColorPanelView = view.findViewById(R.id.cpv_color_panel_new);
        colorPickerView.setColor(initialColor, true);
        colorPickerView.setOnColorChangedListener(this);
        oldColorPanelView.setColor(initialColor);

        return  builder.create();
    }

    @Override
    public void onColorChanged(int newColor) {
        newColorPanelView.setColor(colorPickerView.getColor());
    }
}
