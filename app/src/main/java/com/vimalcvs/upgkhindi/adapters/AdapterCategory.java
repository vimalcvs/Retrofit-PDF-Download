package com.vimalcvs.upgkhindi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.vimalcvs.upgkhindi.Config;
import com.vimalcvs.upgkhindi.R;
import com.vimalcvs.upgkhindi.models.Category;
import com.vimalcvs.upgkhindi.utils.Constant;
import com.vimalcvs.upgkhindi.utils.Utils;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("NotifyDataSetChanged")
public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {

    private final Context context;
    private List<Category> items;
    private OnItemClickListener mOnItemClickListener;

    public AdapterCategory(List<Category> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_main_grid, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            final Category subject = items.get(position);
            holder.tv_name.setText(subject.title);
            try {
                Picasso.get()
                        .load(Config.ADMIN_PANEL_URL + Constant.IMAGE_CATEGORY_PATH + subject.img)
                        .placeholder(R.drawable.icon_place_holder)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                                holder.iv_top.setImageBitmap(bitmap);
                                Palette palette = Palette.from(bitmap).generate();
                                Palette.Swatch swatch = palette.getLightVibrantSwatch();
                                if (swatch != null) {
                                    int rgb = swatch.getRgb();
                                    int bodyTextColor = swatch.getBodyTextColor();
                                    holder.layoutParent.setCardBackgroundColor(rgb);
                                    holder.tv_name.setTextColor(bodyTextColor);
                                } else {
                                    holder.layoutParent.setCardBackgroundColor(context.getResources().getColor(R.color.card_home_list_color));
                                    holder.tv_name.setTextColor(context.getResources().getColor(R.color.text_primary));
                                }
                            }
                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                            }
                        });


            } catch (Exception e) {
                Utils.getErrors(e);
            }
            if (subject.active == 1) {
                holder.iv_update.setVisibility(View.VISIBLE);
            }
            if (subject.active == 0) {
                holder.iv_update.setVisibility(View.GONE);
            }
            holder.layoutParent.setOnClickListener(view -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(subject);
                }
            });
        } catch (Exception e) {
            Utils.getErrors(e);
        }
    }

    public void setListData(List<Category> items) {
        try {
            this.items = items;
            notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return items.size();
    }


    public interface OnItemClickListener {
        void onItemClick(Category obj);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_name;
        final ImageView iv_top, iv_update;
        final CardView layoutParent;

        ViewHolder(View v) {
            super(v);
            tv_name = v.findViewById(R.id.tv_name);
            iv_top = v.findViewById(R.id.iv_top);
            iv_update = v.findViewById(R.id.iv_update);
            layoutParent = v.findViewById(R.id.explore_item);
        }
    }

}