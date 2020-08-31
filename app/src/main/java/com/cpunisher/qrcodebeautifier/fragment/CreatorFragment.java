package com.cpunisher.qrcodebeautifier.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.adapter.ParamAdapter;
import com.cpunisher.qrcodebeautifier.listener.ParamUpdatedListener;
import com.cpunisher.qrcodebeautifier.model.ParamModel;
import com.cpunisher.qrcodebeautifier.model.StyleModel;
import com.cpunisher.qrcodebeautifier.util.ExternalStorageHelper;
import com.cpunisher.qrcodebeautifier.util.References;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class CreatorFragment extends Fragment implements ParamUpdatedListener {

    private StyleModel styleModel;
    private ImageView resultImageView;
    private CircularProgressDrawable circularProgressDrawable;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);
        this.styleModel = (StyleModel) getArguments().getSerializable("style_model");

        View view = inflater.inflate(R.layout.fragment_creator, container, false);
        resultImageView = view.findViewById(R.id.qrcode_result);
        recyclerView = view.findViewById(R.id.param_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ParamAdapter(styleModel, this);
        recyclerView.setAdapter(mAdapter);

        circularProgressDrawable = new CircularProgressDrawable(this.getContext());
        circularProgressDrawable.setStrokeWidth(5.0f);
        circularProgressDrawable.setCenterRadius(30.0f);
        circularProgressDrawable.start();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            try {
                File cachePath = new File(getContext().getCacheDir(), "images");
                cachePath.mkdirs();
                FileOutputStream stream = new FileOutputStream(cachePath + "/image.jpg");
                Bitmap bitmap = ((BitmapDrawable) resultImageView.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), "Share error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            File imagePath = new File(getContext().getCacheDir(), "images");
            File newFile = new File(imagePath, "image.jpg");
            Uri contentUri = FileProvider.getUriForFile(getContext(), "com.cpunisher.qrcodebeautifier.fileprovider", newFile);
            if (contentUri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));
            }
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            try {
                Bitmap bitmap = ((BitmapDrawable) resultImageView.getDrawable()).getBitmap();
                String date = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault()).format(new Date());
                ExternalStorageHelper.saveJpg(bitmap, styleModel.name + "_" + date, this.getContext());
                Toast.makeText(getContext(), "Save successfully", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getContext(), "Save error", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (item.getItemId() == R.id.action_about) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
            stringBuilder.append("&");
        }
        stringBuilder.append("jpg=1");

        Log.d(References.TAG, "Access to " + stringBuilder.toString());
        Glide.with(this)
                .load(stringBuilder.toString())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(new RequestOptions().override(1000, 1000))
                .into(resultImageView);
    }
}
