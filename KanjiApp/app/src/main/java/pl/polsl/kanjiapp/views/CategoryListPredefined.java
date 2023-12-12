package pl.polsl.kanjiapp.views;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Stream;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.adapters.CategoryListAdapter;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.types.Grade;
import pl.polsl.kanjiapp.types.Jlpt;

public class CategoryListPredefined extends Fragment implements CategoryListAdapter.ItemClickListener{

    //protected RecyclerView.LayoutManager mLayoutManager;
    protected static final String TAG = "CategoryListPredefined";
    RecyclerView categoryList;
    public CategoryListPredefined() {
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
        return inflater.inflate(R.layout.fragment_category_list_predefined, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] jlptValues = Arrays.copyOfRange(Jlpt.names(), 1, Jlpt.names().length);
        String[] gradeValues = Arrays.copyOfRange(Grade.names(), 1, Grade.names().length);

        ArrayList<String> categoryValues = new ArrayList<String>(Arrays.asList(jlptValues));
        categoryValues.addAll(Arrays.asList(gradeValues));
        categoryList = getView().findViewById(R.id.recyclerJlpt);
        categoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getContext(), categoryValues, 0);
        categoryListAdapter.setClickListener(this);
        categoryList.setAdapter(categoryListAdapter);

    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick:    help me ");
        Bundle bundle = new Bundle();
        if(position<Jlpt.values().length-1){
            //send jlpt
            bundle.putInt("level", position+1);
            bundle.putInt("categoryType", CategoryType.Jlpt.getValue());
        }
        else{
            position = position-(Jlpt.values().length-1)+1;
            bundle.putInt("level", position);
            bundle.putInt("categoryType", CategoryType.Grade.getValue());
        }

        Toast.makeText(view.getContext(), "Recycle Click" + position, Toast.LENGTH_SHORT).show();
        Navigation.findNavController(view).navigate(R.id.action_startMenu_to_kanjiListView, bundle);

    }
}