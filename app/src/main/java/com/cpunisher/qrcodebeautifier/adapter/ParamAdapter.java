package com.cpunisher.qrcodebeautifier.adapter;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.db.AppDatabase;
import com.cpunisher.qrcodebeautifier.fragment.ColorPickerDialogFragment;
import com.cpunisher.qrcodebeautifier.fragment.EditTextDialogFragment;
import com.cpunisher.qrcodebeautifier.listener.ParamChangeListener;
import com.cpunisher.qrcodebeautifier.listener.ParamUpdatedListener;
import com.cpunisher.qrcodebeautifier.pojo.OptionModel;
import com.cpunisher.qrcodebeautifier.pojo.StyleModel;
import com.cpunisher.qrcodebeautifier.util.ColorHelper;
import com.cpunisher.qrcodebeautifier.util.EntityHelper;
import com.cpunisher.qrcodebeautifier.util.ParamTypeHelper;
import com.jaredrummler.android.colorpicker.ColorPanelView;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.util.Arrays;

public class ParamAdapter extends RecyclerView.Adapter<ParamAdapter.ParamViewHolder> implements ParamChangeListener {

    private ParamUpdatedListener paramUpdatedListener;

    private StyleModel styleModel;
    private String[] mDataset;

    public ParamAdapter(StyleModel styleModel, String[] mDataset, ParamUpdatedListener paramUpdatedListener) {
        this.paramUpdatedListener = paramUpdatedListener;
        this.styleModel = styleModel;
        this.mDataset = mDataset;
        this.notifyDataSetChanged();
        paramUpdatedListener.onParamUpdated(styleModel, mDataset);
    }

    public ParamAdapter(StyleModel styleModel, ParamUpdatedListener paramUpdatedListener) {
        this(styleModel, Arrays.stream(styleModel.params).map(p -> p.def).toArray(String[]::new), paramUpdatedListener);
    }

    @Override
    public ParamViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(ParamTypeHelper.getInstance().getLayout(viewType), parent, false);
        return ParamTypeHelper.getInstance().instanceViewHolder(viewType, view, styleModel, this);
    }

    public void saveToCollection(final Context context, String name, ImageView imageView) {
        new AddCollectionTask(context, EntityHelper.imageToByteArray(imageView)).execute(name);
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

        public ParamViewHolder(StyleModel styleModel, View itemView, ParamChangeListener listener) {
            super(itemView);
            textView = itemView.findViewById(R.id.param_name);
        }

        public void init(StyleModel styleModel, String[] dataset, int position) {
            textView.setText(styleModel.params[position].name);
        }
    }

    public static class ParamEditViewHolder extends ParamViewHolder implements View.OnClickListener {
        public TextView editText;
        public ParamChangeListener listener;
        public StyleModel styleModel;

        public ParamEditViewHolder(StyleModel styleModel, View itemView, ParamChangeListener listener) {
            super(styleModel, itemView, listener);
            this.styleModel = styleModel;
            this.listener = listener;
            editText = itemView.findViewById(R.id.param_input);
            itemView.setOnClickListener(this);
        }

        @Override
        public void init(StyleModel styleModel, String[] dataset, int position) {
            super.init(styleModel, dataset, position);
            editText.setHint(styleModel.params[position].def);
            editText.setText(dataset[position]);
        }

        @Override
        public void onClick(View v) {
            AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
            final int position = getAdapterPosition();
            EditTextDialogFragment editTextDialogFragment = new EditTextDialogFragment(styleModel.params[position], (dialogId, text) -> {
                editText.setText(text);
                listener.onParamChange(text, position);
            });

            Bundle args = new Bundle();
            args.putString("text", editText.getText().toString());
            editTextDialogFragment.setArguments(args);
            editTextDialogFragment.show(activity.getSupportFragmentManager(), "editText");
        }
    }

    public static class ParamOptionViewHolder extends ParamViewHolder {
        public Spinner spinner;
        private boolean spinnerTouched;

        public ParamOptionViewHolder(StyleModel styleModel, View itemView, ParamChangeListener listener) {
            super(styleModel, itemView, listener);
            spinner = itemView.findViewById(R.id.param_spinner);
            itemView.setOnClickListener(v -> spinner.performClick());

            spinner.setOnTouchListener((v, me) -> {
                spinnerTouched = true;
                v.performClick();
                return false;
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (spinnerTouched) {
                        OptionModel optionModel = (OptionModel) parent.getItemAtPosition(position);
                        listener.onParamChange(optionModel.id, getAdapterPosition());
                        spinnerTouched = false;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        @Override
        public void init(StyleModel styleModel, String[] dataset, int position) {
            super.init(styleModel, dataset, position);
            ArrayAdapter<OptionModel> adapter = new ArrayAdapter(itemView.getContext(),
                    R.layout.support_simple_spinner_dropdown_item, styleModel.params[getAdapterPosition()].extra);
            spinner.setAdapter(adapter);
        }
    }

    public static class ParamColorViewHolder extends ParamViewHolder {
        public ColorPanelView colorPanelView;

        public ParamColorViewHolder(StyleModel styleModel, View itemView, ParamChangeListener listener) {
            super(styleModel, itemView, listener);
            colorPanelView = itemView.findViewById(R.id.param_color_panel);
            itemView.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                ColorPickerDialogFragment colorPickerDialogFragment = new ColorPickerDialogFragment(new ColorPickerDialogListener() {
                    @Override
                    public void onColorSelected(int dialogId, int color) {
                        colorPanelView.setColor(color);
                        listener.onParamChange(ColorHelper.toColorHex(color), getAdapterPosition());
                    }

                    @Override
                    public void onDialogDismissed(int dialogId) {
                    }
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

    private class AddCollectionTask extends AsyncTask<String, Void, Void> {

        final Context context;
        byte[] imgData;

        public AddCollectionTask(Context context, byte[] imgData) {
            this.context = context;
            this.imgData = imgData;
        }

        @Override
        protected Void doInBackground(String... strings) {
            AppDatabase.getInstance(context).collectionDao().insertCollection(EntityHelper.toCollection(styleModel, Arrays.asList(mDataset), strings[0], imgData, context));
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            Toast.makeText(context, R.string.collect_successfully, Toast.LENGTH_SHORT).show();
        }
    }
}
