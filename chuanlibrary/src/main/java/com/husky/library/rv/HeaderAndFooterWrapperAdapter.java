package com.husky.library.rv;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author husky
 */
public class HeaderAndFooterWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 100000;

    private RecyclerView.Adapter innerAdapter;
    private SparseArray<View> headerViews = new SparseArray<>();
    private SparseArray<View> footerViews = new SparseArray<>();

    public HeaderAndFooterWrapperAdapter(RecyclerView.Adapter innerAdapter) {
        this.innerAdapter = innerAdapter;
    }

    private boolean isHeaderPos(int position) {
        return position < getHeaderCount();
    }

    private boolean isFooterPos(int position) {
        return position >= getHeaderCount() + getRealItemCount();
    }

    private int getHeaderCount() {
        return headerViews.size();
    }

    private int getFooterCount() {
        return footerViews.size();
    }

    private int getRealItemCount() {
        return innerAdapter.getItemCount();
    }

    public void addHeaderView(View view) {
        headerViews.put(getHeaderCount() + BASE_ITEM_TYPE_HEADER, view);
    }

    public void addFooterView(View view) {
        footerViews.put(getFooterCount() + BASE_ITEM_TYPE_FOOTER, view);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeaderPos(position)) {
            return headerViews.keyAt(position);
        } else if (isFooterPos(position)) {
            return footerViews.keyAt(position - getHeaderCount() - getRealItemCount());
        }
        return innerAdapter.getItemViewType(position - getHeaderCount());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (headerViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(parent.getContext(), headerViews.get(viewType));
        } else if (footerViews.get(viewType) != null) {
            return ViewHolder.createViewHolder(parent.getContext(), footerViews.get(viewType));
        }
        return innerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (isHeaderPos(position)) {
            return;
        }
        if (isFooterPos(position)) {
            return;
        }
        innerAdapter.onBindViewHolder(holder, position - getHeaderCount());
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + getFooterCount() + getRealItemCount();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        innerAdapter.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (headerViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    } else if (footerViews.get(viewType) != null) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        innerAdapter.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isHeaderPos(position) || isFooterPos(position)) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }
}
