package com.vimalcvs.upgkhindi.adapters;

import static com.vimalcvs.upgkhindi.utils.Constant.FALSE;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vimalcvs.upgkhindi.Config;
import com.vimalcvs.upgkhindi.R;
import com.vimalcvs.upgkhindi.models.Chapter;
import com.vimalcvs.upgkhindi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class AdapterChapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Chapter> items;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private OnItemClickListener mOnItemClickListener;

    public AdapterChapter(RecyclerView view, List<Chapter> items) {
        this.items = items;
        lastItemViewDetector(view);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_chapter_item, parent, FALSE);
        vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int position) {
        try {
            if (viewHolder instanceof ViewHolder) {
                final Chapter chapter = items.get(position);
                ViewHolder holder = (ViewHolder) viewHolder;
                holder.cl_layout.setOnClickListener(view -> {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(chapter);
                    }
                });
                holder.tv_title.setText(Html.fromHtml(chapter.title));
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItemsData(final List<Chapter> items) {
        try {
            setLoaded();
            int positionStart = getItemCount();
            int itemCount = items.size();
            this.items.addAll(items);
            notifyItemRangeInserted(positionStart, itemCount);
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void setLoaded() {
        try {
            loading = FALSE;
            for (int i = 0; i < getItemCount(); i++) {
                if (items.get(i) == null) {
                    items.remove(i);
                    notifyItemRemoved(i);
                }
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void setLoading() {
        try {
            if (getItemCount() != 0) {
                loading = true;
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void clearListData() {
        try {
            this.items = new ArrayList<>();
            notifyDataSetChanged();
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    private void lastItemViewDetector(RecyclerView recyclerView) {
        try {
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        int lastPos = layoutManager.findLastVisibleItemPosition();
                        if (!loading && lastPos == getItemCount() - 1 && onLoadMoreListener != null) {
                            int current_page = getItemCount() / Config.LOAD_MORE;
                            onLoadMoreListener.onLoadMore(current_page);
                            loading = true;
                        }
                    }
                });
            }
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Chapter obj);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(int current_page);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tv_title;
        final ConstraintLayout cl_layout;

        ViewHolder(View v) {
            super(v);
            tv_title = v.findViewById(R.id.tv_title);
            cl_layout = v.findViewById(R.id.cl_layout);
        }
    }
}