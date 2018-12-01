package com.easysoftware.drill.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.easysoftware.drill.R;

public class CFItemFragment extends Fragment {
    private final static String TAG = "CFItemFragment";

    private MainBasePresenter mPresenter;
    private CFItemRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CFItemFragment() {
    }

    public static CFItemFragment newInstance(MainBasePresenter presenter) {
        CFItemFragment fragment = new CFItemFragment();
        fragment.init(presenter);
        return fragment;
    }

    public void init(MainBasePresenter presenter) {
        mPresenter = presenter;
        mAdapter = new CFItemRecyclerViewAdapter(mPresenter);
    }

    public void update(MainBasePresenter presenter) {
        mPresenter = presenter;
        mAdapter = new CFItemRecyclerViewAdapter(mPresenter);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cfitem_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        mRecyclerView = (RecyclerView) view;

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setAdapter(mAdapter);

        return view;
    }


    public void filter(String constraint) {
        mAdapter.getFilter().filter(constraint);
    }
}
