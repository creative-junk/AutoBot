package com.crysoft.me.autobot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.models.CategoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maxx on 7/29/2016.
 */
public class LocalCategoryAdapter extends BaseAdapter {
    private List<CategoryModel> categoryList;
    private LayoutInflater layoutInflater;
    private Context myContext;

    public LocalCategoryAdapter(LayoutInflater layoutInflater, List<CategoryModel> categoryList, Context context) {
        this.layoutInflater = layoutInflater;
        this.categoryList = categoryList;
        this.myContext = context;
    }
    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.category_items,null);
            viewHolder.categoryImage = (ImageView) convertView.findViewById(R.id.categoryImage);
            viewHolder.categoryName = (TextView) convertView.findViewById(R.id.categoryTitle);
            viewHolder.categoryTag = (TextView) convertView.findViewById(R.id.categoryTag);
            //viewHolder.productDescription = (TextView) convertView.findViewById(R.id.tvItemDescription);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CategoryModel categoryDetails = categoryList.get(position);

        viewHolder.categoryName.setText(categoryDetails.getCategoryName());
        viewHolder.categoryTag.setText(categoryDetails.getCategoryTag());
//        viewHolder.productDescription.setText(productDetails.getDescription());

        if (categoryDetails.getCategoryImage()!=null){
            Picasso.with(myContext).load(categoryDetails.getCategoryImage()).into(viewHolder.categoryImage);
        }else{

        }
        return convertView;
    }
    public class ViewHolder {
        ImageView categoryImage;
        TextView categoryName;
        TextView categoryTag;
    }




}
