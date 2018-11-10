package com.easysoftware.drill.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.easysoftware.drill.R;

public class CFItemRecyclerViewAdapter extends RecyclerView.Adapter<CFItemRecyclerViewAdapter.ViewHolder>
        implements Filterable {

    private final MainBasePresenter mPresenter;

    public CFItemRecyclerViewAdapter(MainBasePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_cfitem, parent, false);
        return new ViewHolder(view, mPresenter);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mPresenter.onBindCFItemView(holder, position);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getCFItemCount();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Object filteredResults = mPresenter.performFiltering(constraint.toString());
                FilterResults results = new FilterResults();
                results.values = filteredResults;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mPresenter.updateFilterResults(results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements MainContract.CFItemView {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mContentView;

        public ViewHolder(View view, MainBasePresenter presenter) {
            super(view);

            mView = view;
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onViewItemDetails(getAdapterPosition());
                }
            });

            mTitleView = view.findViewById(R.id.tvTitle);
            mContentView = view.findViewById(R.id.tvContent);
        }

        @Override
        public void display(String title, String content) {
            mTitleView.setText(title);
            mContentView.setText(content);
        }
    }
}
