package pl.polsl.kanjiapp.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.types.CategoryType;
import pl.polsl.kanjiapp.types.Jlpt;
import pl.polsl.kanjiapp.types.Grade;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryList extends Fragment {

    //protected RecyclerView.LayoutManager mLayoutManager;
    protected static final String TAG = "CategoryList";
    ListView jlptList;
    ListView gradeList;
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

        Bundle bundle = new Bundle();
        //jlpt list
        String[] jlptValues = Arrays.copyOfRange(Jlpt.names(), 1, Jlpt.names().length);
        jlptList = getView().findViewById(R.id.jlptListView);
        ArrayAdapter jlptAdapter = new ArrayAdapter<String>(getContext(), R.layout.category_list_item, jlptValues);
        jlptList.setAdapter(jlptAdapter);
        jlptList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position+1;
                bundle.putInt("level", pos);
                bundle.putInt("categoryType", CategoryType.Jlpt.getValue());
                Toast.makeText(view.getContext(), "Recycle Click" + pos, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_startMenu_to_kanjiListView, bundle);
            }

        });

        //grade list
        String[] gradeValues = Arrays.copyOfRange(Grade.names(), 1, Grade.names().length);
        gradeList = getView().findViewById(R.id.gradeListView);
        ArrayAdapter gradeAdapter = new ArrayAdapter<String>(getContext(), R.layout.category_list_item, gradeValues);
        gradeList.setAdapter(gradeAdapter);
        gradeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position+1;
                bundle.putInt("level", pos);
                bundle.putInt("categoryType", CategoryType.Grade.getValue());
                Toast.makeText(view.getContext(), "Recycle Click" + pos, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_startMenu_to_kanjiListView, bundle);
            }

        }

        );

    }
}