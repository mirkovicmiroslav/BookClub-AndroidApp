package com.miroslavmirkovic.bookclub.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.miroslavmirkovic.bookclub.R;
import com.miroslavmirkovic.bookclub.adapters.CategoryAdapter;
import com.miroslavmirkovic.bookclub.dto.CategoryDTO;
import com.miroslavmirkovic.bookclub.retrofit.APIClient;
import com.miroslavmirkovic.bookclub.retrofit.APIInterface;
import com.miroslavmirkovic.bookclub.retrofit.pojo.Category;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private ArrayList<CategoryDTO> categoryList;
    private CategoryAdapter categoryAdapter;
    private Toolbar toolbar;
    private APIInterface apiInterface;

    public CategoryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);

        setHasOptionsMenu(true);

        categoryList = new ArrayList<>();
        apiInterface = APIClient.getClient().create(APIInterface.class);

        getAllCategories();

        return view;
    }

    private void getAllCategories() {
        Call<List<Category>> call = apiInterface.getAllCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                final List<Category> categories = response.body();
                for(Category category : categories){
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTO.setIdCategory(category.getIdCategory());
                    categoryDTO.setName(category.getName());

                    categoryList.add(categoryDTO);
                }

                recyclerView = view.findViewById(R.id.recyclerViewCategory);
                categoryAdapter = new CategoryAdapter(getContext(), categoryList);
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                recyclerView.setAdapter(categoryAdapter);
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {

            }
        });
    }
}
