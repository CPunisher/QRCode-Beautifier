package com.cpunisher.qrcodebeautifier.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.db.AppDatabase;
import com.cpunisher.qrcodebeautifier.db.entity.Collection;
import com.cpunisher.qrcodebeautifier.fragment.CreatorFragment;
import com.cpunisher.qrcodebeautifier.http.RequestHelper;
import com.cpunisher.qrcodebeautifier.http.StyleListListener;
import com.cpunisher.qrcodebeautifier.pojo.StyleModel;
import com.cpunisher.qrcodebeautifier.util.EntityHelper;
import com.cpunisher.qrcodebeautifier.util.References;

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

    public void setData(List<StyleModel> styleModels) {
        this.mDataset = styleModels;
        this.notifyDataSetChanged();
    }

    public void fetchStyleList(final Context context) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, References.STYLE_LIST_URL,
                null, new StyleListListener(this), (error) -> {
            Toast.makeText(context, R.string.fetch_list_error, Toast.LENGTH_SHORT);
            Log.e(References.TAG, error.toString());
        });

        RequestHelper.getInstance(context).addToRequestQueue(jsonArrayRequest);
    }

    public void loadCollection(final Context context) {
        new LoadCollectionTask(context).execute();
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

    private class LoadCollectionTask extends AsyncTask<Void, Void, Void> {

        final Context context;

        public LoadCollectionTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Collection> collectionList = AppDatabase.getInstance(context).collectionDao().getAll();
            StyleAdapter.this.mDataset = EntityHelper.toStyleModels(collectionList);
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            StyleAdapter.this.notifyDataSetChanged();
            Toast.makeText(context, R.string.toggle_collection, Toast.LENGTH_SHORT).show();
        }
    }
}
