package com.cpunisher.qrcodebeautifier.fragment;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cpunisher.qrcodebeautifier.R;
import com.cpunisher.qrcodebeautifier.adapter.StyleAdapter;

import java.util.ArrayList;

public class StyleListFragment extends Fragment {

    private RecyclerView recyclerView;
    private StyleAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

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

        mAdapter.toggleList(this.getContext());
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_toggle) {
            mAdapter.toggleList(this.getContext());
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
