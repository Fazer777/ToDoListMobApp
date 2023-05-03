package com.example.todolistver2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.todolistver2.R;
import com.example.todolistver2.Models.Category;

import java.util.Objects;

public class CategoryAdapter extends BaseAdapter {

    private Context context;

//    public CategoryAdapter(Context context, int resource, List<Category> list){
//        super(context, resource,list);
//        this.context = context;
//    }


    public CategoryAdapter(Context context){
        this.context = context;
    }


    @Override
    public int getCount() {
        return Category.categories.size();
    }

    @Override
    public Category getItem(int i) {
        return Category.categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getItemId(String nameCategory){
        for (int i = 0; i < Category.categories.size(); ++i){
            if (Objects.equals(nameCategory, Category.categories.get(i).getName())){
                return  i;
            }
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_category_adapter, viewGroup, false);
        }
        ImageView ivIconCategory = (ImageView) view.findViewById(R.id.item_category_iv_icon);
        TextView tvNameCategory = (TextView) view.findViewById(R.id.item_category_tv_name);
        ivIconCategory.setColorFilter(Category.categories.get(i).getColor());
        tvNameCategory.setText(Category.categories.get(i).getName());
        return view;
    }
}
