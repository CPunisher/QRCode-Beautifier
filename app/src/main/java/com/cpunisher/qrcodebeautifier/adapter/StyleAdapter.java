package com.cpunisher.qrcodebeautifier.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.fragment.CreatorFragment;
import com.cpunisher.qrcodebeautifier.model.StyleModel;

import java.util.List;

public class StyleAdapter extends RecyclerView.Adapter<StyleAdapter.StyleViewHolder> {

    private List<StyleModel> mDataset;

    public StyleAdapter(List<StyleModel> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public StyleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_style, parent, false);
        StyleViewHolder styleViewHolder = new StyleViewHolder(view);
        return styleViewHolder;
    }

    @Override
    public void onBindViewHolder(StyleAdapter.StyleViewHolder holder, int position) {
        StyleModel styleModel = mDataset.get(position);

        Glide.with(holder.itemView.getContext()).load(styleModel.img).into(holder.imageView);
        holder.textView.setText(styleModel.name);
        holder.mData = mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void appendData(StyleModel styleModel) {
        int index = this.mDataset.size();
        this.mDataset.add(index, styleModel);
        this.notifyItemInserted(index);
    }

    public static class StyleViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;
        public View styleWrapper;
        public StyleModel mData;

        public StyleViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.style_img);
            textView = itemView.findViewById(R.id.style_name);
            styleWrapper = itemView.findViewById(R.id.style_wrapper);

            styleWrapper.setOnClickListener((View v) -> {
                AppCompatActivity activity = (AppCompatActivity) itemView.getContext();
                CreatorFragment creatorFragment = new CreatorFragment();

                Bundle args = new Bundle();
                args.putSerializable("style_model", mData);
                creatorFragment.setArguments(args);

                FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out);
                transaction.replace(R.id.fragment_container, creatorFragment);
                transaction.addToBackStack(null);

                transaction.commit();
            });
        }
    }
}
