package com.example.todolistver2.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.todolistver2.R;
import com.example.todolistver2.Models.Category;

import java.util.List;
import java.util.Objects;

public class CategorySpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categoryList;

    public CategorySpinnerAdapter(Context context, List<Category> list){
        this.context = context;
        categoryList = list;
    }


    @Override
    public int getCount() {
        if (categoryList != null){
            return categoryList.size();
        }
        return 0;
    }

    @Override
    public Category getItem(int i) {
        if (categoryList !=null){
            return categoryList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getItemId(String nameCategory){
        for (int i = 0; i < categoryList.size(); ++i){
            if (Objects.equals(nameCategory, categoryList.get(i).getName())){
                return  i;
            }
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_category_spinner_adapter, viewGroup, false);
        }
        ImageView ivIconCategory = (ImageView) view.findViewById(R.id.item_spinner_category_iv_icon);
        TextView tvNameCategory = (TextView) view.findViewById(R.id.item_spinner_category_tv_name);
        ivIconCategory.setColorFilter( categoryList.get(i).getColor());
        tvNameCategory.setText( categoryList.get(i).getName());
        return view;
    }
}
