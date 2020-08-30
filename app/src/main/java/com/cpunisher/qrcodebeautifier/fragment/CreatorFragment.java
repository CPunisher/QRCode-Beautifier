package com.cpunisher.qrcodebeautifier.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.adapter.ParamAdapter;
import com.cpunisher.qrcodebeautifier.listener.ParamUpdatedListener;
import com.cpunisher.qrcodebeautifier.model.ParamModel;
import com.cpunisher.qrcodebeautifier.model.StyleModel;
import com.cpunisher.qrcodebeautifier.util.References;

public class CreatorFragment extends Fragment implements ParamUpdatedListener {

    private StyleModel styleModel;
    private ImageView resultImageView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.styleModel = (StyleModel) getArguments().getSerializable("style_model");

        View view = inflater.inflate(R.layout.fragment_creator, container, false);
        resultImageView = view.findViewById(R.id.qrcode_result);
        recyclerView = view.findViewById(R.id.param_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ParamAdapter(styleModel, this);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onParamUpdated(StyleModel styleModel, String[] params) {
        StringBuilder stringBuilder = new StringBuilder();
         stringBuilder.append(References.QRCODE_URL).append("?");

        for (int i = 0; i < params.length; i++) {
            ParamModel paramModel = styleModel.params[i];
            stringBuilder.append(paramModel.id);
            stringBuilder.append("=");
            stringBuilder.append(Uri.encode(params[i]));
            if (i != styleModel.params.length - 1) stringBuilder.append("&");
        }
        Log.d(References.TAG, "Access to " + stringBuilder.toString());
        // Glide.with(this).load(stringBuilder.toString()).into(resultImageView);
    }
}
