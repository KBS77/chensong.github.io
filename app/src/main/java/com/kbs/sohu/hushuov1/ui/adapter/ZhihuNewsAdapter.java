package com.kbs.sohu.hushuov1.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kbs.sohu.hushuov1.R;
import com.kbs.sohu.hushuov1.model.OnRecyclerViewOnClickListener;
import com.kbs.sohu.hushuov1.model.bean.ZhihuNewsinfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cs on 2017/03/7.
 */

public class ZhihuNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context context;
    private final LayoutInflater inflater;
    private List<ZhihuNewsinfo.Question> list = new ArrayList<ZhihuNewsinfo.Question>();
    private OnRecyclerViewOnClickListener mListener;

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_FOOTER = 1;

    public ZhihuNewsAdapter(Context context, List<ZhihuNewsinfo.Question> list){
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                return new NormalViewHolder(inflater.inflate(R.layout.item_zhihu_news, parent, false), mListener);
            case TYPE_FOOTER:
                return new FooterViewHolder(inflater.inflate(R.layout.list_footer, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NormalViewHolder) {

            ZhihuNewsinfo.Question item = list.get(position);

            if (item.getImages().get(0) == null){
                ((NormalViewHolder)holder).itemImg.setImageResource(R.drawable.sa);
            } else {
                Glide.with(context)
                        .load(item.getImages().get(0))
                        .asBitmap()
                        .placeholder(R.drawable.sa)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .error(R.drawable.sa)
                        .centerCrop()
                        .into(((NormalViewHolder)holder).itemImg);
            }
            ((NormalViewHolder)holder).tvLatestNewsTitle.setText(item.getTitle());
        }
        // handle the footer view holder
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return ZhihuNewsAdapter.TYPE_FOOTER;
        }
        return ZhihuNewsAdapter.TYPE_NORMAL;
    }

    public void setItemClickListener(OnRecyclerViewOnClickListener listener){
        this.mListener = listener;
    }

    public class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView itemImg;
        private TextView tvLatestNewsTitle;
        private OnRecyclerViewOnClickListener listener;

        public NormalViewHolder(View itemView, OnRecyclerViewOnClickListener listener) {
            super(itemView);
            itemImg = (ImageView) itemView.findViewById(R.id.imageViewCover);
            tvLatestNewsTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getLayoutPosition());
            }
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder{

        public FooterViewHolder(View itemView) {
            super(itemView);
        }

    }
}
