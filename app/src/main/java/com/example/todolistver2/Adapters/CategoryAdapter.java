package com.example.todolistver2.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Category;
import com.example.todolistver2.R;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categoryList;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @Override
    public int getCount() {
        if (categoryList != null){
            return categoryList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (categoryList!=null){
            return categoryList.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_categoty_list_adapter, viewGroup, false);
        }
        ImageView ivIconCategory = (ImageView) view.findViewById(R.id.item_category_iv_icon);
        TextView tvNameCategory = (TextView) view.findViewById(R.id.item_category_tv_name);
        ivIconCategory.setColorFilter( categoryList.get(i).getColor());
        tvNameCategory.setText( categoryList.get(i).getName());

        return view;
    }
}
