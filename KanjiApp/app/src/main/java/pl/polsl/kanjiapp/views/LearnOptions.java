package pl.polsl.kanjiapp.views;

import static pl.polsl.kanjiapp.types.CategoryType.intToCategoryType;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import pl.polsl.kanjiapp.R;
import pl.polsl.kanjiapp.types.CategoryType;

public class LearnOptions extends Fragment {
    protected static final String TAG = "LearnOptions";

    int mLevel;
    String setId;
    CategoryType type;
    TextView textView;
    Switch words, sentences;
    Button learn;
    EditText turns;
    int wordEnabled, sentenceEnabled;

    public LearnOptions() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = intToCategoryType(getArguments().getInt("categoryType"));
            if (type == CategoryType.Custom)
                setId = getArguments().getString("level");
            else
                mLevel = getArguments().getInt("level");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learn_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = getView().findViewById(R.id.textViewKanjiSet);
        textView.setText("Learning kanji set: " + type.name() + " " + (mLevel != 0? mLevel : setId.substring(28)));

        learn = getView().findViewById(R.id.learnButton);
        words = getView().findViewById(R.id.switchWords);
        sentences = getView().findViewById(R.id.switchSentences);

        words.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wordEnabled = 1;
                } else {
                    wordEnabled = 0;
                }
            }
        });

        sentences.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sentenceEnabled = 1;
                } else {
                    sentenceEnabled = 0;
                }
            }
        });


        turns = getView().findViewById(R.id.editTextTurns);

        Bundle bundle = new Bundle();

        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo trycatch
                int turnsValue = Integer.parseInt(turns.getText().toString());
                if (type == CategoryType.Custom)
                    bundle.putString("level", setId);
                else
                    bundle.putInt("level", mLevel);
                bundle.putInt("categoryType", type.getValue());
                bundle.putInt("wordEnabled", wordEnabled);
                bundle.putInt("sentenceEnabled", sentenceEnabled);
                bundle.putInt("turns", turnsValue);
                Navigation.findNavController(view).navigate(R.id.action_learnOptions_to_learn, bundle);
            }
        });

    }


}