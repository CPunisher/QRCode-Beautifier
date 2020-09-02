package com.cpunisher.qrcodebeautifier.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
    private StyleAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private boolean displayCollection = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_style_list, container, false);
        recyclerView = view.findViewById(R.id.styles_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new StyleAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);

        mAdapter.fetchStyleList(this.getContext());
        displayCollection = false;
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_toggle) {
            if (displayCollection) {
                mAdapter.fetchStyleList(this.getContext());
                displayCollection = false;
            } else {
                mAdapter.loadCollection(this.getContext());
                displayCollection = true;
            }
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
