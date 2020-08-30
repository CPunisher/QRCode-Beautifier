package com.cpunisher.qrcodebeautifier.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.adapter.StyleAdapter;
import com.cpunisher.qrcodebeautifier.http.RequestHelper;
import com.cpunisher.qrcodebeautifier.http.StyleListListener;
import com.cpunisher.qrcodebeautifier.util.References;

import java.util.ArrayList;

public class StyleListFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_style_list, container, false);
        recyclerView = view.findViewById(R.id.styles_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new StyleAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        updateStyleModel();
        return view;
    }

    private void updateStyleModel() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, References.STYLE_LIST_URL,
                null, new StyleListListener((StyleAdapter) mAdapter), (error) -> {
            Toast.makeText(this.getContext(), "Fail to get style list", Toast.LENGTH_SHORT);
            Log.e(References.TAG, error.toString());
        });

        RequestHelper.getInstance(this.getContext()).addToRequestQueue(jsonArrayRequest);
    }
}
