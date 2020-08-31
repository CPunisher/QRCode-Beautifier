package com.cpunisher.qrcodebeautifier.adapter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.fragment.ColorPickerDialogFragment;
import com.cpunisher.qrcodebeautifier.listener.ParamChangeListener;
import com.cpunisher.qrcodebeautifier.listener.ParamUpdatedListener;
import com.cpunisher.qrcodebeautifier.model.OptionModel;
import com.cpunisher.qrcodebeautifier.model.StyleModel;
import com.cpunisher.qrcodebeautifier.util.ColorHelper;
import com.cpunisher.qrcodebeautifier.util.ParamTypeHelper;
import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

public class ParamAdapter extends RecyclerView.Adapter<ParamAdapter.ParamViewHolder> implements ParamChangeListener {

    private ParamUpdatedListener paramUpdatedListener;

    private StyleModel styleModel;
    private String[] mDataset;

    public ParamAdapter(StyleModel styleModel, ParamUpdatedListener paramUpdatedListener) {
        this.paramUpdatedListener = paramUpdatedListener;
        this.styleModel = styleModel;
        this.mDataset = new String[styleModel.params.length];
        for (int i = 0; i< mDataset.length; i++) this.mDataset[i] = styleModel.params[i].def;
        this.notifyDataSetChanged();
    }

    @Override
    public ParamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(ParamTypeHelper.getInstance().getLayout(viewType), parent, false);
        ParamViewHolder paramViewHolder = ParamTypeHelper.getInstance().instanceViewHolder(viewType, view, this);
        return paramViewHolder;
    }

    @Override
    public void onBindViewHolder(ParamViewHolder holder, int position) {
        holder.init(styleModel, mDataset, position);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    @Override
    public void onParamChange(String value, int position) {
        if (value == null || value.isEmpty()) value = styleModel.params[position].def;
            mDataset[position] = value;
        // notifyItemChanged(position);
        paramUpdatedListener.onParamUpdated(styleModel, mDataset);
    }

    @Override
    public int getItemViewType(int position) {
        return ParamTypeHelper.getInstance().getId(styleModel.params[position].type);
    }

    public static class ParamViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public ParamViewHolder(View itemView, ParamChangeListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.param_name);
        }
        public void init(StyleModel styleModel, String[] dataset, int position) {
            textView.setText(styleModel.params[position].name);
        }
    }

    public static class ParamEditViewHolder extends ParamViewHolder {
        public EditText editText;

        public ParamEditViewHolder(View itemView, ParamChangeListener listener) {
            super(itemView, listener);
            editText = itemView.findViewById(R.id.param_input);
            editText.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    listener.onParamChange(editText.getText().toString(), getAdapterPosition());
                }
            });
        }
        @Override
        public void init(StyleModel styleModel, String[] dataset, int position) {
            super.init(styleModel, dataset, position);
            editText.setHint(styleModel.params[position].def);
            editText.setText(dataset[position]);
        }
    }

    public static class ParamOptionViewHolder extends ParamViewHolder {
        public Spinner spinner;
        public ParamOptionViewHolder(View itemView, ParamChangeListener listener) {
            super(itemView, listener);
            spinner = itemView.findViewById(R.id.param_spinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    OptionModel optionModel = (OptionModel) parent.getItemAtPosition(position);
                    listener.onParamChange(optionModel.id, getAdapterPosition());
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        }
        @Override
        public void init(StyleModel styleModel, String[] dataset, int position) {
            super.init(styleModel, dataset, position);
            ArrayAdapter<OptionModel> adapter = new ArrayAdapter(itemView.getContext(), R.layout.support_simple_spinner_dropdown_item, styleModel.params[position].extra);
            spinner.setAdapter(adapter);
        }
    }

    public static class ParamColorViewHolder extends ParamViewHolder {
        public ColorPanelView colorPanelView;
        public ParamColorViewHolder(View itemView, ParamChangeListener listener) {
            super(itemView, listener);
            colorPanelView = itemView.findViewById(R.id.param_color_panel);
            colorPanelView.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                ColorPickerDialogFragment colorPickerDialogFragment = new ColorPickerDialogFragment(new ColorPickerDialogListener() {
                    @Override
                    public void onColorSelected(int dialogId, int color) {
                        colorPanelView.setColor(color);
                        listener.onParamChange(ColorHelper.toColorHex(color), getAdapterPosition());
                    }
                    @Override
                    public void onDialogDismissed(int dialogId) { }
                });

                Bundle args = new Bundle();
                args.putInt("color", colorPanelView.getColor());
                colorPickerDialogFragment.setArguments(args);
                colorPickerDialogFragment.show(activity.getSupportFragmentManager(), "colorPicker");
            });
        }
        @Override
        public void init(StyleModel styleModel, String[] dataset, int position) {
            super.init(styleModel, dataset, position);
            colorPanelView.setColor(Color.parseColor(dataset[position]));
        }
    }
}
