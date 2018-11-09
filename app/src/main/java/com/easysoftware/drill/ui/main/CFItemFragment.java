package com.easysoftware.drill.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easysoftware.drill.R;

public class CFItemFragment extends Fragment {

    private MainBasePresenter mPresenter;
    private String mTitle;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CFItemFragment() {
    }

    public static CFItemFragment newInstance(MainBasePresenter presenter, String title) {
        CFItemFragment fragment = new CFItemFragment();
        fragment.init(presenter, title);
        return fragment;
    }

    public void init(MainBasePresenter presenter, String title) {
        mPresenter = presenter;
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cfitem_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new CFItemRecyclerViewAdapter(mPresenter));
        }
        return view;
    }


}
