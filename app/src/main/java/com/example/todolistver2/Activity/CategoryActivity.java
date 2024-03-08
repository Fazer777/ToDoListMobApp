package com.example.todolistver2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolistver2.Adapters.CategoryAdapter;
import com.example.todolistver2.Database.DbManager;
import com.example.todolistver2.Models.Category;
import com.example.todolistver2.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;

import java.util.List;


public class CategoryActivity extends AppCompatActivity {

    ListView lvCategories;
    List<Category> categoryList;
    CategoryAdapter categoryAdapter;
    DbManager dbManager;
    Toolbar toolbar;
    MenuHost menuHost;
    //Bottom sheet layout elements
    Button sheetBtnSave;
    ImageButton sheetBtnClose;
    TextView sheetTvColor;
    EditText sheetEtName;
    int selectedColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        lvCategories = findViewById(R.id.lvCategories);
        toolbar = findViewById(R.id.categories_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        dbManager = new DbManager(CategoryActivity.this);
        categoryList = dbManager.getAllCategoriesDatabase();
        categoryAdapter =  new CategoryAdapter(CategoryActivity.this, categoryList);
        menuHost = CategoryActivity.this;
        lvCategories.setAdapter(categoryAdapter);

        lvCategories.setOnItemClickListener((adapterView, view, i, l) -> {
            if (i==0){
                Toast.makeText(CategoryActivity.this, getResources().getText(R.string.unchangeable_category), Toast.LENGTH_SHORT).show();
                return;
            }
            BottomSheetDialog dialog = new BottomSheetDialog(CategoryActivity.this);
            selectedColor = getResources().getColor(R.color.white, getTheme());
            dialog.setContentView(R.layout.bottom_sheet_category);
            initBottomSheetElements(dialog);
            getCategoryData(i);

            sheetBtnClose.setOnClickListener(view1 -> dialog.dismiss());

            sheetBtnSave.setOnClickListener(view1 -> {
                if (sheetEtName.getText().toString().matches("")){
                    Toast.makeText(CategoryActivity.this, getResources().getText(R.string.required_name), Toast.LENGTH_SHORT).show();
                    return;
                }
                Category category = setCategoryData(categoryList.get(i));
                categoryList.set(i, category);
                categoryAdapter.notifyDataSetChanged();
                dbManager.updateCategoryDatabase(category.getItemIndex(), category);
                dialog.dismiss();
            });

            sheetTvColor.setOnClickListener(view1 -> setCategoryColor());
            dialog.show();
        });

        lvCategories.setOnItemLongClickListener((adapterView, view, i, l) -> {
            int position = i;
            if ( position ==0){
                Toast.makeText(CategoryActivity.this, getResources().getText(R.string.unremovable_category), Toast.LENGTH_SHORT).show();
                return false;
            }

            Category category = categoryList.get( position);
            view.setBackgroundColor(getResources().getColor(R.color.teal_700, getTheme()));
            new AlertDialog.Builder(CategoryActivity.this)
                    .setTitle(getResources().getString(R.string.delete_selected_category))
                    .setNegativeButton("Да", (dialogInterface, i1) -> {
                        categoryList.remove(position);
                        categoryAdapter.notifyDataSetChanged();
                        dbManager.deleteCategoryDatabase(category.getItemIndex());
                        Toast.makeText(CategoryActivity.this, getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                        view.setBackgroundColor(getResources().getColor(R.color.Platinum, getTheme()));
                        categoryList = dbManager.getAllCategoriesDatabase();
                        categoryAdapter.setCategoryList(categoryList);
                    })
                    .setPositiveButton("Нет", (dialogInterface, i1) -> {
                        Toast.makeText(CategoryActivity.this, getResources().getString(R.string.cancel_removing), Toast.LENGTH_SHORT).show();
                        view.setBackgroundColor(getResources().getColor(R.color.Platinum, getTheme()));
                        dialogInterface.dismiss();
                    }).setCancelable(false).create().show();

            return true;
        });

        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.categories_toolbar_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.categories_add_category){
                    createCategory();
                    return true;
                }
                return false;
            }
        });

    }

    private void createCategory() {
        BottomSheetDialog dialog = new BottomSheetDialog(CategoryActivity.this);
        selectedColor = getResources().getColor(R.color.white, getTheme());
        dialog.setContentView(R.layout.bottom_sheet_category);
        initBottomSheetElements(dialog);

        sheetBtnClose.setOnClickListener(view -> dialog.dismiss());

        sheetBtnSave.setOnClickListener(view -> {
            if (sheetEtName.getText().toString().matches("")){
                Toast.makeText(CategoryActivity.this, getResources().getText(R.string.required_name), Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkRepetitions()){
                Toast.makeText(CategoryActivity.this, getResources().getText(R.string.category_exists), Toast.LENGTH_SHORT).show();
                return;
            }
            Category category = setCategoryData(new Category());
            category.setItemIndex(categoryList.size());
            categoryList.add(category);
            categoryAdapter.notifyDataSetChanged();
            dbManager.addCategoryDatabase(category);
            dialog.dismiss();
        });

        sheetTvColor.setOnClickListener(view -> setCategoryColor());

        dialog.show();
    }

    private void setCategoryColor() {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newBuilder().create();
        colorPickerDialog.setColorPickerDialogListener(new ColorPickerDialogListener() {
            @Override
            public void onColorSelected(int dialogId, int color) {
                selectedColor = color;
                try {
                    setBackgroundColorTextView(color);
                }
                catch (Exception ex){
                    Toast.makeText(CategoryActivity.this, "Task_drawable: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onDialogDismissed(int dialogId) {

            }
        });
        colorPickerDialog.show(getSupportFragmentManager(), null);
    }

    private void initBottomSheetElements(BottomSheetDialog dialog){
        sheetEtName = dialog.findViewById(R.id.bottom_sheet_category_editText);
        sheetTvColor = dialog.findViewById(R.id.bottom_sheet_category_textView);
        sheetBtnClose = dialog.findViewById(R.id.bottom_sheet_category_btn_close);
        sheetBtnSave = dialog.findViewById(R.id.bottom_sheet_category_btn_save);
    }

    private Category setCategoryData(Category category){
        category.setName(sheetEtName.getText().toString());
        category.setColor(selectedColor);
        return category;
    }

    private void getCategoryData(int position){
        sheetEtName.setText(categoryList.get(position).getName());
        setBackgroundColorTextView(categoryList.get(position).getColor());
    }

    private Boolean checkRepetitions(){
        String tmp = sheetEtName.getText().toString();
        for (Category item: categoryList) {
            if (tmp.equals(item.getName())){
                return true;
            }
        }
        return false;
    }

    private void setBackgroundColorTextView(int color){
        Drawable mDrawable = ContextCompat.getDrawable(CategoryActivity.this, R.drawable.background_white_text_view);
        assert mDrawable != null;
        mDrawable.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        sheetTvColor.setBackground(mDrawable);
    }

}