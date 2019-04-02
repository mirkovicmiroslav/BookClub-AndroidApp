package com.miroslavmirkovic.bookclub.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.dto.CategoryDTO;

import java.util.List;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;
    private List<CategoryDTO> categoryList;

    public CategoryAdapter(Context context, List<CategoryDTO> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        final View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_category, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        myViewHolder.categoryName.setText(categoryList.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.category_name);
            cardView = itemView.findViewById(R.id.cardViewCategory);

        }
    }

}
