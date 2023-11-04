package pl.polsl.kanjiapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import pl.polsl.kanjiapp.types.Jlpt;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryList extends Fragment {

    protected RecyclerView.LayoutManager mLayoutManager;
    RecyclerView jlptList;
    RecyclerView gradeList;
    RecyclerView customList;
    public CategoryList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] jlptValues = Arrays.copyOfRange(Jlpt.names(), 1, 6);
        jlptList = getView().findViewById(R.id.customCategoryList);
        CategoryListAdapter adapter = new CategoryListAdapter(jlptValues);
        jlptList.setAdapter(adapter);
        jlptList.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}