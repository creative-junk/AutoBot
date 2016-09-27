package com.crysoft.me.autobot.adapters;

import android.content.Context;

import com.parse.ParseObject;
import com.parse.ParseQueryAdapter;

/**
 * Created by Maxx on 7/29/2016.
 */
public class CategorySpinnerAdapter extends ParseQueryAdapter<ParseObject> {
    public CategorySpinnerAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }
}
