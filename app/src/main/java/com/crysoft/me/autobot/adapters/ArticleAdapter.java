package com.crysoft.me.autobot.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.models.ArticleModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maxx on 9/20/2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
    public interface OnItemClickListener{
        void onItemClick(ArticleModel article);
    }
    private Context context;
    private List<ArticleModel> articleList;
    private OnItemClickListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView articleTitle,articleSummary;
        public ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            articleTitle=(TextView) itemView.findViewById(R.id.title);
            articleSummary = (TextView) itemView.findViewById(R.id.summary);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

        }
        public void bind(final ArticleModel article, final OnItemClickListener listener){
            articleTitle.setText(Html.fromHtml(article.getArticleTitle()));
            articleSummary.setText(Html.fromHtml(article.getArticleSummary()));

            Picasso.with(context).load(article.getFeaturedImage()).into(thumbnail);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(article);
                }
            });
        }
    }
    public ArticleAdapter(Context mContext, List<ArticleModel> articleList,OnItemClickListener listener){
        this.context = mContext;
        this.articleList = articleList;
        this.listener = listener;
    }

    @Override
    public ArticleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_card,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.MyViewHolder holder, int position) {
        holder.bind(articleList.get(position),listener);

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
